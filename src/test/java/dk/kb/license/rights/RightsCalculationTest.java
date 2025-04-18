package dk.kb.license.rights;

import dk.kb.license.config.ServiceConfig;
import dk.kb.license.facade.RightsModuleFacade;
import dk.kb.license.model.v1.RestrictedIdInputDto;
import dk.kb.license.model.v1.RightsCalculationInputDto;
import dk.kb.license.model.v1.RightsCalculationOutputDto;
import dk.kb.license.storage.BaseModuleStorage;
import dk.kb.license.storage.DsLicenseUnitTestUtil;
import dk.kb.license.storage.RightsModuleStorage;
import dk.kb.license.util.H2DbUtil;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RightsCalculationTest extends DsLicenseUnitTestUtil {
    private final static Logger log = LoggerFactory.getLogger(RightsCalculationTest.class);

    @BeforeAll
    public static void beforeClass() throws IOException, SQLException {
        ServiceConfig.initialize("conf/ds-license*.yaml", "src/test/resources/ds-license-integration-test.yaml");
        // "ddl/rightsmodule_default_holdbackdata.sql"
        H2DbUtil.createEmptyH2DBFromDDL(URL,DRIVER,USERNAME,PASSWORD, List.of("ddl/rightsmodule_create_h2_unittest.ddl"));
        BaseModuleStorage.initialize(DRIVER, URL, USERNAME, PASSWORD);
    }

    @BeforeEach
    public void beforeEach() throws SQLException {
        try (RightsModuleStorage storage = new RightsModuleStorage()){
            storage.clearTableRecords();
        } catch (Exception e) {
            throw e;
        }
        H2DbUtil.createEmptyH2DBFromDDL(URL,DRIVER,USERNAME,PASSWORD, List.of("ddl/rightsmodule_default_holdbackdata.sql"));
    }

    @Test
    public void testHoldbackEducationEdgeCase() throws SQLException {
        RightsCalculationInputDto alwaysEducationRecord = new RightsCalculationInputDto("testRecord1", "2016-01-20T10:34:42+0100",
                RightsCalculationInputDto.PlatformEnum.DRARKIV,4411, 6000, 3190, 1000, "1000", "Program 1", "9283748300", "ds.tv");

        RightsCalculationOutputDto output = RightsModuleFacade.calculateRightsForRecord(alwaysEducationRecord);

        assertEquals("Undervisning", output.getDr().getHoldbackName());
    }

    @Test
    public void testHoldbackTrailersEdgeCase() throws SQLException {
        RightsCalculationInputDto alwaysEducationRecord = new RightsCalculationInputDto("testRecord1", "2016-01-20T10:34:42+0100",
                RightsCalculationInputDto.PlatformEnum.DRARKIV,7000, 1000, 3190, 1000, "1000", "Program 1", "9283748300", "ds.tv");

        RightsCalculationOutputDto output = RightsModuleFacade.calculateRightsForRecord(alwaysEducationRecord);

        assertEquals("", output.getDr().getHoldbackName());
        assertEquals("9999-01-01T00:00:00Z", output.getDr().getHoldbackExpiredDate());
    }

    @Test
    public void badHoldbackRecordTest() throws SQLException {
        RightsCalculationInputDto badHoldbackValues = new RightsCalculationInputDto("badValues", "2016-01-06T18:08:17+0100", RightsCalculationInputDto.PlatformEnum.DRARKIV, 1800
                , 0, 3100, 2211, "1000", "Record with bad holdback values", "9283748300", "ds.tv");

        RightsCalculationOutputDto output = RightsModuleFacade.calculateRightsForRecord(badHoldbackValues);

        assertEquals("9999-01-01T00:00:00Z", output.getDr().getHoldbackExpiredDate());
    }

    @Test
    public void testHoldbackDate() throws SQLException {

        RightsCalculationInputDto tenYearHoldbackRecord = new RightsCalculationInputDto("testRecord1", "2016-01-20T10:34:42+0100",
                RightsCalculationInputDto.PlatformEnum.DRARKIV,4411, 0, 3190, 1000, "1000", "Program 1", "9283748300", "ds.tv");

        RightsCalculationOutputDto output = RightsModuleFacade.calculateRightsForRecord(tenYearHoldbackRecord);

        assertEquals("2027-01-01T00:00:00Z", output.getDr().getHoldbackExpiredDate());
    }

    @Test
    public void testHoldbackDateNews() throws SQLException {
        RightsCalculationInputDto newsRecord = new RightsCalculationInputDto("testRecord1", "2016-01-01T10:34:42+0100",
                RightsCalculationInputDto.PlatformEnum.DRARKIV,1100, 0, 1200, 1000, "1000", "Program 1", "9283748300", "ds.tv");

        RightsCalculationOutputDto output = RightsModuleFacade.calculateRightsForRecord(newsRecord);

        assertEquals("2016-01-31T09:34:42Z", output.getDr().getHoldbackExpiredDate());
    }

    @Test
    public void testHoldbackName() throws SQLException {

        RightsCalculationInputDto tenYearHoldbackRecord = new RightsCalculationInputDto("testRecord1", "2016-01-20T10:34:42+0100",
                RightsCalculationInputDto.PlatformEnum.DRARKIV,4411, 0, 3190, 1000, "1000", "Program 1", "9283748300", "ds.tv");

        RightsCalculationOutputDto output = RightsModuleFacade.calculateRightsForRecord(tenYearHoldbackRecord);

        assertEquals("Dansk Dramatik & Fiktion", output.getDr().getHoldbackName());
    }

    @Test
    public void testHoldbackForeign() throws SQLException {

        RightsCalculationInputDto foreignRecord = new RightsCalculationInputDto("testRecord1", "2016-01-20T10:34:42+0100",
                RightsCalculationInputDto.PlatformEnum.DRARKIV,4411, 0, 3190, 5000, "5000", "Program 1", "9283748300", "ds.tv");

        RightsCalculationOutputDto output = RightsModuleFacade.calculateRightsForRecord(foreignRecord);

        assertEquals("Udenlandsk Dramatik & Fiktion", output.getDr().getHoldbackName());
        assertEquals("9999-01-01T00:00:00Z", output.getDr().getHoldbackExpiredDate());
    }

    @Test
    public void holdbackJanuaryEdgeTest() throws SQLException {
        RightsCalculationInputDto yearlyHoldback1 = new RightsCalculationInputDto("yearlyHoldback1","1999-01-01T10:30:00+0100", RightsCalculationInputDto.PlatformEnum.DRARKIV,
                4411, 0, 3190, 1000, "1000", "Program 1", "9283748300", "ds.tv" );
        RightsCalculationOutputDto output1 = RightsModuleFacade.calculateRightsForRecord(yearlyHoldback1);
        // 1st of January test
        assertEquals("2010-01-01T00:00:00Z", output1.getDr().getHoldbackExpiredDate());
    }

    @Test
    public void holdbackDecemberEdgeTest() throws SQLException {
        RightsCalculationInputDto yearlyHoldback2 = new RightsCalculationInputDto("yearlyHoldback1", "2010-12-31T10:00:00+0100", RightsCalculationInputDto.PlatformEnum.DRARKIV,
                4411, 0, 3190, 1000, "1000", "Program 1", "9283748300", "ds.tv");
        RightsCalculationOutputDto output2 = RightsModuleFacade.calculateRightsForRecord(yearlyHoldback2);
        // 31st of December test
        assertEquals("2021-01-01T00:00:00Z", output2.getDr().getHoldbackExpiredDate());
    }

    @Test
    public void holdbackYearlyRandomDateTest() throws SQLException {
        RightsCalculationInputDto yearlyHoldback3 = new RightsCalculationInputDto("yearlyHoldback1","1990-06-20T10:00:00+0100", RightsCalculationInputDto.PlatformEnum.DRARKIV,
                4411, 0, 3190, 1000, "1000", "Program 1", "9283748300", "ds.tv");
        RightsCalculationOutputDto output3 = RightsModuleFacade.calculateRightsForRecord(yearlyHoldback3);
        // Random date in June test
        assertEquals("2001-01-01T00:00:00Z", output3.getDr().getHoldbackExpiredDate());
    }

    @Test
    public void holdbackRadioTest() throws SQLException {
        RightsCalculationInputDto yearlyHoldback3 = new RightsCalculationInputDto("radioHoldback","1990-06-20T10:00:00+0100", RightsCalculationInputDto.PlatformEnum.DRARKIV,
                4411, 0, 3190, 1000, "1000", "Program 1", "9283748300", "ds.radio");
        RightsCalculationOutputDto radioHoldback = RightsModuleFacade.calculateRightsForRecord(yearlyHoldback3);
        assertEquals("1994-01-01T00:00:00Z", radioHoldback.getDr().getHoldbackExpiredDate());
    }


    @Test
    public void restrictedDrProductionIdTest() throws SQLException {
        try (RightsModuleStorage storage = new RightsModuleStorage()) {
            storage.createRestrictedId("1234567890", "dr_produktions_id", "dr", "Not allowed dr production ID", "TestUser", System.currentTimeMillis());
            storage.commit();
        } catch (Exception e) {
            throw e;
        }

        RightsCalculationInputDto drProductionIdRestrictedEntry = new RightsCalculationInputDto("Restricted DR Production ID","1990-06-20T10:00:00+0100",
                RightsCalculationInputDto.PlatformEnum.DRARKIV,
                4411, 0, 3190, 1000, "1000", "Program 1", "1234567890", "ds.tv");
        RightsCalculationOutputDto restrictedDrProductionID = RightsModuleFacade.calculateRightsForRecord(drProductionIdRestrictedEntry);

        assertTrue(restrictedDrProductionID.getDr().getDrIdRestricted());
    }

    @Test
    public void restrictedDsIdTest() throws SQLException {
        try (RightsModuleStorage storage = new RightsModuleStorage()) {
            storage.createRestrictedId("restrictedId","ds_id","dr","dangerous ID","TestUser",System.currentTimeMillis());
            storage.commit();
        } catch (Exception e) {
            throw e;
        }

        RightsCalculationInputDto restrictedDsId = new RightsCalculationInputDto("restrictedId","1990-06-20T10:00:00+0100",
                RightsCalculationInputDto.PlatformEnum.DRARKIV,
                4411, 0, 3190, 1000, "1000", "Program 1", "9283748300", "ds.tv");
        RightsCalculationOutputDto restrictedDsID = RightsModuleFacade.calculateRightsForRecord(restrictedDsId);

        assertTrue(restrictedDsID.getDr().getDsIdRestricted());
    }
    @Test
    public void restrictedTitleTest() throws SQLException {
        try (RightsModuleStorage storage = new RightsModuleStorage()) {
            storage.createRestrictedId("Restricted Test Title","strict_title","dr","This title can never be shown","TestUser",System.currentTimeMillis());
            storage.commit();
        } catch (Exception e) {
            throw e;
        }

        RightsCalculationInputDto restrictedTitleRecord = new RightsCalculationInputDto("restrictedId","1990-06-20T10:00:00+0100",
                RightsCalculationInputDto.PlatformEnum.DRARKIV,
                4411, 0, 3190, 1000, "1000", "Restricted Test Title", "9283748300", "ds.tv");
        RightsCalculationOutputDto restrictedTitleOutput = RightsModuleFacade.calculateRightsForRecord(restrictedTitleRecord);

        assertTrue(restrictedTitleOutput.getDr().getTitleRestricted());
    }

    @Test
    public void allowedProductionCodeFromMetadataTest() throws SQLException {
        try (RightsModuleStorage storage = new RightsModuleStorage()) {
            storage.createRestrictedId("1000","egenproduktions_kode","dr","1000 equals ownproduction","TestUser",System.currentTimeMillis());
            storage.commit();
        } catch (Exception e) {
            throw e;
        }

        RightsCalculationInputDto allowedOwnProductionCode = new RightsCalculationInputDto("restrictedId","1990-06-20T10:00:00+0100",
                RightsCalculationInputDto.PlatformEnum.DRARKIV,
                4411, 0, 3190, 1000, "1000", "Random program", "9283748300", "ds.tv");
        RightsCalculationOutputDto allowedOwnProduction = RightsModuleFacade.calculateRightsForRecord(allowedOwnProductionCode);

        assertTrue(allowedOwnProduction.getDr().getProductionCodeAllowed());
    }
}
