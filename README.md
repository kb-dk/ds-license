# ds-license

 # Ds-license(Digitale Samlinger) by the Royal Danish Library. 
    
      
    ## Ds-license restricts access to items in collections based on the user credential information.
    The primary method in Ds-license is to filther a list of IDs (recordIds) and only return the ID's that the user has
    access to based in the user information. The filtering is done against a Solr server with all information about the
    records. The application has a GUI interface to define all access rules based on user attributes.
    
    ## User attributes
    The User attributes is key-value pairs, where the values can be a list and a user can have multiple key-value pairs.
    All values and keys are strings. The key-values pairs can be WAYF attributes (https://www.wayf.dk/) which is a 
    standard for describing users at educational institutions and will have some guarateed values always. But the GUI administration
    can define arbitarity key-value rules and not just WAYF attributes. All user atttribute keys must be define in 
    the GUI and they will be available for use when defining a license attribute-group.
    
    ### Examples of UserAttributes (WAYF)
          
    | key                         | values                     | Remark                                          | 
    | ----------------------------| ---------------------------|-------------------------------------------------|
    | SBIPRolemapper              | inhouse,kb                 | inhouse(local computer), kb (organisation)      |
    | mail                        | teg@kb.dk                  | Can be used to give individual access           |
    | shachHomeOrganisation       | ku.dk                      | Educational institution                         |
    | eduPersonPrimaryAffiliation | stud                       | Student. (staff, employee,faculty also values)  |
    
    
    
    ## Groups/Packages
    The groups (also called pakker in Danish) are the building blocks that gives access to materials. There are two 
    types of groups, normal groups and must-groups. Normal groups gives access to materials (positive Solr filter) while must-groups
    restricts access (negative Solr filter).  The more normal groups a user has access will be more materials and more must-groups will
    also give more materials since this will remove the negative filter. The must-groups negative filters are always applied unless
    the user validates access to them and that restriction will be removed. Some materials can be locked under several different must-groups
    restrictions and all must-groups must validate before the user can access it.
    
    ### Example of access two groups
    Group 1 (normal group): lma_long:"radio"
    
    Group 2 (must group):  klausuleret:"ja"
    
    If the user has acesss to both groups, the final filter query will be:   **lma_long:"radio"**
    
    If the user has only access to the first group, the final filter query will be:  **lma_long:"radio" -klausuleret:"ja"**
    
    ## Licences
    Licenses are the mapping from UserAttributes to groups/packages. One license can give access to several groups. 
    For each group a license gives access to, the license must also specify a presentation type (or several) for that group.
    Presentation types are also defined in the GUI and examples of presentation types are:Stream,Search.Download,Thumbnails,Headlines.
    
    ### Licenses structure
    A license must define a valid from and valid to date and is only valid in the date interval. The format is dd-mm-yyyy.
    A license has to defines one or more attribute-groups. An attribute-group is mapping from UserAttributes key and values.
    An attribute-group can define several mappings every single mapping in the attribute group must validate for the license to validate.
    The reason you can define several attribute-groups in one license is to avoid defining many identical licences that gives 
    access to same material but by different conditions.
    
    ### License validation algoritm
    First the validation check will limit to licenses that are valid for the date of the requests. Then for each license every 
    attributed group will be checked. If just one of the attribute groups validates then the license validates. 
    The license will give access to the groups (packages) define for the license, but restricted to the presentationtype (Download etc.)
    For all license that validates this will define a set of all groups (union of all groups) that will determine access.
    All the normal groups will each expand the positive filter query used for filtering Ids. All each must-group valided will remove
    the negative filter blocked by that must-group.
    
    ## Configuration of the Ds-license
     
    ### Property: license_solr_servers
    The configuration requires at least one Solr server for the property 'license_solr_servers'. Several Solr servers must be 
    seperated by commas. When filtering IDs all Solr servers will be called for filtering and each will be called with all IDs.
           
    ### Property: license_solr_filter_field
    The Solr field used for filtering. Multivalued fields allowed. All defined Solr servers must have this field defined for all documents.
    
    ### Properties:  url,driver,username,password 
    The 4 database properties  (JDBC) must be defined: url,driver,username,password
    
    A PostGreSql database is recommended when not running locally. The database tables must be created before use with
    DDL file: test/resources/ddl/licensemodule_create_db.ddl
    
    
    ## API   
    While there are many methods exposed through the API only the following two are necessary for integrating License module into the software stack.

    #### checkAccessForIds
    This is the method that takes the UserAttributes and a list of IDs. The return value will be the IDs that was not removed in the filthering
    and those that the user has access to. The method is called every time a user tries to access or search materials.
    
    #### getUserLicenses
    Method only takes the userattributes and returns a list of licences that the userattributes give access to. For each license
    the name, validFrom,ValidTo and a description will be returned. This information can be shown to the user.
        


Developed and maintained by the Royal Danish Library.

## Requirements

* Maven 3                                  
* Java 11
* Tomcat 9
* PostGreSql recommended (or any JDBC compliant database implementation). 
* For local unittest as development it uses a file base H2 java database that does not require any software installation.


## Build & run

Build with
``` 
mvn package
```

## Setup required to run the project local 
Create local yaml-file: Take a copy of 'ds-license-behaviour.yaml'  and name it'ds-license-environment.yaml'

Update the dbURL for the h2-database file to your environment. Ie. replace XXX with your user.

The H2 will be created if does not exists and data will be persistent between sessions. Delete the h2-file if you want to reset the database.


## Test the webservice with
```
mvn jetty:run
```
## Swagger UI
The Swagger UI is available at <http://localhost:8080/ds-license/api/>, providing access to both the `v1` and the 
`devel` versions of the GUI. 


## Deployment to a server (development/stage/production).
Install Tomcat9 server 

Install PostgreSql (or any JDBC database).

Create a database tablespace and define the tables using the file: resources/ddl/licensemodule_create_db.ddl

Configure tomcat with the context enviroment file conf/ocp/ds-license.xml. Notice it points to the location on the file system where the yaml and logback file are located.

Edit  conf/ds-license.logback.xml

Make a ds-license.yaml file. (Make a copy of /conf/ds-license-environment.yaml rename it, and edit the properties). 

Configure conf/ds-license.yaml with the JDCB properties for the database. 


See the file [DEVELOPER.md](DEVELOPER.md) for developer specific details and how to deploy to tomcat.

