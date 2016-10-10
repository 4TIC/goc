package es.uji.apps.goc.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.goc.dao.MiembroDAO;
import es.uji.apps.goc.dao.OrganoReunionDAO;
import es.uji.apps.goc.dao.OrganoReunionMiembroDAO;
import es.uji.apps.goc.dao.PuntoOrdenDiaDAO;
import es.uji.apps.goc.dao.PuntoOrdenDiaDocumentoDAO;
import es.uji.apps.goc.dao.ReunionDAO;
import es.uji.apps.goc.dao.ReunionDocumentoDAO;
import es.uji.apps.goc.dto.MiembroFirma;
import es.uji.apps.goc.dto.MiembroTemplate;
import es.uji.apps.goc.dto.OrganoFirma;
import es.uji.apps.goc.dto.OrganoLocal;
import es.uji.apps.goc.dto.OrganoReunion;
import es.uji.apps.goc.dto.OrganoReunionMiembro;
import es.uji.apps.goc.dto.OrganoTemplate;
import es.uji.apps.goc.dto.PuntoOrdenDia;
import es.uji.apps.goc.dto.PuntoOrdenDiaDocumento;
import es.uji.apps.goc.dto.PuntoOrdenDiaFirma;
import es.uji.apps.goc.dto.PuntoOrdenDiaTemplate;
import es.uji.apps.goc.dto.QReunion;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.dto.ReunionComentario;
import es.uji.apps.goc.dto.ReunionDocumento;
import es.uji.apps.goc.dto.ReunionFirma;
import es.uji.apps.goc.dto.ReunionTemplate;
import es.uji.apps.goc.exceptions.FirmaReunionException;
import es.uji.apps.goc.exceptions.MiembrosExternosException;
import es.uji.apps.goc.exceptions.OrganosExternosException;
import es.uji.apps.goc.exceptions.PersonasExternasException;
import es.uji.apps.goc.exceptions.ReunionNoDisponibleException;
import es.uji.apps.goc.exceptions.ReunionYaCompletadaException;
import es.uji.apps.goc.model.Cargo;
import es.uji.apps.goc.model.Comentario;
import es.uji.apps.goc.model.Documento;
import es.uji.apps.goc.model.Miembro;
import es.uji.apps.goc.model.Organo;
import es.uji.apps.goc.model.Persona;

import static es.uji.apps.goc.dto.QReunion.reunion;

@Service
@Component
public class ReunionService
{
    @Autowired
    private ReunionDAO reunionDAO;

    @Autowired
    private OrganoReunionDAO organoReunionDAO;

    @Autowired
    private OrganoReunionMiembroService organoReunionMiembroService;

    @Autowired
    private ReunionComentarioService reunionComentarioService;

    @Value("${goc.external.firmasEndpoint}")
    private String firmasEndpoint;

    @Value("${goc.external.authToken}")
    private String authToken;

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

    public List<Reunion> getReunionesByUserId(Boolean completada, Long connectedUserId)
    {
        if (completada)
        {
            return reunionDAO.getReunionesCompletadasByUserId(connectedUserId);
        }

        return reunionDAO.getReunionesByUserId(connectedUserId);
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
        reunion.setFechaCreacion(new Date());

        return reunionDAO.insert(reunion);
    }

    public Reunion updateReunion(Long reunionId, String asunto, String descripcion, Long duracion,
                                 Date fecha, String ubicacion, String urlGrabacion, Long numeroSesion, Boolean publica,
                                 Boolean telematica, String telematicaDescripcion, Long connectedUserId)
            throws ReunionNoDisponibleException
    {
        Reunion reunion = reunionDAO.getReunionConOrganosById(reunionId);

        if (reunion == null)
        {
            throw new ReunionNoDisponibleException();
        }

        reunion.setAsunto(asunto);
        reunion.setDescripcion(descripcion);
        reunion.setDuracion(duracion);
        reunion.setUbicacion(ubicacion);
        reunion.setUrlGrabacion(urlGrabacion);
        reunion.setFecha(fecha);
        reunion.setNumeroSesion(numeroSesion);
        reunion.setPublica(publica);
        reunion.setTelematica(telematica);
        reunion.setTelematicaDescripcion(telematicaDescripcion);

        return reunionDAO.update(reunion);
    }

