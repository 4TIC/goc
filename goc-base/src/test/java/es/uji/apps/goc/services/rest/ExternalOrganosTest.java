package es.uji.apps.goc.services.rest;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import es.uji.apps.goc.dto.OrganoExterno;
import es.uji.apps.goc.dto.ResponseMessage;
import org.junit.Test;
import org.junit.Ignore;

import javax.ws.rs.core.MediaType;

import static es.uji.apps.goc.services.rest.ResponseHasDataMatcher.hasData;
import static org.junit.Assert.assertThat;

@Ignore
public class ExternalOrganosTest extends JerseySpringTest
{
    @Test
    public void obtenerOrganos() throws Exception
    {
        ClientResponse response = resource.path(organosUrl)
                .type(MediaType.APPLICATION_JSON)
                .header("X-UJI-AuthToken", authToken)
                .get(ClientResponse.class);

        ResponseMessage<OrganoExterno> responseMessage = response.getEntity(new GenericType<ResponseMessage<OrganoExterno>>()
        {
        });

        assertThat(responseMessage, hasData());
    }
}
