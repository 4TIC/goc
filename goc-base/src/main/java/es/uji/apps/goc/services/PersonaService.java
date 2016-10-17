package es.uji.apps.goc.services;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import es.uji.apps.goc.dto.PersonaExterna;
import es.uji.apps.goc.exceptions.PersonasExternasException;
import es.uji.apps.goc.exceptions.RolesPersonaExternaException;
import es.uji.apps.goc.model.JSONListaRolesPersonaDeserializer;
import es.uji.apps.goc.model.Persona;
import es.uji.apps.goc.model.Rol;
import es.uji.commons.rest.CoreBaseService;

@Service
public class PersonaService extends CoreBaseService
{
    @Value("${goc.external.authToken}")
    private String authToken;

    @Value("${goc.external.personasEndpoint}")
    private String personasExternasEndpoint;

    public Persona getPersonaFromDirectoryByPersonaId(Long personaId)
            throws PersonasExternasException
    {
        WebResource getOrganosResource = Client.create()
                .resource(this.personasExternasEndpoint + "/" + personaId.toString());

        ClientResponse response = getOrganosResource.type(MediaType.APPLICATION_JSON)
                .header("X-UJI-AuthToken", authToken).get(ClientResponse.class);

        if (response.getStatus() != 200)
        {
            throw new PersonasExternasException();
        }

        PersonaExterna personaExterna = response.getEntity(PersonaExterna.class);

        return personaExternaToPersona(personaExterna);
    }

    private Persona personaExternaToPersona(PersonaExterna personaExterna)
    {
        Persona persona = new Persona();

        persona.setId(personaExterna.getId());
        persona.setNombre(personaExterna.getNombre());
        persona.setEmail(personaExterna.getEmail());

        return persona;
    }

    public List<Rol> getRolesFromPersonaId(Long personaId) throws RolesPersonaExternaException
    {
        WebResource getOrganosResource = Client.create()
                .resource(this.personasExternasEndpoint + "/" + personaId.toString() + "/roles");

        ClientResponse response = getOrganosResource.type(MediaType.APPLICATION_JSON)
                .header("X-UJI-AuthToken", authToken).get(ClientResponse.class);

        if (response.getStatus() != 200)
        {
            throw new RolesPersonaExternaException();
        }

        JSONListaRolesPersonaDeserializer jsonDeserializer = response
                .getEntity(JSONListaRolesPersonaDeserializer.class);

        List<Rol> roles = jsonDeserializer.getRoles();

        return roles;
    }
}
