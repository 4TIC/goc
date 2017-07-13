package es.uji.apps.goc.services.rest;

import com.sun.jersey.api.core.InjectParam;
import es.uji.apps.goc.dto.*;
import es.uji.apps.goc.exceptions.*;
import es.uji.apps.goc.model.Organo;
import es.uji.apps.goc.services.*;
import es.uji.apps.goc.templates.PDFTemplate;
import es.uji.apps.goc.templates.Template;
import es.uji.commons.rest.CoreBaseService;
import es.uji.commons.rest.ParamUtils;
import es.uji.commons.rest.ResponseMessage;
import es.uji.commons.rest.UIEntity;
import es.uji.commons.sso.AccessManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Path("reuniones")
public class ReunionResource extends CoreBaseService
{
    @InjectParam
    private ReunionService reunionService;

    @InjectParam
    private DescriptorService descriptorService;

    @InjectParam
    private ReunionDocumentoService reunionDocumentoService;

    @InjectParam
    private OrganoService organoService;

    @InjectParam
    private OrganoReunionMiembroService organoReunionMiembroService;

    @Value("${uji.smtp.defaultSender}")
    private String defaultSender;

    @Value("${goc.logoDocumentos}")
    private String logoUrl;

    @Value("${goc.nombreInstitucion}")
    private String nombreInstitucion;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getReuniones(@QueryParam("organoId") String organoId,
            @QueryParam("tipoOrganoId") Long tipoOrganoId, @QueryParam("externo") Boolean externo)
            throws OrganosExternosException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        return UIEntity.toUI(
                reunionService.getReunionesByEditorId(false, organoId, tipoOrganoId, externo, connectedUserId));
    }

    @GET
    @Path("{reunionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity getReunionById(@PathParam("reunionId") Long reunionId)
            throws ReunionNoDisponibleException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        return UIEntity.toUI(reunionService.getReunionByIdAndEditorId(reunionId, connectedUserId));
    }

    @GET
    @Path("completadas")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getReunionesCompletadas(@QueryParam("organoId") String organoId,
            @QueryParam("tipoOrganoId") Long tipoOrganoId, @QueryParam("externo") Boolean externo)
            throws OrganosExternosException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        return UIEntity.toUI(
                reunionService.getReunionesByEditorId(true, organoId, tipoOrganoId, externo, connectedUserId));
    }

    @Path("{reunionId}/puntosOrdenDia")
    public ReunionPuntosOrdenDiaResource getReunionPuntosOrdenDiaResource(
            @InjectParam ReunionPuntosOrdenDiaResource reunionPuntosOrdenDiaResource)
    {
        return reunionPuntosOrdenDiaResource;
    }

    @Path("{reunionId}/documentos")
    public ReunionDocumentosResource getReunionDocumentosResource(
            @InjectParam ReunionDocumentosResource reunionDocumentosResource)
    {
        return reunionDocumentosResource;
    }

    @Path("{reunionId}/miembros")
    public ReunionMiembroResource getReunionMiembroResource(@InjectParam ReunionMiembroResource reunionMiembroResource)
    {
        return reunionMiembroResource;
    }

    @GET
    @Path("{reunionId}/tienesuplente")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMessage tieneSuplente(@PathParam("reunionId") Long reunionId)
            throws AsistenteNoEncontradoException, ReunionYaCompletadaException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        Boolean tieneSuplente = organoReunionMiembroService.tieneSuplente(reunionId, connectedUserId);

        if (tieneSuplente)
        {
            return new ResponseMessage(true, "true");
        }

        return new ResponseMessage(true, "false");
    }

    @POST
    @Path("{reunionId}/confirmar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void confirmarAsistencia(@PathParam("reunionId") Long reunionId, UIEntity ui)
            throws AsistenteNoEncontradoException, ReunionYaCompletadaException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        Boolean asistencia = new Boolean(ui.get("confirmacion"));

        reunionService.compruebaReunionNoCompletada(reunionId);

        organoReunionMiembroService.estableceAsistencia(reunionId, connectedUserId, asistencia);
    }

    @POST
    @Path("{reunionId}/suplente")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void establecerSuplente(@PathParam("reunionId") Long reunionId, UIEntity suplente)
            throws AsistenteNoEncontradoException, ReunionYaCompletadaException, ReunionNoAdmiteSuplenciaException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        Long suplenteId = Long.parseLong(suplente.get("suplenteId"));
        String suplenteNombre = suplente.get("suplenteNombre");
        String suplenteEmail = suplente.get("suplenteEmail");
        Long organoMiembroId = Long.parseLong(suplente.get("organoMiembroId"));

        reunionService.compruebaReunionNoCompletada(reunionId);
        reunionService.compruebaReunionAdmiteSuplencia(reunionId);
        organoReunionMiembroService.estableceSuplente(reunionId, connectedUserId, suplenteId, suplenteNombre,
                suplenteEmail, organoMiembroId);
    }

    @DELETE
    @Path("{reunionId}/suplente")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void borraSuplente(@PathParam("reunionId") Long reunionId, UIEntity miembro)
            throws ReunionYaCompletadaException, ReunionNoAdmiteSuplenciaException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        Long miembroId = Long.parseLong(miembro.get("organoMiembroId"));

        reunionService.compruebaReunionNoCompletada(reunionId);
        reunionService.compruebaReunionAdmiteSuplencia(reunionId);
        organoReunionMiembroService.borraSuplente(reunionId, miembroId, connectedUserId);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity addReunion(UIEntity reunionUI)
            throws NotificacionesException, MiembrosExternosException, PersonasExternasException, UrlGrabacionException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        Reunion reunion = reunionUIToModel(reunionUI);
        reunion = reunionService.addReunion(reunion, connectedUserId);

        return UIEntity.toUI(reunion);
    }

    @PUT
    @Path("{reunionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity modificaReunion(@PathParam("reunionId") Long reunionId, UIEntity reunionUI)
            throws ReunionNoDisponibleException, UrlGrabacionException, ReunionYaCompletadaException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        String asunto = reunionUI.get("asunto");
        String asuntoAlternativo = reunionUI.get("asuntoAlternativo");
        String descripcion = reunionUI.get("descripcion");
        String descripcionAlternativa = reunionUI.get("descripcionAlternativa");
        String ubicacion = reunionUI.get("ubicacion");
        String ubicacionAlternativa = reunionUI.get("ubicacionAlternativa");
        String urlGrabacion = reunionUI.get("urlGrabacion");
        Boolean publica = new Boolean(reunionUI.get("publica"));
        Boolean telematica = new Boolean(reunionUI.get("telematica"));
        String telematicaDescripcion = reunionUI.get("telematicaDescripcion");
        String telematicaDescripcionAlternativa = reunionUI.get("telematicaDescripcionAlternativa");
        Boolean admiteSuplencia = new Boolean(reunionUI.get("admiteSuplencia"));
        Boolean admiteComentarios = new Boolean(reunionUI.get("admiteComentarios"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        LocalDateTime dateTime = LocalDateTime.parse(reunionUI.get("fecha"), formatter);
        Date fecha = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());

        Date fechaSegundaConvocatoria = null;

        if (reunionUI.get("fechaSegundaConvocatoria") != null && !reunionUI.get("fechaSegundaConvocatoria").isEmpty())
        {
            LocalDateTime dateTimeSegundaConvocatoria =
                    LocalDateTime.parse(reunionUI.get("fechaSegundaConvocatoria"), formatter);
            fechaSegundaConvocatoria =
                    Date.from(dateTimeSegundaConvocatoria.atZone(ZoneId.systemDefault()).toInstant());
        }

        Long numeroSesion = null;

        if (!reunionUI.get("numeroSesion").isEmpty())
        {
            numeroSesion = Long.parseLong(reunionUI.get("numeroSesion"));
        }

        Long duracion = Long.parseLong(reunionUI.get("duracion"));

        reunionService.compruebaReunionNoCompletada(reunionId);

        Reunion reunion = reunionUIToModel(reunionUI);
        reunion.setId(reunionId);

        reunion =
                reunionService.updateReunion(reunionId, asunto, asuntoAlternativo, descripcion, descripcionAlternativa,
                        duracion, fecha, fechaSegundaConvocatoria, ubicacion, ubicacionAlternativa, urlGrabacion,
                        numeroSesion, publica, telematica, telematicaDescripcion, telematicaDescripcionAlternativa,
                        admiteSuplencia, admiteComentarios, connectedUserId);

        return UIEntity.toUI(reunion);
    }

    private Reunion reunionUIToModel(UIEntity reunionUI)
            throws UrlGrabacionException
    {
        Reunion reunion = new Reunion();
        String urlGrabacion = reunionUI.get("urlGrabacion");

        if (!urlGrabacion.isEmpty())
        {
            try
            {
                new URL(urlGrabacion);
            }
            catch (MalformedURLException e)
            {
                throw new UrlGrabacionException();
            }
        }

        if (ParamUtils.parseLong(reunionUI.get("id")) != null)
        {
            reunion.setId(new Long(reunionUI.get("id")));
        }

        reunion.setAsunto((reunionUI.get("asunto")));
        reunion.setAsuntoAlternativo((reunionUI.get("asuntoAlternativo")));
        reunion.setDescripcion(reunionUI.get("descripcion"));
        reunion.setDescripcionAlternativa(reunionUI.get("descripcionAlternativa"));
        reunion.setUbicacion(reunionUI.get("ubicacion"));
        reunion.setUbicacionAlternativa(reunionUI.get("ubicacionAlternativa"));
        reunion.setUrlGrabacion(urlGrabacion);
        reunion.setTelematica(new Boolean(reunionUI.get("telematica")));
        reunion.setTelematicaDescripcion(reunionUI.get("telematicaDescripcion"));
        reunion.setTelematicaDescripcionAlternativa(reunionUI.get("telematicaDescripcionAlternativa"));

        if (!reunionUI.get("numeroSesion").isEmpty())
        {
            reunion.setNumeroSesion(Long.parseLong(reunionUI.get("numeroSesion")));
        }

        reunion.setPublica(new Boolean(reunionUI.get("publica")));
        reunion.setAdmiteSuplencia(new Boolean(reunionUI.get("admiteSuplencia")));
        reunion.setAdmiteComentarios(new Boolean(reunionUI.get("admiteComentarios")));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        if (reunionUI.get("fecha") != null && !reunionUI.get("fecha").isEmpty())
        {
            LocalDateTime dateTime = LocalDateTime.parse(reunionUI.get("fecha"), formatter);
            reunion.setFecha(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
        }

        if (reunionUI.get("fechaSegundaConvocatoria") != null && !reunionUI.get("fechaSegundaConvocatoria").isEmpty())
        {
            LocalDateTime dateTimeSegundaConvocatoria =
                    LocalDateTime.parse(reunionUI.get("fechaSegundaConvocatoria"), formatter);
            reunion.setFechaSegundaConvocatoria(
                    Date.from(dateTimeSegundaConvocatoria.atZone(ZoneId.systemDefault()).toInstant()));
        }

        reunion.setDuracion(Long.parseLong(reunionUI.get("duracion")));

        return reunion;
    }

    @PUT
    @Path("{reunionId}/completada")
    @Consumes(MediaType.APPLICATION_JSON)
    public void modificaReunionCompletada(@PathParam("reunionId") Long reunionId, UIEntity data)
            throws ReunionNoDisponibleException, ReunionYaCompletadaException, FirmaReunionException,
            OrganosExternosException, PersonasExternasException
    {
        String acuerdos = data.get("acuerdos");
        String acuerdosAlternativos = data.get("acuerdosAlternativos");

        Long responsableActaId = Long.parseLong(data.get("responsable"));
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        reunionService.compruebaReunionNoCompletada(reunionId);
        reunionService.firmarReunion(reunionId, acuerdos, acuerdosAlternativos, responsableActaId, connectedUserId);
    }

    @PUT
    @Path("{reunionId}/enviarconvocatoria")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMessage enviarConvocatoria(@PathParam("reunionId") Long reunionId)
            throws MiembrosExternosException, ReunionNoDisponibleException, NotificacionesException
    {
        String messageError = reunionService.enviarConvocatoria(reunionId);

        if (messageError == null)
        {
            return new ResponseMessage(true, "appI18N.reuniones.convocatoriaEnviada");
        }

        return new ResponseMessage(true, messageError);
    }

    @GET
    @Path("{reunionId}/checktoclose")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseMessage checkReunionToClose(@PathParam("reunionId") Long reunionId)
    {
        String messageError = reunionService.checkReunionToClose(reunionId);

        if (messageError == null)
        {
            return new ResponseMessage(true, "");
        }

        return new ResponseMessage(true, messageError);
    }

    @PUT
    @Path("{reunionId}/organos")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modificaReunionesOrgano(@PathParam("reunionId") Long reunionId, UIEntity reunionOrganosUI)
            throws ReunionNoDisponibleException, NotificacionesException, MiembrosExternosException,
            OrganoConvocadoNoPermitidoException, ReunionYaCompletadaException, PersonasExternasException,
            OrganosExternosException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        List<UIEntity> organosUI = reunionOrganosUI.getRelations().get("organos");
        List<Organo> organos = creaOrganosDesdeOrganosUI(organosUI);

        reunionService.compruebaReunionNoCompletada(reunionId);
        compruebaPermisosAutorizadoOrganos(organos, connectedUserId);
        reunionService.updateOrganosReunionByReunionId(reunionId, organos, connectedUserId);

        organoReunionMiembroService.updateOrganoReunionMiembrosDesdeOrganosUI(organosUI, reunionId, connectedUserId);

        return Response.ok().build();
    }

    private void compruebaPermisosAutorizadoOrganos(List<Organo> organos, Long connectedUserId)
            throws OrganoConvocadoNoPermitidoException
    {
        if (!organoService.usuarioConPermisosParaConvocarOrganos(organos, connectedUserId))
        {
            throw new OrganoConvocadoNoPermitidoException();
        }
    }

    private List<Organo> creaOrganosDesdeOrganosUI(List<UIEntity> organosUI)
    {
        List<Organo> organos = new ArrayList<>();
        if (organosUI != null)
        {
            for (UIEntity organoUI : organosUI)
            {
                Organo organo = new Organo(organoUI.get("id"));
                organo.setExterno(new Boolean(organoUI.get("externo")));
                organos.add(organo);
            }
        }
        return organos;
    }

    @DELETE
    @Path("{reunionId}")
    public Response borraOrgano(@PathParam("reunionId") Long reunionId, UIEntity entity)
            throws ReunionNoDisponibleException, ReunionYaCompletadaException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        reunionService.compruebaReunionNoCompletada(reunionId);
        reunionService.removeReunionById(reunionId, connectedUserId);

        return Response.ok().build();
    }


    @GET
    @Path("{reunionId}/asistencia")
    @Produces("application/pdf")
    public Template reunion(@PathParam("reunionId") Long reunionId, @QueryParam("lang") String lang,
            @Context HttpServletRequest request)
            throws OrganosExternosException, MiembrosExternosException, ReunionNoDisponibleException,
            ReunionNoCompletadaException, AsistenteNoEncontradoException, PersonasExternasException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        Reunion reunion = reunionService.getReunionConOrganosById(reunionId, connectedUserId);

        if (reunion == null)
        {
            throw new ReunionNoDisponibleException();
        }

        if (!reunion.getCompletada())
        {
            throw new ReunionNoCompletadaException();
        }

        ReunionTemplate reunionTemplate =
                reunionService.getReunionTemplateDesdeReunion(reunion, connectedUserId, false);
        String nombreAsistente = reunionService.getNombreAsistente(reunionId, connectedUserId);

        if (nombreAsistente == null)
        {
            throw new AsistenteNoEncontradoException();
        }

        String applang = getLangCode(lang);

        Template template = new PDFTemplate("asistencia-" + applang);
        template.put("logo", logoUrl);
        template.put("nombreInstitucion", nombreInstitucion);
        template.put("nombreAsistente", nombreAsistente);
        template.put("tituloReunion", reunionTemplate.getAsunto());
        template.put("fechaReunion", getFechaReunion(reunionTemplate.getFecha()));
        template.put("horaReunion", getHoraReunion(reunionTemplate.getFecha()));
        template.put("duracionReunion", getDuracionReunion(reunionTemplate.getDuracion()));
        template.put("nombreConvocante", reunionTemplate.getCreadorNombre());

        return template;
    }

    @GET
    @Path("{reunionId}/asistentes")
    @Produces("application/pdf")
    public Template listaAsistentes(@PathParam("reunionId") Long reunionId, @QueryParam("lang") String lang,
            @Context HttpServletRequest request)
            throws ReunionNoDisponibleException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        Reunion reunion = reunionService.getReunionConOrganosById(reunionId, connectedUserId);

        if (reunion == null)
        {
            throw new ReunionNoDisponibleException();
        }

        ReunionTemplate reunionTemplate =
                reunionService.getReunionTemplateDesdeReunion(reunion, connectedUserId, false);

        String applang = getLangCode(lang);

        Template template = new PDFTemplate("asistentes-" + applang);
        template.put("logo", logoUrl);
        template.put("nombreInstitucion", nombreInstitucion);
        template.put("reunion", reunionTemplate);

        return template;
    }


    private String getLangCode(String lang)
    {

        if (lang == null || lang.isEmpty() || !(lang.toLowerCase().equals("ca") || lang.toLowerCase().equals("es")))
        {
            return "ca";
        }

        return lang;
    }

    private String getFechaReunion(Date fecha)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        return sdf.format(fecha);
    }

    private String getHoraReunion(Date fecha)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        return sdf.format(fecha);
    }

    private String getDuracionReunion(Long minutos)
    {
        Long horas = minutos / 60;
        Long minutosSobrantes = minutos % 60;

        return String.format("%d:%02d", horas, minutosSobrantes);
    }

    @GET
    @Path("{reunionId}/descriptores")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> listaDescriptores(@PathParam("reunionId") Long reunionId)
            throws ReunionNoDisponibleException, OrganosExternosException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        return UIEntity.toUI(descriptorService.getDescriptoresByReunionId(reunionId, connectedUserId));
    }

    @GET
    @Path("{reunionId}/invitados")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> listaInvitados(@PathParam("reunionId") Long reunionId)
            throws ReunionNoDisponibleException, OrganosExternosException
    {
        return UIEntity.toUI(reunionService.getInvitadosReunionByReunionId(reunionId));
    }

    @PUT
    @Path("{reunionId}/invitados")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response modificaInvitados(@PathParam("reunionId") Long reunionId, UIEntity reunionInvitadosUI)
            throws ReunionYaCompletadaException, ReunionNoDisponibleException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        List<ReunionInvitado> reunionInvitados = creaInvitadosDesdeInvitadosUI(reunionId, reunionInvitadosUI);

        reunionService.compruebaReunionNoCompletada(reunionId);
        reunionService.updateInvitadosByReunionId(reunionId, reunionInvitados, connectedUserId);

        return Response.ok().build();
    }

    private List<ReunionInvitado> creaInvitadosDesdeInvitadosUI(Long reunionId, UIEntity reunionInvitadosUI)
    {
        List<ReunionInvitado> invitados = new ArrayList<>();
        Reunion reunion = new Reunion(reunionId);

        List<UIEntity> invitadosUI = reunionInvitadosUI.getRelations().get("invitados");

        if (invitadosUI == null) return new ArrayList<>();

        for (UIEntity invitadoUI : invitadosUI)
        {
            ReunionInvitado reunionInvitado = new ReunionInvitado();

            reunionInvitado.setReunion(reunion);
            reunionInvitado.setPersonaId(invitadoUI.getLong("personaId"));
            reunionInvitado.setPersonaNombre(invitadoUI.get("personaNombre"));
            reunionInvitado.setPersonaEmail(invitadoUI.get("personaEmail"));

            invitados.add(reunionInvitado);
        }

        return invitados;
    }
}
