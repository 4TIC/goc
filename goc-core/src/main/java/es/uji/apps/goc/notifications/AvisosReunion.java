package es.uji.apps.goc.notifications;

import com.sun.jersey.core.util.Base64;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.ParameterList;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.Organizer;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.TzId;
import net.fortuna.ical4j.model.property.Uid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.uji.apps.goc.dao.NotificacionesDAO;
import es.uji.apps.goc.dao.OrganoAutorizadoDAO;
import es.uji.apps.goc.dao.OrganoReunionMiembroDAO;
import es.uji.apps.goc.dao.ReunionDAO;
import es.uji.apps.goc.dto.OrganoAutorizado;
import es.uji.apps.goc.dto.OrganoReunion;
import es.uji.apps.goc.dto.OrganoReunionMiembro;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.exceptions.MiembrosExternosException;
import es.uji.apps.goc.exceptions.NotificacionesException;
import es.uji.apps.goc.exceptions.ReunionNoDisponibleException;
import es.uji.apps.goc.model.Persona;

import static java.util.stream.Collectors.toList;

@Component
public class AvisosReunion
{
    private static Logger log = Logger.getLogger(AvisosReunion.class);
    private OrganoReunionMiembroDAO organoReunionMiembroDAO;
    private NotificacionesDAO notificacionesDAO;
    private ReunionDAO reunionDAO;
    private OrganoAutorizadoDAO organoAutorizadoDAO;

    @Value("${goc.publicUrl}")
    private String publicUrl;

    @Value("${uji.smtp.defaultSender}")
    private String defaultSender;

    @Autowired
    public AvisosReunion(ReunionDAO reunionDAO, OrganoReunionMiembroDAO organoReunionMiembroDAO,
            NotificacionesDAO notificacionesDAO, OrganoAutorizadoDAO organoAutorizadoDAO)
    {
        this.reunionDAO = reunionDAO;
        this.organoReunionMiembroDAO = organoReunionMiembroDAO;
        this.notificacionesDAO = notificacionesDAO;
        this.organoAutorizadoDAO = organoAutorizadoDAO;
    }

    @Transactional
    public void enviaAvisoAltaSuplente(Long reunionId, String emailSuplente, String miembroNombre, String miembroCargo)
            throws ReunionNoDisponibleException, MiembrosExternosException, NotificacionesException
    {
        Reunion reunion = reunionDAO.getReunionById(reunionId);

        if (emailSuplente == null || emailSuplente.isEmpty()) return;

        String textoAux = "Heu estat dessignat com a suplent de " + miembroNombre + " (" + miembroCargo + ")";

        String asunto = "[GOC]";

        asunto += " Suplent en reunió " + getNombreOrganos(reunion);

        if (reunion.getNumeroSesion() != null)
        {
            asunto += " n. " + reunion.getNumeroSesion();
        }

        SimpleDateFormat dataFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        asunto += " " + dataFormatter.format(reunion.getFecha());

        buildAndSendMessageWithExtraText(reunion, emailSuplente, asunto, textoAux);
    }

    @Transactional
    public void enviaAvisoBajaSuplente(Long reunionId, String emailSuplente, String miembroNombre, String miembroCargo)
            throws ReunionNoDisponibleException, MiembrosExternosException, NotificacionesException
    {
        Reunion reunion = reunionDAO.getReunionById(reunionId);

        if (emailSuplente == null || emailSuplente.isEmpty()) return;

        String textoAux = "Heu estat donat de baixa com a suplent de " + miembroNombre + " (" + miembroCargo + ")";

        String asunto = "[GOC]";

        asunto += " Baixa com a suplent en reunió " + getNombreOrganos(reunion);

        if (reunion.getNumeroSesion() != null)
        {
            asunto += " n. " + reunion.getNumeroSesion();
        }

        SimpleDateFormat dataFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        asunto += " " + dataFormatter.format(reunion.getFecha());

        buildAndSendMessageWithExtraText(reunion, emailSuplente, asunto, textoAux);
    }

