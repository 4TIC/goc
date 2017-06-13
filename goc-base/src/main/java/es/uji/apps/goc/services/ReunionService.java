package es.uji.apps.goc.services;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.core.InjectParam;
import es.uji.apps.goc.dao.*;
import es.uji.apps.goc.dto.*;
import es.uji.apps.goc.exceptions.*;
import es.uji.apps.goc.model.*;
import es.uji.apps.goc.model.Cargo;
import es.uji.apps.goc.notifications.AvisosReunion;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.MediaType;
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
            Boolean admiteComentarios, Long connectedUserId)
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
            PersonasExternasException
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

        WebResource getFirmasResource = Client.create().resource(this.firmasEndpoint);
        ClientResponse response = getFirmasResource.type(MediaType.APPLICATION_JSON)
                .header("X-UJI-AuthToken", authToken)
                .post(ClientResponse.class, reunionFirma);

        if (response.getStatus() != 204)
        {
            throw new FirmaReunionException();
        }

        reunionDAO.marcarReunionComoCompletadaYActualizarAcuerdo(reunionId, responsableActaId, acuerdos,
                acuerdosAlternativos);
    }

    private ReunionFirma reunionFirmaDesdeReunion(Reunion reunion, Long responsableActaId, Long connectedUserId)
            throws OrganosExternosException, PersonasExternasException
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
        reunionFirma.setCompletada(reunion.getCompletada());
        reunionFirma.setCreadorNombre(reunion.getCreadorNombre());
        reunionFirma.setCreadorEmail(reunion.getCreadorEmail());

        if (responsableActaId != null)
        {
            OrganoReunionMiembro responsable = organoReunionMiembroDAO.getMiembroById(responsableActaId);

            reunionFirma.setResponsableActa(responsable.getNombre());
            reunionFirma.setCargoResponsableActa(responsable.getCargoNombre());
            reunionFirma.setCargoAlternativoResponsableActa(responsable.getCargoNombreAlternativo());
        }

        List<Organo> organos = organoService.getOrganosByReunionIdAndUserId(reunion.getId(), connectedUserId);
        List<ReunionComentario> comentarios =
                reunionComentarioService.getComentariosByReunionId(reunion.getId(), connectedUserId);

        List<OrganoFirma> listaOrganosFirma = getOrganosFirmaDesdeOrganos(organos, reunion);
        reunionFirma.setOrganos(listaOrganosFirma);

        List<ReunionDocumento> reunionDocumentos = reunionDocumentoDAO.getDocumentosByReunionId(reunion.getId());
        List<Documento> listaDocumentosFirma = getReunionDocumentosFirmaDesdeDocumentos(reunionDocumentos);

        reunionFirma.setDocumentos(listaDocumentosFirma);
        reunionFirma.setComentarios(getComentariosFirmaDesdeComentarios(comentarios));

        List<PuntoOrdenDia> puntosOrdenDia = puntoOrdenDiaDAO.getPuntosByReunionId(reunion.getId());
        List<PuntoOrdenDiaFirma> listaPuntosOrdenDiaFirma = getPuntosOrdenDiaFirmaDesdePuntosOrdenDia(puntosOrdenDia);
        reunionFirma.setPuntosOrdenDia(listaPuntosOrdenDiaFirma);

        return reunionFirma;
    }

    private List<PuntoOrdenDiaFirma> getPuntosOrdenDiaFirmaDesdePuntosOrdenDia(List<PuntoOrdenDia> puntosOrdenDia)
    {
        List<PuntoOrdenDiaFirma> listaPuntosOrdenDiaFirma = new ArrayList<>();

        for (PuntoOrdenDia puntoOrdenDia : puntosOrdenDia)
        {
            listaPuntosOrdenDiaFirma.add(getPuntoOrdenDiaFirmaDesdePuntoOrdenDia(puntoOrdenDia));
        }
        return listaPuntosOrdenDiaFirma;
    }

    private PuntoOrdenDiaFirma getPuntoOrdenDiaFirmaDesdePuntoOrdenDia(PuntoOrdenDia puntoOrdenDia)
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

        puntoOrdenDiaFirma.setDocumentos(getDocumentosFirmaDesdePuntosOrdenDiaDocumentos(documentos));

        return puntoOrdenDiaFirma;
    }

    private List<Documento> getDocumentosFirmaDesdePuntosOrdenDiaDocumentos(List<PuntoOrdenDiaDocumento> documentos)
    {
        List<Documento> listaDocumentoTemplate = new ArrayList<>();

        for (PuntoOrdenDiaDocumento puntoOrdenDiaDocumento : documentos)
        {
            listaDocumentoTemplate.add(getDocumentoFirmaDesdePuntoOrdenDiaDocumento(puntoOrdenDiaDocumento));
        }

        return listaDocumentoTemplate;
    }

    private Documento getDocumentoFirmaDesdePuntoOrdenDiaDocumento(PuntoOrdenDiaDocumento puntoOrdenDiaDocumento)
    {
        Documento documentoFirma = new Documento();

        documentoFirma.setId(puntoOrdenDiaDocumento.getId());
        documentoFirma.setDescripcion(puntoOrdenDiaDocumento.getDescripcion());
        documentoFirma.setDescripcionAlternativa(puntoOrdenDiaDocumento.getDescripcionAlternativa());
        documentoFirma.setMimeType(puntoOrdenDiaDocumento.getMimeType());
        documentoFirma.setFechaAdicion(puntoOrdenDiaDocumento.getFechaAdicion());
        documentoFirma.setCreadorId(puntoOrdenDiaDocumento.getCreadorId());
        documentoFirma.setNombreFichero(puntoOrdenDiaDocumento.getNombreFichero());
        documentoFirma.setDatosBase64(new String(Base64.encodeBase64(puntoOrdenDiaDocumento.getDatos())));

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

    private List<Documento> getReunionDocumentosFirmaDesdeDocumentos(List<ReunionDocumento> reunionDocumentos)
    {
        List<Documento> listaDocumentoFirma = new ArrayList<>();

        for (ReunionDocumento reunionDocumento : reunionDocumentos)
        {
            listaDocumentoFirma.add(getDocumentoFirmaDesdeReunionDocumento(reunionDocumento));
        }

        return listaDocumentoFirma;
    }

    private Documento getDocumentoFirmaDesdeReunionDocumento(ReunionDocumento reunionDocumento)
    {
        Documento documentoFirma = new Documento();

        documentoFirma.setId(reunionDocumento.getId());
        documentoFirma.setDescripcion(reunionDocumento.getDescripcion());
        documentoFirma.setDescripcionAlternativa(reunionDocumento.getDescripcionAlternativa());
        documentoFirma.setMimeType(reunionDocumento.getMimeType());
        documentoFirma.setFechaAdicion(reunionDocumento.getFechaAdicion());
        documentoFirma.setCreadorId(reunionDocumento.getCreadorId());
        documentoFirma.setNombreFichero(reunionDocumento.getNombreFichero());
        documentoFirma.setDatosBase64(new String(Base64.encodeBase64(reunionDocumento.getDatos())));

        return documentoFirma;
    }

    public ReunionTemplate getReunionTemplateDesdeReunion(Reunion reunion, Long connectedUserId)
    {
        ReunionTemplate reunionTemplate = new ReunionTemplate();

        reunionTemplate.setId(reunion.getId());
        reunionTemplate.setAsunto(reunion.getAsunto());
        reunionTemplate.setAsuntoAlternativo(reunion.getAsuntoAlternativo());
        reunionTemplate.setDescripcion(reunion.getDescripcion());
        reunionTemplate.setDescripcionAlternativa(reunion.getDescripcionAlternativa());
        reunionTemplate.setDuracion(reunion.getDuracion());
        reunionTemplate.setNumeroSesion(reunion.getNumeroSesion());
        reunionTemplate.setAcuerdos(reunion.getAcuerdos());
        reunionTemplate.setAcuerdosAlternativos(reunion.getAcuerdosAlternativos());
        reunionTemplate.setUbicacion(reunion.getUbicacion());
        reunionTemplate.setUbicacionAlternativa(reunion.getUbicacionAlternativa());
        reunionTemplate.setFecha(reunion.getFecha());
        reunionTemplate.setFechaSegundaConvocatoria(reunion.getFechaSegundaConvocatoria());
        reunionTemplate.setUrlGrabacion(reunion.getUrlGrabacion());
        reunionTemplate.setTelematica(reunion.isTelematica());
        reunionTemplate.setTelematicaDescripcion(reunion.getTelematicaDescripcion());
        reunionTemplate.setTelematicaDescripcionAlternativa(reunion.getTelematicaDescripcionAlternativa());
        reunionTemplate.setAdmiteSuplencia(reunion.isAdmiteSuplencia());
        reunionTemplate.setAdmiteComentarios(reunion.isAdmiteComentarios());
        reunionTemplate.setCompletada(reunion.getCompletada());
        reunionTemplate.setCreadorNombre(reunion.getCreadorNombre());
        reunionTemplate.setCreadorEmail(reunion.getCreadorEmail());
        reunionTemplate.setCreadorId(reunion.getCreadorId());

        if (reunion.getMiembroResponsableActa() != null)
        {
            OrganoReunionMiembro responsable =
                    organoReunionMiembroDAO.getMiembroById(reunion.getMiembroResponsableActa().getId());

            reunionTemplate.setResponsableActa(responsable.getNombre());
            reunionTemplate.setCargoResponsableActa(responsable.getCargoNombre());
            reunionTemplate.setCargoAlternativoResponsableActa(responsable.getCargoNombreAlternativo());
        }

        List<Organo> organos = organoService.getOrganosByReunionIdAndUserId(reunion.getId(), connectedUserId);
        List<ReunionComentario> comentarios =
                reunionComentarioService.getComentariosByReunionId(reunion.getId(), connectedUserId);

        List<OrganoTemplate> listaOrganosTemplate = getOrganosTemplateDesdeOrganos(organos, reunion);
        reunionTemplate.setOrganos(listaOrganosTemplate);

        List<ReunionDocumento> reunionDocumentos = reunionDocumentoDAO.getDocumentosByReunionId(reunion.getId());
        List<Documento> listaDocumentosTemplate = getReunionDocumentosTemplateDesdeDocumentos(reunionDocumentos);

        reunionTemplate.setDocumentos(listaDocumentosTemplate);
        reunionTemplate.setComentarios(getComentariosTemplateDessdeComentarios(comentarios));

        List<PuntoOrdenDia> puntosOrdenDia = puntoOrdenDiaDAO.getPuntosByReunionId(reunion.getId());
        List<PuntoOrdenDiaTemplate> listaPuntosOrdenDiaTemplate =
                getPuntosOrdenDiaTemplateDesdePuntosOrdenDia(puntosOrdenDia);
        reunionTemplate.setPuntosOrdenDia(listaPuntosOrdenDiaTemplate);

        List<ReunionInvitado> invitados = reunionInvitadoDAO.getInvitadosByReunionId(reunion.getId());
        List<InvitadoTemplate> invitadosTemplate =
                getInvitadosTemplateDesdeReunionInvitados(invitados);
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

    private List<InvitadoTemplate> getInvitadosTemplateDesdeReunionInvitados(List<ReunionInvitado> invitados)
    {
        List<InvitadoTemplate> listaInvitadosTemplate = new ArrayList<>();

        for (ReunionInvitado invitado : invitados)
        {
            listaInvitadosTemplate.add(getInvitadoTemplateDessdeInvitado(invitado));
        }

        return listaInvitadosTemplate;
    }

    private InvitadoTemplate getInvitadoTemplateDessdeInvitado(ReunionInvitado invitado)
    {
        InvitadoTemplate invitadoTemplate = new InvitadoTemplate();

        invitadoTemplate.setId(invitado.getPersonaId());
        invitadoTemplate.setNombre(invitado.getPersonaNombre());
        invitadoTemplate.setEmail(invitado.getPersonaEmail());

        return invitadoTemplate;
    }

    private List<OrganoTemplate> getOrganosTemplateDesdeOrganos(List<Organo> organos, Reunion reunion)
    {
        List<OrganoTemplate> listaOrganoTemplate = new ArrayList<>();

        for (Organo organo : organos)
        {
            listaOrganoTemplate.add(getOrganoTemplateDesdeOrgano(organo, reunion));
        }

        return listaOrganoTemplate;
    }

    private OrganoTemplate getOrganoTemplateDesdeOrgano(Organo organo, Reunion reunion)
    {
        OrganoTemplate organoTemplate = new OrganoTemplate();
        organoTemplate.setId(organo.getId());
        organoTemplate.setNombre(organo.getNombre());
        organoTemplate.setNombreAlternativo(organo.getNombreAlternativo());
        organoTemplate.setTipoCodigo(organo.getTipoOrgano().getCodigo());
        organoTemplate.setTipoNombre(organo.getTipoOrgano().getNombre());
        organoTemplate.setTipoNombreAlternativo(organo.getTipoOrgano().getNombreAlternativo());
        organoTemplate.setTipoOrganoId(organo.getTipoOrgano().getId());

        List<OrganoReunionMiembro> listaAsistentes =
                organoReunionMiembroDAO.getAsistenteReunionByOrganoAndReunionId(organo.getId(), organo.isExterno(),
                        reunion.getId());

        organoTemplate.setAsistentes(getAsistentesDesdeListaOrganoReunionMiembro(listaAsistentes));
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

        organoFirma.setAsistentes(getAsistentesFirmaDesdeListaOrganoReunionMiembro(listaAsistentes));

        return organoFirma;
    }

    private List<MiembroFirma> getAsistentesFirmaDesdeListaOrganoReunionMiembro(
            List<OrganoReunionMiembro> listaAsistentes)
    {
        List<MiembroFirma> listaMiembroFirma = new ArrayList<>();

        for (OrganoReunionMiembro organoReunionMiembro : listaAsistentes)
        {
            listaMiembroFirma.add(getAsistenteFirmaDesdeOrganoReunionMiembro(organoReunionMiembro));
        }
        return listaMiembroFirma;
    }

    private MiembroFirma getAsistenteFirmaDesdeOrganoReunionMiembro(OrganoReunionMiembro organoReunionMiembro)
    {
        MiembroFirma miembroFirma = new MiembroFirma();
        miembroFirma.setNombre(organoReunionMiembro.getNombre());
        miembroFirma.setEmail(organoReunionMiembro.getEmail());
        miembroFirma.setId(organoReunionMiembro.getMiembroId());
        miembroFirma.setSuplente(organoReunionMiembro.getSuplenteNombre());
        miembroFirma.setSuplenteId(organoReunionMiembro.getSuplenteId());
        miembroFirma.setAsistenciaConfirmada(organoReunionMiembro.getAsistenciaConfirmada());

        Cargo cargo = new Cargo();
        cargo.setId(organoReunionMiembro.getCargoId());
        cargo.setNombre(organoReunionMiembro.getCargoNombre());
        cargo.setNombreAlternativo(organoReunionMiembro.getCargoNombreAlternativo());

        miembroFirma.setCargo(cargo);

        return miembroFirma;
    }

    private List<MiembroTemplate> getAsistentesDesdeListaOrganoReunionMiembro(
            List<OrganoReunionMiembro> listaAsistentes)
    {
        List<MiembroTemplate> listaMiembroTemplate = new ArrayList<>();

        for (OrganoReunionMiembro organoReunionMiembro : listaAsistentes)
        {
            listaMiembroTemplate.add(getAsistenteDesdeOrganoReunionMiembro(organoReunionMiembro));
        }

        return listaMiembroTemplate;
    }

    private MiembroTemplate getAsistenteDesdeOrganoReunionMiembro(OrganoReunionMiembro organoReunionMiembro)
    {
        MiembroTemplate miembroTemplate = new MiembroTemplate();
        miembroTemplate.setNombre(organoReunionMiembro.getNombre());
        miembroTemplate.setEmail(organoReunionMiembro.getEmail());
        miembroTemplate.setId(organoReunionMiembro.getId().toString());
        miembroTemplate.setMiembroId(organoReunionMiembro.getMiembroId());
        miembroTemplate.setSuplente(organoReunionMiembro.getSuplenteNombre());
        miembroTemplate.setSuplenteId(organoReunionMiembro.getSuplenteId());
        miembroTemplate.setAsistenciaConfirmada(organoReunionMiembro.getAsistenciaConfirmada());
        miembroTemplate.setAsistencia(organoReunionMiembro.getAsistencia());

        Cargo cargo = new Cargo();
        cargo.setId(organoReunionMiembro.getCargoId());
        cargo.setNombre(organoReunionMiembro.getCargoNombre());
        cargo.setNombreAlternativo(organoReunionMiembro.getCargoNombreAlternativo());

        miembroTemplate.setCargo(cargo);

        return miembroTemplate;
    }

    private List<Documento> getReunionDocumentosTemplateDesdeDocumentos(List<ReunionDocumento> reunionDocumentos)
    {
        List<Documento> listaDocumento = new ArrayList<>();

        for (ReunionDocumento reunionDocumento : reunionDocumentos)
        {
            listaDocumento.add(getDocumentoTemplateDesdeReunionDocumento(reunionDocumento));
        }
        return listaDocumento;
    }

    private Documento getDocumentoTemplateDesdeReunionDocumento(ReunionDocumento reunionDocumento)
    {
        Documento documento = new Documento();
        documento.setId(reunionDocumento.getId());
        documento.setDescripcion(reunionDocumento.getDescripcion());
        documento.setDescripcionAlternativa(reunionDocumento.getDescripcionAlternativa());
        documento.setMimeType(reunionDocumento.getMimeType());
        documento.setFechaAdicion(reunionDocumento.getFechaAdicion());
        documento.setCreadorId(reunionDocumento.getCreadorId());
        documento.setNombreFichero(reunionDocumento.getNombreFichero());

        return documento;
    }

    private List<PuntoOrdenDiaTemplate> getPuntosOrdenDiaTemplateDesdePuntosOrdenDia(List<PuntoOrdenDia> puntosOrdenDia)
    {
        List<PuntoOrdenDiaTemplate> listaPuntosOrdenDiaTemplate = new ArrayList<>();

        for (PuntoOrdenDia puntoOrdenDia : puntosOrdenDia)
        {
            listaPuntosOrdenDiaTemplate.add(getPuntoOrdenDiaTemplateDesdePuntoOrdenDia(puntoOrdenDia));
        }

        return listaPuntosOrdenDiaTemplate;
    }

    private PuntoOrdenDiaTemplate getPuntoOrdenDiaTemplateDesdePuntoOrdenDia(PuntoOrdenDia puntoOrdenDia)
    {
        PuntoOrdenDiaTemplate puntoOrdenDiaTemplate = new PuntoOrdenDiaTemplate();
        puntoOrdenDiaTemplate.setId(puntoOrdenDia.getId());
        puntoOrdenDiaTemplate.setOrden(puntoOrdenDia.getOrden());
        puntoOrdenDiaTemplate.setAcuerdos(puntoOrdenDia.getAcuerdos());
        puntoOrdenDiaTemplate.setAcuerdosAlternativos(puntoOrdenDia.getAcuerdosAlternativos());
        puntoOrdenDiaTemplate.setDeliberaciones(puntoOrdenDia.getDeliberaciones());
        puntoOrdenDiaTemplate.setDeliberacionesAlternativas(puntoOrdenDia.getDeliberacionesAlternativas());
        puntoOrdenDiaTemplate.setDescripcion(puntoOrdenDia.getDescripcion());
        puntoOrdenDiaTemplate.setDescripcionAlternativa(puntoOrdenDia.getDescripcionAlternativa());
        puntoOrdenDiaTemplate.setTitulo(puntoOrdenDia.getTitulo());
        puntoOrdenDiaTemplate.setTituloAlternativo(puntoOrdenDia.getTituloAlternativo());
        puntoOrdenDiaTemplate.setPublico(puntoOrdenDia.isPublico());

        List<PuntoOrdenDiaDocumento> documentos =
                puntoOrdenDiaDocumentoDAO.getDocumentosByPuntoOrdenDiaId(puntoOrdenDia.getId());

        List<PuntoOrdenDiaAcuerdo> acuerdos =
                puntoOrdenDiaAcuerdoDAO.getAcuerdosByPuntoOrdenDiaId(puntoOrdenDia.getId());

        List<PuntoOrdenDiaDescriptor> descriptores =
                puntoOrdenDiaDescriptorDAO.getDescriptoresOrdenDia(puntoOrdenDia.getId());

        puntoOrdenDiaTemplate.setDocumentos(getDocumentosTemplateDesdePuntosOrdenDiaDocumentos(documentos));
        puntoOrdenDiaTemplate.setDocumentosAcuerdos(getAcuerdosTemplateDesdePuntosOrdenDiaAcuerdos(acuerdos));
        puntoOrdenDiaTemplate.setDescriptores(getDescriptoresTemplateDesdePuntosOrdenDiaAcuerdos(descriptores));

        return puntoOrdenDiaTemplate;
    }

    private List<Documento> getDocumentosTemplateDesdePuntosOrdenDiaDocumentos(List<PuntoOrdenDiaDocumento> documentos)
    {
        List<Documento> listaDocumento = new ArrayList<>();

        for (PuntoOrdenDiaDocumento puntoOrdenDiaDocumento : documentos)
        {
            listaDocumento.add(getDocumentoTemplateDesdePuntoOrdenDiaDocumento(puntoOrdenDiaDocumento));
        }

        return listaDocumento;
    }

    private List<Documento> getAcuerdosTemplateDesdePuntosOrdenDiaAcuerdos(List<PuntoOrdenDiaAcuerdo> acuerdos)
    {
        List<Documento> listaDocumento = new ArrayList<>();

        for (PuntoOrdenDiaAcuerdo puntoOrdenDiaAcuerdo : acuerdos)
        {
            listaDocumento.add(getAcuerdoTemplateDesdePuntoOrdenDiaAcuerdo(puntoOrdenDiaAcuerdo));
        }

        return listaDocumento;
    }

    private List<DescriptorTemplate> getDescriptoresTemplateDesdePuntosOrdenDiaAcuerdos(
            List<PuntoOrdenDiaDescriptor> descriptores)
    {
        List<DescriptorTemplate> listaDesciptores = new ArrayList<>();

        for (PuntoOrdenDiaDescriptor descriptor : descriptores)
        {
            listaDesciptores.add(getDescriptorTemplateDesdePuntoOrdenDiaAcuerdo(descriptor));
        }

        return listaDesciptores;
    }

    private Documento getDocumentoTemplateDesdePuntoOrdenDiaDocumento(PuntoOrdenDiaDocumento puntoOrdenDiaDocumento)
    {
        Documento documento = new Documento();
        documento.setId(puntoOrdenDiaDocumento.getId());
        documento.setDescripcion(puntoOrdenDiaDocumento.getDescripcion());
        documento.setDescripcionAlternativa(puntoOrdenDiaDocumento.getDescripcionAlternativa());
        documento.setMimeType(puntoOrdenDiaDocumento.getMimeType());
        documento.setFechaAdicion(puntoOrdenDiaDocumento.getFechaAdicion());
        documento.setCreadorId(puntoOrdenDiaDocumento.getCreadorId());
        documento.setNombreFichero(puntoOrdenDiaDocumento.getNombreFichero());

        return documento;
    }

    private Documento getAcuerdoTemplateDesdePuntoOrdenDiaAcuerdo(PuntoOrdenDiaAcuerdo puntoOrdenDiaAcuerdo)
    {
        Documento documento = new Documento();

        documento.setId(puntoOrdenDiaAcuerdo.getId());
        documento.setDescripcion(puntoOrdenDiaAcuerdo.getDescripcion());
        documento.setDescripcionAlternativa(puntoOrdenDiaAcuerdo.getDescripcionAlternativa());
        documento.setMimeType(puntoOrdenDiaAcuerdo.getMimeType());
        documento.setFechaAdicion(puntoOrdenDiaAcuerdo.getFechaAdicion());
        documento.setCreadorId(puntoOrdenDiaAcuerdo.getCreadorId());
        documento.setNombreFichero(puntoOrdenDiaAcuerdo.getNombreFichero());

        return documento;
    }

    private DescriptorTemplate getDescriptorTemplateDesdePuntoOrdenDiaAcuerdo(PuntoOrdenDiaDescriptor descriptor)
    {
        DescriptorTemplate descriptorTemplate = new DescriptorTemplate();

        descriptorTemplate.setId(descriptor.getId());
        descriptorTemplate.setPuntoOrdenDiaId(descriptor.getPuntoOrdenDia().getId());
        descriptorTemplate.setClaveId(descriptor.getClave().getId());
        descriptorTemplate.setDescriptorId(descriptor.getClave().getDescriptor().getId());
        descriptorTemplate.setDescriptorNombre(descriptor.getClave().getDescriptor().getDescriptor());
        descriptorTemplate.setDescriptorNombreAlternativo(
                descriptor.getClave().getDescriptor().getDescriptorAlternativo());
        descriptorTemplate.setDescriptorDescripcion(descriptor.getClave().getDescriptor().getDescripcion());
        descriptorTemplate.setDescriptorDescripcionAlternativa(
                descriptor.getClave().getDescriptor().getDescripcionAlternativa());
        descriptorTemplate.setClaveNombre(descriptor.getClave().getClave());
        descriptorTemplate.setClaveNombreAlternativo(descriptor.getClave().getClaveAlternativa());

        return descriptorTemplate;
    }

    public Reunion getReunionConOrganosById(Long reunionId, Long connectedUserId)
    {
        return reunionDAO.getReunionConOrganosById(reunionId);
    }

    public List<ReunionTemplate> getReunionesAccesiblesByPersonaId(Long connectedUserId)
    {
        List<Reunion> reunionesAccesibles = reunionDAO.getReunionesAccesiblesByPersonaId(connectedUserId);

        reunionesAccesibles = filtrarDuplicadosReuniones(reunionesAccesibles);

        return reunionesAccesibles.stream().map(r -> getReunionTemplateDesdeReunion(r, connectedUserId)).map(r ->
        {
            r.setComoAsistente(r.esAsistente(connectedUserId));
            return r;
        }).collect(Collectors.toList());
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

    public String enviarConvocatoria(Long reunionId)
            throws MiembrosExternosException, ReunionNoDisponibleException, NotificacionesException
    {
        Reunion reunion = reunionDAO.getReunionConMiembrosAndPuntosDiaById(reunionId);

        String error = checkAllFeaturesOfReunion(reunion);
        if (error != null) return error;

        avisosReunion.enviaAvisoNuevaReunion(reunion);

        reunion.setAvisoPrimeraReunion(true);
        reunionDAO.update(reunion);

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
                        acuerdosSearch.getIdiomaAlternatico(), acuerdosSearch.getStartSearch(),
                        acuerdosSearch.getNumResults());
        return filtrarDuplicadosReuniones(reunionesPublicas);
    }

    public Integer getNumReunionesPublicas(AcuerdosSearch acuerdosSearch)
    {
        List<Reunion> reunionesPublicasClave =
                reunionDAO.getReunionesPublicas(acuerdosSearch.getTipoOrganoId(), acuerdosSearch.getOrganoId(),
                        acuerdosSearch.getDescriptorId(), acuerdosSearch.getClaveId(), acuerdosSearch.getAnyo(),
                        acuerdosSearch.getfInicio(), acuerdosSearch.getfFin(), acuerdosSearch.getTexto(),
                        acuerdosSearch.getIdiomaAlternatico());
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
}