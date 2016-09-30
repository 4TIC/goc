package es.uji.apps.goc.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.goc.dao.OrganoReunionDAO;
import es.uji.apps.goc.dao.OrganoReunionMiembroDAO;
import es.uji.apps.goc.dto.OrganoReunion;
import es.uji.apps.goc.dto.OrganoReunionMiembro;
import es.uji.apps.goc.exceptions.AsistenteNoEncontradoException;
import es.uji.apps.goc.exceptions.MiembrosExternosException;
import es.uji.apps.goc.model.Miembro;
import es.uji.commons.rest.UIEntity;

@Service
@Component
public class OrganoReunionMiembroService
{
    @Autowired
    private OrganoReunionMiembroDAO organoReunionMiembroDAO;

    @Autowired
    private MiembroService miembroService;

    @Autowired
    private OrganoReunionService organoReunionService;

    @Autowired
    private OrganoReunionDAO organoReunionDAO;

    public void updateOrganoReunionMiembrosDesdeOrganosUI(List<UIEntity> organosUI, Long reunionId,
            Long connectedUserId) throws MiembrosExternosException
    {
        if (organosUI != null)
        {
            for (UIEntity organoUI : organosUI)
            {
                String organoId = organoUI.get("id");
                Boolean externo = new Boolean(organoUI.get("externo"));

                updateOrganoReunionMiembrosDesdeOrganoUI(organoUI, reunionId);
            }
        }
    }

    protected void addOrganoReunionMiembros(OrganoReunion organoReunion, Long connectedUserId)
            throws MiembrosExternosException
    {
        List<Miembro> miembros = new ArrayList();
        if (organoReunion.getOrganoExternoId() != null)
        {
            miembros = miembroService.getMiembrosExternos(organoReunion.getOrganoExternoId(),
                    connectedUserId);
        }
        else
        {
            miembros = miembroService.getMiembrosLocales(organoReunion.getOrganoLocal().getId(),
                    connectedUserId);
        }

        for (Miembro miembro : miembros)
        {
            OrganoReunionMiembro organoReunionMiembro = new OrganoReunionMiembro();
            organoReunionMiembro.setOrganoReunion(organoReunion);

            if (organoReunion.getOrganoExternoId() != null)
            {
                organoReunionMiembro.setOrganoExterno(true);
            }
            else
            {
                organoReunionMiembro.setOrganoExterno(false);
            }
            organoReunionMiembro.setNombre(miembro.getNombre());
            organoReunionMiembro.setEmail(miembro.getEmail());
            organoReunionMiembro.setAsistencia(true);
            organoReunionMiembro.setOrganoId(miembro.getOrgano().getId());
            organoReunionMiembro.setReunionId(organoReunion.getReunion().getId());
            organoReunionMiembro.setMiembroId(miembro.getId().toString());
            organoReunionMiembro.setCargoId(miembro.getCargo().getId());
            organoReunionMiembro.setCargoNombre(miembro.getCargo().getNombre());
            organoReunionMiembroDAO.insert(organoReunionMiembro);
        }
    }

    @Transactional
    private void updateOrganoReunionMiembrosDesdeOrganoUI(UIEntity organoUI, Long reunionId)
    {
        List<UIEntity> miembrosUI = organoUI.getRelations().get("miembros");
        String organoId = organoUI.get("id");
        Boolean externo = new Boolean(organoUI.get("externo"));

        if (miembrosUI == null)
        {
            return;
        }

        for (UIEntity miembroUI : miembrosUI)
        {
            Long suplenteId = Long.parseLong(miembroUI.get("suplenteId"));
            String suplenteNombre = miembroUI.get("suplenteNombre");
            String suplenteEmail = miembroUI.get("suplenteEmail");
            if (suplenteId.equals(0L))
            {
                suplenteId = null;
            }
            String asistenteId = miembroUI.get("id");
            Boolean asistencia = new Boolean(miembroUI.get("asistencia"));
            organoReunionMiembroDAO.updateAsistente(reunionId, organoId, externo, asistenteId,
                    asistencia, suplenteId, suplenteNombre, suplenteEmail);
        }
    }

    @Transactional
    public void estableceAsistencia(Long reunionId, Long connectedUserId, Boolean asistencia)
            throws AsistenteNoEncontradoException
    {
        List<OrganoReunionMiembro> asistentes = organoReunionMiembroDAO
                .getMiembroByAsistenteIdOrSuplenteId(reunionId, connectedUserId);

        for (OrganoReunionMiembro asistente : asistentes)
        {
            asistente.setAsistenciaConfirmada(asistencia);
            organoReunionMiembroDAO.update(asistente);
        }
    }

    @Transactional
    public void estableceSuplente(Long reunionId, Long connectedUserId, Long suplenteId,
            String suplenteNombre, String suplenteEmail, Long organoMiembroId)
    {
        OrganoReunionMiembro miembro = organoReunionMiembroDAO.getMiembroById(organoMiembroId);

        miembro.setSuplenteId(suplenteId);
        miembro.setSuplenteNombre(suplenteNombre);
        miembro.setSuplenteEmail(suplenteEmail);
        organoReunionMiembroDAO.update(miembro);
    }

    @Transactional
    public void borraSuplente(Long reunionId, Long miembroId, Long connectedUserId)
    {
        OrganoReunionMiembro miembro = organoReunionMiembroDAO.getMiembroById(miembroId);
        miembro.setSuplenteId(null);
        miembro.setSuplenteNombre(null);
        miembro.setSuplenteEmail(null);
        organoReunionMiembroDAO.update(miembro);
    }
}
