package es.uji.apps.goc.services;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.core.InjectParam;
import es.uji.apps.goc.Utils;
import es.uji.apps.goc.auth.LanguageConfig;
import es.uji.apps.goc.dao.*;
import es.uji.apps.goc.dto.*;
import es.uji.apps.goc.exceptions.*;
import es.uji.apps.goc.model.*;
import es.uji.apps.goc.model.Cargo;
import es.uji.apps.goc.notifications.AvisosReunion;
import es.uji.commons.sso.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Component
public class ReunionService
{
    @Value("${goc.external.firmasEndpoint}")
    private String firmasEndpoint;

    @Value("${goc.external.authToken}")
    private String authToken;

    @Value("${goc.publicUrl}")
    private String publicUrl;

    @Autowired
    private ReunionDAO reunionDAO;

    @Autowired
    private OrganoReunionDAO organoReunionDAO;

    @Autowired
    private OrganoReunionMiembroService organoReunionMiembroService;

    @Autowired
    private ReunionComentarioService reunionComentarioService;

    @InjectParam
    private OrganoService organoService;

    @InjectParam
    private PersonaService personaService;

    @InjectParam
    private ReunionDocumentoDAO reunionDocumentoDAO;

    @InjectParam
    private PuntoOrdenDiaDAO puntoOrdenDiaDAO;

    @InjectParam
    private OrganoReunionMiembroDAO organoReunionMiembroDAO;

    @InjectParam
    private MiembroDAO miembroDAO;

    @InjectParam
    private PuntoOrdenDiaDocumentoDAO puntoOrdenDiaDocumentoDAO;

    @InjectParam
    private PuntoOrdenDiaAcuerdoDAO puntoOrdenDiaAcuerdoDAO;

    @InjectParam
    private PuntoOrdenDiaDescriptorDAO puntoOrdenDiaDescriptorDAO;

    @InjectParam
    private AvisosReunion avisosReunion;

    @InjectParam
    private PuntoOrdenDiaService puntoOrdenDiaService;

    @InjectParam
    private ReunionInvitadoDAO reunionInvitadoDAO;

    @Autowired
    private TipoOrganoDAO tipoOrganoDAO;

    @Autowired
    private OrganoDAO organoDAO;

    @Autowired
    private DescriptorDAO descriptorDAO;

    @Autowired
    private ClaveDAO claveDAO;

    @Autowired
    private LanguageConfig languageConfig;

    public List<ReunionEditor> getReunionesByEditorId(Boolean completada, String organoId, Long tipoOrganoId,
            Boolean externo, Long connectedUserId)
    {
        return filtrarDuplicadosReunionEditores(
                reunionDAO.getReunionesByEditorId(connectedUserId, organoId, tipoOrganoId, externo, completada));
    }

    public Reunion addReunion(Reunion reunion, Long connectedUserId)
            throws PersonasExternasException
    {
        Persona convocante = personaService.getPersonaFromDirectoryByPersonaId(connectedUserId);

        reunion.setCreadorId(connectedUserId);
        reunion.setCreadorNombre(convocante.getNombre());
        reunion.setCreadorEmail(convocante.getEmail());
        reunion.setNotificada(false);
        reunion.setCompletada(false);
        reunion.setAvisoPrimeraReunion(false);
        reunion.setFechaCreacion(new Date());

        return reunionDAO.insert(reunion);
    }

    public Reunion updateReunion(Long reunionId, String asunto, String asuntoAlternativo, String descripcion,
            String descripcionAlternativa, Long duracion, Date fecha, Date fechaSegundaConvocatoria, String ubicacion,
            String ubicacionAlternativa, String urlGrabacion, Long numeroSesion, Boolean publica, Boolean telematica,
            String telematicaDescripcion, String telematicaDescripcionAlternativa, Boolean admiteSuplencia,
            Boolean admiteDelegacionVoto, Boolean admiteComentarios, Long connectedUserId)
            throws ReunionNoDisponibleException
    {
        Reunion reunion = reunionDAO.getReunionConOrganosById(reunionId);

        if (reunion == null)
        {
            throw new ReunionNoDisponibleException();
        }

        reunion.setAsunto(asunto);
        reunion.setAsuntoAlternativo(asuntoAlternativo);
        reunion.setDescripcion(descripcion);
        reunion.setDescripcionAlternativa(descripcionAlternativa);
        reunion.setDuracion(duracion);
        reunion.setUbicacion(ubicacion);
        reunion.setUbicacionAlternativa(ubicacionAlternativa);
        reunion.setUrlGrabacion(urlGrabacion);
        reunion.setFecha(fecha);
        reunion.setFechaSegundaConvocatoria(fechaSegundaConvocatoria);
        reunion.setNumeroSesion(numeroSesion);
        reunion.setPublica(publica);
        reunion.setTelematica(telematica);
        reunion.setTelematicaDescripcion(telematicaDescripcion);
        reunion.setTelematicaDescripcionAlternativa(telematicaDescripcionAlternativa);
        reunion.setAdmiteSuplencia(admiteSuplencia);
        reunion.setAdmiteDelegacionVoto(admiteDelegacionVoto);
        reunion.setAdmiteComentarios(admiteComentarios);

        return reunionDAO.update(reunion);
    }

    @Transactional
    public void removeReunionById(Long reunionId, Long connectedUserId)
            throws ReunionNoDisponibleException
    {
        Reunion reunion = reunionDAO.getReunionConOrganosById(reunionId);

        if (reunion == null)
        {
            throw new ReunionNoDisponibleException();
        }

        puntoOrdenDiaService.deleteByReunionId(reunionId);
        reunionDAO.deleteByReunionId(reunionId);
    }


    @Transactional
    public void updateInvitadosByReunionId(Long reunionId, List<ReunionInvitado> reunionInvitados, Long connectedUserId)
            throws ReunionNoDisponibleException
    {
        Reunion reunion = reunionDAO.getReunionConOrganosById(reunionId);

        if (reunion == null)
        {
            throw new ReunionNoDisponibleException();
        }

        reunionInvitadoDAO.deleteByReunionId(reunionId);

        for (ReunionInvitado reunionInvitado : reunionInvitados)
        {
            reunionInvitadoDAO.insert(reunionInvitado);
        }
    }

    @Transactional
    public void updateOrganosReunionByReunionId(Long reunionId, List<Organo> organos, Long connectedUserId)
            throws ReunionNoDisponibleException, MiembrosExternosException, OrganosExternosException
    {
        Reunion reunion = reunionDAO.getReunionConOrganosById(reunionId);

        if (reunion == null)
        {
            throw new ReunionNoDisponibleException();
        }

        List<Long> listaIdsLocales = new ArrayList<>();
        List<String> listaIdsExternos = new ArrayList<>();

        if (organos != null)
        {
            for (Organo organo : organos)
            {
                if (organo.isExterno())
                {
                    listaIdsExternos.add(organo.getId());
                }
                else
                {
                    listaIdsLocales.add(Long.parseLong(organo.getId()));
                }
            }
        }

        borraOrganosNoNecesarios(reunion, listaIdsExternos, listaIdsLocales);

        addOrganosLocalesNoExistentes(reunion, listaIdsLocales, connectedUserId);
        addOrganosExternosNoExistentes(reunion, listaIdsExternos, connectedUserId);
    }

