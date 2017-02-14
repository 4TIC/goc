package es.uji.apps.goc.services.rest;

import com.sun.jersey.api.core.InjectParam;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import es.uji.apps.goc.exceptions.PersonasExternasException;
import es.uji.apps.goc.model.Persona;
import es.uji.apps.goc.services.PersonaService;
import es.uji.commons.rest.CoreBaseService;
import es.uji.commons.rest.UIEntity;
import es.uji.commons.sso.AccessManager;

@Path("personas")
public class PersonaResource extends CoreBaseService
{
    @InjectParam
    private PersonaService personaService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getPersonaByQueryString(@QueryParam("query") String query) throws PersonasExternasException {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        List<Persona> listaPersonas = personaService.getPersonasByQueryString(query, connectedUserId);

        return personasToUI(listaPersonas);
    }

    private List<UIEntity> personasToUI(List<Persona> listaPersonas)
    {
        List<UIEntity> personasUI = new ArrayList<>();

        for (Persona persona: listaPersonas) {
            personasUI.add(personaToUI(persona));
        }
        return personasUI;
    }

    private UIEntity personaToUI(Persona persona) {
        UIEntity personaUI = new UIEntity();

        personaUI.put("id", persona.getId());
        personaUI.put("nombre", persona.getNombre());
        personaUI.put("email", persona.getEmail());

        return personaUI;
    }
}
