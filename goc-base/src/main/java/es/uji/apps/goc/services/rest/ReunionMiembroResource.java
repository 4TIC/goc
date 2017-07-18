package es.uji.apps.goc.services.rest;

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

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    @Path("otros")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getOtrosMiembrosOrganoReunionByReunionId()
            throws MiembrosExternosException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        if (reunionId != null)
        {
            List<OrganoReunionMiembro> listaMiembros =
                    reunionMiembroService.getMiembrosReunionByReunionId(reunionId, connectedUserId);

            return UIEntity.toUI(listaMiembros.stream()
                    .filter(m -> !m.getMiembroId().equals(connectedUserId.toString()))
                    .collect(Collectors.toList()));
        }

        return Collections.emptyList();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getMiembrosOrganoReunion(@QueryParam("organoId") String organoId,
            @QueryParam("externo") Boolean externo)
            throws MiembrosExternosException, MiembroNoDisponibleException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        if (reunionId != null && organoId == null)
        {
            List<OrganoReunionMiembro> listaMiembros =
                    reunionMiembroService.getMiembrosReunionByReunionId(reunionId, connectedUserId);
            return UIEntity.toUI(listaMiembros);

        }

        if (reunionId != null)
        {
            List<OrganoReunionMiembro> listaMiembros =
                    reunionMiembroService.getMiembrosReunionByReunionIdAndOrganoId(reunionId, organoId, externo,
                            connectedUserId);
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

    private List<UIEntity> miembrosToUI(List<Miembro> listaMiembros, String organoId, Boolean externo)
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

        OrganoReunionMiembro miembro =
                reunionMiembroService.updateReunionMiembro(miembroId, asistencia, connectedUserId);

        return UIEntity.toUI(miembro);
    }

}
