package es.uji.apps.goc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    private OrganoReunionDAO organoReunionDAO;

    public void updateOrganoReunionMiembrosDesdeOrganosUI(List<UIEntity> organosUI, Long reunionId,
            Long connectedUserId)
            throws MiembrosExternosException
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

    @Transactional
    protected void addOrganoReunionMiembros(OrganoReunion organoReunion, Long connectedUserId)
            throws MiembrosExternosException
    {
        List<Miembro> miembros = new ArrayList();
        if (organoReunion.isExterno())
        {
            miembros = miembroService.getMiembrosExternos(organoReunion.getOrganoId(), connectedUserId);
        }
        else
        {
            miembros = miembroService.getMiembrosLocales(Long.parseLong(organoReunion.getOrganoId()), connectedUserId);
        }

        for (Miembro miembro : miembros)
        {
            OrganoReunionMiembro organoReunionMiembro = miembro.toOrganoReunionMiembro(organoReunion);
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
            Boolean asistencia = new Boolean(miembroUI.get("asistencia"));
            String email = miembroUI.get("email");

            organoReunionMiembroDAO.updateAsistenteReunionByEmail(reunionId, organoId, externo, email, asistencia,
                    suplenteId, suplenteNombre, suplenteEmail);
        }
    }

    @Transactional
    public void estableceAsistencia(Long reunionId, Long connectedUserId, Boolean asistencia)
            throws AsistenteNoEncontradoException
    {
        List<OrganoReunionMiembro> asistentes =
                organoReunionMiembroDAO.getMiembroByAsistenteIdOrSuplenteId(reunionId, connectedUserId);

        for (OrganoReunionMiembro asistente : asistentes)
        {
            asistente.setAsistenciaConfirmada(asistencia);
            organoReunionMiembroDAO.update(asistente);
        }
    }

    @Transactional
    public void estableceSuplente(Long reunionId, Long connectedUserId, Long suplenteId, String suplenteNombre,
            String suplenteEmail, Long organoMiembroId)
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

    public List<OrganoReunionMiembro> getAsistentes(Long reunionId, Long connectedUserId)
    {
        return organoReunionMiembroDAO.getAsistentesByReunionId(reunionId);
    }
}