    @Transactional
    public void enviaAvisoAltaDelegacionVoto(Long reunionId, String emailDelegadoVoto, String miembroNombre,
            String miembroCargo)
            throws ReunionNoDisponibleException, MiembrosExternosException, NotificacionesException
    {
        Reunion reunion = reunionDAO.getReunionById(reunionId);

        if (emailDelegadoVoto == null || emailDelegadoVoto.isEmpty()) return;

        String textoAux = "Se vos ha dessignat la delegació de vot de " + miembroNombre + " (" + miembroCargo + ")";

        String asunto = "[GOC]";

        asunto += " Delegació de vot en reunió " + getNombreOrganos(reunion);

        if (reunion.getNumeroSesion() != null)
        {
            asunto += " n. " + reunion.getNumeroSesion();
        }

        SimpleDateFormat dataFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        asunto += " " + dataFormatter.format(reunion.getFecha());

        buildAndSendMessageWithExtraText(reunion, emailDelegadoVoto, asunto, textoAux);
    }

    @Transactional
    public void enviaAvisoBajaDelegacionVoto(Long reunionId, String emailDelegadoVoto, String miembroNombre,
            String miembroCargo)
            throws ReunionNoDisponibleException, MiembrosExternosException, NotificacionesException
    {
        Reunion reunion = reunionDAO.getReunionById(reunionId);

        if (emailDelegadoVoto == null || emailDelegadoVoto.isEmpty()) return;

        String textoAux =
                "Heu estat donat de baixa de la delegació de vot de " + miembroNombre + " (" + miembroCargo + ")";

        String asunto = "[GOC]";

        asunto += " Baixa de delegació de vot en reunió " + getNombreOrganos(reunion);

        if (reunion.getNumeroSesion() != null)
        {
            asunto += " n. " + reunion.getNumeroSesion();
        }

        SimpleDateFormat dataFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        asunto += " " + dataFormatter.format(reunion.getFecha());

        buildAndSendMessageWithExtraText(reunion, emailDelegadoVoto, asunto, textoAux);
    }

    @Transactional
    public void enviaAvisoNuevaReunion(Reunion reunion)
            throws ReunionNoDisponibleException, MiembrosExternosException, NotificacionesException
    {
        List<String> miembros = getMiembros(reunion, false);
        List<String> autorizados = getAutorizados(reunion);

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

        String calendarString = null;
        byte[] calendarEncoded = null;
        try {
            calendarString = creaICal(reunion);
            calendarEncoded = Base64.encode(calendarString.getBytes());
        } catch (Exception e) {
            log.error("No se ha podido crear el calendar");
        }
        buildAndSendMessage(reunion, miembros, autorizados, asunto, calendarEncoded, "text/calendar");
    }

    private String creaICal(Reunion reunion) throws URISyntaxException {
        Calendar icsCalendar = createVcalendar();
        VEvent meeting = createEvent(reunion);
        icsCalendar.getComponents().add(meeting);
        return icsCalendar.toString();
    }

