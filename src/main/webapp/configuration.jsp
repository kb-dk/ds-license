<%@ page import="
    java.util.*,
     dk.kb.license.storage.*,     
     dk.kb.license.config.ServiceConfig,
     dk.kb.license.facade.LicenseModuleFacade"%>

<%@ include file="check_gui_enabled.jsp" %>
<%@ include file="show_user.jsp" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html>
<head>
    <title>Adgangslicens editor, konfiguration</title>
    <script type="text/javascript" src="js/jquery-1.8.3.js"></script>
    <script type="text/javascript" src="js/bootstrap.js"></script>

    <link href="css/bootstrap.min.css" rel="stylesheet" media="screen" />
    <link href="css/licensemodule.css" rel="stylesheet" media="screen" />
</head>
<body>


<script>
    function save(type){
        document.configurationForm.event.value=type;
        document.configurationForm.submit();
    }

   function confirmDeletePresentationType(message, typeName) {
       var answer = confirm(message);
       if (answer){
           document.configurationForm.event.value='deletePresentationType';
           document.configurationForm.typeName.value=typeName;
           document.configurationForm.submit();         
       }
     }
     
    function confirmDeleteGroupType(message, typeName) {
       var answer = confirm(message);
       if (answer){
           document.configurationForm.event.value='deleteGroupType';
           document.configurationForm.typeName.value=typeName;
           document.configurationForm.submit();         
       }
     }          

    function confirmDeleteAttributeType(message, typeName) {
       var answer = confirm(message);
       if (answer){
           document.configurationForm.event.value='deleteAttributeType';
           document.configurationForm.typeName.value=typeName;
           document.configurationForm.submit();         
       }
     }          
</script>

<h1>Adgangslicens editor, konfiguration</h1>

<ul class="nav nav-tabs" id="configTab">
    <li class="active"><a href="#list_licenses">Vis alle licenser</a></li>
    <li><a href="#list_configured_presentationtypes">Præsentationstyper</a></li>
    <li><a href="#list_configured_grouptypes">Pakker/Klausuleringer</a></li>
    <li><a href="#list_configured_attributetypes">Attributgrupper</a></li>       
        <li><a href="#auditlog">Auditlog</a></li>
</ul>

<%@ include file="message.jsp" %>

<form name="configurationForm" class="well" action="configurationServlet" method="POST">
    <input type="hidden" name="event" />
    <input type="hidden" name="typeName" />

    <div class="tab-content">
        <div class="tab-pane active" id="list_licenses">
            <%@ include file="list_licenses.jsp" %>
        </div>
        <div class="tab-pane" id="list_configured_presentationtypes">
            <%@ include file="list_presentationtypes.jsp" %>
        </div>
        <div class="tab-pane" id="list_configured_grouptypes">
            <%@ include file="list_grouptypes.jsp" %>
        </div>
        <div class="tab-pane" id="list_configured_attributetypes">
            <%@ include file="list_attributetypes.jsp" %>
        </div>                                        
        <div class="tab-pane" id="auditlog">
            <%@ include file="auditlog.jsp" %>
        </div>           
        
        
    </div>
</form>

<script>
    $('#configTab a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    })
</script>

<%
    //Show correct tab (by number 0,1,2,3,..)
    String tab = (String) request.getAttribute("tab");
    if (tab != null){%>
<script>
    $('#configTab li:eq(<%=tab%>) a').tab('show');
</script>
<%}%>

</body>
</html>