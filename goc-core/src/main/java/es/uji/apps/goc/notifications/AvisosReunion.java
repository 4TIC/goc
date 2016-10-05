package es.uji.apps.goc.notifications;

import es.uji.apps.goc.dao.NotificacionesDAO;
import es.uji.apps.goc.dao.OrganoReunionMiembroDAO;
import es.uji.apps.goc.dao.ReunionDAO;
import es.uji.apps.goc.dto.OrganoReunionMiembro;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.exceptions.MiembrosExternosException;
import es.uji.apps.goc.exceptions.NotificacionesException;
import es.uji.apps.goc.exceptions.ReunionNoDisponibleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class AvisosReunion
{
    private ReunionDAO reunionDAO;
    private OrganoReunionMiembroDAO organoReunionMiembroDAO;
    private NotificacionesDAO notificacionesDAO;

    @Autowired
    public AvisosReunion(ReunionDAO reunionDAO, OrganoReunionMiembroDAO organoReunionMiembroDAO,
                         NotificacionesDAO notificacionesDAO)
    {
        this.reunionDAO = reunionDAO;
        this.organoReunionMiembroDAO = organoReunionMiembroDAO;
        this.notificacionesDAO = notificacionesDAO;
    }

    @Transactional
    public void enviaAvisoNuevaReunion(Long reunionId, String defaultSender)
            throws ReunionNoDisponibleException, MiembrosExternosException, NotificacionesException
    {
        Reunion reunion = getReunion(reunionId);
        List<String> miembros = getMiembros(reunion);

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

    public void enviaAvisoReunionProxima(Reunion reunion, String defaultSender)
            throws ReunionNoDisponibleException, MiembrosExternosException, NotificacionesException
    {
        List<String> miembros = getMiembros(reunion);

        if (miembros == null || miembros.size() == 0)
        {
            return;
        }

        Mensaje mensaje = new Mensaje();
        mensaje.setAsunto("[GOC] Recordatorio reunión: " + reunion.getAsunto());
        mensaje.setContentType("text/html");

        ReunionFormatter formatter = new ReunionFormatter(reunion);
        mensaje.setCuerpo(formatter.format());

        mensaje.setFrom(defaultSender);
        mensaje.setDestinos(miembros);

        notificacionesDAO.enviaNotificacion(mensaje);
    }

    private Reunion getReunion(Long reunionId)
            throws ReunionNoDisponibleException
    {
        Reunion reunion = reunionDAO.getReunionConOrganosById(reunionId);

        if (reunion == null)
        {
            throw new ReunionNoDisponibleException();
        }

        return reunion;
    }

    private List<String> getMiembros(Reunion reunion)
            throws ReunionNoDisponibleException, MiembrosExternosException
    {
        List<OrganoReunionMiembro> listaAsistentesReunion = organoReunionMiembroDAO
                .getAsistentesConfirmadosByReunionId(reunion.getId());

        List<String> miembros = listaAsistentesReunion.stream()
                .map(AvisosReunion::obtenerMailAsistente)
                .collect(toList());

        return miembros;
    }

    private static String obtenerMailAsistente(OrganoReunionMiembro asistente)
    {
        return (asistente.getSuplenteEmail() != null) ? asistente.getSuplenteEmail() : asistente.getEmail();
    }
}