    @Transactional
    private void addOrganosExternosNoExistentes(Reunion reunion, List<String> listaIdsExternos, Long connectedUserId)
            throws MiembrosExternosException, OrganosExternosException
    {
        List<String> listaActualOrganosExternosIds = reunion.getReunionOrganos()
                .stream()
                .filter(el -> el.isExterno())
                .map(elem -> elem.getOrganoId())
                .collect(Collectors.toList());

        List<Organo> organosExternos = organoService.getOrganosExternos();

        for (String organoId : listaIdsExternos)
        {
            if (!listaActualOrganosExternosIds.contains(organoId))
            {
                OrganoReunion organoReunion = new OrganoReunion();
                organoReunion.setReunion(reunion);
                organoReunion.setOrganoId(organoId);
                organoReunion.setExterno(true);

                Organo organo = getOrganoExternoById(organoId, organosExternos);
                organoReunion.setOrganoNombre(organo.getNombre());
                organoReunion.setOrganoNombreAlternativo(organo.getNombreAlternativo());
                organoReunion.setTipoOrganoId(organo.getTipoOrgano().getId());

                organoReunionDAO.insert(organoReunion);

                organoReunionMiembroService.addOrganoReunionMiembros(organoReunion, connectedUserId);
            }
        }
    }

    private Organo getOrganoExternoById(String organoId, List<Organo> organosExternos)
    {
        for (Organo organo : organosExternos)
        {
            if (organo.getId().equals(organoId))
            {
                return organo;
            }
        }

        return null;
    }

    @Transactional
    private void addOrganosLocalesNoExistentes(Reunion reunion, List<Long> listaIdsLocales, Long connectedUserId)
            throws MiembrosExternosException
    {
        List<Long> listaActualOrganosLocalesIds = reunion.getReunionOrganos()
                .stream()
                .filter(el -> el.isExterno() == false)
                .map(elem -> Long.parseLong(elem.getOrganoId()))
                .collect(Collectors.toList());

        for (Long organoId : listaIdsLocales)
        {
            if (!listaActualOrganosLocalesIds.contains(organoId))
            {
                Organo organo = organoService.getOrganoById(organoId, connectedUserId);
                OrganoReunion organoReunion = new OrganoReunion();
                organoReunion.setReunion(reunion);
                organoReunion.setOrganoId(organoId.toString());
                organoReunion.setExterno(false);
                organoReunion.setOrganoNombre(organo.getNombre());
                organoReunion.setOrganoNombreAlternativo(organo.getNombreAlternativo());
                organoReunion.setTipoOrganoId(organo.getTipoOrgano().getId());

                organoReunionDAO.insert(organoReunion);

                organoReunionMiembroService.addOrganoReunionMiembros(organoReunion, connectedUserId);
            }
        }
    }

    private void borraOrganosNoNecesarios(Reunion reunion, List<String> listaIdsExternos, List<Long> listaIdsLocales)
    {
        for (OrganoReunion cr : reunion.getReunionOrganos())
        {
            if (!cr.isExterno() && !listaIdsLocales.contains(Long.parseLong(cr.getOrganoId())))
            {
                organoReunionDAO.delete(OrganoReunion.class, cr.getId());
            }

            if (cr.isExterno() && !listaIdsExternos.contains(cr.getOrganoId()))
            {
                organoReunionDAO.delete(OrganoReunion.class, cr.getId());
            }

        }
    }

    @Transactional(rollbackFor = FirmaReunionException.class)
    public void firmarReunion(Long reunionId, String acuerdos, String acuerdosAlternativos, Long responsableActaId,
            Long connectedUserId)
            throws ReunionYaCompletadaException, FirmaReunionException, OrganosExternosException,
            PersonasExternasException, IOException, NoSuchAlgorithmException
    {
        Reunion reunion = reunionDAO.getReunionConOrganosById(reunionId);

        if (reunion.getAcuerdos() != null)
        {
            throw new ReunionYaCompletadaException();
        }

        reunion.setCompletada(true);
        reunion.setAcuerdos(acuerdos);
        reunion.setAcuerdosAlternativos(acuerdosAlternativos);

        ReunionFirma reunionFirma = reunionFirmaDesdeReunion(reunion, responsableActaId, connectedUserId);

        Client client = Client.create(Utils.createClientConfig());

        WebResource getFirmasResource = client.resource(this.firmasEndpoint);

        ClientResponse response = getFirmasResource.type(MediaType.APPLICATION_JSON)
                .header("X-UJI-AuthToken", authToken)
                .post(ClientResponse.class, reunionFirma);

        if (response.getStatus() != 200)
        {
            throw new FirmaReunionException();
        }

        RespuestaFirma respuestaFirma = RespuestaFirma.buildRespuestaFirma(response);

        actualizarAcuerdosPuntosDelDia(respuestaFirma);
        actualizarAsistencias(reunion.getId(), respuestaFirma);

        reunionDAO.marcarReunionComoCompletadaYActualizarAcuerdoYUrl(reunionId, responsableActaId, acuerdos,
                acuerdosAlternativos, respuestaFirma);
    }

    private void actualizarAsistencias(Long reunionId, RespuestaFirma respuestaFirma)
    {
        for (RespuestaFirmaAsistencia respuestaFirmaAsistencia : respuestaFirma.getAsistencias())
        {
            reunionDAO.updateAsistenciaInvitadoReunion(reunionId, respuestaFirmaAsistencia);
            reunionDAO.updateAsistenciaInvitadoOrgano(reunionId, respuestaFirmaAsistencia);
            reunionDAO.updateAsistenciaMiembrosYSuplentes(reunionId, respuestaFirmaAsistencia);
        }
    }

    private void actualizarAcuerdosPuntosDelDia(RespuestaFirma respuestaFirma)
    {
        for (RespuestaFirmaPuntoOrdenDiaAcuerdo acuerdo : respuestaFirma.getPuntoOrdenDiaAcuerdos())
        {
            reunionDAO.updateAcuerdoPuntoDelDiaUrlActa(acuerdo);
        }
    }

