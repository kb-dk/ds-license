package dk.kb.license.webservice;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.fasterxml.jackson.jaxrs.xml.JacksonXMLProvider;
import dk.kb.license.api.v1.impl.DsLicenseApiServiceImpl;
import dk.kb.util.webservice.exception.ServiceExceptionMapper;


public class Application_v1 extends javax.ws.rs.core.Application {

    @Override
    public Set<Class<?>> getClasses() {
        return new HashSet<>(Arrays.asList(
                JacksonJsonProvider.class,
                JacksonXMLProvider.class,
                DsLicenseApiServiceImpl.class,
                ServiceExceptionMapper.class
        ));
    }


}
