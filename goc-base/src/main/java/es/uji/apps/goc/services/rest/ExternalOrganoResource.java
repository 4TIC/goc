package es.uji.apps.goc.services.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.goc.model.Miembro;
import es.uji.apps.goc.model.Organo;
import es.uji.apps.goc.services.ExternalService;
import es.uji.commons.rest.CoreBaseService;
import es.uji.commons.rest.UIEntity;
import es.uji.commons.sso.AccessManager;

@Path("/external/organo")
public class ExternalOrganoResource extends CoreBaseService
{
    @InjectParam
    private ExternalService externalService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getOrganosExternos()
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        List<Organo> listaOrganos = externalService.getOrganosExternos(connectedUserId);

        return organoToUI(listaOrganos);
    }

    private List<UIEntity> organoToUI(List<Organo> listaOrganos)
    {
        List<UIEntity> organosUI = new ArrayList<>();

        for (Organo organo : listaOrganos)
        {
            UIEntity organoUI = new UIEntity();

            organoUI.put("id", organo.getId());
            organoUI.put("nombre", organo.getNombre());
            organoUI.put("tipo_id", organo.getTipoOrgano().getId());
            organoUI.put("tipo_codigo", organo.getTipoOrgano().getCodigo());
            organoUI.put("tipo_nombre", organo.getTipoOrgano().getNombre());

            organosUI.add(organoUI);
        }
        return organosUI;
    }

    @GET
    @Path("{organoId}/miembros")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getMiembrosByOrganoExternoId(@PathParam("organoId") String organoId)
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        List<Miembro> listaMiembros = externalService.getMiembrosByOrganoId(organoId,
                connectedUserId);

        return miembroToUI(listaMiembros);
    }

    private List<UIEntity> miembroToUI(List<Miembro> listaMiembros)
    {
        List<UIEntity> miembrosUI = new ArrayList<>();

        for (Miembro miembro : listaMiembros)
        {
            UIEntity miembroUI = new UIEntity();

            miembroUI.put("id", miembro.getId());
            miembroUI.put("nombre", miembro.getNombre());
            miembroUI.put("email", miembro.getEmail());
            miembroUI.put("organo_id", miembro.getOrgano().getId());
            miembroUI.put("organo_nombre", miembro.getOrgano().getNombre());
            miembroUI.put("cargo_id", miembro.getCargo().getId());
            miembroUI.put("cargo_nombre", miembro.getCargo().getNombre());

            miembrosUI.add(miembroUI);
        }
        return miembrosUI;
    }
}