    private ReunionFirma reunionFirmaDesdeReunion(Reunion reunion, Long responsableActaId, Long connectedUserId)
            throws OrganosExternosException, PersonasExternasException, IOException, NoSuchAlgorithmException
    {
        ReunionFirma reunionFirma = new ReunionFirma();

        reunionFirma.setId(reunion.getId());
        reunionFirma.setAsunto(reunion.getAsunto());
        reunionFirma.setAsuntoAlternativo(reunion.getAsuntoAlternativo());
        reunionFirma.setDescripcion(reunion.getDescripcion());
        reunionFirma.setDescripcionAlternativa(reunion.getDescripcionAlternativa());
        reunionFirma.setDuracion(reunion.getDuracion());
        reunionFirma.setNumeroSesion(reunion.getNumeroSesion());
        reunionFirma.setAcuerdos(reunion.getAcuerdos());
        reunionFirma.setAcuerdosAlternativos(reunion.getAcuerdosAlternativos());
        reunionFirma.setUbicacion(reunion.getUbicacion());
        reunionFirma.setUbicacionAlternativa(reunion.getUbicacionAlternativa());
        reunionFirma.setFecha(reunion.getFecha());
        reunionFirma.setUrlGrabacion(reunion.getUrlGrabacion());
        reunionFirma.setTelematica(reunion.isTelematica());
        reunionFirma.setTelematicaDescripcion(reunion.getTelematicaDescripcion());
        reunionFirma.setTelematicaDescripcionAlternativa(reunion.getTelematicaDescripcionAlternativa());
        reunionFirma.setAdmiteSuplencia(reunion.isAdmiteSuplencia());
        reunionFirma.setAdmiteDelegacionVoto(reunion.isAdmiteDelegacionVoto());
        reunionFirma.setCompletada(reunion.getCompletada());
        reunionFirma.setCreadorNombre(reunion.getCreadorNombre());
        reunionFirma.setCreadorEmail(reunion.getCreadorEmail());

        if (responsableActaId != null)
        {
            OrganoReunionMiembro responsable = organoReunionMiembroDAO.getMiembroById(responsableActaId);

            reunionFirma.setResponsableActaId(responsable.getMiembroId());
            reunionFirma.setResponsableActaEmail(responsable.getEmail());
            reunionFirma.setResponsableActa(responsable.getNombre());
            reunionFirma.setCargoResponsableActa(responsable.getCargoNombre());
            reunionFirma.setCargoAlternativoResponsableActa(responsable.getCargoNombreAlternativo());
        }

        List<Organo> organos = organoService.getOrganosByReunionId(reunion.getId(), connectedUserId);
        List<ReunionComentario> comentarios =
                reunionComentarioService.getComentariosByReunionId(reunion.getId(), connectedUserId);

        List<OrganoFirma> listaOrganosFirma = getOrganosFirmaDesdeOrganos(organos, reunion);
        reunionFirma.setOrganos(listaOrganosFirma);

        List<ReunionDocumento> reunionDocumentos = reunionDocumentoDAO.getDocumentosByReunionId(reunion.getId());
        List<DocumentoFirma> listaDocumentosFirma = getReunionDocumentosFirmaDesdeDocumentos(reunionDocumentos);

        reunionFirma.setDocumentos(listaDocumentosFirma);
        reunionFirma.setComentarios(getComentariosFirmaDesdeComentarios(comentarios));

        List<PuntoOrdenDia> puntosOrdenDia = puntoOrdenDiaDAO.getPuntosByReunionId(reunion.getId());
        List<PuntoOrdenDiaFirma> listaPuntosOrdenDiaFirma = getPuntosOrdenDiaFirmaDesdePuntosOrdenDia(puntosOrdenDia);
        reunionFirma.setPuntosOrdenDia(listaPuntosOrdenDiaFirma);

        List<Persona> invitados = reunionDAO.getInvitadosPresencialesByReunionId(reunion.getId());
        List<InvitadoFirma> invitadosFirma = getInvitadosFirmaDesdeReunionInvitados(invitados);
        reunionFirma.setInvitados(invitadosFirma);

        return reunionFirma;
    }

    private List<InvitadoFirma> getInvitadosFirmaDesdeReunionInvitados(List<Persona> invitados)
    {
        List<InvitadoFirma> listaInvitadosFirma = new ArrayList<>();

        for (Persona invitado : invitados)
        {
            listaInvitadosFirma.add(getInvitadoFirmaDesdeInvitado(invitado));
        }

        return listaInvitadosFirma;
    }

    private InvitadoFirma getInvitadoFirmaDesdeInvitado(Persona invitado)
    {
        InvitadoFirma invitadoFirma = new InvitadoFirma();

        invitadoFirma.setId(invitado.getId());
        invitadoFirma.setNombre(invitado.getNombre());
        invitadoFirma.setEmail(invitado.getEmail());

        return invitadoFirma;
    }

    private List<PuntoOrdenDiaFirma> getPuntosOrdenDiaFirmaDesdePuntosOrdenDia(List<PuntoOrdenDia> puntosOrdenDia)
            throws IOException, NoSuchAlgorithmException
    {
        List<PuntoOrdenDiaFirma> listaPuntosOrdenDiaFirma = new ArrayList<>();

        for (PuntoOrdenDia puntoOrdenDia : puntosOrdenDia)
        {
            listaPuntosOrdenDiaFirma.add(getPuntoOrdenDiaFirmaDesdePuntoOrdenDia(puntoOrdenDia));
        }
        return listaPuntosOrdenDiaFirma;
    }

    private PuntoOrdenDiaFirma getPuntoOrdenDiaFirmaDesdePuntoOrdenDia(PuntoOrdenDia puntoOrdenDia)
            throws IOException, NoSuchAlgorithmException
    {
        PuntoOrdenDiaFirma puntoOrdenDiaFirma = new PuntoOrdenDiaFirma();
        puntoOrdenDiaFirma.setId(puntoOrdenDia.getId());
        puntoOrdenDiaFirma.setTitulo(puntoOrdenDia.getTitulo());
        puntoOrdenDiaFirma.setTituloAlternativo(puntoOrdenDia.getTituloAlternativo());
        puntoOrdenDiaFirma.setAcuerdos(puntoOrdenDia.getAcuerdos());
        puntoOrdenDiaFirma.setAcuerdosAlternativos(puntoOrdenDia.getAcuerdosAlternativos());
        puntoOrdenDiaFirma.setDeliberaciones(puntoOrdenDia.getDeliberaciones());
        puntoOrdenDiaFirma.setDeliberacionesAlternativas(puntoOrdenDia.getDeliberacionesAlternativas());
        puntoOrdenDiaFirma.setDescripcion(puntoOrdenDia.getDescripcion());
        puntoOrdenDiaFirma.setDescripcionAlternativa(puntoOrdenDia.getDescripcionAlternativa());
        puntoOrdenDiaFirma.setOrden(puntoOrdenDia.getOrden());

        List<PuntoOrdenDiaDocumento> documentos =
                puntoOrdenDiaDocumentoDAO.getDocumentosByPuntoOrdenDiaId(puntoOrdenDia.getId());

        List<PuntoOrdenDiaAcuerdo> documentosAcuerdos =
                puntoOrdenDiaAcuerdoDAO.getAcuerdosByPuntoOrdenDiaId(puntoOrdenDia.getId());

        puntoOrdenDiaFirma.setDocumentos(getDocumentosFirmaDesdePuntosOrdenDiaDocumentos(documentos));
        puntoOrdenDiaFirma.setDocumentosAcuerdos(
                getDocumentosAcuerdosFirmaDesdePuntosOrdenDiaDocumentos(documentosAcuerdos));

        return puntoOrdenDiaFirma;
    }

    private List<DocumentoFirma> getDocumentosAcuerdosFirmaDesdePuntosOrdenDiaDocumentos(
            List<PuntoOrdenDiaAcuerdo> documentos)
            throws IOException, NoSuchAlgorithmException
    {
        List<DocumentoFirma> listaDocumentoTemplate = new ArrayList<>();

        for (PuntoOrdenDiaAcuerdo puntoOrdenDiaAcuerdo : documentos)
        {
            listaDocumentoTemplate.add(getAcuerdoDocumentoFirmaDesdePuntoOrdenDiaDocumento(puntoOrdenDiaAcuerdo));
        }

        return listaDocumentoTemplate;
    }

    private DocumentoFirma getAcuerdoDocumentoFirmaDesdePuntoOrdenDiaDocumento(
            PuntoOrdenDiaAcuerdo puntoOrdenDiaAcuerdo)
            throws IOException, NoSuchAlgorithmException
    {
        DocumentoFirma documentoFirma = new DocumentoFirma();

        documentoFirma.setId(puntoOrdenDiaAcuerdo.getId());
        documentoFirma.setDescripcion(puntoOrdenDiaAcuerdo.getDescripcion());
        documentoFirma.setDescripcionAlternativa(puntoOrdenDiaAcuerdo.getDescripcionAlternativa());
        documentoFirma.setMimeType(puntoOrdenDiaAcuerdo.getMimeType());
        documentoFirma.setFechaAdicion(puntoOrdenDiaAcuerdo.getFechaAdicion());
        documentoFirma.setCreadorId(puntoOrdenDiaAcuerdo.getCreadorId());
        documentoFirma.setNombreFichero(puntoOrdenDiaAcuerdo.getNombreFichero());
        documentoFirma.setHash(Utils.getHash(puntoOrdenDiaAcuerdo.getDatos()));
        documentoFirma.setUrlDescarga(publicUrl + "/goc/rest/reuniones/" + puntoOrdenDiaAcuerdo.getPuntoOrdenDia()
                .getReunion()
                .getId() + "/puntosOrdenDia/" + puntoOrdenDiaAcuerdo.getPuntoOrdenDia()
                .getId() + "/acuerdos/" + puntoOrdenDiaAcuerdo.getId() + "/descargar");

        return documentoFirma;
    }

