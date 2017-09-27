package es.uji.apps.goc.services.rest;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.spi.spring.container.servlet.SpringServlet;
import com.sun.jersey.test.framework.JerseyTest;
import com.sun.jersey.test.framework.WebAppDescriptor;
import es.uji.apps.goc.Utils;
import es.uji.apps.goc.dto.OrganoExterno;
import es.uji.commons.rest.json.UIEntityListMessageBodyReader;
import es.uji.commons.rest.json.UIEntityMessageBodyReader;
import es.uji.commons.rest.json.UIEntityMessageBodyWriter;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;

import java.io.FileInputStream;
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
        super(new WebAppDescriptor.Builder(PACKAGE_NAME).contextParam("contextConfigLocation",
                "classpath:applicationContext.xml")
                .contextParam("webAppRootKey", PACKAGE_NAME)
                .contextListenerClass(ContextLoaderListener.class)
                .requestListenerClass(RequestContextListener.class)
                .servletClass(SpringServlet.class)
                .clientConfig(Utils.createClientConfig())
                .initParam("com.sun.jersey.config.property.packages",
                        "es.uji.commons.rest.shared; es.uji.commons.rest.json; com.fasterxml.jackson.jaxrs.json; " + PACKAGE_NAME)
                .build());

        initAuthProperties();

        this.client().addFilter(new LoggingFilter());
        this.resource = resource();
    }

    private void initAuthProperties()
    {
        try
        {
            String home = System.getProperty("goc.home");

            Properties config = new Properties();

            FileInputStream in = new FileInputStream(home + "/app.properties");
            config.load(in);

            authToken = config.getProperty("goc.external.authToken");
            Integer index = config.getProperty("goc.external.organosEndpoint").indexOf("/rest") + 6;
            organosUrl = config.getProperty("goc.external.organosEndpoint").substring(index);
            miembrosUrl = config.getProperty("goc.external.miembrosEndpoint").substring(index);
            firmasUrl = config.getProperty("goc.external.firmasEndpoint").substring(index);
            notificacionesUrl = config.getProperty("goc.external.notificacionesEndpoint").substring(index);
        }
        catch (IOException e)
        {
            throw new RuntimeException("No se han podido cargar las propiedades del fichero de configuración");
        }
    }
}
