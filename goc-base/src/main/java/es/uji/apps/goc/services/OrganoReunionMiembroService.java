package es.uji.apps.goc.services;

import es.uji.apps.goc.dao.ReunionDAO;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.exceptions.NotificacionesException;
import es.uji.apps.goc.exceptions.ReunionNoDisponibleException;
import es.uji.apps.goc.notifications.AvisosReunion;
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

    @Autowired
    private ReunionDAO reunionDAO;

    @Autowired
    private AvisosReunion avisosReunion;

    public void updateOrganoReunionMiembrosDesdeOrganosUI(List<UIEntity> organosUI, Long reunionId,
            Long connectedUserId)
            throws MiembrosExternosException, NotificacionesException, ReunionNoDisponibleException
    {
        if (organosUI != null)
        {
            for (UIEntity organoUI : organosUI)
            {
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
            throws MiembrosExternosException, ReunionNoDisponibleException, NotificacionesException
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
            String email = miembroUI.get("email");
            Long suplenteId = Long.parseLong(miembroUI.get("suplenteId"));
            String suplenteNombre = miembroUI.get("suplenteNombre");
            String suplenteEmail = miembroUI.get("suplenteEmail");
            if (suplenteId.equals(0L))
            {
                suplenteId = null;
            }
            Boolean asistencia = new Boolean(miembroUI.get("asistencia"));

            OrganoReunionMiembro miembroGuardado =
                    organoReunionMiembroDAO.getByReunionAndOrganoAndEmail(reunionId, organoId, externo, email);

            enviaMailSuplente(reunionId, suplenteId, suplenteEmail, miembroGuardado);

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
            asistente.setAsistencia(asistencia);
            organoReunionMiembroDAO.update(asistente);
        }
    }

    @Transactional
    public void estableceSuplente(Long reunionId, Long connectedUserId, Long suplenteId, String suplenteNombre,
            String suplenteEmail, Long organoMiembroId)
            throws MiembrosExternosException, ReunionNoDisponibleException, NotificacionesException
    {
        OrganoReunionMiembro miembro = organoReunionMiembroDAO.getMiembroById(organoMiembroId);

        enviaMailSuplente(reunionId, suplenteId, suplenteEmail, miembro);

        miembro.setSuplenteId(suplenteId);
        miembro.setSuplenteNombre(suplenteNombre);
        miembro.setSuplenteEmail(suplenteEmail);

        organoReunionMiembroDAO.update(miembro);
    }

    @Transactional
    public void borraSuplente(Long reunionId, Long miembroId, Long connectedUserId)
            throws MiembrosExternosException, ReunionNoDisponibleException, NotificacionesException
    {
        OrganoReunionMiembro miembro = organoReunionMiembroDAO.getMiembroById(miembroId);

        enviaMailSuplente(reunionId, null,null, miembro);

        miembro.setSuplenteId(null);
        miembro.setSuplenteNombre(null);
        miembro.setSuplenteEmail(null);

        organoReunionMiembroDAO.update(miembro);
    }

    public Boolean tieneSuplente(Long reunionId, Long connectedUserId)
    {
        List<OrganoReunionMiembro> asistentes =
                organoReunionMiembroDAO.getMiembroByAsistenteIdOrSuplenteId(reunionId, connectedUserId);

        for (OrganoReunionMiembro asistente : asistentes)
        {
            if (asistente.getSuplenteId() != null) return true;
        }

        return false;
    }

    private void enviaMailSuplente(Long reunionId, Long suplenteId, String suplenteEmail,
            OrganoReunionMiembro miembroGuardado)
            throws ReunionNoDisponibleException, MiembrosExternosException, NotificacionesException
    {
        if (miembroGuardado == null) return;

        if (suplenteId != null && !suplenteId.equals(miembroGuardado.getSuplenteId()))
        {
            avisosReunion.enviaAvisoAltaSuplente(reunionId, suplenteEmail, miembroGuardado.getNombre(),
                    miembroGuardado.getCargoNombre());

            if (miembroGuardado.getSuplenteId() != null)
            {
                avisosReunion.enviaAvisoBajaSuplente(reunionId, miembroGuardado.getSuplenteEmail(),
                        miembroGuardado.getNombre(), miembroGuardado.getCargoNombre());
            }
        }

        if (suplenteId == null && miembroGuardado.getSuplenteId() != null)
        {
            avisosReunion.enviaAvisoBajaSuplente(reunionId, miembroGuardado.getSuplenteEmail(),
                    miembroGuardado.getNombre(), miembroGuardado.getCargoNombre());
        }
    }
}
