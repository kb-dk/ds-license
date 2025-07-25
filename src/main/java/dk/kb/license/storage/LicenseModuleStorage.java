package dk.kb.license.storage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * The DB consist of the following tables:
 * <p>
 * 3 tables for configuration: PRESENTATIONTYPES: configured presentationtypes.
 * GROUPTYPES: configured groups ATTRIBUTETYPES: configured attributes
 * <p>
 * The following tables to store created licenses: LICENSE (top parent)
 * ATTRIBUTEGROUP (parent=LICENSE) ATTRIBUTE (parent = ATTRIBUTEGROUP) VALUE
 * (parent = ATTRIBUTE) LICENSECONTENT (parent = LICENSE) PRESENTATION (parent =
 * LICENSECONTENT)
 * 
 */
public class LicenseModuleStorage extends BaseModuleStorage  {

    private static final Logger log = LoggerFactory.getLogger(LicenseModuleStorage.class);

    // Table and column names
    private static final String LICENSEPRESENTATIONTYPES_TABLE = "PRESENTATIONTYPES";
    private static final String LICENSEGROUPTYPES_TABLE = "GROUPTYPES";
    private static final String ATTRIBUTETYPES_TABLE = "ATTRIBUTETYPES";
    private static final String LICENSE_TABLE = "LICENSE";
    private static final String ATTRIBUTEGROUP_TABLE = "ATTRIBUTEGROUP";
    private static final String ATTRIBUTE_TABLE = "ATTRIBUTE";
    private static final String VALUE_TABLE = "VALUE_ORG";
    private static final String LICENSECONTENT_TABLE = "LICENSECONTENT";
    private static final String PRESENTATION_TABLE = "PRESENTATION";
    
    private static final String VALIDTO_COLUMN = "VALIDTO";
    private static final String VALIDFROM_COLUMN = "VALIDFROM";
    private static final String NAME_COLUMN = "NAME";
    private static final String NAME_EN_COLUMN = "NAME_EN";
    private static final String DESCRIPTION_DK_COLUMN = "DESCRIPTION_DK";
    private static final String DESCRIPTION_EN_COLUMN = "DESCRIPTION_EN";
    private static final String QUERY_COLUMN = "QUERYSTRING";
    private static final String NUMBER_COLUMN = "NUMBER";
    private static final String LICENSEID_COLUMN = "LICENSEID";
    private static final String LICENSECONTENTID_COLUMN = "LICENSECONTENTID";
    private static final String ATTRIBUTEGROUPID_COLUMN = "ATTRIBUTEGROUPID";
    private static final String ATTRIBUTEID_COLUMN = "ATTRIBUTEID";
    
    private static final String ID_COLUMN = "ID"; // ID used for all tables
    private static final String KEY_COLUMN = "KEY_ID";
    private static final String VALUE_COLUMN = "VALUE_ORG";
    private static final String VALUE_DK_COLUMN = "VALUE_DK";
    private static final String VALUE_EN_COLUMN = "VALUE_EN";
    private static final String RESTRICTION_COLUMN = "RESTRICTION";

    private final static String selectLicensePresentationTypesQuery = "SELECT * FROM "
            + LICENSEPRESENTATIONTYPES_TABLE;

    private final static String selectAllLicensesQuery = "SELECT * FROM " + LICENSE_TABLE;
    private final static String selectLicenseQuery = "SELECT * FROM " + LICENSE_TABLE + " WHERE ID = ? ";
    private final static String selectGroupTypeQueryById = "SELECT * FROM " + LICENSEGROUPTYPES_TABLE + " WHERE ID = ? ";
    private final static String selectPresentationTypeQueryById = "SELECT * FROM " + LICENSEPRESENTATIONTYPES_TABLE + " WHERE ID = ? ";
    private final static String selectPresentationTypeQueryByKey = "SELECT * FROM " + LICENSEPRESENTATIONTYPES_TABLE + " WHERE KEY_ID = ? ";

    private final static String persistLicensePresentationTypeQuery = "INSERT INTO "
            + LICENSEPRESENTATIONTYPES_TABLE + " (" + ID_COLUMN + "," + KEY_COLUMN + "," + VALUE_DK_COLUMN + ","
            + VALUE_EN_COLUMN + ") VALUES (?,?,?,?)"; // #|?|=4

    private final static String persistAttributeGroupForLicenseQuery = "INSERT INTO " + ATTRIBUTEGROUP_TABLE + " ("
            + ID_COLUMN + "," + NUMBER_COLUMN + "," + LICENSEID_COLUMN + ") VALUES (?,?,?)"; // #|?|=3

    private final static String selectAttributeGroupsForLicenseQuery = " SELECT * FROM " + ATTRIBUTEGROUP_TABLE
            + " WHERE " + LICENSEID_COLUMN + "= ? ORDER BY NUMBER";

    private final static String persistAttributeForAttributeGroupQuery = "INSERT INTO " + ATTRIBUTE_TABLE + " ("
            + ID_COLUMN + "," + NAME_COLUMN + "," + ATTRIBUTEGROUPID_COLUMN + ") VALUES (?,?,?)"; // #|?|=3

    private final static String selectAttributesForAttributeGroupQuery = " SELECT * FROM " + ATTRIBUTE_TABLE + " WHERE "
            + ATTRIBUTEGROUPID_COLUMN + " = ?";