    private List<DocumentoFirma> getDocumentosFirmaDesdePuntosOrdenDiaDocumentos(
            List<PuntoOrdenDiaDocumento> documentos)
            throws IOException, NoSuchAlgorithmException
    {
        List<DocumentoFirma> listaDocumentoTemplate = new ArrayList<>();

        for (PuntoOrdenDiaDocumento puntoOrdenDiaDocumento : documentos)
        {
            listaDocumentoTemplate.add(getDocumentoFirmaDesdePuntoOrdenDiaDocumento(puntoOrdenDiaDocumento));
        }

        return listaDocumentoTemplate;
    }

    private DocumentoFirma getDocumentoFirmaDesdePuntoOrdenDiaDocumento(PuntoOrdenDiaDocumento puntoOrdenDiaDocumento)
            throws IOException, NoSuchAlgorithmException
    {
        DocumentoFirma documentoFirma = new DocumentoFirma();

        documentoFirma.setId(puntoOrdenDiaDocumento.getId());
        documentoFirma.setDescripcion(puntoOrdenDiaDocumento.getDescripcion());
        documentoFirma.setDescripcionAlternativa(puntoOrdenDiaDocumento.getDescripcionAlternativa());
        documentoFirma.setMimeType(puntoOrdenDiaDocumento.getMimeType());
        documentoFirma.setFechaAdicion(puntoOrdenDiaDocumento.getFechaAdicion());
        documentoFirma.setCreadorId(puntoOrdenDiaDocumento.getCreadorId());
        documentoFirma.setNombreFichero(puntoOrdenDiaDocumento.getNombreFichero());
        documentoFirma.setHash(Utils.getHash(puntoOrdenDiaDocumento.getDatos()));
        documentoFirma.setUrlDescarga(publicUrl + "/goc/rest/reuniones/" + puntoOrdenDiaDocumento.getPuntoOrdenDia()
                .getReunion()
                .getId() + "/puntosOrdenDia/" + puntoOrdenDiaDocumento.getPuntoOrdenDia()
                .getId() + "/documentos/" + puntoOrdenDiaDocumento.getId() + "/descargar");

        return documentoFirma;
    }

    private List<Comentario> getComentariosFirmaDesdeComentarios(List<ReunionComentario> comentarios)
    {
        List<Comentario> listaComentariosFirma = new ArrayList<>();

        for (ReunionComentario comentario : comentarios)
        {
            listaComentariosFirma.add(getComentarioFirmaDesdeComentario(comentario));
        }

        return listaComentariosFirma;
    }

    private Comentario getComentarioFirmaDesdeComentario(ReunionComentario comentario)
    {
        Comentario comentarioFirma = new Comentario();
        comentarioFirma.setId(comentario.getId());
        comentarioFirma.setComentario(comentario.getComentario());
        comentarioFirma.setCreadorNombre(comentario.getCreadorNombre());
        comentarioFirma.setCreadorId(comentario.getCreadorId());
        comentarioFirma.setFecha(comentario.getFecha());

        return comentarioFirma;
    }

    private List<DocumentoFirma> getReunionDocumentosFirmaDesdeDocumentos(List<ReunionDocumento> reunionDocumentos)
            throws IOException, NoSuchAlgorithmException
    {
        List<DocumentoFirma> listaDocumentoFirma = new ArrayList<>();

        for (ReunionDocumento reunionDocumento : reunionDocumentos)
        {
            listaDocumentoFirma.add(getDocumentoFirmaDesdeReunionDocumento(reunionDocumento));
        }

        return listaDocumentoFirma;
    }

    private DocumentoFirma getDocumentoFirmaDesdeReunionDocumento(ReunionDocumento reunionDocumento)
            throws IOException, NoSuchAlgorithmException
    {
        DocumentoFirma documentoFirma = new DocumentoFirma();

        documentoFirma.setId(reunionDocumento.getId());
        documentoFirma.setDescripcion(reunionDocumento.getDescripcion());
        documentoFirma.setDescripcionAlternativa(reunionDocumento.getDescripcionAlternativa());
        documentoFirma.setMimeType(reunionDocumento.getMimeType());
        documentoFirma.setFechaAdicion(reunionDocumento.getFechaAdicion());
        documentoFirma.setCreadorId(reunionDocumento.getCreadorId());
        documentoFirma.setNombreFichero(reunionDocumento.getNombreFichero());
        documentoFirma.setHash(Utils.getHash(reunionDocumento.getDatos()));
        documentoFirma.setUrlDescarga(publicUrl + "/goc/rest/reuniones/" + reunionDocumento.getReunion()
                .getId() + "/documentos/" + reunionDocumento.getId() + "/descargar");

        return documentoFirma;
    }

    public ReunionTemplate getReunionTemplateDesdeReunion(Reunion reunion, Long connectedUserId,
            Boolean withNoAsistentes, Boolean mainLanguage)
    {
        ReunionTemplate reunionTemplate = new ReunionTemplate();

        reunionTemplate.setId(reunion.getId());
        reunionTemplate.setAsunto(mainLanguage ? reunion.getAsunto() : reunion.getAsuntoAlternativo());
        reunionTemplate.setDescripcion(mainLanguage ? reunion.getDescripcion() : reunion.getDescripcionAlternativa());
        reunionTemplate.setDuracion(reunion.getDuracion());
        reunionTemplate.setNumeroSesion(reunion.getNumeroSesion());
        reunionTemplate.setAcuerdos(mainLanguage ? reunion.getAcuerdos() : reunion.getAcuerdosAlternativos());
        reunionTemplate.setUbicacion(mainLanguage ? reunion.getUbicacion() : reunion.getUbicacionAlternativa());
        reunionTemplate.setFecha(reunion.getFecha());
        reunionTemplate.setFechaSegundaConvocatoria(reunion.getFechaSegundaConvocatoria());
        reunionTemplate.setUrlGrabacion(reunion.getUrlGrabacion());
        reunionTemplate.setTelematica(reunion.isTelematica());
        reunionTemplate.setTelematicaDescripcion(
                mainLanguage ? reunion.getTelematicaDescripcion() : reunion.getTelematicaDescripcionAlternativa());
        reunionTemplate.setPublica(reunion.isPublica());
        reunionTemplate.setAdmiteSuplencia(reunion.isAdmiteSuplencia());
        reunionTemplate.setAdmiteDelegacionVoto(reunion.isAdmiteDelegacionVoto());
        reunionTemplate.setAdmiteComentarios(reunion.isAdmiteComentarios());
        reunionTemplate.setCompletada(reunion.getCompletada());
        reunionTemplate.setCreadorNombre(reunion.getCreadorNombre());
        reunionTemplate.setCreadorEmail(reunion.getCreadorEmail());
        reunionTemplate.setCreadorId(reunion.getCreadorId());
        reunionTemplate.setUrlActa(mainLanguage ? reunion.getUrlActa() : reunion.getUrlActaAlternativa());

        if (reunion.getMiembroResponsableActa() != null)
        {
            OrganoReunionMiembro responsable =
                    organoReunionMiembroDAO.getMiembroById(reunion.getMiembroResponsableActa().getId());

            reunionTemplate.setResponsableActa(responsable.getNombre());
            reunionTemplate.setCargoResponsableActa(
                    mainLanguage ? responsable.getCargoNombre() : responsable.getCargoNombreAlternativo());
        }

        List<Organo> organos = organoService.getOrganosByReunionId(reunion.getId(), connectedUserId);
        List<ReunionComentario> comentarios =
                reunionComentarioService.getComentariosByReunionId(reunion.getId(), connectedUserId);

        List<OrganoTemplate> listaOrganosTemplate =
                getOrganosTemplateDesdeOrganos(organos, reunion, withNoAsistentes, mainLanguage);
        reunionTemplate.setOrganos(listaOrganosTemplate);

        List<ReunionDocumento> reunionDocumentos = reunionDocumentoDAO.getDatosDocumentosByReunionId(reunion.getId());
        List<DocumentoTemplate> listaDocumentosTemplate =
                getReunionDocumentosTemplateDesdeDocumentos(reunionDocumentos, mainLanguage);

        reunionTemplate.setDocumentos(listaDocumentosTemplate);
        reunionTemplate.setComentarios(getComentariosTemplateDessdeComentarios(comentarios));

        List<PuntoOrdenDia> puntosOrdenDia = puntoOrdenDiaDAO.getPuntosByReunionId(reunion.getId());
        List<PuntoOrdenDiaTemplate> listaPuntosOrdenDiaTemplate =
                getPuntosOrdenDiaTemplateDesdePuntosOrdenDia(puntosOrdenDia, mainLanguage);
        reunionTemplate.setPuntosOrdenDia(listaPuntosOrdenDiaTemplate);

        List<Persona> invitados = reunionDAO.getInvitadosPresencialesByReunionId(reunion.getId());
        List<InvitadoTemplate> invitadosTemplate = getInvitadosTemplateDesdeReunionInvitados(invitados);
        reunionTemplate.setInvitados(invitadosTemplate);

        return reunionTemplate;
    }

