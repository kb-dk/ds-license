package dk.kb.license.storage;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.kb.license.config.ServiceConfig;
import dk.kb.license.validation.LicenseValidator;


/** 
*  Cache implementation that will reload all licenses every 15 minutes. This is an essential cache for performance. Loading the whole license configuration
*  from the 10 different tables every time a license is called will be massive overhead
*   
*  When important license configuration is changed, the H2Storage class will fire a reload to this cache and the change be will be instantaneous.
*   
*/
public class LicenseCache {

    // Cached instances
    private static ArrayList<License> cachedLicenses;
    private static ArrayList<GroupType> cachedLicenseGroupTypes;
    private static ArrayList<GroupType> cachedLicenseRestrictionGroupTypes;
    private static ArrayList<AttributeType> cachedAttributeTypes;
    private static ArrayList<PresentationType> cachedLicensePresentationTypes;
    private static HashMap<String, GroupType> groupIdMap;
    private static HashMap<String, PresentationType> presentationTypeIdMap;

    private static final Logger log = LoggerFactory.getLogger(LicenseCache.class);    
    private static final double reloadTimeInMillis=ServiceConfig.getCacheRefreshTimeInSeconds()*1000L;
    private static long lastReloadTime = 0;

    public static ArrayList<License> getAllLicense() {
        checkReload();
        return cachedLicenses;
    }

    public static ArrayList<GroupType> getConfiguredLicenseGroupTypes() {
        checkReload();
        return cachedLicenseGroupTypes;
    }

    public static ArrayList<GroupType> getConfiguredRestrictionLicenseGroupTypes() {
        checkReload();
        return cachedLicenseRestrictionGroupTypes;
    }

    public static ArrayList<AttributeType> getConfiguredAttributeTypes() {
        checkReload();
        return cachedAttributeTypes;

    }

    public static ArrayList<PresentationType> getConfiguredLicenseTypes() {
        checkReload();
        return cachedLicensePresentationTypes;
    }

    private static synchronized void checkReload() {        
        
        if (System.currentTimeMillis() - lastReloadTime > reloadTimeInMillis) {
            reloadCache();
        }
    }

    public static void reloadCache() {
        LicenseModuleStorage storage =  null;
        try {
            storage = new LicenseModuleStorage();
            log.info("Reloading cache from DB");
            lastReloadTime = System.currentTimeMillis();

            // Load all Licenses
            ArrayList<License> licenseList = new ArrayList<License>();
            ArrayList<License> names = storage.getAllLicenseNames();

            for (License current : names) {
                License license = storage.getLicense(current.getId());
                licenseList.add(license);
            }
            cachedLicenses = licenseList;
            log.debug("#licenses reload=" + cachedLicenses.size());

            // Load LicenseGroupTypes
            cachedLicenseGroupTypes = storage.getLicenseGroupTypes();

            // Load LicenseRestrictionGroupTypes
            ArrayList<GroupType> allList = storage.getLicenseGroupTypes();
            cachedLicenseRestrictionGroupTypes = LicenseValidator.filterRestrictionGroups(allList);

            // Load AttributeTypes
            cachedAttributeTypes = storage.getAttributeTypes();


            // Load LicensePresentationTypes
            cachedLicensePresentationTypes = storage.getLicensePresentationTypes();
            //create Dk2En name map
            groupIdMap = new HashMap<String,GroupType>();

            for (GroupType current : cachedLicenseGroupTypes){
                groupIdMap.put(current.getKey(), current);            	
            }

            presentationTypeIdMap = new HashMap<String, PresentationType>();
            for (PresentationType current : cachedLicensePresentationTypes){
                presentationTypeIdMap.put(current.getKey(), current);            	
            }


        } catch (SQLException e) {
            log.error("Error in reload cache", e);
            throw new RuntimeException(e);
        }
        finally{
            storage.close();            
        }				
    }

    public static String getPresentationtypeName(String id, String locale){

        if (LicenseValidator.LOCALE_DA.equals(locale)){
            return presentationTypeIdMap.get(id).getValue_dk();
        }
        else if (LicenseValidator.LOCALE_EN.equals(locale)){
            return presentationTypeIdMap.get(id).getValue_en();
        }		
        return null; 	
    }

    public static String getGroupName(String id, String locale){

        if (LicenseValidator.LOCALE_DA.equals(locale)){
            return groupIdMap.get(id).getValue_dk();
        }
        else if (LicenseValidator.LOCALE_EN.equals(locale)){
            return groupIdMap.get(id).getValue_en();
        }			   
        return null; 	
    }

}
