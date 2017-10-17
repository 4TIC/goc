package es.uji.apps.goc.services.rest;

import static es.uji.apps.goc.services.rest.ResponseHasDataMatcher.hasData;
import static org.junit.Assert.assertThat;

import javax.ws.rs.core.MediaType;

import es.uji.apps.goc.dto.MiembroExterno;
import org.junit.Test;
import org.junit.Ignore;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;

import es.uji.apps.goc.dto.ResponseMessage;

@Ignore
public class ExternalMiembrosTest extends JerseySpringTest
{
    @Test
    public void obtenerMiembros() throws Exception
    {
        ClientResponse response = resource.path(miembrosUrl)
                .type(MediaType.APPLICATION_JSON)
                .header("X-UJI-AuthToken", authToken)
                .get(ClientResponse.class);

        ResponseMessage<MiembroExterno> responseMessage = response.getEntity(new GenericType<ResponseMessage<MiembroExterno>>()
        {
        });

        assertThat(responseMessage, hasData());
    }
}