    private List<Comentario> getComentariosTemplateDessdeComentarios(List<ReunionComentario> comentarios)
    {
        List<Comentario> listaComentariosTemplate = new ArrayList<>();

        for (ReunionComentario comentario : comentarios)
        {
            listaComentariosTemplate.add(getComentarioTemplateDessdeComentario(comentario));
        }

        return listaComentariosTemplate;
    }

    private Comentario getComentarioTemplateDessdeComentario(ReunionComentario comentario)
    {
        Comentario comentarioTemplate = new Comentario();
        comentarioTemplate.setId(comentario.getId());
        comentarioTemplate.setComentario(comentario.getComentario());
        comentarioTemplate.setFecha(comentario.getFecha());
        comentarioTemplate.setCreadorNombre(comentario.getCreadorNombre());
        comentarioTemplate.setCreadorId(comentario.getCreadorId());

        return comentarioTemplate;
    }

    private List<InvitadoTemplate> getInvitadosTemplateDesdeReunionInvitados(List<Persona> invitados)
    {
        List<InvitadoTemplate> listaInvitadosTemplate = new ArrayList<>();

        for (Persona invitado : invitados)
        {
            listaInvitadosTemplate.add(getInvitadoTemplateDessdeInvitado(invitado));
        }

        return listaInvitadosTemplate;
    }

    private InvitadoTemplate getInvitadoTemplateDessdeInvitado(Persona invitado)
    {
        InvitadoTemplate invitadoTemplate = new InvitadoTemplate();

        invitadoTemplate.setId(invitado.getId());
        invitadoTemplate.setNombre(invitado.getNombre());
        invitadoTemplate.setEmail(invitado.getEmail());

        return invitadoTemplate;
    }

    private List<OrganoTemplate> getOrganosTemplateDesdeOrganos(List<Organo> organos, Reunion reunion,
            Boolean withNoAsistentes, boolean mainLanguage)
    {
        List<OrganoTemplate> listaOrganoTemplate = new ArrayList<>();

        for (Organo organo : organos)
        {
            listaOrganoTemplate.add(getOrganoTemplateDesdeOrgano(organo, reunion, withNoAsistentes, mainLanguage));
        }

        return listaOrganoTemplate;
    }

    private OrganoTemplate getOrganoTemplateDesdeOrgano(Organo organo, Reunion reunion, Boolean withNoAsistentes,
            boolean mainLanguage)
    {
        OrganoTemplate organoTemplate = new OrganoTemplate();
        organoTemplate.setId(organo.getId());
        organoTemplate.setNombre(mainLanguage ? organo.getNombre() : organo.getNombreAlternativo());
        organoTemplate.setTipoCodigo(organo.getTipoOrgano().getCodigo());
        organoTemplate.setTipoNombre(
                mainLanguage ? organo.getTipoOrgano().getNombre() : organo.getTipoOrgano().getNombreAlternativo());
        organoTemplate.setTipoOrganoId(organo.getTipoOrgano().getId());

        List<OrganoReunionMiembro> listaAsistentes;
        List<OrganoReunionMiembro> listaMiembros =
                organoReunionMiembroDAO.getMiembroReunionByOrganoAndReunionId(organo.getId(), organo.isExterno(),
                        reunion.getId());

        if (withNoAsistentes)
        {
            listaAsistentes = listaMiembros;
        }
        else
        {
            listaAsistentes =
                    organoReunionMiembroDAO.getAsistenteReunionByOrganoAndReunionId(organo.getId(), organo.isExterno(),
                            reunion.getId());
        }

        organoTemplate.setAsistentes(
                getAsistentesDesdeListaOrganoReunionMiembro(listaAsistentes, listaMiembros, mainLanguage));
        organoTemplate.setAusentes(
                getAusentesDesdeListaOrganoReunionMiembro(listaAsistentes, listaMiembros, mainLanguage));

        return organoTemplate;
    }

    private List<OrganoFirma> getOrganosFirmaDesdeOrganos(List<Organo> organos, Reunion reunion)
    {
        List<OrganoFirma> listaOrganoFirma = new ArrayList<>();

        for (Organo organo : organos)
        {
            listaOrganoFirma.add(getOrganoFirmaDesdeOrgano(organo, reunion));
        }

        return listaOrganoFirma;
    }

    private OrganoFirma getOrganoFirmaDesdeOrgano(Organo organo, Reunion reunion)
    {
        OrganoFirma organoFirma = new OrganoFirma();
        organoFirma.setId(organo.getId());
        organoFirma.setNombre(organo.getNombre());
        organoFirma.setNombreAlternativo(organo.getNombreAlternativo());
        organoFirma.setTipoCodigo(organo.getTipoOrgano().getCodigo());
        organoFirma.setTipoNombre(organo.getTipoOrgano().getNombre());
        organoFirma.setTipoNombreAlternativo(organo.getTipoOrgano().getNombreAlternativo());
        organoFirma.setTipoOrganoId(organo.getTipoOrgano().getId());

        List<OrganoReunionMiembro> listaAsistentes =
                organoReunionMiembroDAO.getAsistenteReunionByOrganoAndReunionId(organo.getId(), organo.isExterno(),
                        reunion.getId());

        List<OrganoReunionMiembro> listaMiembros =
                organoReunionMiembroDAO.getMiembroReunionByOrganoAndReunionId(organo.getId(), organo.isExterno(),
                        reunion.getId());

        organoFirma.setAsistentes(getAsistentesFirmaDesdeListaOrganoReunionMiembro(listaAsistentes, listaMiembros));

        return organoFirma;
    }

