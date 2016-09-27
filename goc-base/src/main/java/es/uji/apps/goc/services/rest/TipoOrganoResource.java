package es.uji.apps.goc.services.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.goc.model.TipoOrgano;
import es.uji.apps.goc.services.TipoOrganoService;
import es.uji.commons.rest.CoreBaseService;
import es.uji.commons.rest.UIEntity;
import es.uji.commons.sso.AccessManager;

@Path("tipoOrganos")
public class TipoOrganoResource extends CoreBaseService
{
    @InjectParam
    TipoOrganoService tipoOrganoService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getTiposOrganos()
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        List<TipoOrgano> listaTiposOrganos = tipoOrganoService.getTiposOrgano(connectedUserId);

        return UIEntity.toUI(listaTiposOrganos);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity addTipoOrgano(UIEntity tipoOrganoUI)
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        TipoOrgano tipoOrgano = tipoOrganoUI.toModel(TipoOrgano.class);
        tipoOrgano = tipoOrganoService.addTipoOrgano(tipoOrgano, connectedUserId);

        return UIEntity.toUI(tipoOrgano);
    }

    @PUT
    @Path("{tipoOrganoId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity modificaTipoOrgano(@PathParam("tipoOrganoId") Long tipoOrganoId,
            UIEntity tipoOrganoUI)
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        TipoOrgano tipoOrgano = tipoOrganoUI.toModel(TipoOrgano.class);
        tipoOrgano = tipoOrganoService.updateTipoOrgano(tipoOrgano, connectedUserId);

        return UIEntity.toUI(tipoOrgano);
    }

    @DELETE
    @Path("{tipoOrganoId}")
    public Response borraTipoOrgano(@PathParam("tipoOrganoId") Long tipoOrganoId, UIEntity entity)
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        tipoOrganoService.removeTipoOrganoById(tipoOrganoId);
        return Response.ok().build();
    }
}
