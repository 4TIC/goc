package es.uji.apps.goc.services.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.goc.dto.OrganoReunionMiembro;
import es.uji.apps.goc.exceptions.MiembroNoDisponibleException;
import es.uji.apps.goc.exceptions.MiembrosExternosException;
import es.uji.apps.goc.model.Miembro;
import es.uji.apps.goc.services.MiembroService;
import es.uji.apps.goc.services.ReunionMiembroService;
import es.uji.commons.rest.CoreBaseService;
import es.uji.commons.rest.UIEntity;
import es.uji.commons.sso.AccessManager;

@Path("/reuniones/{reunionId}/miembros")
public class ReunionMiembroResource extends CoreBaseService
{
    @InjectParam
    private ReunionMiembroService reunionMiembroService;

    @InjectParam
    private MiembroService miembroService;

    @PathParam("reunionId")
    Long reunionId;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getMiembrosOrganoReunion(@QueryParam("organoId") String organoId,
            @QueryParam("externo") Boolean externo)
            throws MiembrosExternosException, MiembroNoDisponibleException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        if (reunionId != null && organoId == null)
        {
            List<OrganoReunionMiembro> listaMiembros = reunionMiembroService
                    .getMiembrosReunionByReunionId(reunionId, connectedUserId);
            return UIEntity.toUI(listaMiembros);

        }

        if (reunionId != null)
        {
            List<OrganoReunionMiembro> listaMiembros = reunionMiembroService
                    .getMiembrosReunionByReunionIdAndOrganoId(reunionId, organoId, externo, connectedUserId);
            return UIEntity.toUI(listaMiembros);
        }

        List<Miembro> listaMiembros;

        if (externo)
        {
            listaMiembros = miembroService.getMiembrosExternos(organoId, connectedUserId);
        }
        else
        {
            listaMiembros = miembroService.getMiembrosLocales(Long.parseLong(organoId), connectedUserId);
        }

        return miembrosToUI(listaMiembros, organoId, externo);
    }

    private List<UIEntity> miembrosToUI(List<Miembro> listaMiembros, String organoId,
            Boolean externo)
    {
        List<UIEntity> miembrosUI = new ArrayList<>();

        for (Miembro miembro : listaMiembros)
        {
            miembrosUI.add(miembroToUI(miembro, organoId, externo));
        }

        return miembrosUI;
    }

    private UIEntity miembroToUI(Miembro miembro, String organoId, Boolean externo)
    {
        UIEntity ui = new UIEntity();
        ui.put("id", miembro.getId());
        ui.put("nombre", miembro.getNombre());
        ui.put("email", miembro.getEmail());
        ui.put("organoId", organoId);
        ui.put("organoExterno", externo);
        ui.put("asistencia", true);

        return ui;
    }

    @PUT
    @Path("{miembroId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity modificaMiembroOrganoReunion(@PathParam("miembroId") Long miembroId,
            @QueryParam("reunionId") Long reunionId, UIEntity miembroUI)
            throws MiembroNoDisponibleException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        Boolean asistencia = new Boolean(miembroUI.get("asistencia"));

        OrganoReunionMiembro miembro = reunionMiembroService.updateReunionMiembro(miembroId,
                asistencia, connectedUserId);

        return UIEntity.toUI(miembro);
    }

}
