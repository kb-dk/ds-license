package dk.kb.license;

import dk.kb.license.model.v1.RestrictedIdOutputDto;
import dk.kb.license.model.v1.RestrictionsCalculationInputDto;
import dk.kb.license.model.v1.RightsCalculationInputDto;
import dk.kb.license.model.v1.RightsCalculationOutputDrDto;
import dk.kb.license.model.v1.RightsCalculationOutputDto;
import dk.kb.license.storage.BaseModuleStorage;
import dk.kb.license.storage.RightsModuleStorage;
import dk.kb.util.webservice.exception.InternalServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class RightsCalculation {
    private final static Logger log = LoggerFactory.getLogger(RightsCalculation.class);


    public static void testMethod(){
        RightsCalculationOutputDto rightsCalculationOutputDto = new RightsCalculationOutputDto();

                // Get holdback name
        // Get holdback expired - do calculations
        // Get title restricted

    }

    /**
     * Check if a given DS id is restricted.
     * @param id of DsRecord
     * @return true if restricted, otherwise false.
     */
    public static boolean isDsIdRestricted(String id){
        try {
            return BaseModuleStorage.performStorageAction("Get restricted ID", new RightsModuleStorage(), storage ->
                    performLookupInRestrictionsTable(id, "ds_id",  storage));
        } catch (SQLException e) {
            throw new InternalServiceException("An SQL exception happened while checking for ID restriction", e);
        }
    }

    /**
     * Check if a given DR production id is restricted.
     * @param id from DRs production ID metadata.
     * @return true if restricted, otherwise false.
     */
    public static boolean isDrProductionIdRestricted(String id){
        try {
            return BaseModuleStorage.performStorageAction("Get restricted ID", new RightsModuleStorage(), storage ->
                    performLookupInRestrictionsTable(id, "dr_produktions_id", storage));
        } catch (SQLException e) {
            throw new InternalServiceException("An SQL exception happened while checking for ID restriction", e);
        }
    }
    /**
     * Check if a given record is restricted in the DR platform.
     * @param id from DRs production ID metadata.
     * @return true if restricted, otherwise false.
     */
    public static boolean isTitleRestricted(String id){
        try {
            return BaseModuleStorage.performStorageAction("Get restricted Title", new RightsModuleStorage(), storage ->
                    performLookupInRestrictionsTable(id, "strict_title", storage));
        } catch (SQLException e) {
            throw new InternalServiceException("An SQL exception happened while checking for ID restriction", e);
        }
    }

    private static boolean performLookupInRestrictionsTable(String id, String idType, BaseModuleStorage storage) throws SQLException {
        log.debug("Performing lookup in restrictions table for id: '{}', with idType: '{}'", id, idType);
        RestrictedIdOutputDto idOutput = ((RightsModuleStorage)storage).getRestrictedId(id, idType, "dr");
        // If the object is null, then id is not restricted
        return idOutput != null;
    }

    /**
     * Check if a given productionCode from the metadata for a given record is allowed in the system.
     * @param productionCode from tvmeter/ritzau metadata
     * @return true if allowed, otherwise false
     */
    public static boolean isProductionCodeAllowed(String productionCode){
        try {
            return BaseModuleStorage.performStorageAction("Get restricted ID", new RightsModuleStorage(), storage -> {
                RestrictedIdOutputDto idOutput = ((RightsModuleStorage)storage).getRestrictedId(productionCode, "egenproduktions_kode", "dr");

                if (idOutput == null){
                    log.warn("The specified production code '{}' is not known in the database. Therefore it cannot be allowed.", productionCode);
                    return false;
                } else {
                    log.debug("Production Code is allowed");
                    return true;
                }
           });
        } catch (SQLException e) {
            throw new InternalServiceException("An SQL exception happened while checking if production code is allowed to be shown.", e);
        }
    }

    /**
     * For a {@link RightsCalculationInputDto} do all calculations needed to return a {@link RightsCalculationOutputDto} containing all information needed
     * for a record in the DR Archive.
     * @param rightsCalculationInputDto object containing all values needed for RightsCalculation.
     * @return a {@link RightsCalculationOutputDto} containing all fields and values that are needed as part of a solr document to manage rights and restrictions in the
     * LicenseModule part of DS-License.
     */
    public static RightsCalculationOutputDrDto calculateDrRights(RightsCalculationInputDto rightsCalculationInputDto) {
        RightsCalculationOutputDrDto drOutput = new RightsCalculationOutputDrDto();

        // TODO: Refactor restrictions based parts to own method and create method for holdback parts as well
        // Do all restrictions checks against the restricted_ids table of the RightsModule
        RestrictionsCalculationInputDto restrictionsInput = rightsCalculationInputDto.getRestrictionsInput();
        boolean dsIdRestricted = isDsIdRestricted(restrictionsInput.getRecordId());
        boolean drProductionIdRestricted = isDrProductionIdRestricted(restrictionsInput.getDrProductionId());
        boolean isProductionCodeAllowed = isProductionCodeAllowed(restrictionsInput.getProductionCode());
        boolean isTitleRestricted = isTitleRestricted(restrictionsInput.getTitle());

        drOutput.setDsIdRestricted(dsIdRestricted);
        drOutput.setDrIdRestricted(drProductionIdRestricted);
        drOutput.setProductionCodeAllowed(isProductionCodeAllowed);
        drOutput.setTitleRestricted(isTitleRestricted);
        return drOutput;
    }
}