    private List<MiembroFirma> getAsistentesFirmaDesdeListaOrganoReunionMiembro(
            List<OrganoReunionMiembro> listaAsistentes, List<OrganoReunionMiembro> listaMiembros)
    {
        List<MiembroFirma> listaMiembroFirma = new ArrayList<>();

        for (OrganoReunionMiembro organoReunionMiembro : listaAsistentes)
        {
            listaMiembroFirma.add(getAsistenteFirmaDesdeOrganoReunionMiembro(organoReunionMiembro));
        }

        for (MiembroFirma miembroFirma : listaMiembroFirma)
        {
            setDelegacionDeVotoToFirma(miembroFirma, listaMiembros);
        }

        return listaMiembroFirma;
    }

    private void setDelegacionDeVotoToFirma(MiembroFirma miembroFirma, List<OrganoReunionMiembro> miembros)
    {
        for (OrganoReunionMiembro miembro : miembros)
        {
            if (miembro.getDelegadoVotoId() != null && miembroFirma.getId()
                    .equals(miembro.getDelegadoVotoId().toString()) && !(miembroFirma.getId()
                    .equals(miembro.getMiembroId())))
            {
                miembroFirma.addDelegacionDeVoto(miembro.getNombre());
            }
        }

        miembroFirma.buildNombresDelegacionesDeVoto();
    }

    private MiembroFirma getAsistenteFirmaDesdeOrganoReunionMiembro(OrganoReunionMiembro organoReunionMiembro)
    {
        MiembroFirma miembroFirma = new MiembroFirma();
        miembroFirma.setNombre(organoReunionMiembro.getNombre());
        miembroFirma.setEmail(organoReunionMiembro.getEmail());
        miembroFirma.setId(organoReunionMiembro.getMiembroId());
        miembroFirma.setSuplente(organoReunionMiembro.getSuplenteNombre());
        miembroFirma.setSuplenteId(organoReunionMiembro.getSuplenteId());
        miembroFirma.setDelegadoVotoId(organoReunionMiembro.getDelegadoVotoId());
        miembroFirma.setDelegadoVoto(organoReunionMiembro.getDelegadoVotoNombre());
        miembroFirma.setAsistenciaConfirmada(organoReunionMiembro.getAsistenciaConfirmada());
        miembroFirma.setAsistencia(organoReunionMiembro.getAsistencia());

        Cargo cargo = new Cargo();
        cargo.setId(organoReunionMiembro.getCargoId());
        cargo.setCodigo(organoReunionMiembro.getCargoCodigo());
        cargo.setNombre(organoReunionMiembro.getCargoNombre());
        cargo.setNombreAlternativo(organoReunionMiembro.getCargoNombreAlternativo());

        miembroFirma.setCargo(cargo);

        return miembroFirma;
    }

    private List<MiembroTemplate> getAsistentesDesdeListaOrganoReunionMiembro(
            List<OrganoReunionMiembro> listaAsistentes, List<OrganoReunionMiembro> listaMiembros, boolean mainLanguage)
    {
        List<MiembroTemplate> listaMiembroTemplate = new ArrayList<>();

        for (OrganoReunionMiembro organoReunionMiembro : listaAsistentes)
        {
            listaMiembroTemplate.add(getAsistenteDesdeOrganoReunionMiembro(organoReunionMiembro, mainLanguage));
        }

        for (MiembroTemplate miembroTemplate : listaMiembroTemplate)
        {
            setDelegacionDeVotoToPlantilla(miembroTemplate, listaMiembros);
        }

        return listaMiembroTemplate;
    }

    private List<MiembroTemplate> getAusentesDesdeListaOrganoReunionMiembro(List<OrganoReunionMiembro> listaAsistentes,
            List<OrganoReunionMiembro> listaMiembros, boolean mainLanguage)
    {
        return listaMiembros.stream()
                .filter(a -> !listaAsistentes.contains(a))
                .map(a -> getAsistenteDesdeOrganoReunionMiembro(a, mainLanguage))
                .collect(Collectors.toList());
    }

    private void setDelegacionDeVotoToPlantilla(MiembroTemplate miembroTemplate, List<OrganoReunionMiembro> miembros)
    {
        for (OrganoReunionMiembro miembro : miembros)
        {
            if (miembro.getDelegadoVotoId() != null && miembroTemplate.getMiembroId()
                    .equals(miembro.getDelegadoVotoId().toString()) && !(miembroTemplate.getMiembroId()
                    .equals(miembro.getMiembroId())))
            {
                miembroTemplate.addDelegacionDeVoto(miembro.getNombre());
            }
        }
    }

    private MiembroTemplate getAsistenteDesdeOrganoReunionMiembro(OrganoReunionMiembro organoReunionMiembro,
            boolean mainLanguage)
    {
        MiembroTemplate miembroTemplate = new MiembroTemplate();
        miembroTemplate.setNombre(organoReunionMiembro.getNombre());
        miembroTemplate.setEmail(organoReunionMiembro.getEmail());
        miembroTemplate.setCondicion(
                mainLanguage ? organoReunionMiembro.getCondicion() : organoReunionMiembro.getCondicionAlternativa());
        miembroTemplate.setId(organoReunionMiembro.getId().toString());
        miembroTemplate.setMiembroId(organoReunionMiembro.getMiembroId());
        miembroTemplate.setSuplente(organoReunionMiembro.getSuplenteNombre());
        miembroTemplate.setSuplenteId(organoReunionMiembro.getSuplenteId());
        miembroTemplate.setDelegadoVoto(organoReunionMiembro.getDelegadoVotoNombre());
        miembroTemplate.setDelegadoVotoId(organoReunionMiembro.getDelegadoVotoId());
        miembroTemplate.setAsistenciaConfirmada(organoReunionMiembro.getAsistenciaConfirmada());
        miembroTemplate.setAsistencia(organoReunionMiembro.getAsistencia());

        CargoTemplate cargo = new CargoTemplate();
        cargo.setId(organoReunionMiembro.getCargoId());
        cargo.setNombre(
                mainLanguage ? organoReunionMiembro.getCargoNombre() : organoReunionMiembro.getCargoNombreAlternativo());
        cargo.setCodigo(organoReunionMiembro.getCargoCodigo());

        miembroTemplate.setCargo(cargo);

        return miembroTemplate;
    }

    private List<DocumentoTemplate> getReunionDocumentosTemplateDesdeDocumentos(
            List<ReunionDocumento> reunionDocumentos, boolean mainLanguage)
    {
        List<DocumentoTemplate> listaDocumento = new ArrayList<>();

        for (ReunionDocumento reunionDocumento : reunionDocumentos)
        {
            listaDocumento.add(getDocumentoTemplateDesdeReunionDocumento(reunionDocumento, mainLanguage));
        }
        return listaDocumento;
    }

    private DocumentoTemplate getDocumentoTemplateDesdeReunionDocumento(ReunionDocumento reunionDocumento,
            boolean mainLanguage)
    {
        DocumentoTemplate documento = new DocumentoTemplate();
        documento.setId(reunionDocumento.getId());
        documento.setDescripcion(
                mainLanguage ? reunionDocumento.getDescripcion() : reunionDocumento.getDescripcionAlternativa());
        documento.setMimeType(reunionDocumento.getMimeType());
        documento.setFechaAdicion(reunionDocumento.getFechaAdicion());
        documento.setCreadorId(reunionDocumento.getCreadorId());
        documento.setNombreFichero(reunionDocumento.getNombreFichero());

        return documento;
    }

