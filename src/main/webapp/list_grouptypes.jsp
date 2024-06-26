<%@ include file="check_gui_enabled.jsp" %>

<%ArrayList<GroupType> configuredGroupTypes = LicenseCache.getConfiguredLicenseGroupTypes();%>

<h2>Pakker:</h2>
<table class="table table-condensed table-hover">
   <thead>
   <tr>
    <th class="id">ID</th>
    <th>Key</th>
    <th>Value</th>    
    <th>Description</th>
    <th>Query string</th>
    <th></th>
   </tr>
   </thead>
   <tbody>
<%
int rows1=0;
for (int i = 0;i< configuredGroupTypes.size();i++ ){
  
   GroupType current = configuredGroupTypes.get(i);
   if (current.isRestrictionGroup()){
       continue;
   }

%>
   <tr class="<%=Util.getStyle(rows1++)%>">
      <td class="id"><%=current.getId()%></td>      
      <td><%=current.getKey()%></td>
      <td><%=current.getValue_dk()%><br><%=current.getValue_en()%></td>      
      <td><%=current.getDescription_dk()%><br><%=current.getDescription_en()%></td>
      <td><%=current.getQuery()%></td>      
      <td>
        <a class="btn btn-primary" href="edit_grouptype.jsp?grouptypeId=<%=current.getId()%>">Rediger</a>
        <input class="btn btn-primary btn-delete" type="button" value="Slet" onclick="javascript: confirmDeleteGroupType('Delete group type:<%=current.getKey()%>','<%=current.getKey()%>');"/>
      </td>
  </tr>

<%}%>
   </tbody>
</table>




<h2>Klausuleringer:</h2>
<table class="table table-condensed table-hover">
   <thead>
   <tr>
    <th class="id">ID</th>
    <th>Key</th>
    <th>Value</th>    
    <th>Description</th>
    <th>Query string</th>
    <th></th>
   </tr>
   </thead>
   <tbody>
<%
int rows2=0;
for (int i = 0;i< configuredGroupTypes.size();i++ ){
  GroupType current = configuredGroupTypes.get(i);
  if (!current.isRestrictionGroup()){
      continue;
  }

%>
   <tr class="<%=Util.getStyle(rows2++)%>">
      <td class="id"><%=current.getId()%></td>
      <td><%=current.getKey()%></td>
      <td><%=current.getValue_dk()%><br><%=current.getValue_en()%></td>      
      <td><%=current.getDescription_dk()%><br><%=current.getDescription_en()%></td>
      <td><%=current.getQuery()%></td>      
      <td>
        <a class="btn btn-primary" href="edit_grouptype.jsp?grouptypeId=<%=current.getId()%>">Rediger</a>
        <input class="btn btn-primary btn-delete" type="button" value="Slet" onclick="javascript: confirmDeleteGroupType('Delete group type:<%=current.getKey()%>','<%=current.getKey()%>');"/>
      </td>
  </tr>

<%}%>
   </tbody>
</table>

<h2>Opret ny pakke eller klausulering:</h2>

<label class="radio">
  <input type="radio" name="type" id="pakke_radio"  value="pakke" checked>
  Ny pakke
</label>
<label class="radio">
  <input type="radio" name="type"  id="klausulering_radio" value="klausulering">
  Ny klausulering
</label>


<div class="infoGroup">
  <span class="help-inline">Key</span>  
  <input type="text" name="key_grouptype" class="span3" value="">
  <span class="help-inline">V&aelig;rdi</span>  
  <input type="text" name="value_grouptype" class="span3" value="">
  <span class="help-inline">Beskrivelse</span>  
  <input type="text" name="value_groupdescription" class="span3" value="">
  <span class="help-inline">Query</span>
  <input type="text" name="value_groupquery" class="span3" value="">  
</div>
<br>
<div class="infoGroup">
  <span class="help-inline">V&aelig;rdi(En)</span>  
  <input type="text" name="value_en_grouptype" class="span3" value="">
  <span class="help-inline">Beskrivelse(En)</span>  
  <input type="text" name="value_en_groupdescription" class="span3" value="">
  <input class="btn btn-primary" type="button" value="Opret ny" onclick="javascript: save('save_grouptype');"/>
 </div>
 
 
