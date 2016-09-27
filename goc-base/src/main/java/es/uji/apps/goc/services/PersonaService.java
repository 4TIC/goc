package es.uji.apps.goc.services;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import es.uji.apps.goc.dto.OrganoExterno;
import es.uji.apps.goc.dto.PersonaExterna;
import es.uji.apps.goc.exceptions.PersonasExternasException;
import es.uji.apps.goc.model.JSONListaOrganosExternosDeserializer;
import es.uji.apps.goc.model.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.goc.dao.ReunionDAO;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.dto.ReunionTemplate;
import es.uji.apps.goc.exceptions.MiembrosExternosException;
import es.uji.apps.goc.exceptions.OrganosExternosException;
import es.uji.apps.goc.exceptions.ReunionNoDisponibleException;
import es.uji.commons.rest.CoreBaseService;
import es.uji.commons.sso.AccessManager;
import es.uji.commons.web.template.HTMLTemplate;
import es.uji.commons.web.template.Template;

import java.util.List;

@Service
public class PersonaService extends CoreBaseService
{
    @Value("${goc.external.authToken}")
    private String authToken;

    @Value("${goc.external.personasEndpoint}")
    private String personasExternasEndpoint;

    public Persona getPersonaFromDirectoryByPersonaId(Long personaId) throws PersonasExternasException
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

}
