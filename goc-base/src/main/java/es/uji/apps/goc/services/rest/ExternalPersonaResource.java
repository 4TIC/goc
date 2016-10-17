package es.uji.apps.goc.services.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.goc.model.Persona;
import es.uji.apps.goc.model.Rol;
import es.uji.apps.goc.services.ExternalService;
import es.uji.commons.rest.CoreBaseService;
import es.uji.commons.rest.UIEntity;
import es.uji.commons.sso.AccessManager;

@Path("/external/personas")
public class ExternalPersonaResource extends CoreBaseService
{
    @InjectParam
    private ExternalService externalService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getPersonaByQueryString(@QueryParam("query") String query)
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        List<Persona> listaPersonas = externalService.getPersonasByQueryString(query, connectedUserId);

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

    @GET
    @Path("{personaId}")
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity getPersonaById(@PathParam("personaId") Long personaId)
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        Persona persona = externalService.getPersonaById(personaId, connectedUserId);

        return personaToUI(persona);
    }

    @GET
    @Path("{personaId}/roles")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getRolesByPersonaId(@PathParam("personaId") Long personaId)
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        Rol rol = externalService.getRolesByPersonaId(personaId);

        return UIEntity.toUI(Collections.singletonList(rol));
    }

}