    public void removeReunionById(Long reunionId, Long connectedUserId)
            throws ReunionNoDisponibleException
    {
        Reunion reunion = reunionDAO.getReunionConOrganosById(reunionId);

        if (reunion == null)
        {
            throw new ReunionNoDisponibleException();
        }

        reunionDAO.delete(Reunion.class, reunionId);
    }

    @Transactional
    public void updateOrganosReunionByReunionId(Long reunionId, List<Organo> organos,
                                                Long connectedUserId)
            throws ReunionNoDisponibleException, MiembrosExternosException
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

    private void addOrganosExternosNoExistentes(Reunion reunion, List<String> listaIdsExternos,
                                                Long connectedUserId) throws MiembrosExternosException
    {
        List<String> listaActualOrganosExternosIds = reunion.getReunionOrganos().stream()
                .filter(el -> el.getOrganoExternoId() != null)
                .map(elem -> elem.getOrganoExternoId()).collect(Collectors.toList());

        for (String organoId : listaIdsExternos)
        {
            if (!listaActualOrganosExternosIds.contains(organoId))
            {
                OrganoReunion organoReunion = new OrganoReunion();
                organoReunion.setReunion(reunion);
                organoReunion.setOrganoExternoId(organoId);
                organoReunionDAO.insert(organoReunion);

                organoReunionMiembroService.addOrganoReunionMiembros(organoReunion,
                        connectedUserId);
            }
        }
    }

    private void addOrganosLocalesNoExistentes(Reunion reunion, List<Long> listaIdsLocales,
                                               Long connectedUserId) throws MiembrosExternosException
    {
        List<Long> listaActualOrganosLocalesIds = reunion.getReunionOrganos().stream()
                .filter(el -> el.getOrganoLocal() != null)
                .map(elem -> elem.getOrganoLocal().getId()).collect(Collectors.toList());

        for (Long organoId : listaIdsLocales)
        {
            if (!listaActualOrganosLocalesIds.contains(organoId))
            {
                OrganoReunion organoReunion = new OrganoReunion();
                OrganoLocal organo = new OrganoLocal(organoId);
                organoReunion.setReunion(reunion);
                organoReunion.setOrganoLocal(organo);
                organoReunionDAO.insert(organoReunion);

                organoReunionMiembroService.addOrganoReunionMiembros(organoReunion,
                        connectedUserId);
            }
        }
    }

    private void borraOrganosNoNecesarios(Reunion reunion, List<String> listaIdsExternos,
                                          List<Long> listaIdsLocales)
    {
        for (OrganoReunion cr : reunion.getReunionOrganos())
        {
            if (cr.getOrganoLocal() != null
                    && !listaIdsLocales.contains(cr.getOrganoLocal().getId()))
            {
                organoReunionDAO.delete(OrganoReunion.class, cr.getId());
            }

            if (cr.getOrganoExternoId() != null
                    && !listaIdsExternos.contains(cr.getOrganoExternoId()))
            {
                organoReunionDAO.delete(OrganoReunion.class, cr.getId());
            }

        }
    }

    public void firmarReunion(Long reunionId, String acuerdos, Long responsableActaId,
                              Long connectedUserId) throws ReunionYaCompletadaException, FirmaReunionException,
            OrganosExternosException, PersonasExternasException
    {
        Reunion reunion = reunionDAO.getReunionConOrganosById(reunionId);

        if (reunion.getAcuerdos() != null)
        {
            throw new ReunionYaCompletadaException();
        }

        reunion.setCompletada(true);
        reunion.setAcuerdos(acuerdos);

        ReunionFirma reunionFirma = reunionFirmaDesdeReunion(reunion, connectedUserId);
        WebResource getFirmasResource = Client.create().resource(this.firmasEndpoint);
        ClientResponse response = getFirmasResource.type(MediaType.APPLICATION_JSON)
                .header("X-UJI-AuthToken", authToken).post(ClientResponse.class, reunionFirma);

        if (response.getStatus() != 204)
        {
            throw new FirmaReunionException();
        }

        reunionDAO.marcarReunionComoCompletadaYActualizarAcuerdo(reunionId, responsableActaId,
                acuerdos);
    }

