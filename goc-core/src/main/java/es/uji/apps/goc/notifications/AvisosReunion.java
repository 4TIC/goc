package es.uji.apps.goc.notifications;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.goc.dao.NotificacionesDAO;
import es.uji.apps.goc.dao.OrganoReunionMiembroDAO;
import es.uji.apps.goc.dao.ReunionDAO;
import es.uji.apps.goc.dto.OrganoReunionMiembro;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.exceptions.MiembrosExternosException;
import es.uji.apps.goc.exceptions.NotificacionesException;
import es.uji.apps.goc.exceptions.ReunionNoDisponibleException;

import static java.util.stream.Collectors.toList;

@Component
public class AvisosReunion
{
    private ReunionDAO reunionDAO;
    private OrganoReunionMiembroDAO organoReunionMiembroDAO;
    private NotificacionesDAO notificacionesDAO;

    @Value("${goc.publicUrl}")
    private String publicUrl;

    @Value("${uji.smtp.defaultSender}")
    private String defaultSender;

    @Autowired
    public AvisosReunion(ReunionDAO reunionDAO, OrganoReunionMiembroDAO organoReunionMiembroDAO,
            NotificacionesDAO notificacionesDAO)
    {
        this.reunionDAO = reunionDAO;
        this.organoReunionMiembroDAO = organoReunionMiembroDAO;
        this.notificacionesDAO = notificacionesDAO;
    }

    @Transactional
    public void enviaAvisoNuevaReunion(Reunion reunion)
            throws ReunionNoDisponibleException, MiembrosExternosException, NotificacionesException
    {
        List<String> miembros = getMiembros(reunion, false);

        Mensaje mensaje = new Mensaje();

        String asunto = "[GOC] ";

        if (reunion.getAvisoPrimeraReunion())
        {
            asunto += "Rectificada ";
        }

        asunto += "Nueva reunión: Se te ha incluido como miembro en una nueva convocatoria de reunión";

        mensaje.setAsunto(asunto);
        mensaje.setContentType("text/html");

        ReunionFormatter formatter = new ReunionFormatter(reunion);
        mensaje.setCuerpo(formatter.format(publicUrl));

        mensaje.setFrom(defaultSender);
        mensaje.setReplyTo(defaultSender);
        mensaje.setDestinos(miembros);

        notificacionesDAO.enviaNotificacion(mensaje);
    }

    public Boolean enviaAvisoReunionProxima(Reunion reunion)
            throws ReunionNoDisponibleException, MiembrosExternosException, NotificacionesException
    {
        List<String> miembros = getMiembros(reunion, true);

        if (miembros == null || miembros.size() == 0)
        {
            return false;
        }

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");

        Mensaje mensaje = new Mensaje();
        mensaje.setAsunto(
                "[GOC] Recordatorio reunión: " + reunion.getAsunto() + " (" + df.format(reunion.getFecha()) + ")");
        mensaje.setContentType("text/html");

        ReunionFormatter formatter = new ReunionFormatter(reunion);
        mensaje.setCuerpo(formatter.format(publicUrl));
        mensaje.setFrom(defaultSender);
        mensaje.setReplyTo(defaultSender);

        notificacionesDAO.enviaNotificacion(mensaje);

        return true;
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

    private List<String> getMiembros(Reunion reunion, Boolean confirmados)
            throws ReunionNoDisponibleException, MiembrosExternosException
    {

        List<OrganoReunionMiembro> listaAsistentesReunion = new ArrayList<>();

        if (confirmados)
        {
            listaAsistentesReunion = organoReunionMiembroDAO.getAsistentesConfirmadosByReunionId(reunion.getId());
        }
        else
        {
            listaAsistentesReunion = organoReunionMiembroDAO.getMiembrosByReunionId(reunion.getId());
        }

        List<String> miembros =
                listaAsistentesReunion.stream().map(AvisosReunion::obtenerMailAsistente).collect(toList());

        return miembros;
    }

    private static String obtenerMailAsistente(OrganoReunionMiembro asistente)
    {
        return (asistente.getSuplenteEmail() != null) ? asistente.getSuplenteEmail() : asistente.getEmail();
    }
}
