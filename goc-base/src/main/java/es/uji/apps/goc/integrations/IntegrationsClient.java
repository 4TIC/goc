package es.uji.apps.goc.integrations;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;

@Component
public abstract class IntegrationsClient
{
    @Value("${goc.external.authToken}")
    private String authToken;

    protected WebResource createResource(String url)
    {
        ClientConfig clientConfig = new DefaultClientConfig();

        Client client = Client.create(clientConfig);
        return client.resource(url);
    }

    protected ClientResponse get(String url)
    {
        return createResource(url)
                .type(MediaType.APPLICATION_JSON)
                .header("X-UJI-AuthToken", authToken)
                .get(ClientResponse.class);
    }
}
