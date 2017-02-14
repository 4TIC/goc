package es.uji.apps.goc.services;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import es.uji.apps.goc.dto.PersonaExterna;
import es.uji.apps.goc.exceptions.PersonasExternasException;
import es.uji.apps.goc.exceptions.RolesPersonaExternaException;
import es.uji.apps.goc.model.JSONListaPersonasExternasDeserializer;
import es.uji.apps.goc.model.Persona;
import es.uji.commons.rest.CoreBaseService;

@Service
public class PersonaService extends CoreBaseService {
    @Value("${goc.external.authToken}")
    private String authToken;

    @Value("${goc.external.personasEndpoint}")
    private String personasExternasEndpoint;

    public Persona getPersonaFromDirectoryByPersonaId(Long personaId) throws PersonasExternasException {
        WebResource getOrganosResource =
            Client.create().resource(this.personasExternasEndpoint + "/" + personaId.toString());

        ClientResponse response =
            getOrganosResource.type(MediaType.APPLICATION_JSON).header("X-UJI-AuthToken", authToken)
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new PersonasExternasException();
        }

        PersonaExterna personaExterna = response.getEntity(PersonaExterna.class);

        return personaExternaToPersona(personaExterna);
    }

    private Persona personaExternaToPersona(PersonaExterna personaExterna) {
        Persona persona = new Persona();

        persona.setId(personaExterna.getId());
        persona.setNombre(personaExterna.getNombre());
        persona.setEmail(personaExterna.getEmail());

        return persona;
    }

    public List<String> getRolesFromPersonaId(Long personaId) throws RolesPersonaExternaException {
        WebResource getOrganosResource =
            Client.create().resource(this.personasExternasEndpoint + "/" + personaId.toString() + "/roles");

        ClientResponse response =
            getOrganosResource.type(MediaType.APPLICATION_JSON).header("X-UJI-AuthToken", authToken)
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new RolesPersonaExternaException();
        }

        return response.getEntity(new GenericType<List<String>>() {
        });
    }

    public List<Persona> getPersonasByQueryString(
        String query,
        Long connectedUserId
    ) throws PersonasExternasException {
        WebResource getPersonasResource = Client.create().resource(this.personasExternasEndpoint + "?query=" + query);

        ClientResponse response =
            getPersonasResource.type(MediaType.APPLICATION_JSON).header("X-UJI-AuthToken", authToken)
                .get(ClientResponse.class);

        if (response.getStatus() != 200) {
            throw new PersonasExternasException();
        }

        JSONListaPersonasExternasDeserializer jsonDeserializer = response.getEntity(JSONListaPersonasExternasDeserializer.class);

        List<PersonaExterna> listaPersonasExternos = jsonDeserializer.getPersonas();
        return creaListaMiembroDesdeListaMiembrosExternos(listaPersonasExternos);
    }

    private List<Persona> creaListaMiembroDesdeListaMiembrosExternos(List<PersonaExterna> listaPersonasExternas) {
        List<Persona> listaPersonas = new ArrayList<>();

        for (PersonaExterna personaExterna : listaPersonasExternas)
        {
            listaPersonas.add(personaExternaToPersona(personaExterna));
        }

        return listaPersonas;
    }
}
