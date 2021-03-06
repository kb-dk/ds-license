package dk.kb.license.api.v1.impl;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import dk.kb.license.model.v1.*;
import dk.kb.license.api.v1.DsLicenseApi;
import dk.kb.license.facade.LicenseModuleFacade;

import dk.kb.license.storage.ConfiguredLicensePresentationType;
import dk.kb.license.storage.License;
import dk.kb.license.validation.LicenseValidator;


import dk.kb.license.webservice.exception.ServiceException;
import dk.kb.license.webservice.exception.InternalServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.io.File;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.validation.Valid;
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
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;
import javax.ws.rs.core.MediaType;
import org.apache.cxf.jaxrs.model.wadl.Description;
import org.apache.cxf.jaxrs.model.wadl.DocTarget;
import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.ext.multipart.*;

import io.swagger.annotations.Api;

/**
 * ds-license
 *
 * <p>ds-license by the Royal Danish Library 
 *
 */
public class DsLicenseApiServiceImpl implements DsLicenseApi {
    private Logger log = LoggerFactory.getLogger(this.toString());



    /* How to access the various web contexts. See https://cxf.apache.org/docs/jax-rs-basics.html#JAX-RSBasics-Contextannotations */

    @Context
    private transient UriInfo uriInfo;

    @Context
    private transient SecurityContext securityContext;

    @Context
    private transient HttpHeaders httpHeaders;

    @Context
    private transient Providers providers;

    @Context
    private transient Request request;

    // Disabled as it is always null? TODO: Investigate when it can be not-null, then re-enable with type
    //@Context
    //private transient ContextResolver contextResolver;

    @Context
    private transient HttpServletRequest httpServletRequest;

    @Context
    private transient HttpServletResponse httpServletResponse;

    @Context
    private transient ServletContext servletContext;

    @Context
    private transient ServletConfig servletConfig;

    @Context
    private transient MessageContext messageContext;



    /**
     * Request a Hello World message, for testing purposes
     * 
     * @param alternateHello: Optional alternative to using the word &#39;Hello&#39; in the reply
     * 
     * @return <ul>
     *   <li>code = 200, message = "A JSON structure containing a Hello World message", response = HelloReplyDto.class</li>
     *   </ul>
     * @throws ServiceException when other http codes should be returned
     *
     * @implNote return will always produce a HTTP 200 code. Throw ServiceException if you need to return other codes
     */
    @Override
    public HelloReplyDto getGreeting(String alternateHello) throws ServiceException {
        // TODO: Implement...


        try { 
            HelloReplyDto response = new HelloReplyDto();
            response.setMessage("KbqLzzD6");
            return response;
        } catch (Exception e){
            throw handleException(e);
        }

    }

    /**
     * Ping the server to check if the server is reachable.
     * 
     * @return <ul>
     *   <li>code = 200, message = "OK", response = String.class</li>
     *   <li>code = 406, message = "Not Acceptable", response = ErrorDto.class</li>
     *   <li>code = 500, message = "Internal Error", response = String.class</li>
     *   </ul>
     * @throws ServiceException when other http codes should be returned
     *
     * @implNote return will always produce a HTTP 200 code. Throw ServiceException if you need to return other codes
     */
    @Override
    public String ping() throws ServiceException {
        // TODO: Implement...


        try { 
            String response = "C8wES";
            return response;
        } catch (Exception e){
            throw handleException(e);
        }

    }

    @Override
    public CheckAccessForIdsOutputDto checkAccessForIds(@NotNull CheckAccessForIdsInputDto input) {

        //MonitorCache.registerNewRestMethodCall("checkAccessForIds");
        try{

            ConfiguredLicensePresentationType presentationType = LicenseValidator.matchPresentationtype(input.getPresentationType());
        }
        catch(IllegalArgumentException e){
            log.error("Unknown presentationtype:"+input.getPresentationType());
            CheckAccessForIdsOutputDto output =  new CheckAccessForIdsOutputDto();
            output.setAccessIds(new ArrayList<String>());
            output.setPresentationType(input.getPresentationType());
            output.setQuery("(NoAccess:NoAccess)"); //query that returns nothing
            return output;
        }

        try {      
            CheckAccessForIdsOutputDto output = LicenseValidator.checkAccessForIds(input);      
            return output;
        } catch (Exception e) {
            log.error("Error in checkAccessForIds:",e);
            throw handleException(e);
        }   
    }

