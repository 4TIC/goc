package es.uji.apps.goc.integrations;

import com.sun.jersey.api.client.ClientResponse;
import es.uji.apps.goc.exceptions.CuentasExternasException;
import es.uji.apps.goc.model.Persona;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CuentasClient extends IntegrationsClient
{
    @Value("${goc.external.cuentasEndpoint}")
    private String cuentasEndpoint;

    public Long obtainPersonaIdFrom(String username) throws CuentasExternasException
    {
        ClientResponse response = get(cuentasEndpoint + "/" + username);

        if (response.getStatus() != 200)
        {
            throw new CuentasExternasException();
        }

        Persona persona = response.getEntity(Persona.class);
        return persona.getId();
    }
}