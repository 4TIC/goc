package es.uji.apps.goc;

import io.swagger.jaxrs.config.SwaggerContextService;
import io.swagger.models.Info;
import io.swagger.models.Swagger;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.In;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.Arrays;

public class Bootstrap extends HttpServlet
{
    @Override
    public void init(ServletConfig config) throws ServletException
    {
        Info info = new Info();
        info.setTitle("REST API: Gestión de órganos colegiados");
        info.setDescription("Puntos de extensión a implementar");
        info.setVersion("0.1.0");

        Swagger swagger = new Swagger();
        swagger.setInfo(info);
        swagger.setBasePath("/goc/rest");
        swagger.setProduces(Arrays.asList("application/json"));
        swagger.securityDefinition("X-UJI-AuthToken", new ApiKeyAuthDefinition("X-UJI-AuthToken", In.HEADER));

        new SwaggerContextService()
                .withServletConfig(config)
                .updateSwagger(swagger);
    }
}