    @Override
    public ValidateAccessOutputDto validateAccess(@Valid ValidateAccessInputDto input) {

        System.out.println("validate access called");
        //MonitorCache.registerNewRestMethodCall("validateAccess");

        try {
            boolean access =  LicenseValidator.validateAccess(input);   
            ValidateAccessOutputDto output = new ValidateAccessOutputDto();
            output.setAccess(access);
            return output;              
        } catch (Exception e) {
            throw handleException(e);
        }   

    }


    @Override
    public GetUsersLicensesOutputDto getUserLicenses(@NotNull GetUsersLicensesInputDto input) {
        //  MonitorCache.registerNewRestMethodCall("getUserLicenses");
        log.info("getUserLicenses called");

        ArrayList<LicenseOverviewDto> list = new ArrayList<LicenseOverviewDto>();
        GetUsersLicensesOutputDto output = new GetUsersLicensesOutputDto();
        output.setLicenses(list);
        try {   
            ArrayList<License> licenses = LicenseValidator.getUsersLicenses(input);

            for (License current: licenses){
                LicenseOverviewDto  item = new LicenseOverviewDto();
                if ("en".equals(input.getLocale())){                      
                    item.setName(current.getLicenseName_en());
                    item.setDescription(current.getDescription_en());         

                }
                else{
                    item.setName(current.getLicenseName());
                    item.setDescription(current.getDescription_dk());              
                }

                item.setValidFrom(current.getValidFrom());
                item.setValidTo(current.getValidTo());    
                list.add(item);
            }     
            return output;    
        } catch (Exception e) {
            throw handleException(e);
        }   
    }






    
    public String getUserLicenseQuery(@NotNull GetUserQueryInputDto input) {

        //MonitorCache.registerNewRestMethodCall("getUserLicenseQuery");
        log.info("getUserLicenseQuery called");
        try {
            GetUserQueryOutputDto output = LicenseValidator.getUserQuery(input);   
            
log.info("-------------------getUserLicenseQuery----------------");
log.info("input (presentationtype): "+input.getPresentationType());
log.info("input (attributes): "+input.getAttributes());         
log.info("output (User license query):"+output.getQuery());
log.info("output (groups)" + output.getUserLicenseGroups());
            
            return output.getQuery();  
        } catch (Exception e) {
            throw handleException(e);
        }   

    }



    @Override
    public GetUserGroupsOutputDto getUserGroups(GetUserGroupsInputDto input) {                  
        //MonitorCache.registerNewRestMethodCall("getUserGroups"); 
        log.info("getUserGroups called");
        try {      
            ArrayList<UserGroupDto> groups = LicenseValidator.getUsersGroups(input);                     
            GetUserGroupsOutputDto output = new GetUserGroupsOutputDto();
            output.setGroups(groups);

            return output;    
        } catch (Exception e) {
            throw handleException(e);
        }   

    }


    @Override
    public GetUserGroupsAndLicensesOutputDto getUserGroupsAndLicenses(GetUserGroupsAndLicensesInputDto input) {

        //MonitorCache.registerNewRestMethodCall("getUserGroupsAndLicensesJSON");

        GetUserGroupsInputDto input1 = new GetUserGroupsInputDto();
        input1.setAttributes(input.getAttributes());
        input1.setLocale(input.getLocale());

        GetUsersLicensesInputDto input2 = new GetUsersLicensesInputDto();
        input2.setAttributes(input.getAttributes());
        input2.setLocale(input.getLocale());

        GetUserGroupsOutputDto userGroups = getUserGroups(input1);          

        GetUsersLicensesOutputDto userLicenses = getUserLicenses(input2);        
        GetUserGroupsAndLicensesOutputDto  output = new GetUserGroupsAndLicensesOutputDto();
        output.setAllPresentationTypes(LicenseValidator.getAllPresentationtypeNames(input.getLocale()));
        output.setAllGroups(LicenseValidator.getAllGroupeNames(input.getLocale()));
        output.setGroups(userGroups.getGroups());
        output.setLicenses(userLicenses.getLicenses());           
        return output;

    }


    @Override
    public String extractStatistics() {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * This method simply converts any Exception into a Service exception
     * @param e: Any kind of exception
     * @return A ServiceException
     * @see dk.kb.license.webservice.ServiceExceptionMapper
     */
    private ServiceException handleException(Exception e) {
        if (e instanceof ServiceException) {
            return (ServiceException) e; // Do nothing - this is a declared ServiceException from within module.
        } else {// Unforseen exception (should not happen). Wrap in internal service exception
            log.error("ServiceException(HTTP 500):", e); //You probably want to log this.
            return new InternalServiceException(e.getMessage());
        }
    }


}
