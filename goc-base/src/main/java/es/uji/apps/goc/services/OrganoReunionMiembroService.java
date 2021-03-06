package es.uji.apps.goc.services;

import es.uji.apps.goc.dao.*;
import es.uji.apps.goc.dto.OrganoInvitado;
import es.uji.apps.goc.dto.OrganoReunion;
import es.uji.apps.goc.dto.OrganoReunionInvitado;
import es.uji.apps.goc.dto.OrganoReunionMiembro;
import es.uji.apps.goc.exceptions.AsistenteNoEncontradoException;
import es.uji.apps.goc.exceptions.MiembrosExternosException;
import es.uji.apps.goc.exceptions.NotificacionesException;
import es.uji.apps.goc.exceptions.ReunionNoDisponibleException;
import es.uji.apps.goc.model.Miembro;
import es.uji.apps.goc.notifications.AvisosReunion;
import es.uji.commons.rest.ParamUtils;
import es.uji.commons.rest.UIEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Component
public class OrganoReunionMiembroService
{
    @Autowired
    private OrganoReunionMiembroDAO organoReunionMiembroDAO;

    @Autowired
    private OrganoReunionInvitadoDAO organoReunionInvitadoDAO;

    @Autowired
    private MiembroService miembroService;

    @Autowired
    private OrganoReunionDAO organoReunionDAO;

    @Autowired
    private ReunionDAO reunionDAO;

    @Autowired
    private AvisosReunion avisosReunion;

    @Autowired
    private OrganoInvitadoDAO organoInvitadoDAO;

    @Transactional
    public void updateOrganoReunionInvitadosDesdeOrganosUI(List<UIEntity> organosUI, Long reunionId,
            Long connectedUserId)
    {
        organoReunionInvitadoDAO.deleteAllByReunionId(reunionId);

        if (organosUI != null)
        {
            for (UIEntity organoUI : organosUI)
            {
                updateOrganoReunionInvitadosDesdeOrganoUI(organoUI, reunionId);
            }
        }
    }

    @Transactional
    private void updateOrganoReunionInvitadosDesdeOrganoUI(UIEntity organoUI, Long reunionId)
    {
        String organoId = organoUI.get("id");

        if (organoId == null) return;

        List<OrganoInvitado> organoInvitados = organoInvitadoDAO.getInvitadosByOrgano(organoId);

        for (OrganoInvitado organoInvitado : organoInvitados)
        {
            OrganoReunionInvitado organoReunionInvitado = new OrganoReunionInvitado();

            organoReunionInvitado.setReunionId(reunionId);
            organoReunionInvitado.setPersonaId(organoInvitado.getPersonaId().toString());
            organoReunionInvitado.setEmail(organoInvitado.getPersonaEmail());
            organoReunionInvitado.setNombre(organoInvitado.getPersonaNombre());
            organoReunionInvitado.setOrganoExterno(new Boolean(organoUI.get("externo")));
            organoReunionInvitado.setOrganoId(organoId);
            organoReunionInvitado.setSoloConsulta(organoInvitado.isSoloConsulta());

            OrganoReunion organoReunion = organoReunionDAO.getOrganoReunionByReunionIdAndOrganoId(reunionId, ParamUtils.parseLong(organoId));

            organoReunionInvitado.setOrganoReunion(organoReunion);

            organoReunionInvitadoDAO.insert(organoReunionInvitado);
        }
    }

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
            Boolean guardarAsistencia = false;

            OrganoReunionMiembro miembroGuardado =
                    organoReunionMiembroDAO.getByReunionAndOrganoAndEmail(reunionId, organoId, externo, email);

            if (!asistencia.equals(miembroGuardado.getAsistencia()))
            {
                guardarAsistencia = true;
            }

            enviaMailSuplente(reunionId, suplenteId, suplenteEmail, miembroGuardado);

