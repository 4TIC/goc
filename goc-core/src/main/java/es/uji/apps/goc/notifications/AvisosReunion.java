package es.uji.apps.goc.notifications;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.goc.dao.MiembroDAO;
import es.uji.apps.goc.dao.NotificacionesDAO;
import es.uji.apps.goc.dao.OrganoReunionMiembroDAO;
import es.uji.apps.goc.dao.ReunionDAO;
import es.uji.apps.goc.dto.OrganoReunionMiembro;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.exceptions.MiembrosExternosException;
import es.uji.apps.goc.exceptions.NotificacionesException;
import es.uji.apps.goc.exceptions.ReunionNoDisponibleException;

@Component
public class AvisosReunion
{
    @Autowired
    private ReunionDAO reunionDAO;

    @Autowired
    private MiembroDAO miembroDAO;

    @Autowired
    private OrganoReunionMiembroDAO organoReunionMiembroDAO;

    @Autowired
    private NotificacionesDAO notificacionesDAO;

    @Transactional
    public void enviaAvisoNuevaReunion(Long reunionId, Long connectedUserId, String defaultSender)
            throws ReunionNoDisponibleException, MiembrosExternosException, NotificacionesException
    {
        Reunion reunion = getReunion(reunionId, connectedUserId);
        List<String> miembros = getMiembros(reunion, connectedUserId);

        Mensaje mensaje = new Mensaje();
        mensaje.setAsunto(
                "[GOC] Nueva reunión: Se te ha incluido como miembro en una nueva convocatoria de reunión");
        mensaje.setContentType("text/html");

        ReunionFormatter formatter = new ReunionFormatter(reunion);
        mensaje.setCuerpo(formatter.format());

        mensaje.setFrom(defaultSender);
        mensaje.setDestinos(miembros);

        notificacionesDAO.enviaNotificacion(mensaje);
    }

    public void enviaAvisoReunionProxima(Long reunionId, Long connectedUserId, String defaultSender)
            throws ReunionNoDisponibleException, MiembrosExternosException, NotificacionesException
    {
        Reunion reunion = getReunion(reunionId, connectedUserId);
        List<String> miembros = getMiembros(reunion, connectedUserId);

        Mensaje mensaje = new Mensaje();
        mensaje.setAsunto("[GOC] Recordatorio reunión: " + reunion.getAsunto());
        mensaje.setContentType("text/html");

        ReunionFormatter formatter = new ReunionFormatter(reunion);
        mensaje.setCuerpo(formatter.format());

        mensaje.setFrom(defaultSender);

        mensaje.setDestinos(miembros);

        if (miembros.size() > 0) {
            notificacionesDAO.enviaNotificacion(mensaje);
        }
    }

    private Reunion getReunion(Long reunionId, Long connectedUserId)
            throws ReunionNoDisponibleException
    {
        Reunion reunion = reunionDAO.getReunionConOrganosById(reunionId);

        if (reunion == null)
        {
            throw new ReunionNoDisponibleException();
        }
        return reunion;
    }

    private List<String> getMiembros(Reunion reunion, Long connectedUserId)
            throws ReunionNoDisponibleException, MiembrosExternosException
    {

        List<OrganoReunionMiembro> listaAsistentesReunion = organoReunionMiembroDAO
                .getAsistentesConfirmadosByReunionId(reunion.getId());

        List<String> miembros = listaAsistentesReunion.stream()
                .map(asistente -> asistente.getSuplenteEmail() != null
                        ? asistente.getSuplenteEmail() : asistente.getEmail())
                .collect(Collectors.toList());

        return miembros;
    }
}