    private ReunionFirma reunionFirmaDesdeReunion(Reunion reunion, Long connectedUserId)
            throws OrganosExternosException, PersonasExternasException
    {
        ReunionFirma reunionFirma = new ReunionFirma();

        reunionFirma.setId(reunion.getId());
        reunionFirma.setAsunto(reunion.getAsunto());
        reunionFirma.setDescripcion(reunion.getDescripcion());
        reunionFirma.setDuracion(reunion.getDuracion());
        reunionFirma.setNumeroSesion(reunion.getNumeroSesion());
        reunionFirma.setAcuerdos(reunion.getAcuerdos());
        reunionFirma.setUbicacion(reunion.getUbicacion());
        reunionFirma.setFecha(reunion.getFecha());
        reunionFirma.setUrlGrabacion(reunion.getUrlGrabacion());
        reunionFirma.setTelematica(reunion.isTelematica());
        reunionFirma.setTelematicaDescripcion(reunion.getTelematicaDescripcion());
        reunionFirma.setCompletada(reunion.getCompletada());
        reunionFirma.setCreadorNombre(reunion.getCreadorNombre());
        reunionFirma.setCreadorEmail(reunion.getCreadorEmail());

        List<Organo> organos = organoService.getOrganosByReunionIdAndUserId(reunion.getId(),
                connectedUserId);
        List<ReunionComentario> comentarios = reunionComentarioService
                .getComentariosByReunionId(reunion.getId(), connectedUserId);

        List<OrganoFirma> listaOrganosFirma = getOrganosFirmaDesdeOrganos(organos, reunion);
        reunionFirma.setOrganos(listaOrganosFirma);

        List<ReunionDocumento> reunionDocumentos = reunionDocumentoDAO
                .getDocumentosByReunionId(reunion.getId());
        List<Documento> listaDocumentosFirma = getReunionDocumentosFirmaDesdeDocumentos(
                reunionDocumentos);

        reunionFirma.setDocumentos(listaDocumentosFirma);
        reunionFirma.setComentarios(getComentariosFirmaDesdeComentarios(comentarios));

        List<PuntoOrdenDia> puntosOrdenDia = puntoOrdenDiaDAO.getPuntosByReunionId(reunion.getId());
        List<PuntoOrdenDiaFirma> listaPuntosOrdenDiaFirma = getPuntosOrdenDiaFirmaDesdePuntosOrdenDia(
                puntosOrdenDia);
        reunionFirma.setPuntosOrdenDia(listaPuntosOrdenDiaFirma);

        return reunionFirma;
    }

    private List<PuntoOrdenDiaFirma> getPuntosOrdenDiaFirmaDesdePuntosOrdenDia(
            List<PuntoOrdenDia> puntosOrdenDia)
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
        puntoOrdenDiaFirma.setOrden(puntoOrdenDia.getOrden());
        puntoOrdenDiaFirma.setAcuerdos(puntoOrdenDia.getAcuerdos());
        puntoOrdenDiaFirma.setDeliberaciones(puntoOrdenDia.getDeliberaciones());
        puntoOrdenDiaFirma.setDescripcion(puntoOrdenDia.getDescripcion());
        puntoOrdenDiaFirma.setTitulo(puntoOrdenDia.getTitulo());

        List<PuntoOrdenDiaDocumento> documentos = puntoOrdenDiaDocumentoDAO
                .getDocumentosByPuntoOrdenDiaId(puntoOrdenDia.getId());