            organoReunionMiembroDAO.updateAsistenteReunionByEmail(reunionId, organoId, externo, email, asistencia,
                    suplenteId, suplenteNombre, suplenteEmail, guardarAsistencia);
        }
    }


    @Transactional
    public void estableceAsistencia(Long reunionMiembroId, Long connectedUserId, Boolean asistencia)
            throws AsistenteNoEncontradoException
    {
        OrganoReunionMiembro miembro = organoReunionMiembroDAO.getMiembroById(reunionMiembroId);

        miembro.setAsistenciaConfirmada(asistencia);
        miembro.setAsistencia(asistencia);
        organoReunionMiembroDAO.update(miembro);
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
    public void estableceDelegadoVoto(Long reunionId, Long connectedUserId, Long delegadoVotoId,
            String delegadoVotoNombre, String delegadoVotoEmail, Long organoMiembroId)
            throws MiembrosExternosException, ReunionNoDisponibleException, NotificacionesException
    {
        OrganoReunionMiembro miembro = organoReunionMiembroDAO.getMiembroById(organoMiembroId);

        enviaMailDelegadoVoto(reunionId, delegadoVotoId, delegadoVotoEmail, miembro);

        miembro.setDelegadoVotoId(delegadoVotoId);
        miembro.setDelegadoVotoEmail(delegadoVotoEmail);
        miembro.setDelegadoVotoNombre(delegadoVotoNombre);
        miembro.setAsistencia(false);
        miembro.setAsistenciaConfirmada(false);

        organoReunionMiembroDAO.update(miembro);
    }

    @Transactional
    public void borraSuplente(Long reunionId, Long miembroId, Long connectedUserId)
            throws MiembrosExternosException, ReunionNoDisponibleException, NotificacionesException
    {
        OrganoReunionMiembro miembro = organoReunionMiembroDAO.getMiembroById(miembroId);

        enviaMailSuplente(reunionId, null, null, miembro);

        miembro.setSuplenteId(null);
        miembro.setSuplenteNombre(null);
        miembro.setSuplenteEmail(null);

        organoReunionMiembroDAO.update(miembro);
    }

    @Transactional
    public void borraDelegadoVoto(Long reunionId, Long miembroId, Long connectedUserId)
            throws MiembrosExternosException, ReunionNoDisponibleException, NotificacionesException
    {
        OrganoReunionMiembro miembro = organoReunionMiembroDAO.getMiembroById(miembroId);

        enviaMailDelegadoVoto(reunionId, null, null, miembro);

        miembro.setDelegadoVotoId(null);
        miembro.setDelegadoVotoEmail(null);
        miembro.setDelegadoVotoNombre(null);

        organoReunionMiembroDAO.update(miembro);
    }

    public Boolean tieneSuplente(Long reunionMiembroId, Long connectedUserId)
    {
        OrganoReunionMiembro miembro = organoReunionMiembroDAO.getMiembroById(reunionMiembroId);

        if (miembro.getSuplenteId() != null) return true;

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

    private void enviaMailDelegadoVoto(Long reunionId, Long delegadoVotoId, String delegadoVotoEmail,
            OrganoReunionMiembro miembroGuardado)
            throws MiembrosExternosException, ReunionNoDisponibleException, NotificacionesException
    {
        if (miembroGuardado == null) return;

        if (delegadoVotoId != null && !delegadoVotoId.equals(miembroGuardado.getDelegadoVotoId()))
        {
            avisosReunion.enviaAvisoAltaDelegacionVoto(reunionId, delegadoVotoEmail, miembroGuardado.getNombre(),
                    miembroGuardado.getCargoNombre());

            if (miembroGuardado.getDelegadoVotoId() != null)
            {
                avisosReunion.enviaAvisoBajaDelegacionVoto(reunionId, miembroGuardado.getDelegadoVotoEmail(),
                        miembroGuardado.getNombre(), miembroGuardado.getCargoNombre());
            }
        }

        if (delegadoVotoId == null && miembroGuardado.getDelegadoVotoId() != null)
        {
            avisosReunion.enviaAvisoBajaDelegacionVoto(reunionId, miembroGuardado.getDelegadoVotoEmail(),
                    miembroGuardado.getNombre(), miembroGuardado.getCargoNombre());
        }
    }
}
