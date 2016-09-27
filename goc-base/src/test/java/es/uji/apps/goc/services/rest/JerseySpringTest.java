package es.uji.apps.goc.services.rest;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import es.uji.apps.goc.dto.OrganoExterno;
import es.uji.commons.rest.json.UIEntityListMessageBodyReader;
import es.uji.commons.rest.json.UIEntityMessageBodyReader;
import es.uji.commons.rest.json.UIEntityMessageBodyWriter;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import java.io.IOException;
import java.util.Properties;

public class JerseySpringTest extends JerseyTest
{
    private static final String PACKAGE_NAME = "es.uji.apps.goc.services.rest";

    protected WebResource resource;
    protected String authToken;
    protected String organosUrl;
    protected String miembrosUrl;
    protected String firmasUrl;
    protected String notificacionesUrl;

    public JerseySpringTest()
    {
        super(new WebAppDescriptor.Builder(PACKAGE_NAME)
                .contextParam("contextConfigLocation", "classpath:applicationContext.xml")
                .contextParam("webAppRootKey", PACKAGE_NAME)
                .contextListenerClass(ContextLoaderListener.class)
                .requestListenerClass(RequestContextListener.class)
                .servletClass(SpringServlet.class)
                .clientConfig(createClientConfig())
                .initParam("com.sun.jersey.config.property.packages",
                        "es.uji.commons.rest.shared; es.uji.commons.rest.json; com.fasterxml.jackson.jaxrs.json; " + PACKAGE_NAME).build());

        this.client().addFilter(new LoggingFilter());
        this.resource = resource();

        initAuthProperties();
    }

    private static ClientConfig createClientConfig()
    {
        ClientConfig config = new DefaultClientConfig();
        config.getClasses().add(UIEntityMessageBodyReader.class);
        config.getClasses().add(UIEntityListMessageBodyReader.class);
        config.getClasses().add(UIEntityMessageBodyWriter.class);

        return config;
    }

    private void initAuthProperties()
    {
        try
        {
            Properties config = new Properties();
            config.load(OrganoExterno.class.getClassLoader().getResourceAsStream("app.properties"));

            authToken = config.getProperty("goc.external.authToken");
            organosUrl = config.getProperty("goc.external.organosEndpoint").replace("http://localhost:9005/goc/rest", "");
            miembrosUrl = config.getProperty("goc.external.miembrosEndpoint").replace("http://localhost:9005/goc/rest", "");
            firmasUrl = config.getProperty("goc.external.firmasEndpoint").replace("http://localhost:9005/goc/rest", "");
            notificacionesUrl = config.getProperty("goc.external.notificacionesEndpoint").replace("http://localhost:9005/goc/rest", "");
        }
        catch (IOException e)
        {
            throw new RuntimeException("No se han podido cargar las propiedades del fichero de configuración");
        }
    }
}