    private List<PuntoOrdenDiaTemplate> getPuntosOrdenDiaTemplateDesdePuntosOrdenDia(List<PuntoOrdenDia> puntosOrdenDia,
            boolean mainLanguage)
    {
        List<PuntoOrdenDiaTemplate> listaPuntosOrdenDiaTemplate = new ArrayList<>();

        for (PuntoOrdenDia puntoOrdenDia : puntosOrdenDia)
        {
            listaPuntosOrdenDiaTemplate.add(getPuntoOrdenDiaTemplateDesdePuntoOrdenDia(puntoOrdenDia, mainLanguage));
        }

        return listaPuntosOrdenDiaTemplate;
    }

    private PuntoOrdenDiaTemplate getPuntoOrdenDiaTemplateDesdePuntoOrdenDia(PuntoOrdenDia puntoOrdenDia,
            boolean mainLanguage)
    {
        PuntoOrdenDiaTemplate puntoOrdenDiaTemplate = new PuntoOrdenDiaTemplate();
        puntoOrdenDiaTemplate.setId(puntoOrdenDia.getId());
        puntoOrdenDiaTemplate.setOrden(puntoOrdenDia.getOrden());
        puntoOrdenDiaTemplate.setAcuerdos(
                mainLanguage ? puntoOrdenDia.getAcuerdos() : puntoOrdenDia.getAcuerdosAlternativos());
        puntoOrdenDiaTemplate.setDeliberaciones(
                mainLanguage ? puntoOrdenDia.getDeliberaciones() : puntoOrdenDia.getDeliberacionesAlternativas());
        puntoOrdenDiaTemplate.setDescripcion(
                mainLanguage ? puntoOrdenDia.getDescripcion() : puntoOrdenDia.getDescripcionAlternativa());
        puntoOrdenDiaTemplate.setTitulo(
                mainLanguage ? puntoOrdenDia.getTitulo() : puntoOrdenDia.getTituloAlternativo());
        puntoOrdenDiaTemplate.setPublico(puntoOrdenDia.isPublico());
        puntoOrdenDiaTemplate.setUrlActa(
                mainLanguage ? puntoOrdenDia.getUrlActa() : puntoOrdenDia.getUrlActaAlternativa());

        List<PuntoOrdenDiaDocumento> documentos =
                puntoOrdenDiaDocumentoDAO.getDatosDocumentosByPuntoOrdenDiaId(puntoOrdenDia.getId());

        List<PuntoOrdenDiaAcuerdo> acuerdos =
                puntoOrdenDiaAcuerdoDAO.getDatosAcuerdosByPuntoOrdenDiaId(puntoOrdenDia.getId());

        List<PuntoOrdenDiaDescriptor> descriptores =
                puntoOrdenDiaDescriptorDAO.getDescriptoresOrdenDia(puntoOrdenDia.getId());

        puntoOrdenDiaTemplate.setDocumentos(
                getDocumentosTemplateDesdePuntosOrdenDiaDocumentos(documentos, mainLanguage));
        puntoOrdenDiaTemplate.setDocumentosAcuerdos(
                getAcuerdosTemplateDesdePuntosOrdenDiaAcuerdos(acuerdos, mainLanguage));
        puntoOrdenDiaTemplate.setDescriptores(
                getDescriptoresTemplateDesdePuntosOrdenDiaAcuerdos(descriptores, mainLanguage));

        return puntoOrdenDiaTemplate;
    }

    private List<DocumentoTemplate> getDocumentosTemplateDesdePuntosOrdenDiaDocumentos(
            List<PuntoOrdenDiaDocumento> documentos, boolean mainLanguage)
    {
        List<DocumentoTemplate> listaDocumento = new ArrayList<>();

        for (PuntoOrdenDiaDocumento puntoOrdenDiaDocumento : documentos)
        {
            listaDocumento.add(getDocumentoTemplateDesdePuntoOrdenDiaDocumento(puntoOrdenDiaDocumento, mainLanguage));
        }

        return listaDocumento;
    }

    private List<DocumentoTemplate> getAcuerdosTemplateDesdePuntosOrdenDiaAcuerdos(List<PuntoOrdenDiaAcuerdo> acuerdos,
            boolean mainLanguage)
    {
        List<DocumentoTemplate> listaDocumento = new ArrayList<>();

        for (PuntoOrdenDiaAcuerdo puntoOrdenDiaAcuerdo : acuerdos)
        {
            listaDocumento.add(getAcuerdoTemplateDesdePuntoOrdenDiaAcuerdo(puntoOrdenDiaAcuerdo, mainLanguage));
        }

        return listaDocumento;
    }

    private List<DescriptorTemplate> getDescriptoresTemplateDesdePuntosOrdenDiaAcuerdos(
            List<PuntoOrdenDiaDescriptor> descriptores, boolean mainLanguage)
    {
        List<DescriptorTemplate> listaDesciptores = new ArrayList<>();

        for (PuntoOrdenDiaDescriptor descriptor : descriptores)
        {
            listaDesciptores.add(getDescriptorTemplateDesdePuntoOrdenDiaAcuerdo(descriptor, mainLanguage));
        }

        return listaDesciptores;
    }

    private DocumentoTemplate getDocumentoTemplateDesdePuntoOrdenDiaDocumento(
            PuntoOrdenDiaDocumento puntoOrdenDiaDocumento, boolean mainLanguage)
    {
        DocumentoTemplate documento = new DocumentoTemplate();
        documento.setId(puntoOrdenDiaDocumento.getId());
        documento.setDescripcion(
                mainLanguage ? puntoOrdenDiaDocumento.getDescripcion() : puntoOrdenDiaDocumento.getDescripcionAlternativa());
        documento.setMimeType(puntoOrdenDiaDocumento.getMimeType());
        documento.setFechaAdicion(puntoOrdenDiaDocumento.getFechaAdicion());
        documento.setCreadorId(puntoOrdenDiaDocumento.getCreadorId());
        documento.setNombreFichero(puntoOrdenDiaDocumento.getNombreFichero());

        return documento;
    }

    private DocumentoTemplate getAcuerdoTemplateDesdePuntoOrdenDiaAcuerdo(PuntoOrdenDiaAcuerdo puntoOrdenDiaAcuerdo,
            boolean mainLanguage)
    {
        DocumentoTemplate documento = new DocumentoTemplate();

        documento.setId(puntoOrdenDiaAcuerdo.getId());
        documento.setDescripcion(
                mainLanguage ? puntoOrdenDiaAcuerdo.getDescripcion() : puntoOrdenDiaAcuerdo.getDescripcionAlternativa());
        documento.setMimeType(puntoOrdenDiaAcuerdo.getMimeType());
        documento.setFechaAdicion(puntoOrdenDiaAcuerdo.getFechaAdicion());
        documento.setCreadorId(puntoOrdenDiaAcuerdo.getCreadorId());
        documento.setNombreFichero(puntoOrdenDiaAcuerdo.getNombreFichero());

        return documento;
    }

    private DescriptorTemplate getDescriptorTemplateDesdePuntoOrdenDiaAcuerdo(PuntoOrdenDiaDescriptor descriptor,
            boolean mainLanguage)
    {
        DescriptorTemplate descriptorTemplate = new DescriptorTemplate();

        descriptorTemplate.setId(descriptor.getId());
        descriptorTemplate.setPuntoOrdenDiaId(descriptor.getPuntoOrdenDia().getId());
        descriptorTemplate.setClaveId(descriptor.getClave().getId());
        descriptorTemplate.setDescriptorId(descriptor.getClave().getDescriptor().getId());
        descriptorTemplate.setDescriptorNombre(
                mainLanguage ? descriptor.getClave().getDescriptor().getDescriptor() : descriptor.getClave()
                        .getDescriptor()
                        .getDescriptorAlternativo());
        descriptorTemplate.setDescriptorDescripcion(
                mainLanguage ? descriptor.getClave().getDescriptor().getDescripcion() : descriptor.getClave()
                        .getDescriptor()
                        .getDescripcionAlternativa());
        descriptorTemplate.setClaveNombre(
                mainLanguage ? descriptor.getClave().getClave() : descriptor.getClave().getClaveAlternativa());

        return descriptorTemplate;
    }

