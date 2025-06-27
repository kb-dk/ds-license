package dk.kb.license.api.v1.impl;

import dk.kb.license.api.v1.*;
import java.util.ArrayList;

import dk.kb.license.facade.RightsModuleFacade;
import dk.kb.license.model.v1.AuditEntryOutputDto;
import dk.kb.license.model.v1.ChangeTypeEnumDto;
import java.util.List;
import java.util.Map;
import dk.kb.license.model.v1.ObjectTypeEnumDto;
import dk.kb.license.model.v1.RestrictedIdOutputDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.io.File;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.kb.util.webservice.ImplBase;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;
import javax.ws.rs.core.MediaType;
import org.apache.cxf.jaxrs.model.wadl.Description;
import org.apache.cxf.jaxrs.model.wadl.DocTarget;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.ext.multipart.*;
import org.apache.cxf.interceptor.InInterceptors;

import dk.kb.util.webservice.exception.InvalidArgumentServiceException;
import dk.kb.util.webservice.exception.NotFoundServiceException;
import dk.kb.license.facade.AuditFacade;

import io.swagger.annotations.Api;

/**
 * ds-license
 *
 * <p># Ds-license(Digitale Samlinger) by the Royal Danish Library.        ## Ds-license restricts access to items in collections based on the user credential information. The primary method in Ds-license is to filter a list of IDs (recordIds) and only return the subset  ID's that the user has access to based in the user information. Access can be restricted  to a presentationtype value. (Download,Search etc.) The filtering is done against a Solr server with all information about the records. The application has a GUI interface to define all access rules based on user attributes.   ## User attributes The User attributes is key-value pairs, where the values can be a list and a user can have multiple key-value pairs. All values and keys are strings. The key-values pairs can be WAYF attributes (https://www.wayf.dk/) which is a  standard for describing users at educational institutions and will have some guarateed values always. But the GUI administration can define arbitarity key-value rules and not just WAYF attributes. All user atttribute keys must be define in  the GUI and they will be available for use when defining a license attribute-group.  ### Examples of UserAttributes (WAYF)        | key                         | values                     | Remark                                          |  | ----------------------------| ---------------------------|-------------------------------------------------| | SBIPRolemapper              | inhouse,kb                 | inhouse(local computer), kb (organisation)      | | mail                        | teg@kb.dk                  | Can be used to give individual access           | | shachHomeOrganisation       | ku.dk                      | Educational institution                         | | eduPersonPrimaryAffiliation | stud                       | Student. (staff, employee,faculty also values)  |     ## Packages (pakker) and restrictions (klausulering) The packages and restrictions are the building blocks that gives access to materials. In  the UML they are both groups, but on the GUI they will be seen as two seperate entities.  Packages gives access to materials (positive Solr filter) while restrictions forbid access (negative Solr filter).  The more packages groups a user validates will give more materials and more validated restrictions will also give more materials since this will remove the negative filter. The restrictions negative filters are always applied unless the user validates access to them and that restriction will be removed. Some materials can be locked under several different restrictions and all must validate before the user can access it.  Giving a user access   to a restriction will not mean the user can see all the restricted material. He can still only see what  the packages give access too and the restricted material within those packages. Whenever a new restriction is added to the configuration it will take immediate effect and block all access for all users. To give access to the new restriction a licence needed to be edited and configure access to that restriction.      ### Example of access with two groups Group 1 (package): lma_long:\"radio\"  Group 2 (restriction):  klausuleret:\"ja\"  If the user has acesss to both groups, the final filter query will be:   **lma_long:\"radio\"**  If the user has only access to the first group, the final filter query will be:  **lma_long:\"radio\" -klausuleret:\"ja\"**  ### Example of access with four groups The normal groups filter queries will be OR'ed.  Group 1 (package): lma_long:\"tv\"  Group 2 (package): lma_long:\"radio\"     Group 3 (restriction):  klausuleret:\"ja\"  Group 4 (restiction):  individuelt_forbud:\"ja\"  If the user has access to group 1,2 and 3 the filter query will be: **(lma_long:\"radio\" OR lma_long\"tv\") -individuelt_forbud:\"ja\"**      ## Licences Licenses are the mapping from UserAttributes to groups/packages. One license can give access to several groups.  For each group a license gives access to, the license must also specify a presentation type (or several) for that group. Presentation types are also defined in the GUI and examples of presentation types are:Stream,Search.Download,Thumbnails,Headlines.  ### Licenses structure A license must define a valid from date and valid to date and is only valid in the date interval. The format is dd-mm-yyyy. A license has to defines one or more attribute-groups. An attribute-group is mapping from UserAttributes keys to values. An attribute-group can define several mappings and every single mapping in the attribute group has to validate for the attribute-group to validate, which will then validate the whole license.So a license can have several attribute groups and just one of them has to validate for the whole license to validate. The reason you can define several attribute-groups in one license is to avoid defining many identical licences that gives  access to same material but by different conditions.  ### License validation algoritm First the validation check will limit to licenses that are valid for the date of the requests. Then for each license every  attributed group will be checked. If just one of the attribute groups validates then the license validates.  The license will give access to the groups (pakker) defined for the license, but restricted to the presentationtype (Download etc.) Every license that validate will give access to more material. All the allow groups will each expand the positive filter query used for filtering Ids. All restriction-group validated will remove the negative filter blocked by that restriction-group. See 'uml/License_validation_logic.png' for a visualisation  of the validation logic for a license.   ## UML model  The UML diagram can be found in the /uml/licensemodule_uml.png folder. The database persistence model and object layer is a  direct implementation of the UML model, except for some naming. The UML model is created using DIA and the project file is also in the folder.      ### UML model explained There are 3 general type classes that can be maintained and the values are configured are used when defining a license. It is not possible to delete a configuration value if it is used in a license.  ## Configuration of the Ds-license   ### Property: solr.servers The configuration requires at least one Solr server for the property 'solr.servers'. When filtering IDs all Solr servers will be called for filtering and each will be called with all IDs.          ### Property: solr.idField The Solr field used for filtering. Multivalued fields allowed. This field must exist in the schema for the Solr servers. Example: If the filter field is 'id' and the filter query generated by from the package is 'subject:danmark' Then the query will be (id:id1 OR id:id2 OR id:id3..) AND (subject:Denmark). Solr will return documents that is a  subset of the ID's asked for. And this is what happens in the id-filtering.   ### Properties:  url,driver,username,password  The 4 database properties  (JDBC) must be defined: url,driver,username,password      A PostGreSql database is recommended when not running locally. The database tables must be created before use with DDL file: test/resources/ddl/licensemodule_create_db.ddl   ## API    While there are many methods exposed through the API only the following two are necessary for integrating License module into the software stack.  #### checkAccessForIds This is the method that takes the UserAttributes and a list of IDs. The return value will be the IDs that was not removed in the filtering and those that the user has access to. The method is called every time a user tries to access or search materials.  #### getUserLicenses Method only takes the userattributes and returns a list of licences that the userattributes give access to. For each license the name, validFrom,ValidTo and a description will be returned. This information can be shown to the user.  ## Rights API The methods exposed in the Rights API enables the add and editing data for rights calculations.  There are to types of rights calculations. - restricted IDs (klausuleringer) - holdback expire date  ### restricted IDs (klausuleringer) The restricted IDs API enables blacklisting (and whitelisting) of records based on an ID in the metadata.  Whenever a restricted ID is added or deleted, the corresponding records are reindexed.  There are 4 different types of Ids supported. #### DS_ID Restrict (blacklist) a record based on the local id in ds.  #### DR_PRODUCTION_ID Restrict (blacklist) records based on the DR production ID. Only relevant for 'DR Arkivet'. Note that multiple records in ds can have the same DR production ID.  #### STRICT_TITLE Restrict (blacklist) records based on their title. Note that the title of the record has to match the restricted title exactly.  #### OWNPRODUCTION_CODE The Rightsmodule also contains a list of production codes, used for identifying if a radio or tv record is ownproduction and thus can be shown in the DR archive.  ### Holdback calculations The rights API exposes methods to update two types of data for calculating when the holdback of a record expires. The holdback calculations are only relevant for the DR Arkiv records.  #### Holdback Rules A set of rules (formål) specifying the number of holdback days. The rules are identified by their name and id. If the number of days is less than 365, the holdback days are added directly to the publication date. If the holdback days are equal to or greater 365, the holdback days are added to the first January 1. following the publication date.  #### Holdback ranges Each record is mapped to a holdback rule by looking at the values for their form and content (indhold). The ranges are used to to map a record to a holdback rule.   Developed and maintained by the Royal Danish Library. 
 *
 */
@InInterceptors(interceptors = "dk.kb.license.webservice.KBAuthorizationInterceptor")
public class DsAuditApiServiceImpl extends ImplBase implements DsAuditApi {

    @Override
    public List<AuditEntryOutputDto> getAuditEntries(Long objectId) {
        try {
//          log.debug("Get restricted ID id:{} idType:{} platform:{}", id, idType, platform);
            ArrayList<AuditEntryOutputDto> result = AuditFacade.getAuditEntries(objectId);
            if (result == null) {
                throw new NotFoundServiceException("Auditlog id not found");
            }
            return result;
        } catch (Exception e) {
            throw handleException(e);
        }
    }

    @Override
    public AuditEntryOutputDto getAuditEntry(Long id) {
        try {
//          log.debug("Get restricted ID id:{} idType:{} platform:{}", id, idType, platform);
            AuditEntryOutputDto result = AuditFacade.getAuditEntry(id);
            if (result == null) {
                throw new NotFoundServiceException("Auditlog id not found");
            }
            return result;
        } catch (Exception e) {
            throw handleException(e);
        }
    }
}
