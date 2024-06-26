package dk.kb.license.servlets;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.kb.license.config.ServiceConfig;
import dk.kb.license.facade.LicenseModuleFacade;
import dk.kb.license.model.v1.CheckAccessForIdsInputDto;
import dk.kb.license.model.v1.CheckAccessForIdsOutputDto;
import dk.kb.license.model.v1.GetUserQueryInputDto;
import dk.kb.license.model.v1.GetUserQueryOutputDto;
import dk.kb.license.model.v1.GetUsersLicensesInputDto;
import dk.kb.license.model.v1.UserObjAttributeDto;
import dk.kb.license.model.v1.ValidateAccessInputDto;
import dk.kb.license.storage.GroupType;
import dk.kb.license.storage.PresentationType;
import dk.kb.license.storage.License;
import dk.kb.license.storage.LicenseCache;
import dk.kb.license.validation.LicenseValidator;


/**
 * This is used by the JSP frontend only. Create/edit a license has its own logic in
 * {@link CreateLicenseServlet CreateLicenseServlet.class}
 * 
 * The methods here are all the minor methods such as editing on the configuration for presentationtypes, packages etc.
 */
public class ConfigurationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(ConfigurationServlet.class);

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String event = request.getParameter("event");
        log.info("New event for ConfigurationServlet:" + event);


        try {
            // tab 0 is list licenses

            if ("save_presentationtype".equals(event)) {
                request.setAttribute("tab", "1");
                String key = request.getParameter("key_presentationtype");
                String value = request.getParameter("value_presentationtype");
                String value_en = request.getParameter("value_en_presentationtype");
                log.debug("Saving new presentationtype:" + key);
                LicenseModuleFacade.persistLicensePresentationType(key, value, value_en,request.getSession());
            } else if ("save_grouptype".equals(event)) {
                request.setAttribute("tab", "2");
                String key = request.getParameter("key_grouptype");
                String value_dk = request.getParameter("value_grouptype");
                String value_en = request.getParameter("value_en_grouptype");
                String description = request.getParameter("value_groupdescription");
                String description_en = request.getParameter("value_en_groupdescription");
                String query = request.getParameter("value_groupquery");
                String typeStr = request.getParameter("type");

                boolean isKlausulering = false;

                if ("klausulering".equals(typeStr)) {
                    isKlausulering = true;
                }
                log.debug("Saving new grouptype:" + key + " klausulering:" + isKlausulering);
                LicenseModuleFacade.persistLicenseGroupType(key, value_dk, value_en, description, description_en, query, isKlausulering,request.getSession());

            } else if ("save_attributetype".equals(event)) {

                request.setAttribute("tab", "3");
                String value = request.getParameter("value_attributetype");
                log.debug("Saving new attributetype:" + value);
                LicenseModuleFacade.persistAttributeType(value,request.getSession());

            } else if ("validate".equals(event)) {
                log.debug("validate called");
                request.setAttribute("tab", "4");
                String validation_attribute_values = request.getParameter("validation_attribute_values");
                String validation_groups = request.getParameter("validation_groups");
                String validation_presentationtype = request.getParameter("validation_presentationtype");

                request.setAttribute("validation_attribute_values", validation_attribute_values);
                request.setAttribute("validation_groups", validation_groups);
                request.setAttribute("validation_presentationtype", validation_presentationtype);


                String result = decomposeValidateAccess(validation_attribute_values, validation_groups, validation_presentationtype);
                request.setAttribute("validation_result", result);
            } else if ("validateQuery".equals(event)) {
                log.debug("validateQuery called");
                request.setAttribute("tab", "5");
                String validationQuery_attribute_values = request.getParameter("validationQuery_attribute_values");
                String validationQuery_presentationtype = request.getParameter("validationQuery_presentationtype");

                request.setAttribute("validationQuery_attribute_values", validationQuery_attribute_values);
                request.setAttribute("validationQuery_presentationtype", validationQuery_presentationtype);

                String result = decomposeValidateQuery(validationQuery_attribute_values, validationQuery_presentationtype);
                request.setAttribute("validationQuery_result", result);
            } else if ("checkAccessIds".equals(event)) {
                log.debug("checkAccessIds called");
                request.setAttribute("tab", "6");
                String checkAccessIds_attribute_values = request.getParameter("checkAccessIds_attribute_values");
                String checkAccessIds_presentationtype = request.getParameter("checkAccessIds_presentationtype");
                String checkAccessIds_ids = request.getParameter("checkAccessIds_ids");

                request.setAttribute("checkAccessIds_attribute_values", checkAccessIds_attribute_values);
                request.setAttribute("checkAccessIds_presentationtype", checkAccessIds_presentationtype);
                request.setAttribute("checkAccessIds_ids", checkAccessIds_ids);

                String result = decomposCheckAccessIds(checkAccessIds_attribute_values, checkAccessIds_presentationtype, checkAccessIds_ids);
                request.setAttribute("checkAccessIds_result", result);
            } else if ("deletePresentationType".equals(event)) {
                log.debug("deletePresentationType called");
                request.setAttribute("tab", "1");
                String typeName = request.getParameter("typeName");
                LicenseModuleFacade.deletePresentationType(typeName,request.getSession());
            } else if ("deleteGroupType".equals(event)) {
                log.debug("deleteGroup called");
                request.setAttribute("tab", "2");
                String typeName = request.getParameter("typeName");
                LicenseModuleFacade.deleteLicenseGroupType(typeName,request.getSession());
            } else if ("deleteAttributeType".equals(event)) {
                log.debug("deleteAttributeType called");
                request.setAttribute("tab", "3");
                String typeName = request.getParameter("typeName");
                LicenseModuleFacade.deleteAttributeType(typeName,request.getSession());
            } else if ("updateGroup".equals(event)) {
                log.debug("updateGroup called");
                request.setAttribute("tab", "2");

                String id = request.getParameter("id");
                //String key = request.getParameter("key");//Not used. Update by ID.
                String value = request.getParameter("value_grouptype");
                String value_en = request.getParameter("value_en_grouptype");
                String description = request.getParameter("value_groupdescription");
                String description_en = request.getParameter("value_en_groupdescription");
                String query = request.getParameter("value_groupquery");
                String isRestrictionGroupStr = request.getParameter("denyGroupCheck");
                boolean isRestrictionGroup = false;

                if (isRestrictionGroupStr != null) { // Checkbox is checked
                    isRestrictionGroup = true;
                }
                log.debug("Updating license group with id:" + id);
                LicenseModuleFacade.updateLicenseGroupType(Long.parseLong(id), value, value_en, description, description_en, query, isRestrictionGroup,request.getSession());
            } else if ("updatePresentationType".equals(event)) {
                log.debug("updatePresentationType called");
                request.setAttribute("tab", "1");
                String id = request.getParameter("id");
                //String key = request.getParameter("key");//Not used. Update by ID.
                String value = request.getParameter("value_presentationtype");
                String value_en = request.getParameter("value_en_presentationtype");
                log.debug("Updating presentatintype with id:" + id);
                LicenseModuleFacade.updatePresentationType(Long.parseLong(id), value, value_en,request.getSession());
            } else {
                log.error("Unknown event:" + event);
                request.setAttribute("message", "Unknown event:" + event);
            }

        } catch (Exception e) {//various server errors
            log.error("unexpected error", e);
            request.setAttribute("message", e.getMessage());
            returnFormPage(request, response);
            return;
        }

        returnFormPage(request, response);
        return;
    }

    private void returnFormPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("configuration.jsp");
        dispatcher.forward(request, response);
        return;
    }

    private String decomposeValidateAccess (String validation_attribute_values, String validation_groups, String validation_presentationtype) {
        StringBuilder infoMessage = new StringBuilder();   
        //parse input first.
        ValidateAccessInputDto input = new ValidateAccessInputDto();
        PresentationType presentationType = null;
        ArrayList<UserObjAttributeDto> attributes;
        try{

            attributes = createUserObjFromFormData(validation_attribute_values);
            ArrayList<String> groups = createGroupsFromFormData(validation_groups);		
            ArrayList<String> presentationTypes = createPresentationTypesFromFormData(validation_presentationtype);
            if (presentationTypes.size() !=1){
                infoMessage.append("Der skal angives een presentationstype");
                return infoMessage.toString();	
            }
            presentationType = LicenseValidator.matchPresentationtype(presentationTypes.get(0));
            input.setAttributes(attributes);
            input.setGroups(groups);
            input.setPresentationType(presentationTypes.get(0));

        }
        catch(RuntimeException e){
            infoMessage.append("Input validerings fejl fra web-form:"+e.getMessage());
            return infoMessage.toString();
        }        


        //The following logic is taken from LicenseValidator.validateAccess().
        //I see no other way that to repeat it when I want to the decomposition.

        ArrayList<GroupType> groupsType = null;
        ArrayList<GroupType> restrictionGroups = null; 
        try{
            boolean validated = LicenseValidator.validateAccess(input);
            infoMessage.append("Resultat af validateAccess() kald:"+validated +" \n");
            infoMessage.append("Detaljer: \n");
            groupsType = LicenseValidator.buildGroups(input.getGroups());
            restrictionGroups = LicenseValidator.filterRestrictionGroups(groupsType);
            if (restrictionGroups.size() > 0){
                infoMessage.append("Restriction-grupper i input:"+restrictionGroups +"\n");	
            }
            else{
                infoMessage.append("Der blev ikke fundet Restriction-grupper i input.\n");				
            }

            ArrayList<License> allLicenses = LicenseCache.getAllLicense();
            infoMessage.append("Samlet antal licenser i databasen:"+allLicenses.size()+"\n");		

            ArrayList<License> dateFilteredLicenses = LicenseValidator.filterLicenseByValidDate(allLicenses, System.currentTimeMillis());	
            infoMessage.append("Samlet antal licenser inden for perioden:"+dateFilteredLicenses.size()+"\n");

            ArrayList<License> accessLicenses = LicenseValidator.findLicensesValidatingAccess(input.getAttributes(),dateFilteredLicenses);
            infoMessage.append("Følgende licenser opfylder access-krav(uden check af grupper):"+accessLicenses+"\n");
            if (accessLicenses.size()==0){
                infoMessage.append("Ingen licenser opfylder access-krav(uden check af grupper) \n");	
                return infoMessage.toString();
            }


            //Test method getUsersLicenseGroups
            GetUsersLicensesInputDto inputGroups = new GetUsersLicensesInputDto();
            inputGroups.setAttributes(attributes);						

            if (restrictionGroups.size() == 0){
                log.error("presentationtype:"+presentationType);
                ArrayList<License> validatedLicenses = LicenseValidator.filterLicensesWithGroupNamesAndPresentationTypeNoRestrictionGroup(accessLicenses, groupsType, presentationType);

                if (validatedLicenses.size() == 0){				
                    infoMessage.append("Ingen licenser opfylder gruppe betingelsen. \n");		
                }
                else{				
                    infoMessage.append("Følgende license opfylder gruppe-betingelsen:"+validatedLicenses +"\n");				
                }								        	          
            }
            else{
                ArrayList<License> validatedLicenses = LicenseValidator.filterLicensesWithGroupNamesAndPresentationTypeRestrictionGroup(accessLicenses, restrictionGroups , presentationType);
                if (validatedLicenses.size() == 0){
                    infoMessage.append("Access-krav licenserne opfylder ikke alle Restriction-gruppe betingelser.\n");		
                }
                else{
                    infoMessage.append("Følgende licenser opfylder tilsammen Restriction-gruppe betingelser:"+validatedLicenses +"\n");				
                }
            }		
            //infoMessage.append("Generated Query:"+userGroupsDTO.getQueryString());
        }

        catch(RuntimeException e){
            infoMessage.append("Fejl under validateAccess kald:"+e.getMessage());	
            return infoMessage.toString();
        }
        return infoMessage.toString();
    }


    private String decomposeValidateQuery (String validation_attribute_values,  String validation_presentationtypes) {
        StringBuilder infoMessage = new StringBuilder();   
        //parse input first.
        GetUserQueryInputDto input = new GetUserQueryInputDto();
        ArrayList<UserObjAttributeDto> attributes;
        try{

            attributes = createUserObjFromFormData(validation_attribute_values);		
            ArrayList<String> presentationTypes = createPresentationTypesFromFormData(validation_presentationtypes);
            if (presentationTypes.size() == 0){
                infoMessage.append("Der skal angives een presentationstype");
                return infoMessage.toString();	
            }
            input.setAttributes(attributes);

            input.setPresentationType(presentationTypes.get(0));

        }
        catch(RuntimeException e){
            infoMessage.append("Input validerings fejl fra web-form:"+e.getMessage());
            return infoMessage.toString();
        }        


        //The following logic is taken from LicenseValidator.getUserQuery
        //I see no other way that to repeat it when I want to the decomposition.

        try{
            GetUserQueryOutputDto output = LicenseValidator.getUserQuery(input);
            infoMessage.append("Detaljer: \n");
            infoMessage.append("Brugeren opfylder følgende grupper:"+output.getUserLicenseGroups() +"\n");
            infoMessage.append("Brugeren mangler følgende Restriction grupper:"+output.getUserNotInDenyGroups() +"\n");	
            infoMessage.append("Query:"+output.getQuery() +"\n");
        }
        catch(RuntimeException e){
            infoMessage.append("Fejl under validateQuery kald:"+e.getMessage());	
            return infoMessage.toString();
        }
        return infoMessage.toString();
    }

    private String decomposCheckAccessIds(String checkAccessIds_attribute_values,  String checkAccessIds_presentationtype, String checkAccessIds_ids) {
        StringBuilder infoMessage = new StringBuilder();   
        CheckAccessForIdsInputDto input = new CheckAccessForIdsInputDto();
        ArrayList<UserObjAttributeDto> attributes;
        try{
            attributes = createUserObjFromFormData(checkAccessIds_attribute_values);		
            input.setAttributes(attributes);
            input.setPresentationType(checkAccessIds_presentationtype);
            input.setAccessIds(createIdsFormData(checkAccessIds_ids));			

        }
        catch(RuntimeException e){
            infoMessage.append("Input validerings fejl fra web-form:"+e.getMessage());
            return infoMessage.toString();
        }        

        try{
            CheckAccessForIdsOutputDto output = LicenseValidator.checkAccessForIds(input, ServiceConfig.SOLR_FILTER_ID_FIELD);
            infoMessage.append("Detaljer: \n");			
            infoMessage.append("Presentationtype:"+output.getPresentationType() +" \n");
            infoMessage.append("Access query part:"+output.getQuery() +" \n");
            infoMessage.append("#Ids:"+output.getAccessIds().size() +" \n");
            infoMessage.append("Ids:"+output.getAccessIds() +" \n");						
        }
        catch(Exception e){
            infoMessage.append("Fejl under checkAccessIds kald:"+e.getMessage());	
            return infoMessage.toString();
        }
        return infoMessage.toString();
    }



    private ArrayList<String> createGroupsFromFormData(String validation_groups){
        ArrayList<String> groups = new ArrayList<String>();
        String[] tmp = validation_groups.split(",");   //StringUtils.split(validation_groups, ",");
        if (tmp.length == 0){
            throw new IllegalArgumentException("Der skal være angivet mindst en gruppe i attributegrupper feltet");
        }
        for (String group : tmp){
            groups.add(group.trim());
        }		  
        return groups;		
    }

    private List<String> createIdsFormData(String validation_ids){
        ArrayList<String> ids = new ArrayList<String>();
        String[] tmp = validation_ids.split(","); //StringUtils.split(validation_ids, ",");
        if (tmp.length == 0){
            throw new IllegalArgumentException("Der skal være angivet mindst et recordId");
        }
        for (String id : tmp){
            ids.add(id.trim());
        }		  
        return ids;		
    }

    private ArrayList<String> createPresentationTypesFromFormData(String validation_presentationTypes){
        ArrayList<String> presentationTypes = new ArrayList<String>();
        String[] tmp = validation_presentationTypes.split(","); //StringUtils..split(validation_presentationTypes, ",");
        if (tmp.length == 0){
            throw new IllegalArgumentException("Der skal være angivet mindst en presentationtype");
        }
        for (String group : tmp){
            presentationTypes.add(group.trim());
        }		  
        return presentationTypes;		
    }

    private ArrayList<UserObjAttributeDto> createUserObjFromFormData(String validation_attribute_values){
        String[] lines = validation_attribute_values.split("\n"); //StringUtils.split(validation_attribute_values, "\n");
        if (lines.length == 0){
            throw new IllegalArgumentException("Der skal være mindst 1 linie i attribut/values tekstboksen");
        }

        ArrayList<UserObjAttributeDto> attributes = new ArrayList<UserObjAttributeDto>(); 
        // every line on the form attributename: value1 , value2, value 3 ,...
        for (String line : lines){
            UserObjAttributeDto attribute = new UserObjAttributeDto();
            attributes.add(attribute);
            String[] tmp = line.split(":");//StringUtils.split(line, ":");
            if (tmp.length != 2){
                throw new IllegalArgumentException("Attribute/value linie kan ikke parses. Der skal være et : efter attributename for linie:"+line);
            }
            attribute.setAttribute(tmp[0].trim());
            String[] values = tmp[1].split(","); //StringUtils.split(tmp[1], ",");
            if (values.length == 0){
                throw new IllegalArgumentException("Der skal være mindst en attributevalue for linie:"+line);
            }

            ArrayList<String> valueList = new ArrayList<String>(); 
            for (String value : values){
                valueList.add(value.trim());
            }
            attribute.setValues(valueList);	   	

        }
        return attributes;
    }

}
