package es.uji.apps.goc.dao;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import es.uji.apps.goc.exceptions.NotificacionesException;
import es.uji.apps.goc.notifications.Mensaje;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.ws.rs.core.MediaType;

@Repository
public class NotificacionesDAO
{
    @Value("${goc.external.authToken}")
    private String authToken;

    @Value("${goc.external.notificacionesEndpoint}")
    private String notificacionesEndpoint;

    public void enviaNotificacion(Mensaje mensaje) throws NotificacionesException
    {
        WebResource resource = Client.create().resource(this.notificacionesEndpoint);

        ClientResponse response = resource
                .type(MediaType.APPLICATION_JSON)
                .header("X-UJI-AuthToken", authToken)
                .post(ClientResponse.class, mensaje);

        if (response.getStatus() != 204)
        {
            throw new NotificacionesException();
        }
    }
}
