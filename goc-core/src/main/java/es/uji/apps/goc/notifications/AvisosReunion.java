package es.uji.apps.goc.notifications;

import es.uji.apps.goc.dao.NotificacionesDAO;
import es.uji.apps.goc.dao.OrganoReunionMiembroDAO;
import es.uji.apps.goc.dao.ReunionDAO;
import es.uji.apps.goc.dao.ReunionInvitadoDAO;
import es.uji.apps.goc.dto.OrganoReunion;
import es.uji.apps.goc.dto.OrganoReunionMiembro;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.dto.ReunionInvitado;
import es.uji.apps.goc.exceptions.MiembrosExternosException;
import es.uji.apps.goc.exceptions.NotificacionesException;
import es.uji.apps.goc.exceptions.ReunionNoDisponibleException;
import es.uji.apps.goc.model.Persona;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class AvisosReunion
{
    private OrganoReunionMiembroDAO organoReunionMiembroDAO;
    private NotificacionesDAO notificacionesDAO;
    private ReunionDAO reunionDAO;

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
    public void enviaAvisoAltaSuplente(Long reunionId, String emailSuplente, String miembroNombre, String miembroCargo)
            throws ReunionNoDisponibleException, MiembrosExternosException, NotificacionesException
    {
        Reunion reunion = reunionDAO.getReunionById(reunionId);

        if (emailSuplente == null || emailSuplente.isEmpty()) return;

        String textoAux = "Heu estat dessignat com a suplent de " + miembroNombre + " (" + miembroCargo +")";

        String asunto = "[GOC]";

        asunto += " Suplent en reunió " + getNombreOrganos(reunion);

        if (reunion.getNumeroSesion() != null)
        {
            asunto += " n. " + reunion.getNumeroSesion();
        }

        SimpleDateFormat dataFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        asunto += " " + dataFormatter.format(reunion.getFecha());

        buildAndSendMessageWithExtraText(reunion, Collections.singletonList(emailSuplente), asunto, textoAux);
    }

    @Transactional
    public void enviaAvisoBajaSuplente(Long reunionId, String emailSuplente, String miembroNombre, String miembroCargo)
            throws ReunionNoDisponibleException, MiembrosExternosException, NotificacionesException
    {
        Reunion reunion = reunionDAO.getReunionById(reunionId);

        if (emailSuplente == null || emailSuplente.isEmpty()) return;

        String textoAux = "Heu estat donat de baixa com a suplent de " + miembroNombre + " (" + miembroCargo +")";

        String asunto = "[GOC]";

        asunto += " baixa com a suplent en reunió " + getNombreOrganos(reunion);

        if (reunion.getNumeroSesion() != null)
        {
            asunto += " n. " + reunion.getNumeroSesion();
        }

        SimpleDateFormat dataFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        asunto += " " + dataFormatter.format(reunion.getFecha());

        buildAndSendMessageWithExtraText(reunion, Collections.singletonList(emailSuplente), asunto, textoAux);
    }

    @Transactional
    public void enviaAvisoNuevaReunion(Reunion reunion)
            throws ReunionNoDisponibleException, MiembrosExternosException, NotificacionesException
    {
        List<String> miembros = getMiembros(reunion, false);

        String asunto = "[GOC]";

        if (reunion.getAvisoPrimeraReunion())
        {
            asunto += " [Rectificada]";
        }

        asunto += " convocatòria " + getNombreOrganos(reunion);

        if (reunion.getNumeroSesion() != null)
        {
            asunto += " n. " + reunion.getNumeroSesion();
        }

        SimpleDateFormat dataFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        asunto += " " + dataFormatter.format(reunion.getFecha());

        buildAndSendMessage(reunion, miembros, asunto);
    }

    private String getNombreOrganos(Reunion reunion)
    {
        List<String> nombreOrganos = new ArrayList<>();

        for (OrganoReunion organo : reunion.getReunionOrganos())
        {
            nombreOrganos.add(organo.getOrganoNombre());
        }

        return StringUtils.join(nombreOrganos, ", ");
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

        buildAndSendMessage(reunion, miembros,
                "[GOC] Recordatori reunió: " + reunion.getAsunto() + " (" + df.format(reunion.getFecha()) + ")");

        return true;
    }

    private void buildAndSendMessageWithExtraText(Reunion reunion, List<String> miembros, String asunto, String textoAux)
            throws NotificacionesException
    {
        Mensaje mensaje = new Mensaje();
        mensaje.setAsunto(asunto);
        mensaje.setContentType("text/html");

        ReunionFormatter formatter = new ReunionFormatter(reunion);
        mensaje.setCuerpo(formatter.format(publicUrl, textoAux));
        mensaje.setFrom(defaultSender);
        mensaje.setReplyTo(defaultSender);
        mensaje.setDestinos(miembros);

        notificacionesDAO.enviaNotificacion(mensaje);
    }

    private void buildAndSendMessage(Reunion reunion, List<String> miembros, String asunto)
            throws NotificacionesException
    {
        buildAndSendMessageWithExtraText(reunion, miembros, asunto, null);
    }

    private List<String> getMiembros(Reunion reunion, Boolean confirmados)
            throws ReunionNoDisponibleException, MiembrosExternosException
    {
        List<OrganoReunionMiembro> listaAsistentesReunion;

        if (confirmados)
        {
            listaAsistentesReunion = organoReunionMiembroDAO.getAsistentesConfirmadosByReunionId(reunion.getId());
        }
        else
        {
            listaAsistentesReunion = organoReunionMiembroDAO.getMiembrosByReunionId(reunion.getId());
        }

        List<Persona> invitados = reunionDAO.getInvitadosByReunionId(reunion.getId());

        List<String> emailMiembros =
                listaAsistentesReunion.stream().map(AvisosReunion::obtenerMailAsistente).collect(toList());

        List<String> emailInvitados =
                invitados.stream().map(invitado -> invitado.getEmail()).collect(toList());

        emailMiembros.addAll(emailInvitados);

        return emailMiembros.stream().distinct().collect(toList());
    }

    private static String obtenerMailAsistente(OrganoReunionMiembro asistente)
    {
        return (asistente.getSuplenteEmail() != null) ? asistente.getSuplenteEmail() : asistente.getEmail();
    }
}
