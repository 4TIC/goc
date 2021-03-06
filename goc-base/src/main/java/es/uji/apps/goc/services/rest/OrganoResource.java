package es.uji.apps.goc.services.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.goc.dto.OrganoAutorizado;
import es.uji.apps.goc.dto.OrganoInvitado;
import es.uji.apps.goc.exceptions.OrganoNoDisponibleException;
import es.uji.apps.goc.exceptions.OrganosExternosException;
import es.uji.apps.goc.exceptions.RolesPersonaExternaException;
import es.uji.apps.goc.model.Organo;
import es.uji.apps.goc.model.TipoOrgano;
import es.uji.apps.goc.services.OrganoService;
import es.uji.apps.goc.services.PersonaService;
import es.uji.commons.rest.CoreBaseService;
import es.uji.commons.rest.ParamUtils;
import es.uji.commons.rest.UIEntity;
import es.uji.commons.sso.AccessManager;

@Path("organos")
public class OrganoResource extends CoreBaseService
{
    @InjectParam
    OrganoService organoService;

    @InjectParam
    PersonaService personaService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getOrganos(@QueryParam("reunionId") Long reunionId)
            throws OrganosExternosException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        List<Organo> listaOrganos;

        if (reunionId != null)
        {
            listaOrganos = organoService.getOrganosByReunionId(reunionId, connectedUserId);
        }
        else
        {
        listaOrganos = organoService.getOrganosByAdminAndAutorizadoId(connectedUserId);
        }

        return organosToUI(listaOrganos);
    }

    @GET
    @Path("convocables")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getOrganosByAutorizado(@QueryParam("reunionId") Long reunionId)
            throws OrganosExternosException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        List<Organo> listaOrganos;

        if (reunionId != null)
        {
            listaOrganos = organoService.getOrganosByReunionId(reunionId, connectedUserId);
        }
        else
        {
            listaOrganos = organoService.getOrganosPorAutorizadoId(connectedUserId);
        }

        return organosToUI(listaOrganos);

    }

    @GET
    @Path("activos/usuario")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getOrganosActivosByUserId()
            throws OrganosExternosException, RolesPersonaExternaException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        List<Organo> listaOrganos;

        if (personaService.isUsuario(connectedUserId))
        {
            listaOrganos = organoService.getOrganosPorAutorizadoId(connectedUserId);
        }
        else
        {
            listaOrganos = organoService.getOrganosByAdminAndAutorizadoId(connectedUserId);
        }

        listaOrganos = listaOrganos.stream().filter(o -> o.isExterno() || !o.isInactivo()).collect(Collectors.toList());

        return organosToUI(listaOrganos);
    }

    private List<UIEntity> organosToUI(List<Organo> listaOrganos)
    {
        List<UIEntity> listaUI = new ArrayList<>();

        for (Organo organo : listaOrganos)
        {
            listaUI.add(organoToUI(organo));
        }

        return listaUI;
    }

    private UIEntity organoToUI(Organo organo)
    {
        UIEntity ui = new UIEntity();
        ui.put("id", organo.getId());
        ui.put("nombre", organo.getNombre());
        ui.put("nombreAlternativo", organo.getNombreAlternativo());
        ui.put("externo", organo.isExterno());
        ui.put("inactivo", organo.isInactivo());
        ui.put("tipoOrganoId", organo.getTipoOrgano().getId());

        return ui;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity addOrgano(UIEntity organoUI)
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        Organo organo = uiToModel(organoUI);
        organo = organoService.addOrgano(organo, connectedUserId);

        return UIEntity.toUI(organo);
    }

    @PUT
    @Path("{organoId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity modificaOrgano(@PathParam("organoId") Long organoId, UIEntity organoUI)
            throws OrganoNoDisponibleException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        String nombre = organoUI.get("nombre");
        String nombreAlternativo = organoUI.get("nombreAlternativo");
        Long tipoOrganoId = Long.parseLong(organoUI.get("tipoOrganoId"));
        Boolean inactivo = new Boolean(organoUI.get("inactivo"));

        Organo organo = organoService.updateOrgano(organoId, nombre, nombreAlternativo, tipoOrganoId, inactivo,
                connectedUserId);

        return UIEntity.toUI(organo);
    }

    private Organo uiToModel(UIEntity organoUI)
    {
        Organo organo = new Organo();

        if (ParamUtils.parseLong(organoUI.get("id")) != null)
        {
            organo.setId(organoUI.get("id"));
        }

        organo.setNombre(organoUI.get("nombre"));
        organo.setNombreAlternativo(organoUI.get("nombreAlternativo"));
        organo.setInactivo(false);

        TipoOrgano tipoOrgano = new TipoOrgano();
        tipoOrgano.setId(Long.parseLong(organoUI.get("tipoOrganoId")));
        organo.setTipoOrgano(tipoOrgano);

        return organo;
    }

    @GET
    @Path("autorizados")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getAutorizados(@QueryParam("organoId") String organoId,
            @QueryParam("externo") Boolean externo)
    {
        return UIEntity.toUI(organoService.getAutorizados(organoId, externo));
    }

    @POST
    @Path("autorizados")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity addAutorizado(UIEntity autorizado)
    {
        OrganoAutorizado newOrganoAutorizado = organoService.addAutorizado(autorizado.toModel(OrganoAutorizado.class));

        return UIEntity.toUI(newOrganoAutorizado);
    }

    @DELETE
    @Path("autorizados/{organoAutorizadoId}")
    public Response borraAutorizado(@PathParam("organoAutorizadoId") Long organoAutorizadoId)
    {
        organoService.removeAutorizado(organoAutorizadoId);

        return Response.ok().build();
    }

    @GET
    @Path("invitados")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getInvitados(@QueryParam("organoId") String organoId)
    {
        return UIEntity.toUI(organoService.getInvitados(organoId));
    }

    @POST
    @Path("invitados")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity addInvitado(UIEntity invitado)
    {
        OrganoInvitado newOrganoInvitado = organoService.addInvitado(invitado.toModel(OrganoInvitado.class));

        return UIEntity.toUI(newOrganoInvitado);
    }

    @PUT
    @Path("invitados/{organoInvitadoId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity updateInvitado(UIEntity invitado, @PathParam("organoInvitadoId") Long organoInvitadoId)
    {
        organoService.updateInvitado(invitado.toModel(OrganoInvitado.class));

        return UIEntity.toUI(invitado);
    }

    @DELETE
    @Path("invitados/{organoInvitadoId}")
    public Response borraInvitado(@PathParam("organoInvitadoId") Long organoInvitadoId)
    {
        organoService.removeInvitado(organoInvitadoId);

        return Response.ok().build();
    }
}