    private final static String persistValueForAttributeQuery = "INSERT INTO " + VALUE_TABLE + " (" + ID_COLUMN + ","
            + VALUE_COLUMN + "," + ATTRIBUTEID_COLUMN + ") VALUES (?,?,?)"; // #|?|=3

    private final static String selectValuesForAttributeQuery = " SELECT * FROM " + VALUE_TABLE + " WHERE "
            + ATTRIBUTEID_COLUMN + " = ?";

    private final static String selectLicenseGroupTypesQuery = " SELECT * FROM " + LICENSEGROUPTYPES_TABLE
            + " ORDER BY " + KEY_COLUMN;

    private final static String persistLicenseGroupTypeQuery = "INSERT INTO " + LICENSEGROUPTYPES_TABLE + " ("
            + ID_COLUMN + "," + KEY_COLUMN + "," + VALUE_DK_COLUMN + " ," + VALUE_EN_COLUMN + " ,"
            + DESCRIPTION_DK_COLUMN + " ," + DESCRIPTION_EN_COLUMN + " ," + QUERY_COLUMN + " ," + RESTRICTION_COLUMN
            + ") VALUES (?,?,?,?,?,?,?,?)"; // #|?|=8

    private final static String updateLicenseGroupTypeQuery = "UPDATE " + LICENSEGROUPTYPES_TABLE + " SET "
            + VALUE_DK_COLUMN + " = ? , " + VALUE_EN_COLUMN + " = ? ," + DESCRIPTION_DK_COLUMN + " = ? ,"
            + DESCRIPTION_EN_COLUMN + " = ? ," + QUERY_COLUMN + " = ? ," + RESTRICTION_COLUMN + " = ? " + "WHERE "
            + ID_COLUMN + " = ? ";

    private final static String updateLicensePresentationTypeQuery = "UPDATE " + LICENSEPRESENTATIONTYPES_TABLE
            + " SET " + VALUE_DK_COLUMN + " = ? , " + VALUE_EN_COLUMN + " = ? " + "WHERE " + ID_COLUMN + " = ? ";

    private final static String persistLicenseQuery = "INSERT INTO " + LICENSE_TABLE + " (" + ID_COLUMN + ","
            + NAME_COLUMN + "," + NAME_EN_COLUMN + "," + DESCRIPTION_DK_COLUMN + "," + DESCRIPTION_EN_COLUMN + ","
            + VALIDFROM_COLUMN + "," + VALIDTO_COLUMN + ") VALUES (?,?,?,?,?,?,?)"; // #|?|=7

    private final static String selectAttributeTypesQuery = "SELECT * FROM " + ATTRIBUTETYPES_TABLE
            + " ORDER BY " + VALUE_COLUMN;;

            
    private final static String selectAttributeTypesByNameQuery = "SELECT * FROM " + ATTRIBUTETYPES_TABLE
            + " WHERE " + VALUE_COLUMN + " = ?";

            
    private final static String deleteAttributeTypeByNameQuery = "DELETE FROM " + ATTRIBUTETYPES_TABLE
            + " WHERE " + VALUE_COLUMN + " = ?";

    private final static String deleteGroupTypeByKeyQuery = "DELETE FROM " + LICENSEGROUPTYPES_TABLE + " WHERE "
            + KEY_COLUMN + " = ?";
    
    private final static String selectGroupTypeByKeyQuery = "SELECT * FROM " + LICENSEGROUPTYPES_TABLE + " WHERE "
            + KEY_COLUMN + " = ?";

    private final static String deletePresentationTypeByKeyQuery = "DELETE FROM "
            + LICENSEPRESENTATIONTYPES_TABLE + " WHERE " + KEY_COLUMN + " = ?";

    private final static String persistAttributeTypeQuery = "INSERT INTO " + ATTRIBUTETYPES_TABLE + " ("
            + ID_COLUMN + "," + VALUE_COLUMN + ") VALUES (?,?)"; // #|?|=2

    private final static String selectLicenseContentForLicenseQuery = "SELECT * FROM " + LICENSECONTENT_TABLE
            + " WHERE " + LICENSEID_COLUMN + " = ? ";

    private final static String persistLicenseContentForLicenseQuery = "INSERT INTO " + LICENSECONTENT_TABLE + " ("
            + ID_COLUMN + "," + NAME_COLUMN + "," + LICENSEID_COLUMN + ") VALUES (?,?,?)"; // #|?|=3

    private final static String selectPresentationTypesForLicenseContentQuery = "SELECT * FROM " + PRESENTATION_TABLE
            + " WHERE " + LICENSECONTENTID_COLUMN + " = ? ";

    private final static String persistPresentationTypesForLicenseContentQuery = "INSERT INTO " + PRESENTATION_TABLE
            + " (" + ID_COLUMN + "," + NAME_COLUMN + "," + LICENSECONTENTID_COLUMN + ") VALUES (?,?,?)"; // #|?|=3

    // Deletes
    private final static String deletePresentationsByLicenseContentIdQuery = "DELETE FROM " + PRESENTATION_TABLE
            + " WHERE " + LICENSECONTENTID_COLUMN + " = ?";

    private final static String deleteLicenseContentsByLicenseIdQuery = "DELETE FROM " + LICENSECONTENT_TABLE
            + " WHERE " + LICENSEID_COLUMN + " = ?";