        puntoOrdenDiaFirma
                .setDocumentos(getDocumentosFirmaDesdePuntosOrdenDiaDocumentos(documentos));
        return puntoOrdenDiaFirma;
    }

    private List<Documento> getDocumentosFirmaDesdePuntosOrdenDiaDocumentos(
            List<PuntoOrdenDiaDocumento> documentos)
    {
        List<Documento> listaDocumentoTemplate = new ArrayList<>();

        for (PuntoOrdenDiaDocumento puntoOrdenDiaDocumento : documentos)
        {
            listaDocumentoTemplate
                    .add(getDocumentoFirmaDesdePuntoOrdenDiaDocumento(puntoOrdenDiaDocumento));
        }
        return listaDocumentoTemplate;
    }

    private Documento getDocumentoFirmaDesdePuntoOrdenDiaDocumento(
            PuntoOrdenDiaDocumento puntoOrdenDiaDocumento)
    {
        Documento documentoFirma = new Documento();

        documentoFirma.setId(puntoOrdenDiaDocumento.getId());
        documentoFirma.setDescripcion(puntoOrdenDiaDocumento.getDescripcion());
        documentoFirma.setMimeType(puntoOrdenDiaDocumento.getMimeType());
        documentoFirma.setFechaAdicion(puntoOrdenDiaDocumento.getFechaAdicion());
        documentoFirma.setCreadorId(puntoOrdenDiaDocumento.getCreadorId());
        documentoFirma.setNombreFichero(puntoOrdenDiaDocumento.getNombreFichero());
        documentoFirma
                .setDatosBase64(new String(Base64.encodeBase64(puntoOrdenDiaDocumento.getDatos())));

        return documentoFirma;
    }

    private List<Comentario> getComentariosFirmaDesdeComentarios(
            List<ReunionComentario> comentarios)
    {
        List<Comentario> listaComentariosFirma = new ArrayList<>();

        for (ReunionComentario comentario : comentarios)
        {
            listaComentariosFirma.add(getComentarioFirmaDessdeComentario(comentario));
        }

        return listaComentariosFirma;
    }

    private Comentario getComentarioFirmaDessdeComentario(ReunionComentario comentario)
    {
        Comentario comentarioFirma = new Comentario();
        comentarioFirma.setId(comentario.getId());
        comentarioFirma.setComentario(comentario.getComentario());
        comentarioFirma.setCreadorNombre(comentario.getCreadorNombre());
        comentarioFirma.setCreadorId(comentario.getCreadorId());
        return comentarioFirma;
    }

    private List<Documento> getReunionDocumentosFirmaDesdeDocumentos(
            List<ReunionDocumento> reunionDocumentos)
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
        documentoFirma.setMimeType(reunionDocumento.getMimeType());
        documentoFirma.setFechaAdicion(reunionDocumento.getFechaAdicion());
        documentoFirma.setCreadorId(reunionDocumento.getCreadorId());
        documentoFirma.setNombreFichero(reunionDocumento.getNombreFichero());
        documentoFirma.setDatosBase64(new String(Base64.encodeBase64(reunionDocumento.getDatos())));

        return documentoFirma;
    }

    public List<Reunion> getReunionesByOrganoIdAndUserId(String organoId, Boolean externo, Boolean completada,
                                                         Long connectedUserId)
    {
        if (externo)
        {
            if (completada)
            {
                return reunionDAO.getReunionesCompletadasByOrganoExternoIdAndUserId(organoId, connectedUserId);
            }
            return reunionDAO.getReunionesByOrganoExternoIdAndUserId(organoId, connectedUserId);
        }
        else
        {
            if (completada)
            {
                return reunionDAO.getReunionesCompletadasByOrganoLocalIdAndUserId(Long.parseLong(organoId),
                        connectedUserId);
            }
            return reunionDAO.getReunionesByOrganoLocalIdAndUserId(Long.parseLong(organoId),
                    connectedUserId);
        }
    }

    public ReunionTemplate getReunionTemplateDesdeReunion(Reunion reunion, Long connectedUserId)
            throws OrganosExternosException, PersonasExternasException
    {
        ReunionTemplate reunionTemplate = new ReunionTemplate();

        reunionTemplate.setId(reunion.getId());
        reunionTemplate.setAsunto(reunion.getAsunto());
        reunionTemplate.setDescripcion(reunion.getDescripcion());
        reunionTemplate.setDuracion(reunion.getDuracion());
        reunionTemplate.setNumeroSesion(reunion.getNumeroSesion());
        reunionTemplate.setAcuerdos(reunion.getAcuerdos());
        reunionTemplate.setUbicacion(reunion.getUbicacion());
        reunionTemplate.setFecha(reunion.getFecha());
        reunionTemplate.setUrlGrabacion(reunion.getUrlGrabacion());
        reunionTemplate.setTelematica(reunion.isTelematica());
        reunionTemplate.setTelematicaDescripcion(reunion.getTelematicaDescripcion());
        reunionTemplate.setCompletada(reunion.getCompletada());
        reunionTemplate.setCreadorNombre(reunion.getCreadorNombre());
        reunionTemplate.setCreadorEmail(reunion.getCreadorEmail());

        if (reunion.getMiembroResponsableActa() != null)
        {
            OrganoReunionMiembro responsable = organoReunionMiembroDAO
                    .getMiembroById(reunion.getMiembroResponsableActa().getId());

            reunionTemplate.setResponsableActa(
                    responsable.getNombre() + "(" + responsable.getCargoNombre() + ")");
        }

        List<Organo> organos = organoService.getOrganosByReunionIdAndUserId(reunion.getId(),
                connectedUserId);
        List<ReunionComentario> comentarios = reunionComentarioService
                .getComentariosByReunionId(reunion.getId(), connectedUserId);

        List<OrganoTemplate> listaOrganosTemplate = getOrganosTemplateDesdeOrganos(organos,
                reunion);
        reunionTemplate.setOrganos(listaOrganosTemplate);

        List<ReunionDocumento> reunionDocumentos = reunionDocumentoDAO
                .getDocumentosByReunionId(reunion.getId());
        List<Documento> listaDocumentosTemplate = getReunionDocumentosTemplateDesdeDocumentos(
                reunionDocumentos);

        reunionTemplate.setDocumentos(listaDocumentosTemplate);
        reunionTemplate.setComentarios(getComentariosTemplateDessdeComentarios(comentarios));

        List<PuntoOrdenDia> puntosOrdenDia = puntoOrdenDiaDAO.getPuntosByReunionId(reunion.getId());
        List<PuntoOrdenDiaTemplate> listaPuntosOrdenDiaTemplate = getPuntosOrdenDiaTemplateDesdePuntosOrdenDia(
                puntosOrdenDia);
        reunionTemplate.setPuntosOrdenDia(listaPuntosOrdenDiaTemplate);
        return reunionTemplate;
    }

    private List<Comentario> getComentariosTemplateDessdeComentarios(
            List<ReunionComentario> comentarios)
    {
        List<Comentario> listaComentariosTemplate = new ArrayList<>();

        for (ReunionComentario comentario : comentarios)
        {
            listaComentariosTemplate
                    .add(getComentarioTemplateDessdeComentario(comentario, reunion));
        }

        return listaComentariosTemplate;
    }

    private Comentario getComentarioTemplateDessdeComentario(ReunionComentario comentario,
                                                             QReunion reunion)
    {
        Comentario comentarioTemplate = new Comentario();
        comentarioTemplate.setId(comentario.getId());
        comentarioTemplate.setComentario(comentario.getComentario());
        comentarioTemplate.setFecha(comentario.getFecha());
        comentarioTemplate.setCreadorNombre(comentario.getCreadorNombre());
        comentarioTemplate.setCreadorId(comentario.getCreadorId());
        return comentarioTemplate;
    }

    private List<OrganoTemplate> getOrganosTemplateDesdeOrganos(List<Organo> organos,
                                                                Reunion reunion)
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
        organoTemplate.setTipoCodigo(organo.getTipoOrgano().getCodigo());
        organoTemplate.setTipoNombre(organo.getTipoOrgano().getNombre());
        organoTemplate.setTipoOrganoId(organo.getTipoOrgano().getId());

        List<OrganoReunionMiembro> listaAsistentes = organoReunionMiembroDAO
                .getAsistenteReunionByOrganoAndReunionId(organo.getId(), organo.isExterno(),
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
        organoFirma.setTipoCodigo(organo.getTipoOrgano().getCodigo());
        organoFirma.setTipoNombre(organo.getTipoOrgano().getNombre());
        organoFirma.setTipoOrganoId(organo.getTipoOrgano().getId());

        List<OrganoReunionMiembro> listaAsistentes = organoReunionMiembroDAO
                .getAsistenteReunionByOrganoAndReunionId(organo.getId(), organo.isExterno(),
                        reunion.getId());

        organoFirma
                .setAsistentes(getAsistentesFirmaDesdeListaOrganoReunionMiembro(listaAsistentes));
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

    private MiembroFirma getAsistenteFirmaDesdeOrganoReunionMiembro(
            OrganoReunionMiembro organoReunionMiembro)
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

    private MiembroTemplate getAsistenteDesdeOrganoReunionMiembro(
            OrganoReunionMiembro organoReunionMiembro)
    {
        MiembroTemplate miembroTemplate = new MiembroTemplate();

        miembroTemplate.setNombre(organoReunionMiembro.getNombre());
        miembroTemplate.setEmail(organoReunionMiembro.getEmail());
        miembroTemplate.setId(organoReunionMiembro.getId().toString());
        miembroTemplate.setSuplente(organoReunionMiembro.getSuplenteNombre());
        miembroTemplate.setSuplenteId(organoReunionMiembro.getSuplenteId());
        miembroTemplate.setAsistenciaConfirmada(organoReunionMiembro.getAsistenciaConfirmada());

        Cargo cargo = new Cargo();
        cargo.setId(organoReunionMiembro.getCargoId());
        cargo.setNombre(organoReunionMiembro.getCargoNombre());
        miembroTemplate.setCargo(cargo);
        return miembroTemplate;
    }

    private List<Documento> getReunionDocumentosTemplateDesdeDocumentos(
            List<ReunionDocumento> reunionDocumentos)
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
        documento.setMimeType(reunionDocumento.getMimeType());
        documento.setFechaAdicion(reunionDocumento.getFechaAdicion());
        documento.setCreadorId(reunionDocumento.getCreadorId());
        documento.setNombreFichero(reunionDocumento.getNombreFichero());

        return documento;
    }

    private List<PuntoOrdenDiaTemplate> getPuntosOrdenDiaTemplateDesdePuntosOrdenDia(
            List<PuntoOrdenDia> puntosOrdenDia)
    {
        List<PuntoOrdenDiaTemplate> listaPuntosOrdenDiaTemplate = new ArrayList<>();

        for (PuntoOrdenDia puntoOrdenDia : puntosOrdenDia)
        {
            listaPuntosOrdenDiaTemplate
                    .add(getPuntoOrdenDiaTemplateDesdePuntoOrdenDia(puntoOrdenDia));
        }
        return listaPuntosOrdenDiaTemplate;
    }

    private PuntoOrdenDiaTemplate getPuntoOrdenDiaTemplateDesdePuntoOrdenDia(
            PuntoOrdenDia puntoOrdenDia)
    {
        PuntoOrdenDiaTemplate puntoOrdenDiaTemplate = new PuntoOrdenDiaTemplate();
        puntoOrdenDiaTemplate.setId(puntoOrdenDia.getId());
        puntoOrdenDiaTemplate.setOrden(puntoOrdenDia.getOrden());
        puntoOrdenDiaTemplate.setAcuerdos(puntoOrdenDia.getAcuerdos());
        puntoOrdenDiaTemplate.setDeliberaciones(puntoOrdenDia.getDeliberaciones());
        puntoOrdenDiaTemplate.setDescripcion(puntoOrdenDia.getDescripcion());
        puntoOrdenDiaTemplate.setTitulo(puntoOrdenDia.getTitulo());

        List<PuntoOrdenDiaDocumento> documentos = puntoOrdenDiaDocumentoDAO
                .getDocumentosByPuntoOrdenDiaId(puntoOrdenDia.getId());

        puntoOrdenDiaTemplate
                .setDocumentos(getDocumentosTemplateDesdePuntosOrdenDiaDocumentos(documentos));
        return puntoOrdenDiaTemplate;
    }

    private List<Documento> getDocumentosTemplateDesdePuntosOrdenDiaDocumentos(
            List<PuntoOrdenDiaDocumento> documentos)
    {
        List<Documento> listaDocumento = new ArrayList<>();

        for (PuntoOrdenDiaDocumento puntoOrdenDiaDocumento : documentos)
        {
            listaDocumento
                    .add(getDocumentoTemplateDesdePuntoOrdenDiaDocumento(puntoOrdenDiaDocumento));
        }
        return listaDocumento;
    }

    private Documento getDocumentoTemplateDesdePuntoOrdenDiaDocumento(
            PuntoOrdenDiaDocumento puntoOrdenDiaDocumento)
    {
        Documento documento = new Documento();

        documento.setId(puntoOrdenDiaDocumento.getId());
        documento.setDescripcion(puntoOrdenDiaDocumento.getDescripcion());
        documento.setMimeType(puntoOrdenDiaDocumento.getMimeType());
        documento.setFechaAdicion(puntoOrdenDiaDocumento.getFechaAdicion());
        documento.setCreadorId(puntoOrdenDiaDocumento.getCreadorId());
        documento.setNombreFichero(puntoOrdenDiaDocumento.getNombreFichero());

        return documento;
    }

    private Map<String, List<Miembro>> getMapOrganosMiembros(List<Organo> organos,
                                                             Long connectedUserId) throws MiembrosExternosException
    {
        Map<String, List<Miembro>> mapOrganosMiembros = new HashMap<>();

        for (Organo organo : organos)
        {
            List<Miembro> miembros;

            if (organo.isExterno())
            {
                miembros = miembroDAO.getMiembrosExternos(organo.getId(), connectedUserId);
            }
            else
            {
                miembros = miembroDAO.getMiembrosByOrganoId(Long.parseLong(organo.getId()));
            }

            mapOrganosMiembros.put(organo.getId().toString(), miembros);
        }

        return mapOrganosMiembros;
    }

    public Reunion getReunionConOrganosById(Long reunionId, Long connectedUserId)
    {
        return reunionDAO.getReunionConOrganosById(reunionId);
    }

    public List<Reunion> getReunionesCompletadasByAsistenteIdOrSuplenteId(Long connectedUserId)
    {
        List<OrganoReunionMiembro> listaOrganosReunionMiembro = organoReunionMiembroDAO
                .getReunionesByAsistenteIdOrSuplenteId(connectedUserId);

        List<Long> reunionesIds = listaOrganosReunionMiembro.stream()
                .map(organoReunionMiembro -> organoReunionMiembro.getReunionId())
                .collect(Collectors.toList());

        return reunionDAO.getReunionesCompletadasByListaIds(reunionesIds);
    }

    public void compruebaReunionNoCompletada(Long reunionId) throws ReunionYaCompletadaException
    {
        Reunion reunion = reunionDAO.getReunionById(reunionId);

        if (reunion.getCompletada() != null && reunion.getCompletada())
        {
            throw new ReunionYaCompletadaException();
        }
    }

    public List<Reunion> getReunionesByTipoOrganoIdAndUserId(Long tipoOrganoId, Boolean completada,
                                                             Long connectedUserId) throws OrganosExternosException
    {
        List<Organo> listaOrganosExternos = organoService.getOrganosExternos();

        List<String> listaOrganosExternosIds = listaOrganosExternos.stream()
                .filter(organo -> organo.getTipoOrgano().getId().equals(tipoOrganoId))
                .map(organo -> organo.getId()).collect(Collectors.toList());

        List<Organo> listaOrganosLocales = organoService.getOrganosLocales(connectedUserId);
        List<Long> listaOrganosLocalesIds = listaOrganosLocales.stream()
                .filter(organo -> organo.getTipoOrgano().getId().equals(tipoOrganoId))
                .map(organo -> Long.parseLong(organo.getId())).collect(Collectors.toList());

        List<Reunion> reuniones = new ArrayList<>();
        for (String organoExternoId : listaOrganosExternosIds)
        {
            List<OrganoReunion> reunionesExternas = organoReunionDAO
                    .getOrganoReunionByOrganoExternoId(organoExternoId);
            for (OrganoReunion organoReunion : reunionesExternas)
            {
                reuniones.add(reunionDAO.getReunionById(organoReunion.getReunion().getId()));
            }
        }

        for (Long organoLocalId : listaOrganosLocalesIds)
        {
            List<OrganoReunion> reunionesLocales = organoReunionDAO
                    .getOrganoReunionByOrganoLocalId(organoLocalId);
            for (OrganoReunion organoReunion : reunionesLocales)
            {
                reuniones.add(reunionDAO.getReunionById(organoReunion.getReunion().getId()));
            }
        }

        if (completada)
        {
            return reuniones.stream().filter(r -> r.isCompletada() != null && r.isCompletada()).collect(Collectors.toList());
        }
        return reuniones.stream().filter(r -> r.isCompletada() == null || r.isCompletada() == false).collect(Collectors.toList());
    }
}
