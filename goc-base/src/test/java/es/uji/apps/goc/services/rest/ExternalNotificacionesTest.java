package es.uji.apps.goc.services.rest;

import com.sun.jersey.api.client.ClientResponse;
import es.uji.apps.goc.notifications.Mensaje;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.util.Arrays;

import static es.uji.commons.testing.hamcrest.ClientNoContentResponseMatcher.noContentClientResponse;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ExternalNotificacionesTest extends JerseySpringTest
{
    @Test
    @Ignore
    public void envioNotificaciones() throws Exception
    {
        Mensaje mensaje = new Mensaje();
        mensaje.setAsunto("test");
        mensaje.setContentType("text/plain");
        mensaje.setCuerpo("prova");
        mensaje.setFrom("borillo@uji.es");
        mensaje.setDestinos(Arrays.asList("borillo@gmail.com"));

        ClientResponse response = this.resource.path(notificacionesUrl)
                .type(MediaType.APPLICATION_JSON)
                .header("X-UJI-AuthToken", authToken)
                .post(ClientResponse.class, mensaje);

        assertThat(response, noContentClientResponse());
    }
}
