package dk.kb.license;

import dk.kb.license.config.ServiceConfig;
import dk.kb.license.facade.RightsModuleFacade;
import dk.kb.license.model.v1.DrHoldbackRuleDto;
import dk.kb.license.model.v1.HoldbackCalculationInputDto;
import dk.kb.license.model.v1.RestrictedIdOutputDto;
import dk.kb.license.model.v1.RestrictionsCalculationInputDto;
import dk.kb.license.model.v1.RightsCalculationInputDto;
import dk.kb.license.model.v1.RightsCalculationOutputDrDto;
import dk.kb.license.model.v1.RightsCalculationOutputDto;
import dk.kb.license.storage.BaseModuleStorage;
import dk.kb.license.storage.RightsModuleStorage;
import dk.kb.util.DatetimeParser;
import dk.kb.util.MalformedIOException;
import dk.kb.util.webservice.exception.InternalServiceException;
import dk.kb.util.webservice.exception.InvalidArgumentServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
            return BaseModuleStorage.performStorageAction("Get restricted Title", new RightsModuleStorage(), storage -> {
                return performLookupInRestrictionsTable(id, "strict_title", storage);
            });
        } catch (SQLException e) {
            throw new InternalServiceException("An SQL exception happened while checking for ID restriction", e);
        }
    }

    private static boolean performLookupInRestrictionsTable(String id, String idType, BaseModuleStorage storage) throws SQLException {
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
    public static RightsCalculationOutputDrDto calculateDrRights(RightsCalculationInputDto rightsCalculationInputDto) throws SQLException {
        RightsCalculationOutputDrDto drOutput = new RightsCalculationOutputDrDto();

        setRestrictionsForRecordDrArchive(rightsCalculationInputDto, drOutput);
        if (rightsCalculationInputDto.getHoldbackInput().getOrigin().equals("ds.tv")){
            setHoldbackForTvRecordDrArchive(rightsCalculationInputDto, drOutput);
        } else if (rightsCalculationInputDto.getHoldbackInput().getOrigin().equals("ds.radio")){
            setHoldbackForRadioRecordDrArchive(rightsCalculationInputDto, drOutput);
        } else {
            throw new InvalidArgumentServiceException("DR Holdback can only be calculated for records from origins: 'ds.radio' and 'ds.tv'");
        }

        return drOutput;
    }


    /**
     * Sets the restrictions for a record in the DR Archive based on the provided
     * rights calculation input. This method performs checks against the
     * restricted_ids table of the RightsModule to determine if specific
     * identifiers and codes are restricted.
     *
     * <p>This method processes the following checks:</p>
     * <ul>
     *   <li>Checks if the dataset ID is restricted.</li>
     *   <li>Checks if the DR production ID is restricted.</li>
     *   <li>Checks if the production code is allowed.</li>
     *   <li>Checks if the title is restricted.</li>
     * </ul>
     *
     * <p>The results of these checks are then set in the provided
     * {@link RightsCalculationOutputDrDto} object.</p>
     *
     * @param rightsCalculationInputDto the input DTO containing the restrictions
     *                                    input to be checked. It should not be
     *                                    null.
     * @param drOutput the output DTO where the results of the restrictions
     *                  checks will be set. It should not be null.
     *
     * @throws IllegalArgumentException if either {@code rightsCalculationInputDto}
     *                                  or {@code drOutput} is null.
     */
    private static void setRestrictionsForRecordDrArchive(RightsCalculationInputDto rightsCalculationInputDto, RightsCalculationOutputDrDto drOutput) {
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
    }

    private static void setHoldbackForTvRecordDrArchive(RightsCalculationInputDto rightsCalculationInputDto, RightsCalculationOutputDrDto drOutput) throws SQLException {
        HoldbackCalculationInputDto holdbackInput = rightsCalculationInputDto.getHoldbackInput();
        String recordId = rightsCalculationInputDto.getRecordId();
        String startDate = rightsCalculationInputDto.getStartTime();

        DrHoldbackRuleDto holdbackRule = getHoldbackRule(holdbackInput);

        String holdbackName = getHoldbackName(holdbackInput, holdbackRule);
        String holdbackExpiredDate = getHoldbackExpiredDate(holdbackRule, recordId, startDate);

        drOutput.setHoldbackName(holdbackName);
        drOutput.setHoldbackExpiredDate(holdbackExpiredDate);
    }

    private static void setHoldbackForRadioRecordDrArchive(RightsCalculationInputDto rightsCalculationInputDto, RightsCalculationOutputDrDto drOutput) {
        String holdbackExpiredDate = calculateHoldbackDateByYears(rightsCalculationInputDto.getStartTime(), 3);

        drOutput.setHoldbackName("Radio");
        drOutput.setHoldbackExpiredDate(holdbackExpiredDate);
    }

    private static DrHoldbackRuleDto getHoldbackRule(HoldbackCalculationInputDto holdbackInput) throws SQLException {
        // Get form value
        int form = holdbackInput.getForm();
        // get contentsitem/indhold value
        int content = holdbackInput.getIndhold();
        String holdbackId = RightsModuleFacade.getHoldbackIdFromContentAndFormValues(content, form);

        if (holdbackId.isEmpty()){
            // An empty rule should end with a holdback date of 9999-01-01 as we cant calculate holdback for these records.
            return new DrHoldbackRuleDto();
        }
        // Check for ID being 2.05 and correct it to either 2.05.01 or 2.05.02
        holdbackId = validateHoldbackBasedOnProductionCountry(holdbackId, holdbackInput.getProductionCountry());

        return RightsModuleFacade.getDrHoldbackRuleById(holdbackId);
    }


    /**
     * Retrieves the holdback name based on the provided holdback calculation input
     * and holdback rule. This method applies specific logic based on the values
     * in the input DTO to determine the appropriate holdback name.
     *
     * <p>The method handles the following cases:</p>
     * <ul>
     *   <li>If the purpose ({@code hensigt}) in the input is `6000`, the method
     *       returns the name "Undervisning" regardless of other input values.</li>
     *   <li>If the form in the input is `7000`, the method returns an empty string,
     *       which is later interpreted as a holdback date of year `9999`. This is
     *       because records with a form of `7000` are trailers and should be filtered away.</li>
     *   <li>For any other cases, the method returns the name from the provided
     *       {@link DrHoldbackRuleDto} object.</li>
     * </ul>
     *
     * @param holdbackInput the input DTO containing holdback calculation data.
     *                      Must not be null.
     * @param holdbackRule the holdback rule DTO containing the holdback name.
     *                     Must not be null.
     * @return the determined holdback name as a String.
     * @throws SQLException if a database access error occurs during the operation.
     * @throws NullPointerException if either {@code holdbackInput} or
     *                              {@code holdbackRule} is null.
     */
    private static String getHoldbackName(HoldbackCalculationInputDto holdbackInput, DrHoldbackRuleDto holdbackRule) {
        // If purpose is 6000, then the purpose name is "Undervisning" no matter what.
        if (holdbackInput.getHensigt() == 6000){
            log.debug("Nielsen/TVMeter intent is: '6000', therefore the holdback name is set as 'Undervisning'.");
            return "Undervisning";
        }
        // IF form = 7000 return an empty string, which later translate to a holdback date of year 9999 as records with form = 7000 are trailers and should be filtered out.
        if (holdbackInput.getForm() == 7000){
            return "";
        }

        return holdbackRule.getName();
    }

    /**
     * Validates the holdback ID based on the specified production country.
     * This method includes special handling for a specific holdback ID
     * ("2.05"), where the production country is used to determine the
     * resulting holdback ID.
     *
     * <p>If the holdback ID is "2.05" and the production country is "1000",
     * the method appends ".01" to the holdback ID which means that it is produced in Denmark.
     * For any other production country, it appends ".02" to the holdback ID which means anywhere not Denmark.
     * For all other holdback IDs, the method returns the holdback ID unchanged.</p>
     *
     * @param holdbackId the holdback ID to validate, should not be null.
     * @param productionCountry the production country identifier as an
     *                          int, should not be null.
     * @return the validated holdback ID as a String.
     *
     * @throws NullPointerException if either {@code holdbackId} or
     *                              {@code productionCountry} is null.
     */
    private static String validateHoldbackBasedOnProductionCountry(String holdbackId, int productionCountry) {
        // Handling of special case for purposeID 2.05, where country of production is needed to create the correct value.
        if (holdbackId.equals("2.05")) {
            return productionCountry == 1000 ? holdbackId + ".01" : holdbackId + ".02";
        } else {
            return holdbackId;
        }
    }

    /**
     * Calculates the holdback expiration date based on the specified holdback rule and start date.
     *
     * <p>If the holdback rule's name is empty, the method logs a debug message and returns a default expiration
     * date of "9999-01-01T00:00:00Z". If the holdback days are less than a configured threshold (default = 365), it calculates
     * the expiration date by adding the holdback days directly to the start date. If the holdback days are
     * equal to or greater than the threshold, it calculates the expiration date on a yearly basis form the following 1st of January by converting
     * the holdback days to years and adjusting the start date accordingly.</p>
     *
     * @param holdbackRule The holdback rule containing the number of holdback days and its name.
     * @param recordId identifier of record associated with this holdback rule.
     * @param startDate The start date which the holdbackExpiredDate will be calculated from.
     * @return The calculated holdback expiration date in ISO-8601 ({@link DateTimeFormatter#ISO_INSTANT}-format).
     */
    private static String getHoldbackExpiredDate(DrHoldbackRuleDto holdbackRule, String recordId, String startDate) {
        if (holdbackRule.getName() == null || holdbackRule.getName().isEmpty()){
            log.debug("Purpose name was empty for record with id: '{}'. Setting holdback date to 9999-01-01T00:00:00Z", recordId);
            return "9999-01-01T00:00:00Z";
        }

        if (holdbackRule.getId().equals("2.05.02")){
            log.debug("Foreign produced fictional content cannot be shown. Setting holdback date to 9999-01-01T00:00:00Z for record with id: '{}'", recordId);
            return "9999-01-01T00:00:00Z";
        }

        int holdbackDays = holdbackRule.getDays();

        if (holdbackDays < ServiceConfig.getHoldbackLogicChangeDays()) {
            log.debug("Calculating holdback on a daily basis for record: '{}'", recordId);
            // Add holdback days directly to startTime if holdback is less than a year
            return calculateHoldbackDateByDays(ZonedDateTime.parse(startDate), holdbackDays);
        } else {
            log.debug("Calculating holdback on a yearly basis for record: '{}'", recordId);
            // When holdback is more than a year, it should be calculated from the 1st of January the following year and days aren't actually needed more, so we convert
            // days to years, to make it easier to add correct amount of time
            int holdbackYears = holdbackDays / 365;
            return calculateHoldbackDateByYears(startDate, holdbackYears);
        }
    }


    /**
     * Apply the amount of holdback days to the start date and return the date for when holdback has expired.
     * @param startDate a date representing the date when a program was broadcast.
     * @param holdbackDays the amount of days that has to parse before a program can be retrieved in the archive.
     * @return the date, when the holdback period has expired as a string in the format: yyyy-MM-dd'T'HH:mm:ssZ.
     */
    private static String calculateHoldbackDateByDays(ZonedDateTime startDate, int holdbackDays) {
        ZonedDateTime holdbackExpiredDate = startDate.plusDays(holdbackDays);

        // Using .ISO_INSTANT as this is solr standard
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;

        String formattedHoldbackDate = holdbackExpiredDate.format(formatter);
        return formattedHoldbackDate;
    }

    /**
     * Apply the amount of holdback years to the start date and return the date for when holdback has expired.
     * @param startDate a date representing the date when a program was broadcast.
     * @param holdbackYears the amount of years that has to parse before a program can be retrieved in the archive.
     * @return the date, when the holdback period has expired as a string in the format: yyyy-MM-dd'T'HH:mm:ssZ.
     */
    private static String calculateHoldbackDateByYears(String startDate, int holdbackYears) {
        ZonedDateTime cleanedStartDate = getCleanZonedDateTimeFromString(startDate);
        ZonedDateTime holdbackCalculationStartDate = getFirstComingJanuary(cleanedStartDate);
        ZonedDateTime holdbackExpiredDate = holdbackCalculationStartDate.plusYears(holdbackYears);

        // Using .ISO_INSTANT as this is solr standard
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;

        String formattedHoldbackDate = holdbackExpiredDate.format(formatter);
        return formattedHoldbackDate;
    }

    /**
     * Get 1st of January for the following year for any LocalDateTime.
     * @param dateTime to extract year from
     * @return a new ZonedDateTime with the date 1st of january next year from the input datetime
     */
    public static ZonedDateTime getFirstComingJanuary(ZonedDateTime dateTime) {
        int year = dateTime.getYear();
        return ZonedDateTime.of(year + 1, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
    }


    /**
     * Parse a date-time string into a {@link ZonedDateTime} object using the format "yyyy-MM-dd'T'HH:mm:ss[XX][XXX]".
     *
     * @param datetime The date-time string to be parsed.
     * @return A {@link ZonedDateTime} object representing the parsed date-time.
     */
    public static ZonedDateTime getCleanZonedDateTimeFromString(String datetime){
        String dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss[XX][XXX]";
        try {
            return DatetimeParser.parseStringToZonedDateTime(datetime, dateTimeFormat);
        } catch (MalformedIOException e) {
            throw new RuntimeException(e);
        }
    }
}
