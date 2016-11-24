package es.uji.apps.goc.services.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.jersey.api.core.InjectParam;
import es.uji.apps.goc.dto.ReunionFirma;
import es.uji.apps.goc.exceptions.RolesPersonaExternaException;
import es.uji.apps.goc.firmas.FirmaService;
import es.uji.apps.goc.model.Persona;
import es.uji.apps.goc.notifications.CanNotSendException;
import es.uji.apps.goc.notifications.MailSender;
import es.uji.apps.goc.notifications.Mensaje;
import es.uji.apps.goc.services.ExternalService;
import es.uji.apps.goc.services.PersonaService;
import es.uji.commons.rest.CoreBaseService;
import es.uji.commons.sso.AccessManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("external")
public class ExternalResource extends CoreBaseService
{
    @InjectParam
    private ExternalService externalService;

    @InjectParam
    private PersonaService personaService;

    @InjectParam
    private MailSender mailSender;

    @InjectParam
    private FirmaService firmaService;

    @Path("organos")
    public ExternalOrganoResource getExternalOrganoResource(
            @InjectParam ExternalOrganoResource externalOrganoResource)
    {
        return externalOrganoResource;
    }

    @Path("personas")
    public ExternalPersonaResource getExternalPersonaResource(
            @InjectParam ExternalPersonaResource externalPersonaResource)
    {
        return externalPersonaResource;
    }

    @POST
    @Path("notificaciones")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void enviaNotificacion(Mensaje mensaje) throws CanNotSendException
    {
        mailSender.send(mensaje);
    }

    @POST
    @Path("firmas")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void firmaReunion(ReunionFirma reunionFirma)
    {
        firmaService.firmaReunion(reunionFirma);
    }

    @GET
    @Path("config/menus")
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectNode menus(@QueryParam("lang") String lang) throws RolesPersonaExternaException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        List<String> roles = personaService.getRolesFromPersonaId(connectedUserId);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        ArrayNode rows = result.putArray("row");

        Boolean admin = roles.stream().filter(r -> r.equals("ADMIN")).findAny().isPresent();
        String currentLanguage = (lang != null) ? lang : "es";

        if (admin)
        {
            if (currentLanguage.equals("es"))
            {
                rows.add(menuEntry(mapper, "goc.view.organo.Main", "Órganos"));
                rows.add(menuEntry(mapper, "goc.view.tipoOrgano.Main", "Tipos de órganos"));
                rows.add(menuEntry(mapper, "goc.view.miembro.Main", "Miembros"));
                rows.add(menuEntry(mapper, "goc.view.reunion.Main", "Reuniones"));
                rows.add(menuEntry(mapper, "goc.view.historicoReunion.Main",
                        "Histórico de Reuniones"));
                rows.add(menuEntry(mapper, "goc.view.cargo.Main", "Cargos"));
            }
            else
            {
                rows.add(menuEntry(mapper, "goc.view.organo.Main", "Òrgans"));
                rows.add(menuEntry(mapper, "goc.view.tipoOrgano.Main", "Tipus d'òrgans"));
                rows.add(menuEntry(mapper, "goc.view.miembro.Main", "Membres"));
                rows.add(menuEntry(mapper, "goc.view.reunion.Main", "Reunions"));
                rows.add(menuEntry(mapper, "goc.view.historicoReunion.Main",
                        "Històric de Reunions"));
                rows.add(menuEntry(mapper, "goc.view.cargo.Main", "Càrrecs"));
            }
            return result;

        }

        if (currentLanguage.equals("es"))
        {
            rows.add(menuEntry(mapper, "goc.view.organo.Main", "Órganos"));
            rows.add(menuEntry(mapper, "goc.view.miembro.Main", "Miembros"));
            rows.add(menuEntry(mapper, "goc.view.reunion.Main", "Reuniones"));
            rows.add(menuEntry(mapper, "goc.view.historicoReunion.Main", "Histórico de Reuniones"));
        }
        else
        {
            rows.add(menuEntry(mapper, "goc.view.organo.Main", "Òrgans"));
            rows.add(menuEntry(mapper, "goc.view.miembro.Main", "Membres"));
            rows.add(menuEntry(mapper, "goc.view.reunion.Main", "Reunions"));
            rows.add(menuEntry(mapper, "goc.view.historicoReunion.Main", "Històric de Reunions"));
        }
        return result;
    }

    @GET
    @Path("cuentas/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Persona personaAsociadaACuenta(@PathParam("username") String username)
            throws RolesPersonaExternaException
    {
        if (username == null)
        {
            throw new RuntimeException("Cuenta de usuario no definida");
        }

        Persona persona = new Persona();

        if (username.equals("nmanero"))
            persona.setId(88849L);

        if (username.equals("borillo"))
            persona.setId(9792L);

        return persona;
    }

    private ObjectNode menuEntry(ObjectMapper mapper, String className, String title)
    {
        ObjectNode node = mapper.createObjectNode();

        node.put("id", className);
        node.put("title", title);
        node.put("text", title);
        node.put("leaf", "true");
        node.put("checked", (JsonNode) null);
        node.putArray("row");

        return node;
    }
}
