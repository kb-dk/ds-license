<%@page pageEncoding="UTF-8"%>
<%@ page import="
    java.util.*,
    dk.kb.license.storage.*,
    dk.kb.license.model.v1.*"%>

<%@ include file="check_gui_enabled.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
    <title>Rediger license gruppe</title>
    <script language="javascript" type="text/javascript" src="js/jquery-1.8.3.js"></script>
    <script language="javascript" type="text/javascript" src="js/bootstrap.js"></script>

    <link href="css/bootstrap.min.css" rel="stylesheet" media="screen" />
    <link href="css/licensemodule.css" rel="stylesheet" media="screen" />

</head>
<body>
<%
    String id = request.getParameter("presentationtypeId");
ArrayList<PresentationType> allTypes  = LicenseCache.getConfiguredLicenseTypes();      
PresentationType edit = null;
for ( PresentationType current : allTypes){
   if (( current.getId() + "").equals(id)){
    edit=current;
    break;
   }
}
%>

<script>
    function updatePresentationType(id){
        document.configurationForm.event.value='updatePresentationType';
        document.configurationForm.id.value=id;
        document.configurationForm.submit();
    }

</script>

<h1>Adgangslicens editor, rediger license gruppe </h1>
<br>
<form name="configurationForm" class="well" action="configurationServlet" method="POST">
    <input type="hidden" name="event" />
    <input type="hidden" name="id" />

<div class="infoGroup">  
  <span class="help-inline">Key</span>  
  <input type="text" name="key_presentationtype" class="span3" readonly="true" value="<%=edit.getKey()%>">
  <span class="help-inline">V&aelig;rdi</span>  
  <input type="text" name="value_presentationtype" class="span3" value="<%=edit.getValue_dk()%>">
  <span class="help-inline">V&aelig;rdi(En)</span>  
  <input type="text" name="value_en_presentationtype" class="span3" value="<%=edit.getValue_en()%>">
  <input class="btn btn-primary" type="button" value="Opdater" onclick="javascript: updatePresentationType('<%=edit.getId()%>');"/>
  <a class="btn btn-small" href="configuration.jsp">Fortryd</a>
</div>
  
</form>

</body>

</html>