    private final static String deleteAttributesByAttributeGroupIdQuery = "DELETE FROM " + ATTRIBUTE_TABLE + " WHERE "
            + ATTRIBUTEGROUPID_COLUMN + " = ?";

    private final static String countAttributesByAttributeNameQuery = "SELECT COUNT(*) FROM " + ATTRIBUTE_TABLE
            + " WHERE " + NAME_COLUMN + " = ?";

    private final static String countGroupTypeByGroupNameQuery = "SELECT COUNT(*) FROM " + LICENSECONTENT_TABLE
            + " WHERE " + NAME_COLUMN + " = ?";
   
    
    private final static String countPresentationTypeByPresentationNameQuery = "SELECT COUNT(*) FROM "
            + PRESENTATION_TABLE + " WHERE " + NAME_COLUMN + " = ?";

    private final static String deleteValuesByAttributeIdQuery = "DELETE FROM " + VALUE_TABLE + " WHERE "
            + ATTRIBUTEID_COLUMN + " = ?";

    private final static String deleteAttributeGroupByLicenseIdQuery = "DELETE FROM " + ATTRIBUTEGROUP_TABLE
            + " WHERE " + LICENSEID_COLUMN + " = ?";

    private final static String deleteLicenseByLicenseIdQuery = "DELETE FROM " + LICENSE_TABLE + " WHERE " + ID_COLUMN
            + " = ?";

    public LicenseModuleStorage() throws SQLException {
        super();
    }

    public long persistLicensePresentationType(String key, String value_dk, String value_en) throws SQLException {
        log.info("Persisting new license presentationtype: " + key);

        validateValue(key);
        validateValue(value_dk);
        value_dk = value_dk.trim();
        key = key.trim();
        validateValue(value_en);
        value_en = value_en.trim();

        long id =generateUniqueID();
        
        try (PreparedStatement stmt = connection.prepareStatement(persistLicensePresentationTypeQuery)) {
            stmt.setLong(1, id);
            stmt.setString(2, key);
            stmt.setString(3, value_dk);
            stmt.setString(4, value_en);
            stmt.execute();
        } catch (SQLException e) {
            log.error("SQL Exception in persistPresentationType: " + e.getMessage());
            throw e;
        }
        return id;
       
    }

