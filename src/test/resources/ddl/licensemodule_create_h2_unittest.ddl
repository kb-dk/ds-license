CREATE TABLE IF NOT EXISTS PRESENTATIONTYPES (      
       ID BIGINT PRIMARY KEY,
       KEY_ID VARCHAR(256) NOT NULL,
       VALUE_DK VARCHAR(256) NOT NULL,                                   
       VALUE_EN VARCHAR(256) NOT NULL
);
CREATE UNIQUE INDEX  IF NOT EXISTS PRESTYPE_ID_IN ON PRESENTATIONTYPES(ID);

CREATE TABLE IF NOT EXISTS GROUPTYPES (      
       ID BIGINT PRIMARY KEY,
       KEY_ID VARCHAR(256) NOT NULL,
       VALUE_DK VARCHAR(256) NOT NULL,
       VALUE_EN VARCHAR(256) NOT NULL,                                 
       RESTRICTION BOOLEAN NOT NULL,
       QUERYSTRING VARCHAR(2048) NOT NULL,
       DESCRIPTION_DK VARCHAR(512) NOT NULL,
       DESCRIPTION_EN VARCHAR(512) NOT NULL
);
CREATE UNIQUE INDEX  IF NOT EXISTS GROUPTYPE_ID_IN ON GROUPTYPES(ID);

CREATE TABLE IF NOT EXISTS ATTRIBUTETYPES (      
       ID BIGINT PRIMARY KEY,
       VALUE_ORG VARCHAR(256) NOT NULL                                    
);

CREATE UNIQUE INDEX  IF NOT EXISTS  ATTTYPE_ID_IN ON ATTRIBUTETYPES(ID);

CREATE TABLE IF NOT EXISTS LICENSE (
       ID BIGINT PRIMARY KEY,
       NAME VARCHAR(256) NOT NULL,
       NAME_EN VARCHAR(256) NOT NULL,
       DESCRIPTION_DK VARCHAR(1024) NOT NULL,
       DESCRIPTION_EN VARCHAR(1024) NOT NULL,
       VALIDFROM VARCHAR(32) NOT NULL,
       VALIDTO VARCHAR(32) NOT NULL
);       
CREATE UNIQUE INDEX  IF NOT EXISTS LICENSE_ID_IN ON LICENSE(ID);

CREATE TABLE  IF NOT EXISTS ATTRIBUTEGROUP (
       ID BIGINT PRIMARY KEY,
       NUMBER INT NOT NULL,
       LICENSEID BIGINT NOT NULL

);       
CREATE UNIQUE INDEX  IF NOT EXISTS ATTRGRP_ID_IN ON ATTRIBUTEGROUP(ID);
CREATE INDEX  IF NOT EXISTS ATTRGRP_LICENSEID_IN ON ATTRIBUTEGROUP(LICENSEID);

CREATE TABLE  IF NOT EXISTS ATTRIBUTE (
       ID BIGINT PRIMARY KEY,
       NAME VARCHAR(256) NOT NULL,
       ATTRIBUTEGROUPID BIGINT NOT NULL

);       
CREATE UNIQUE INDEX IF NOT EXISTS ATTRIBUTE_ID_IN ON ATTRIBUTE(ID);
CREATE INDEX  IF NOT EXISTS ATTR_GROUPID_IN ON ATTRIBUTE(ATTRIBUTEGROUPID);

CREATE TABLE IF NOT EXISTS VALUE_ORG (
       ID BIGINT PRIMARY KEY,
       VALUE_ORG VARCHAR(256) NOT NULL,
       ATTRIBUTEID BIGINT NOT NULL

);       
CREATE UNIQUE INDEX  IF NOT EXISTS VALUE_ID_IN ON VALUE_ORG(ID);
CREATE INDEX  IF NOT EXISTS VALUE_ATTRID_IN ON VALUE_ORG(ATTRIBUTEID);

CREATE TABLE  IF NOT EXISTS LICENSECONTENT (
       ID BIGINT PRIMARY KEY,       
       NAME VARCHAR(256) NOT NULL,       
       LICENSEID BIGINT NOT NULL

);       
CREATE UNIQUE INDEX  IF NOT EXISTS LICCONTENT_ID_IN ON LICENSECONTENT(ID);
CREATE INDEX  IF NOT EXISTS LICCONTENT_LICID_IN ON LICENSECONTENT(LICENSEID);

CREATE TABLE IF NOT EXISTS PRESENTATION (
       ID BIGINT PRIMARY KEY,
       NAME VARCHAR(256) NOT NULL,
       LICENSECONTENTID BIGINT NOT NULL

);       
CREATE UNIQUE INDEX  IF NOT EXISTS PRESENTATION_ID_IN ON PRESENTATION(ID);
CREATE INDEX IF NOT EXISTS PRES_LICCONTID_IN ON PRESENTATION(LICENSECONTENTID);


CREATE TABLE IF NOT EXISTS AUDITLOG (
       ID BIGINT PRIMARY KEY,
       OBJECTID BIGINT NOT NULL,
       MODIFIEDTIME BIGINT NOT NULL,
       USERNAME VARCHAR(256) NOT NULL,
       CHANGETYPE VARCHAR(256) NOT NULL,
       CHANGENAME VARCHAR(256) NOT NULL,
       CHANGECOMMENT VARCHAR(1024),
       TEXTBEFORE VARCHAR(65535),
       TEXTAFTER VARCHAR(65535) 
);       

CREATE UNIQUE INDEX IF NOT EXISTS AUDITLOG_ID_IN_IN ON AUDITLOG(ID);
CREATE INDEX IF NOT EXISTS AUDITLOG_OBJECTID_IN ON AUDITLOG(OBJECTID);




