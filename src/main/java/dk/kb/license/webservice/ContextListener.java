package dk.kb.license.webservice;

import java.io.IOException;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import dk.kb.license.config.ServiceConfig;
import dk.kb.license.storage.*;
import dk.kb.license.util.H2DbUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Listener to handle the various setups and configuration sanity checks that can be carried out at when the
 * context is deployed/initalized.
 */

public class ContextListener implements ServletContextListener {
    private final Logger log = LoggerFactory.getLogger(getClass());


    /**
     * On context initialisation this
     * i) Initialises the logging framework (logback).
     * ii) Initialises the configuration class.
     * @param sce context provided by the web server upon initialization.
     * @throws java.lang.RuntimeException if anything at all goes wrong.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            log.info("Initializing service v{}", getClass().getPackage().getImplementationVersion());
            InitialContext ctx = new InitialContext();
            String configFile = (String) ctx.lookup("java:/comp/env/application-config");
            //TODO this should not refer to something in template. Should we perhaps use reflection here?
            ServiceConfig.initialize(configFile);                                                          
            initialiseStorage();  
        }
        catch (NamingException e) {
            throw new RuntimeException("Failed to lookup settings", e);
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to load settings", e);
        } 
        log.info("Service initialized.");
    }
    
    
    /*
     * Must be called after properties are initialized
     */
    public void initialiseStorage() {
       log.info("Initializing storage");
       
       String driver = ServiceConfig.getDBDriver();
       String url = ServiceConfig.getDBUrl();
       String user = ServiceConfig.getDBUserName();
       String password = ServiceConfig.getDBPassword();
                     
       //If running jetty for development
       if ("org.h2.Driver".equals(driver)) { //Would be slightly better if we can detect it is jetty in local environment
         createLocalH2ForJettyEnvironment(driver, url, user, password);
       }
       
       LicenseModuleStorage.initialize(driver,url,user,password);                        
    }


    private void createLocalH2ForJettyEnvironment(String driver, String url, String user, String password) {
        try {
         log.info("Setting up H2 database under jetty in development mode");          
         H2DbUtil.createEmptyH2DBFromDDL(url, driver,  user, password);
       }
       catch(Exception e) {
         log.error("Unable to create local h2 database for jetty environment",e);             
       }
    }
    

    // this is called by the web-container at shutdown. (defined in web.xml)
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
         log.info("Shutdown service v{}", getClass().getPackage().getImplementationVersion());
         LicenseModuleStorage.shutdown();
            
            Enumeration<Driver> drivers = DriverManager.getDrivers();
            
            while (drivers.hasMoreElements()) {
                Driver driver = drivers.nextElement();
                
                try {
                   log.debug("deregistering jdbc driver: {}", driver);
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException e) {
                    log.debug("Error deregistering driver {}", driver, e);
                }
            }            
        } catch (Exception e) {
            log.error("failed to shutdown Ds-Storage", e);
        }
        
    }

}
