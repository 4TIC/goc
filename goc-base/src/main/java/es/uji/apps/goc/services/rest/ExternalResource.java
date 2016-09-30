package es.uji.apps.goc.services.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.goc.dto.ReunionFirma;
import es.uji.apps.goc.firmas.FirmaService;
import es.uji.apps.goc.notifications.CanNotSendException;
import es.uji.apps.goc.notifications.MailSender;
import es.uji.apps.goc.notifications.Mensaje;
import es.uji.apps.goc.services.ExternalService;
import es.uji.commons.rest.CoreBaseService;

@Path("external")
public class ExternalResource extends CoreBaseService
{
    @InjectParam
    private ExternalService externalService;

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
    public ObjectNode menus(@QueryParam("lang") String lang)
    {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        ArrayNode rows = result.putArray("row");

        if (lang.equals("es")) {
            rows.add(menuEntry(mapper, "goc.view.organo.Main", "Órganos"));
            rows.add(menuEntry(mapper, "goc.view.tipoOrgano.Main", "Tipos de órganos"));
            rows.add(menuEntry(mapper, "goc.view.miembro.Main", "Miembros"));
            rows.add(menuEntry(mapper, "goc.view.reunion.Main", "Reuniones"));
            rows.add(menuEntry(mapper, "goc.view.cargo.Main", "Cargos"));
        } else {
            rows.add(menuEntry(mapper, "goc.view.organo.Main", "Òrgans"));
            rows.add(menuEntry(mapper, "goc.view.tipoOrgano.Main", "Tipus d'organs"));
            rows.add(menuEntry(mapper, "goc.view.miembro.Main", "Membres"));
            rows.add(menuEntry(mapper, "goc.view.reunion.Main", "Reunions"));
            rows.add(menuEntry(mapper, "goc.view.cargo.Main", "Càrrecs"));
        }

        return result;
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