    public Reunion getReunionConOrganosById(Long reunionId, Long connectedUserId)
    {
        return reunionDAO.getReunionConOrganosById(reunionId);
    }

    public List<ReunionPermiso> getReunionesAccesiblesByPersonaId(Long connectedUserId)
    {
        return reunionDAO.getReunionesAccesiblesByPersonaId(connectedUserId);
    }

    public void compruebaReunionNoCompletada(Long reunionId)
            throws ReunionYaCompletadaException
    {
        Reunion reunion = reunionDAO.getReunionById(reunionId);

        if (reunion.getCompletada() != null && reunion.getCompletada())
        {
            throw new ReunionYaCompletadaException();
        }
    }

    public void compruebaReunionAdmiteSuplencia(Long reunionId)
            throws ReunionNoAdmiteSuplenciaException
    {
        Reunion reunion = reunionDAO.getReunionById(reunionId);

        if (reunion.isAdmiteSuplencia() != null && !reunion.isAdmiteSuplencia())
        {
            throw new ReunionNoAdmiteSuplenciaException();
        }
    }


    public void compruebaReunionAdmiteDelegacionVoto(Long reunionId)
            throws ReunionNoAdmiteDelegacionVotoException
    {
        Reunion reunion = reunionDAO.getReunionById(reunionId);

        if (reunion.isAdmiteDelegacionVoto() != null && !reunion.isAdmiteDelegacionVoto())
        {
            throw new ReunionNoAdmiteDelegacionVotoException();
        }
    }

    public String enviarConvocatoria(Long reunionId, User user)
            throws MiembrosExternosException, ReunionNoDisponibleException, NotificacionesException
    {
        Reunion reunion = reunionDAO.getReunionConMiembrosAndPuntosDiaById(reunionId);

        String error = checkAllFeaturesOfReunion(reunion);
        if (error != null) return error;

        avisosReunion.enviaAvisoNuevaReunion(reunion);

        if (!reunion.getAvisoPrimeraReunion())
        {
            reunion.setAvisoPrimeraReunion(true);
            reunion.setAvisoPrimeraReunionUser(user.getName());
            reunion.setAvisoPrimeraReunionFecha(new Date());
            reunionDAO.update(reunion);
        }

        return null;
    }

    public String checkReunionToClose(Long reunionId)
    {
        Reunion reunion = reunionDAO.getReunionConMiembrosAndPuntosDiaById(reunionId);

        String error = checkAllFeaturesOfReunion(reunion);
        if (error != null) return error;

        return null;
    }

    private String checkAllFeaturesOfReunion(Reunion reunion)
    {
        if (reunion.getReunionPuntosOrdenDia().size() == 0) return "appI18N.reuniones.reunionSinOrdenDia";

        if (reunion.noContieneMiembros()) return "appI18N.reuniones.reunionSinMiembros";
        return null;
    }

    public List<TipoOrganoLocal> getTiposOrganosConReunionesPublicas()
    {
        return tipoOrganoDAO.getTiposOrganoConReunionesPublicas();
    }

    public List<OrganoLocal> getOrganosConReunionesPublicas(Long tipoOrganoId, Integer anyo)
    {
        return organoDAO.getOrganosConReunionesPublicas(tipoOrganoId, anyo);
    }

    public List<Descriptor> getDescriptoresConReunionesPublicas(Integer anyo)
    {
        List<Reunion> reuniones = reunionDAO.getReunionesPublicas(anyo);
        List<Long> idsReuniones = reuniones.stream().map(r -> r.getId()).collect(Collectors.toList());
        return descriptorDAO.getDescriptoresConReunionesPublicas(idsReuniones, anyo);
    }

    public List<Integer> getAnyosConReunionesPublicas()
    {
        return reunionDAO.getAnyosConReunionesPublicas();
    }

    public List<Clave> getClavesConReunionesPublicas(Long descriptorId, Integer anyo)
    {
        List<Reunion> reuniones = reunionDAO.getReunionesPublicas(anyo);
        List<Long> idsReuniones = reuniones.stream().map(r -> r.getId()).collect(Collectors.toList());
        return claveDAO.getClavesConReunionesPublicas(idsReuniones, descriptorId, anyo);
    }

    public List<Reunion> getReunionesPublicas(AcuerdosSearch acuerdosSearch)
    {
        List<Reunion> reunionesPublicas =
                reunionDAO.getReunionesPublicasPaginated(acuerdosSearch.getTipoOrganoId(), acuerdosSearch.getOrganoId(),
                        acuerdosSearch.getDescriptorId(), acuerdosSearch.getClaveId(), acuerdosSearch.getAnyo(),
                        acuerdosSearch.getfInicio(), acuerdosSearch.getfFin(), acuerdosSearch.getTexto(),
                        acuerdosSearch.getIdiomaAlternativo(), acuerdosSearch.getStartSearch(),
                        acuerdosSearch.getNumResults());
        return filtrarDuplicadosReuniones(reunionesPublicas);
    }

    public Integer getNumReunionesPublicas(AcuerdosSearch acuerdosSearch)
    {
        List<Reunion> reunionesPublicasClave =
                reunionDAO.getReunionesPublicas(acuerdosSearch.getTipoOrganoId(), acuerdosSearch.getOrganoId(),
                        acuerdosSearch.getDescriptorId(), acuerdosSearch.getClaveId(), acuerdosSearch.getAnyo(),
                        acuerdosSearch.getfInicio(), acuerdosSearch.getfFin(), acuerdosSearch.getTexto(),
                        acuerdosSearch.getIdiomaAlternativo());
        return filtrarDuplicadosReuniones(reunionesPublicasClave).size();
    }

    private List<Reunion> filtrarDuplicadosReuniones(List<Reunion> reunionesTotal)
    {
        List<Reunion> reuniones = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (Reunion reunion : reunionesTotal)
        {
            if (!ids.contains(reunion.getId()))
            {
                ids.add(reunion.getId());
                reuniones.add(reunion);
            }
        }
        return reuniones;
    }

    private List<ReunionEditor> filtrarDuplicadosReunionEditores(List<ReunionEditor> reunionesTotal)
    {
        List<ReunionEditor> reuniones = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (ReunionEditor reunion : reunionesTotal)
        {
            if (!ids.contains(reunion.getId()))
            {
                ids.add(reunion.getId());
                reuniones.add(reunion);
            }
        }

        return reuniones;
    }

    public Reunion getReunionByIdAndEditorId(Long reunionId, Long connectedUserId)
            throws ReunionNoDisponibleException
    {
        ReunionEditor reunionEditor = reunionDAO.getReunionByIdAndEditorId(reunionId, connectedUserId);

        if (reunionEditor == null)
        {
            throw new ReunionNoDisponibleException();
        }

        return reunionDAO.getReunionById(reunionId);
    }

    public List<OrganoReunion> getOrganosReunionByReunionId(Long reunionId)
    {
        return reunionDAO.getOrganosReunionByReunionId(reunionId);
    }

    public List<ReunionInvitado> getInvitadosReunionByReunionId(Long reunionId)
    {
        return reunionInvitadoDAO.getInvitadosByReunionId(reunionId);
    }

    public String getNombreAsistente(Long reunionId, Long connectedUserId)
    {
        return reunionDAO.getNombreAsistente(reunionId, connectedUserId);
    }
}