    private VEvent createEvent(Reunion reunion) throws URISyntaxException {
        String asunto = reunion.getAsunto();
        DateTime start = null;
        DateTime end = null;
        if(reunion.getFecha() != null && reunion.getDuracion() != null) {
            start = new DateTime(reunion.getFecha());
            LocalDateTime endDate = LocalDateTime.ofInstant(reunion.getFecha().toInstant(), ZoneId.systemDefault()).plusMinutes(reunion.getDuracion());
            end = new DateTime(Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant()));
        }
        VEvent meeting = new VEvent(start, end, asunto);
        Uid uid = new Uid(reunion.getId() + "@goc.com");
        meeting.getProperties().add(uid);
        meeting.getProperties().add(new Location(reunion.getUbicacion()));
        ParameterList parameterList = new ParameterList();
        parameterList.add(new Cn(reunion.getCreadorNombre()));
        if(reunion.getCreadorEmail() != null && reunion.getCreadorNombre() != null) {
            Organizer organizer = new Organizer(parameterList, reunion.getCreadorEmail());
            meeting.getProperties().add(organizer);
        }
        return meeting;
    }

    private Calendar createVcalendar() {
        Calendar icsCalendar = new net.fortuna.ical4j.model.Calendar();
        icsCalendar.getProperties().add(new ProdId("-//GOC//Calendario reuniones 1.0//EN"));
        icsCalendar.getProperties().add(CalScale.GREGORIAN);
        icsCalendar.getProperties().add(Method.PUBLISH);
        VTimeZone vTimeZone = new VTimeZone();
        vTimeZone.getProperties().add(new TzId("Europe/Madrid"));
        icsCalendar.getComponents().add(vTimeZone);
        return icsCalendar;
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
        List<String> autorizados = getAutorizados(reunion);

        if (miembros == null || miembros.size() == 0)
        {
            return false;
        }

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");

        String calendarString = null;
        byte[] calendarEncoded = null;
        try {
            calendarString = creaICal(reunion);
            calendarEncoded = Base64.encode(calendarString.getBytes());
        } catch (Exception e) {
            log.error("No se ha podido crear el calendar");
        }
        buildAndSendMessage(reunion, miembros, autorizados,
                "[GOC] Recordatori reunió: " + reunion.getAsunto() + " (" + df.format(reunion.getFecha()) + ")",
            calendarEncoded, "text/calendar");

        return true;
    }

    private void buildAndSendMessageWithExtraText(Reunion reunion, String miembro, String asunto, String textoAux)
            throws NotificacionesException
    {
        Mensaje mensaje = new Mensaje();
        mensaje.setAsunto(asunto);
        mensaje.setContentType("text/html");

        ReunionFormatter formatter = new ReunionFormatter(reunion);
        mensaje.setCuerpo(formatter.format(publicUrl, textoAux));
        mensaje.setFrom(defaultSender);
        mensaje.setReplyTo(getReplyTo(reunion));
        mensaje.setDestinos(Collections.singletonList(miembro));

        notificacionesDAO.enviaNotificacion(mensaje);
    }

    private void buildAndSendMessage(Reunion reunion, List<String> miembros, List<String> autorizados, String asunto,
        byte[] fileBase64, String fileContentType)
            throws NotificacionesException
    {
        Mensaje mensaje = new Mensaje();
        mensaje.setAsunto(asunto);
        mensaje.setContentType("text/html");

        ReunionFormatter formatter = new ReunionFormatter(reunion);
        mensaje.setCuerpo(formatter.format(publicUrl, null));
        mensaje.setFrom(defaultSender);
        mensaje.setReplyTo(getReplyTo(reunion));
        mensaje.setDestinos(miembros);
        mensaje.setAutorizados(autorizados);
        if(fileBase64 != null) {
            mensaje.setFileBase64(fileBase64);
            mensaje.setFileContentType(fileContentType);
        }

        notificacionesDAO.enviaNotificacion(mensaje);
    }

    private String getReplyTo(Reunion reunion)
    {
        return reunion.getCreadorEmail() != null ? reunion.getCreadorEmail() : defaultSender;
    }

    private List<String> getAutorizados(Reunion reunion)
    {
        List<OrganoAutorizado> autorizados = organoAutorizadoDAO.getAutorizadosByReunionId(reunion.getId());

        return autorizados.stream()
                .filter(a -> a.getPersonaEmail() != null)
                .map(a -> a.getPersonaEmail())
                .collect(toList());
    }

    private List<String> getMiembros(Reunion reunion, Boolean confirmados)
            throws ReunionNoDisponibleException, MiembrosExternosException
    {
        List<OrganoReunionMiembro> listaAsistentesReunion;

        if (confirmados)
        {
            listaAsistentesReunion = organoReunionMiembroDAO.getAsistentesByReunionId(reunion.getId());
        }
        else
        {
            listaAsistentesReunion = organoReunionMiembroDAO.getMiembrosByReunionId(reunion.getId());
        }

        List<Persona> invitados = reunionDAO.getInvitadosPresencialesByReunionId(reunion.getId());

        List<String> emailMiembros =
                listaAsistentesReunion.stream().map(AvisosReunion::obtenerMailAsistente).collect(toList());

        List<String> emailInvitados = invitados.stream().map(invitado -> invitado.getEmail()).collect(toList());

        emailMiembros.addAll(emailInvitados);

        return emailMiembros.stream().distinct().collect(toList());
    }

    private static String obtenerMailAsistente(OrganoReunionMiembro asistente)
    {
        return (asistente.getSuplenteEmail() != null) ? asistente.getSuplenteEmail() : asistente.getEmail();
    }
}