    public ArrayList<PresentationType> getLicensePresentationTypes() throws SQLException {

        ArrayList<PresentationType> list = new ArrayList<PresentationType>();

        try (PreparedStatement stmt = connection.prepareStatement(selectLicensePresentationTypesQuery)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Long id = rs.getLong(ID_COLUMN);
                String key = rs.getString(KEY_COLUMN);
                String value = rs.getString(VALUE_DK_COLUMN);
                String value_en = rs.getString(VALUE_EN_COLUMN);
                PresentationType item = new PresentationType(key, value, value_en);
                item.setId(id);
                list.add(item);
            }
            return list;
        } catch (SQLException e) {
            log.error("SQL Exception in getPresentationTypes: " + e.getMessage());
            throw e;
        }
    }

    public long deleteLicense(long licenseId) throws SQLException {
        log.info("Deleting license with id: " + licenseId);
        License license = null;
        try {
            license = getLicense(licenseId);
        } catch (IllegalArgumentException e) {
            // No license in DB with that ID, nothing to delete
            log.warn("No license with id: " + licenseId);
            return 0;//      
        }

        for (AttributeGroup currentAttributeGroup : license.getAttributeGroups()) {

            ArrayList<Attribute> attributes = currentAttributeGroup.getAttributes();
            for (Attribute currentAttribute : attributes) {
                deleteById(deleteValuesByAttributeIdQuery, currentAttribute.getId());
            }

            deleteById(deleteAttributesByAttributeGroupIdQuery, currentAttributeGroup.getId());
        }

        for (LicenseContent currentLicenseContent : license.getLicenseContents()) {
            deleteById(deletePresentationsByLicenseContentIdQuery, currentLicenseContent.getId());
        }

        deleteById(deleteLicenseContentsByLicenseIdQuery, licenseId);
        deleteById(deleteAttributeGroupByLicenseIdQuery, licenseId);
        deleteById(deleteLicenseByLicenseIdQuery, licenseId);

        LicenseCache.reloadCache(); // Force reload so the change will be instant in the cache
        return licenseId;
    }

    // query can be null or empty
    public long persistLicenseGroupType(String key, String value, String value_en, String description,
            String description_en, String query, boolean restriction) throws IllegalArgumentException, SQLException {

        if (!StringUtils.isNotEmpty(key)) {
            throw new IllegalArgumentException("Key can not be null when creating new Group");
        }

        if (!StringUtils.isNotEmpty(value)) {
            throw new IllegalArgumentException("Value can not be null when creating new Group");
        }

        if (!StringUtils.isNotEmpty(value_en)) {
            throw new IllegalArgumentException("Value(EN) can not be null when creating new Group");
        }

        if (!StringUtils.isNotEmpty(query)) {
            throw new IllegalArgumentException("Query can not be null when creating new Group");
        }

        log.info("Persisting new license group type: " + key);

        validateValue(value);
        value = value.trim();
        long id = generateUniqueID();
        try (PreparedStatement stmt = connection.prepareStatement(persistLicenseGroupTypeQuery);) {
            stmt.setLong(1, id);
            stmt.setString(2, key);
            stmt.setString(3, value);
            stmt.setString(4, value_en);
            stmt.setString(5, description);
            stmt.setString(6, description_en);
            stmt.setString(7, query);
            stmt.setBoolean(8, restriction);
            stmt.execute();
            connection.commit();
        } catch (SQLException e) {
            log.error("SQL Exception in persistLicenseGroupType: " + e.getMessage());
            throw e;
        }
        LicenseCache.reloadCache(); // Force reload so the change will be instant in the cache
        return id;
    }

    public void updateLicenseGroupType(long id, String value_dk, String value_en, String description,
            String description_en, String query, boolean restriction) throws SQLException {

        try (PreparedStatement stmt = connection.prepareStatement(updateLicenseGroupTypeQuery);) {
            log.info("Updating Group type with id: " + id);

            // if it exists already, we do not add it.

            stmt.setString(1, value_dk);
            stmt.setString(2, value_en);
            stmt.setString(3, description);
            stmt.setString(4, description_en);
            stmt.setString(5, query);
            stmt.setBoolean(6, restriction);
            stmt.setLong(7, id);

            int updated = stmt.executeUpdate();
            if (updated != 1) {
                throw new SQLException("Grouptype id not found: " + id);
            }

            connection.commit();

        } catch (SQLException e) {
            log.error("Exception in updateLicenseGroupType: " + e.getMessage());
            throw e;
        }
        LicenseCache.reloadCache(); // Force reload so the change will be instant in the cache
    }

    public void updatePresentationType(long id, String value_dk, String value_en) throws SQLException {

        try (PreparedStatement stmt = connection.prepareStatement(updateLicensePresentationTypeQuery);) {
            log.info("Updating Presentation type with id: " + id);

            // if it exists already, we do not add it.
            stmt.setString(1, value_dk);
            stmt.setString(2, value_en);
            stmt.setLong(3, id);

            int updated = stmt.executeUpdate();
            if (updated != 1) {
                throw new SQLException("Presentationtype id not found: " + id);
            }

            connection.commit();

        } catch (SQLException e) {
            log.error("Exception in updatePresentationType: " + e.getMessage());
            throw e;
        }
        LicenseCache.reloadCache(); // Force reload so the change will be instant in the cache
    }

    public long deleteLicenseGroupType(String groupName) throws IllegalArgumentException, SQLException {

        log.info("Deleting grouptype: " + groupName);
        // First check it is not used in any license, in that case throw exception.

        try (PreparedStatement stmt = connection.prepareStatement(countGroupTypeByGroupNameQuery);) {
            stmt.setString(1, groupName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int number = rs.getInt(1);
                if (number > 0) {
                    throw new IllegalArgumentException("Can not delete group with name:" + groupName
                            + " because it is used in at least 1 license");
                }
            }

        } catch (SQLException e) {
            log.error("SQL Exception in deleteLicenseGroupType: " + e.getMessage());
            throw e;
        }

   
        long id;
        try (PreparedStatement stmt = connection.prepareStatement(selectGroupTypeByKeyQuery);) {
            stmt.setString(1, groupName);
            ResultSet rs=stmt.executeQuery();
            
            rs.next();
            id = rs.getLong(ID_COLUMN);                                  
            
        } catch (SQLException e) {
            log.error("SQL Exception in deleteLicenseGroupType: " + e.getMessage());
            throw e;
        }
                        
        try (PreparedStatement stmt = connection.prepareStatement(deleteGroupTypeByKeyQuery);) {
            stmt.setString(1, groupName);
            int updated = stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("SQL Exception in deleteLicenseGroupType: " + e.getMessage());
            throw e;
        }

        LicenseCache.reloadCache(); // Force reload so the change will be instant in the cache
        return id;
    }

    public void deletePresentationType(String presentationName) throws IllegalArgumentException, SQLException {

        log.info("Deleting presentation type: " + presentationName);
        // First check it is not used in any license, in that case throw exception.

        try (PreparedStatement stmt = connection.prepareStatement(countPresentationTypeByPresentationNameQuery);) {
            stmt.setString(1, presentationName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int number = rs.getInt(1);
//               System.out.println("presentation used times:"+number + " with name:"+presentationName);
                if (number > 0) {
                    throw new IllegalArgumentException("Can not delete presentationtype with name: " + presentationName
                            + " because it is used in at least 1 license");
                }
            }

        } catch (SQLException e) {
            log.error("SQL Exception in deletePresentationType: " + e.getMessage());
            throw e;
        }

        try (PreparedStatement stmt = connection.prepareStatement(deletePresentationTypeByKeyQuery);) {
            stmt.setString(1, presentationName);
            int updated = stmt.executeUpdate();
            log.info("deleted " + updated + " presentationtype with name: " + presentationName);

        } catch (SQLException e) {
            log.error("SQL Exception in deletePresentationType: " + e.getMessage());
            throw e;
        }

        LicenseCache.reloadCache(); // Force reload so the change will be instant in the cache
    }

    public long persistLicense(License license) throws IllegalArgumentException, SQLException {

        log.info("Persisting new license: " + license.getLicenseName());

        // validate name, description, validTo,validFrom
        boolean validateMainFields = license.validateMainFields();
        boolean validateAttributesValues = license.validateAttributesAndValuesNotNull();
        if (!validateMainFields) {
            throw new IllegalArgumentException(
                    "Validation error. Name/description too short or validTo/validFrom not legal dates. Date format is  dd-MM-yyyy");
        }
        if (!validateAttributesValues) {
            throw new IllegalArgumentException("Validation error. Attributes or values can not be empty");
        }

        long licenseId = license.getId();
        if (license.getId() > 0) { // This is an existing license in the DB, delete it before updating
            // licenpersistLicenseseId = license.getId();
            // Delete old license before updating (creating new)
            log.info("Deleting license before updating");
            deleteLicense(licenseId);

        } else {
            licenseId = generateUniqueID(); // new ID.
        }
        try (PreparedStatement stmt = connection.prepareStatement(persistLicenseQuery);) {

            stmt.setLong(1, licenseId);
            stmt.setString(2, license.getLicenseName());
            stmt.setString(3, license.getLicenseName_en());
            stmt.setString(4, license.getDescription_dk());
            stmt.setString(5, license.getDescription_en());
            stmt.setString(6, license.getValidFrom());
            stmt.setString(7, license.getValidTo());
            stmt.execute();

            persistAttributeGroupsForLicense(licenseId, license.getAttributeGroups());
            persistLicenseContentsForLicense(licenseId, license.getLicenseContents());
        } catch (SQLException e) {
            log.error("SQL Exception in persistLicense: " + e.getMessage());
            throw e;
        }
    
        return licenseId;
        //TODO! Important! Remove to facade!
        //LicenseCache.reloadCache(); // Force reload so the change will be instant in the cache
    }

    public ArrayList<GroupType> getLicenseGroupTypes() throws SQLException {

        ArrayList<GroupType> list = new ArrayList<GroupType>();
        try (PreparedStatement stmt = connection.prepareStatement(selectLicenseGroupTypesQuery);) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Long id = rs.getLong(ID_COLUMN);
                String key = rs.getString(KEY_COLUMN);
                String value_dk = rs.getString(VALUE_DK_COLUMN);
                String value_en = rs.getString(VALUE_EN_COLUMN);
                String description = rs.getString(DESCRIPTION_DK_COLUMN);
                String description_en = rs.getString(DESCRIPTION_EN_COLUMN);
                String query = rs.getString(QUERY_COLUMN);
                boolean restriction = rs.getBoolean(RESTRICTION_COLUMN);
                GroupType item = new GroupType(id, key, value_dk, value_en,description, description_en, query, restriction);
                list.add(item);
            }
            return list;
        } catch (SQLException e) {
            log.error("SQL Exception in getLicenseGroupTypes(): " + e.getMessage());
            throw e;
        }
    }

    public long persistAttributeType(String value) throws SQLException {

        log.info("Persisting new  attribute type: " + value);

        validateValue(value);
        value = value.trim();
          long id= generateUniqueID();
        try (PreparedStatement stmt = connection.prepareStatement(persistAttributeTypeQuery);) {
            stmt.setLong(1, id);
            stmt.setString(2, value);
            stmt.execute();

        } catch (SQLException e) {
            log.error("SQL Exception in persistAttributeType: " + e.getMessage());
            throw e;
        }
        LicenseCache.reloadCache(); // Force reload so the change will be instant in the cache
        return id;
    }

    public long deleteAttributeType(String attributeTypeName) throws IllegalArgumentException, SQLException {

        log.info("Deleting attributetype: " + attributeTypeName);
        // First check it is not used in any license, in that case throw exception.

        try (PreparedStatement stmt = connection.prepareStatement(countAttributesByAttributeNameQuery);) {

            stmt.setString(1, attributeTypeName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int number = rs.getInt(1);
                if (number > 0) {
                    throw new IllegalArgumentException("Can not delete attribute with name:" + attributeTypeName
                            + " because it is used in at least 1 license");
                }
            }

        } catch (SQLException e) {
            log.error("SQL Exception in deleteAttributeType: " + e.getMessage());
            throw e;
        }

        //GetId

        long id;
        
        try (PreparedStatement stmt = connection.prepareStatement(selectAttributeTypesByNameQuery);) {
            stmt.setString(1, attributeTypeName);
            ResultSet rs = stmt.executeQuery();
            rs.next(); //There is 1
            id= rs.getLong(ID_COLUMN);
            
        } catch (SQLException e) {
            log.error("SQL Exception in deleteAttributeType: " + e.getMessage());
            throw e;
        }
        
        try (PreparedStatement stmt = connection.prepareStatement(deleteAttributeTypeByNameQuery);) {
            stmt.setString(1, attributeTypeName);
            int updated = stmt.executeUpdate();
            log.info("deleted " + updated + " attributetypes with name:" + attributeTypeName);
        } catch (SQLException e) {
            log.error("SQL Exception in deleteAttributeType: " + e.getMessage());
            throw e;
        }

        LicenseCache.reloadCache(); // Force reload so the change will be instant in the cache
        return id;
    }

    public ArrayList<AttributeType> getAttributeTypes() throws SQLException {

        ArrayList<AttributeType> list = new ArrayList<AttributeType>();

        try (PreparedStatement stmt = connection.prepareStatement(selectAttributeTypesQuery);) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Long id = rs.getLong(ID_COLUMN);
                String value = rs.getString(VALUE_COLUMN);
                AttributeType item = new AttributeType(value);
                item.setId(id); 
                list.add(item);
            }
            return list;
        } catch (SQLException e) {
            log.error("SQL Exception in getAttributes: " + e.getMessage());
            throw e;
        }
    }

    // this method only loads the name of the license and NOT all associations.
    public ArrayList<License> getAllLicenseNames() throws SQLException {

        ArrayList<License> list = new ArrayList<License>();
        try (PreparedStatement stmt = connection.prepareStatement(selectAllLicensesQuery);) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                License license = new License();
                Long id = rs.getLong(ID_COLUMN);
                String name = rs.getString(NAME_COLUMN);
                String description_dk = rs.getString(DESCRIPTION_DK_COLUMN);
                String description_en = rs.getString(DESCRIPTION_EN_COLUMN);
                String validFrom = rs.getString(VALIDFROM_COLUMN);
                String validTo = rs.getString(VALIDTO_COLUMN);
                license.setId(id);
                license.setLicenseName(name);
                license.setDescription_dk(description_dk);
                license.setDescription_en(description_en);
                license.setValidFrom(validFrom);// todo format
                license.setValidTo(validTo);// todo format
                list.add(license);
            }
            return list;
        } catch (SQLException e) {
            log.error("SQL Exception in getAllLicenseNames: " + e.getMessage());
            throw e;
        }
    }

    // this method a license from licenseId with all associations (complete
    // object-tree)
    public License getLicense(long licenseId) throws IllegalArgumentException, SQLException {

        License license = new License();

        try (PreparedStatement stmt = connection.prepareStatement(selectLicenseQuery);) {
            stmt.setLong(1, licenseId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) { // maximum one due to unique/primary key constraint
                Long id = rs.getLong(ID_COLUMN);
                String name = rs.getString(NAME_COLUMN);
                String name_en = rs.getString(NAME_EN_COLUMN);
                String description_dk = rs.getString(DESCRIPTION_DK_COLUMN);
                String description_en = rs.getString(DESCRIPTION_EN_COLUMN);
                String validFrom = rs.getString(VALIDFROM_COLUMN);
                String validTo = rs.getString(VALIDTO_COLUMN);
                license.setId(id);
                license.setLicenseName(name);
                license.setLicenseName_en(name_en);
                license.setDescription_dk(description_dk);
                license.setDescription_en(description_en);
                license.setValidFrom(validFrom);
                license.setValidTo(validTo);

                license.setAttributeGroups(getAttributeGroupsForLicense(id));
                license.setLicenseContents(getLicenseContentsForLicense(id));

                return license;
            }
            throw new IllegalArgumentException("License not found for licenseId: " + licenseId);

        } catch (SQLException e) {
            log.error("SQL Exception in getLicense: " + e.getMessage());
            throw e;
        }
    }

    public PresentationType getPresentationTypeById(long id) throws IllegalArgumentException, SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(selectPresentationTypeQueryById);) {
            stmt.setLong(1, id);
            
            ResultSet rs = stmt.executeQuery();            
            while (rs.next()) { // maximum one due to unique/primary key constraint                
                String key = rs.getString( KEY_COLUMN);
                String dk = rs.getString( VALUE_DK_COLUMN);
                String en = rs.getString( VALUE_EN_COLUMN);                
                PresentationType type = new PresentationType(key, dk, en);
                type.setId(id);
                return type;                                
            }
            throw new IllegalArgumentException("Presentationtype not found for id: " + id);

        } catch (SQLException e) {
            log.error("SQL Exception in getPresentationTypeById: " + e.getMessage());
            throw e;
        }
    }
    
    public GroupType getGroupTypeById(long id) throws IllegalArgumentException, SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(selectGroupTypeQueryById);) {
            stmt.setLong(1, id);
            
            ResultSet rs = stmt.executeQuery();            
            while (rs.next()) { // maximum one due to unique/primary key constraint                
                String key = rs.getString(KEY_COLUMN);
                String value_dk = rs.getString(VALUE_DK_COLUMN);
                String value_en = rs.getString(VALUE_EN_COLUMN);
                String description = rs.getString(DESCRIPTION_DK_COLUMN);
                String description_en = rs.getString(DESCRIPTION_EN_COLUMN);
                String query = rs.getString(QUERY_COLUMN);
                boolean restriction = rs.getBoolean(RESTRICTION_COLUMN);
                GroupType group = new GroupType(id, key, value_dk, value_en, description, description_en, query, restriction);
            return group;
            }
            throw new IllegalArgumentException("Presentationtype not found for id: " + id);

        } catch (SQLException e) {
            log.error("SQL Exception in getPresentationTypeById: " + e.getMessage());
            throw e;
        }
    }

    public PresentationType getPresentationTypeByKey(String key) throws IllegalArgumentException, SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(selectPresentationTypeQueryByKey);) {
            stmt.setString(1, key);
            
            ResultSet rs = stmt.executeQuery();            
            while (rs.next()) { // maximum one due to unique/primary key constraint                
                Long id = rs.getLong(ID_COLUMN);
                String dk = rs.getString(VALUE_DK_COLUMN);
                String en = rs.getString(VALUE_EN_COLUMN);
                PresentationType type = new PresentationType(key, dk, en);
                type.setId(id);
                return type;                                
            }
            throw new IllegalArgumentException("Presentationtype not found for key: " + key);

        } catch (SQLException e) {
            log.error("SQL Exception in getPresentationType: " + e.getMessage());
            throw e;
        }
    }

    protected void validateValue(String value) {
        // sanity, must have length at least 2
        if (value == null || value.trim().length() < 2) {
            throw new IllegalArgumentException("Value empty or too short");
        }
    }

    /*
     * public StatisticsDTO getStatistics() throws SQLException{
     * 
     * StatisticsDTO output = new StatisticsDTO(); long
     * t1=System.currentTimeMillis(); PreparedStatement stmt = null; try{
     * 
     * //Extract statistics for number of items added for each day, up to 50 days
     * back. Calendar cal = Calendar.getInstance();
     * 
     * Date start=cal.getTime(); Date end=cal.getTime();; ArrayList<Integer>
     * itemsAddedDaysAgo = new ArrayList<Integer>();
     * 
     * for (int daysAgo=0;daysAgo<50;daysAgo++){ //last 50 days
     * cal.add(Calendar.DAY_OF_YEAR, -1); start=cal.getTime();
     * 
     * long startLong=start.getTime(); long endLong=end.getTime(); stmt =
     * singleDBConnection.prepareStatement("SELECT COUNT(*) FROM " +
     * ATTRIBUTESTORE_TABLE + " WHERE "+MODIFIED_COLUMN +" > ? AND "
     * +MODIFIED_COLUMN +" <= ?"); stmt.setLong(1, startLong); stmt.setLong(2,
     * endLong); ResultSet rs=stmt.executeQuery(); while (rs.next()){ int
     * inserted=rs.getInt(1); itemsAddedDaysAgo.add(new Integer(inserted)); }
     * end=cal.getTime(); }
     * 
     * stmt = singleDBConnection.prepareStatement("SELECT COUNT(*) FROM " +
     * ATTRIBUTESTORE_TABLE); ResultSet rs=stmt.executeQuery(); rs.next(); //Will
     * always have 1 row int count = rs.getInt(1);
     * 
     * output.setTotalItems(count); output.setItemsAddedDaysAgo(itemsAddedDaysAgo);
     * output.setLast100KeyValuesAdded(lastKeyValues);
     * output.setExtractTimeInMillis(System.currentTimeMillis()-t1);
     * 
     * 
     * return output; } catch (SQLException e){ e.printStackTrace();
     * log.error("SQL Exception in extractStatistics:"+e.getMessage()); throw e; }
     * finally{ closeStatement(stmt); }
     * 
     * 
     * }
     */

    protected void persistAttributeGroupsForLicense(Long licenseId, ArrayList<AttributeGroup> attributegroups)
            throws SQLException {

        if (attributegroups == null || attributegroups.size() == 0) {
            throw new IllegalArgumentException("No attributegroups defined for license");
        }

        for (AttributeGroup current : attributegroups) {
            long attributeGroupId = generateUniqueID();

            try (PreparedStatement stmt = connection.prepareStatement(persistAttributeGroupForLicenseQuery);) {

                stmt.setLong(1, attributeGroupId);
                stmt.setInt(2, current.getNumber());
                stmt.setLong(3, licenseId);
                stmt.execute();

                persistAttributesForAttributeGroup(attributeGroupId, current.getAttributes());
            } catch (SQLException e) {
                log.error("SQL Exception in persistAttributeGroupsForLicense: " + e.getMessage());
                throw e;
            }
        }
    }

    // This method delete by ID from a given tabel defined in the query
    protected void deleteById(String query, Long id) throws SQLException {

        try (PreparedStatement stmt = connection.prepareStatement(query);) {
            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            log.error("SQL Exception in deleteById for query: " + query + " Exception: " + e.getMessage());
            throw e;
        }
    }

    protected ArrayList<AttributeGroup> getAttributeGroupsForLicense(Long licenseId) throws SQLException {

        ArrayList<AttributeGroup> list = new ArrayList<AttributeGroup>();
        try (PreparedStatement stmt = connection.prepareStatement(selectAttributeGroupsForLicenseQuery);) {
            stmt.setLong(1, licenseId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Long id = rs.getLong(ID_COLUMN);
                int number = rs.getInt(NUMBER_COLUMN);
                AttributeGroup item = new AttributeGroup(number);
                item.setId(id);
                list.add(item);

                ArrayList<Attribute> attributes = getAttributesForAttributeGroup(id);
                item.setAttributes(attributes);
            }
            return list;
        } catch (SQLException e) {
            log.error("SQL Exception in getAttributeGroupsForLicense: " + e.getMessage());
            throw e;
        }
    }

    protected void persistAttributesForAttributeGroup(Long attributeGroupId, ArrayList<Attribute> attributes)
            throws SQLException {

        if (attributes == null || attributes.size() == 0) {
            throw new IllegalArgumentException("No attributes defined for attributegroup: " + attributeGroupId);
        }

        for (Attribute current : attributes) {
            try (PreparedStatement stmt = connection.prepareStatement(persistAttributeForAttributeGroupQuery);) {
                long attributeId = generateUniqueID();
                stmt.setLong(1, attributeId);
                stmt.setString(2, current.getAttributeName());
                stmt.setLong(3, attributeGroupId);
                stmt.execute();

                persistValuesForAttribute(attributeId, current.getValues());
            } catch (SQLException e) {
                log.error("SQL Exception in persistAttributesForAttributeGroup: " + e.getMessage());
                throw e;
            }
        }
    }

    protected ArrayList<LicenseContent> getLicenseContentsForLicense(Long licenseId) throws SQLException {
        ArrayList<LicenseContent> list = new ArrayList<LicenseContent>();
        try (PreparedStatement stmt = connection.prepareStatement(selectLicenseContentForLicenseQuery);) {

            stmt.setLong(1, licenseId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Long id = rs.getLong(ID_COLUMN);
                String name = rs.getString(NAME_COLUMN);
                LicenseContent item = new LicenseContent();
                item.setId(id);
                item.setName(name);
                list.add(item);

                ArrayList<Presentation> presentations = getPresentationsForLicenseContent(id);
                item.setPresentations(presentations);

            }
            return list;
        } catch (SQLException e) {
            log.error("SQL Exception in getLicenseContentsForLicense: " + e.getMessage());
            throw e;
        }

    }

    protected void persistLicenseContentsForLicense(Long licenseId, ArrayList<LicenseContent> licenseContents)
            throws SQLException {

        for (LicenseContent current : licenseContents) {

            try (PreparedStatement stmt = connection.prepareStatement(persistLicenseContentForLicenseQuery);) {
                long licenseContentId = generateUniqueID();

                stmt.setLong(1, licenseContentId);
                stmt.setString(2, current.getName());
                stmt.setLong(3, licenseId);
                stmt.execute();

                persistPresentationsForLicenseContent(licenseContentId, current.getPresentations());

            } catch (SQLException e) {
                log.error("SQL Exception in persistLicenseContentsForLicense: " + e.getMessage());
                throw e;
            }
        }
    }

    protected void persistPresentationsForLicenseContent(Long licenseContentId, ArrayList<Presentation> presentations)
            throws SQLException {

        if (presentations == null || presentations.size() == 0) {
            throw new IllegalArgumentException("No presentationtypes defined.(licensecontentId: " + licenseContentId +")");
        }

        for (Presentation current : presentations) {
            try (PreparedStatement stmt = connection.prepareStatement(persistPresentationTypesForLicenseContentQuery);) {

                stmt.setLong(1, generateUniqueID());
                stmt.setString(2, current.getKey());
                stmt.setLong(3, licenseContentId);
                stmt.execute();
            } catch (SQLException e) {
                log.error("SQL Exception in persistPresentationsForLicenseContent: " + e.getMessage());
                throw e;
            }
        }
    }

    protected ArrayList<Presentation> getPresentationsForLicenseContent(long licenseContentId) throws SQLException {
        
        ArrayList<Presentation> list = new ArrayList<Presentation>();                
        try (PreparedStatement stmt = connection.prepareStatement(selectPresentationTypesForLicenseContentQuery);) {
        
            stmt.setLong(1, licenseContentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Long id = rs.getLong(ID_COLUMN);
                String name = rs.getString(NAME_COLUMN);
                Presentation item = new Presentation(name);
                item.setId(id);
                list.add(item);
            }
            return list;
        } catch (SQLException e) {
            log.error("SQL Exception in getPresentationsForLicenseContent: " + e.getMessage());
            throw e;
        }

    }

    protected ArrayList<Attribute> getAttributesForAttributeGroup(long attributeGroupId) throws SQLException {
        
        ArrayList<Attribute> list = new ArrayList<Attribute>();
        
        try (PreparedStatement stmt = connection.prepareStatement(selectAttributesForAttributeGroupQuery);) {
        
            stmt.setLong(1, attributeGroupId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Long id = rs.getLong(ID_COLUMN);
                String name = rs.getString(NAME_COLUMN);
                Attribute item = new Attribute();
                item.setId(id);
                item.setAttributeName(name);

                ArrayList<AttributeValue> attributeValues = getValuesForAttribute(id);
                item.setValues(attributeValues);
                list.add(item);

            }
            return list;
        } catch (SQLException e) {
            log.error("SQL Exception in getAttributesForAttributeGroup: " + e.getMessage());
            throw e;
        } 
    }

    protected void persistValuesForAttribute(Long attributeId, ArrayList<AttributeValue> values) throws SQLException {

        if (values == null || values.size() == 0) {
            throw new IllegalArgumentException("No values defined for attribute: " + attributeId);
        }

        for (AttributeValue current : values) {

            try (PreparedStatement stmt = connection.prepareStatement(persistValueForAttributeQuery);) {
                stmt.setLong(1, generateUniqueID());
                stmt.setString(2, current.getValue());
                stmt.setLong(3, attributeId);
                stmt.execute();
            }

            catch (SQLException e) {
                log.error("SQL Exception in  persistValuesForAttribute: " + e.getMessage());
                throw e;
            }
        }
    }

    protected ArrayList<AttributeValue> getValuesForAttribute(long attributeId) throws SQLException {
        
        ArrayList<AttributeValue> list = new ArrayList<AttributeValue>();
        try (PreparedStatement stmt = connection.prepareStatement(selectValuesForAttributeQuery);) {      
        
            stmt.setLong(1, attributeId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Long id = rs.getLong(ID_COLUMN);
                String value = rs.getString(VALUE_COLUMN);
                AttributeValue item = new AttributeValue(value);
                item.setId(id);
                item.setValue(value);
                list.add(item);
            }
            return list;
        } catch (SQLException e) {
            log.error("SQL Exception in getValuesForAttribute: " + e.getMessage());
            throw e;
        }
    }

    /*
     * FOR TEST JETTY RUN ONLY!
     * 
     */
    public void createNewDatabase(String ddlFile) throws SQLException {
        connection.createStatement().execute("RUNSCRIPT FROM '" + ddlFile + "'");
    }

}
