package dk.kb.license.config;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dk.kb.license.solr.SolrServerClient;
import dk.kb.util.yaml.YAML;

/**
 * Sample configuration class using the Singleton pattern.
 * This should work well for most projects with non-dynamic properties.
 */
public class ServiceConfig {

    private static final Logger log = LoggerFactory.getLogger(ServiceConfig.class);
    
    public static String SOLR_FILTER_ID_FIELD = null;
    public static String SOLR_FILTER_RESOURCE_ID_FIELD = null;
    public static List<SolrServerClient> SOLR_SERVERS = null;
    
    /**
     * Besides parsing of YAML files using SnakeYAML, the YAML helper class provides convenience
     * methods like {@code getInteger("someKey", defaultValue)} and {@code getSubMap("config.sub1.sub2")}.
     */
    private static YAML serviceConfig;

    /**
     * Initialized the configuration from the provided configFile.
     * This should normally be called from {@link dk.kb.license.webservice.ContextListener} as
     * part of web server initialization of the container.
     * @param configFile the configuration to load.
     * @throws IOException if the configuration could not be loaded or parsed.
     */
    public static synchronized void initialize(String... configFile ) throws IOException {
        serviceConfig = YAML.resolveLayeredConfigs(configFile);
        serviceConfig.setExtrapolate(true);
    
        List<String> solr_servers = serviceConfig.getList("license_solr_servers");
        SOLR_FILTER_ID_FIELD = serviceConfig.getString("license_solr_filter_field");
        SOLR_FILTER_RESOURCE_ID_FIELD = serviceConfig.getString("license_solr_resource_filter_field");
        
        SOLR_SERVERS = solr_servers.stream().map(String::trim).map(SolrServerClient::new).collect(Collectors.toList());

        log.info("Loaded solr-servers:"+solr_servers);
        log.info("Loaded solr id filter field:"+SOLR_FILTER_ID_FIELD);
        log.info("Loaded solr resourceId filter field:"+SOLR_FILTER_RESOURCE_ID_FIELD);
    }

    
    //For unittest
    public static void setSOLR_FILTER_FIELD(String sOLR_FILTER_FIELD) {
        SOLR_FILTER_ID_FIELD = sOLR_FILTER_FIELD;
    }

    
    /**
     * Direct access to the backing YAML-class is used for configurations with more flexible content
     * and/or if the service developer prefers key-based property access.
     * @return the backing YAML-handler for the configuration.
     */
    public static YAML getConfig() {
        if (serviceConfig == null) {
            throw new IllegalStateException("The configuration should have been loaded, but was not");
        }
        return serviceConfig;
    }
  
    public static  String getDBDriver() {
        String dbDriver= serviceConfig.getString("db.driver");
        return dbDriver;
    }

    public static  String getDBUrl() {
        String dbUrl= serviceConfig.getString("db.url");
        return dbUrl;
    }

    public static  String getDBUserName() {
        String dbUserName= serviceConfig.getString("db.username");
        return dbUserName;
    }

    public static  String getDBPassword() {
        String dbPassword= serviceConfig.getString("db.password");
        return dbPassword;
    }
    
    
}
