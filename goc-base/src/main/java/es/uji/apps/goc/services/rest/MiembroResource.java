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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.goc.exceptions.MiembroNoDisponibleException;
import es.uji.apps.goc.exceptions.MiembrosExternosException;
import es.uji.apps.goc.exceptions.OrganoNoDisponibleException;
import es.uji.apps.goc.model.Cargo;
import es.uji.apps.goc.model.Miembro;
import es.uji.apps.goc.model.Organo;
import es.uji.apps.goc.services.MiembroService;
import es.uji.commons.rest.CoreBaseService;
import es.uji.commons.rest.UIEntity;
import es.uji.commons.sso.AccessManager;

@Path("miembros")
public class MiembroResource extends CoreBaseService
{
    @InjectParam
    private MiembroService miembroService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getTiposOrganos(@QueryParam("organoId") String organoId,
            @QueryParam("externo") Boolean externo) throws MiembrosExternosException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        List<Miembro> listaMiembros = getMiembros(organoId, externo, connectedUserId);
        return UIEntity.toUI(listaMiembros);
    }

    private List<Miembro> getMiembros(String organoId, Boolean externo, Long connectedUserId)
            throws MiembrosExternosException
    {
        List<Miembro> listaMiembros;
        if (externo != null && externo)
        {
            listaMiembros = miembroService.getMiembrosExternos(organoId, connectedUserId);
        }
        else
        {
            listaMiembros = miembroService.getMiembrosLocales(Long.parseLong(organoId),
                    connectedUserId);
        }
        return listaMiembros;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity addMiembro(UIEntity miembroUI) throws OrganoNoDisponibleException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        Miembro miembro = uiToModel(miembroUI);
        miembro = miembroService.addMiembro(miembro, connectedUserId);

        return UIEntity.toUI(miembro);
    }

    @PUT
    @Path("{miembroId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity modificaMiembro(@PathParam("miembroId") Long miembroId, UIEntity miembroUI)
            throws OrganoNoDisponibleException, MiembroNoDisponibleException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        String nombre = miembroUI.get("nombre");
        String email = miembroUI.get("email");
        String cargoId = miembroUI.get("cargoId");

        Miembro miembro = miembroService.updateMiembro(miembroId, nombre, email, cargoId,
                connectedUserId);

        return UIEntity.toUI(miembro);
    }

    @DELETE
    @Path("{miembroId}")
    public Response borraMiembro(@PathParam("miembroId") Long miembroId, UIEntity entity)
            throws MiembroNoDisponibleException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        miembroService.removeMiembroById(miembroId, connectedUserId);
        return Response.ok().build();
    }

    private Miembro uiToModel(UIEntity miembroUI)
    {
        Miembro miembro = new Miembro();
        miembro.setId(Long.parseLong(miembroUI.get("personaId")));
        miembro.setNombre(miembroUI.get("nombre"));
        miembro.setEmail(miembroUI.get("email"));

        if (miembroUI.get("organoId") != null)
        {
            Organo organo = new Organo(miembroUI.get("organoId"));
            miembro.setOrgano(organo);
        }

        if (miembroUI.get("cargoId") != null)
        {
            Cargo cargo = new Cargo(miembroUI.get("cargoId"));
            miembro.setCargo(cargo);
        }

        return miembro;
    }

}
