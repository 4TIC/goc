package es.uji.apps.goc.services.rest;

import com.mysema.query.Tuple;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.FormDataMultiPart;
import es.uji.apps.goc.dto.*;
import es.uji.apps.goc.exceptions.DocumentoNoEncontradoException;
import es.uji.apps.goc.exceptions.PuntoOrdenDiaNoDisponibleException;
import es.uji.apps.goc.exceptions.ReunionNoDisponibleException;
import es.uji.apps.goc.exceptions.ReunionYaCompletadaException;
import es.uji.apps.goc.model.DocumentoUI;
import es.uji.apps.goc.services.PuntoOrdenDiaDocumentoService;
import es.uji.apps.goc.services.PuntoOrdenDiaService;
import es.uji.apps.goc.services.ReunionService;
import es.uji.commons.rest.CoreBaseService;
import es.uji.commons.rest.ParamUtils;
import es.uji.commons.rest.StreamUtils;
import es.uji.commons.rest.UIEntity;
import es.uji.commons.sso.AccessManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Path("/reuniones/{reunionId}/puntosOrdenDia")
public class ReunionPuntosOrdenDiaResource extends CoreBaseService
{
    @InjectParam
    private ReunionService reunionService;

    @InjectParam
    private PuntoOrdenDiaService puntoOrdenDiaService;

    @InjectParam
    private PuntoOrdenDiaDocumentoService puntoOrdenDiaDocumentoService;

    @PathParam("reunionId")
    Long reunionId;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getReunionPuntosOrdenDia()
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        List<PuntoOrdenDia> listaPuntosOrdenDia = puntoOrdenDiaService.getPuntosByReunionId(reunionId, connectedUserId);
        List<Tuple> listaNumeroDocumentosPorPuntoOrdenDiaId =
                puntoOrdenDiaDocumentoService.getNumeroDocumentosPorReunion(connectedUserId);
        List<Tuple> listaNumeroAcuerdosPorPuntoOrdenDiaId =
                puntoOrdenDiaDocumentoService.getNumeroAcuerdosPorReunion(connectedUserId);

        return puntosOrdenDiaConNumeroDocumentosToUI(listaPuntosOrdenDia, listaNumeroDocumentosPorPuntoOrdenDiaId,
                listaNumeroAcuerdosPorPuntoOrdenDiaId);
    }

    private List<UIEntity> puntosOrdenDiaConNumeroDocumentosToUI(List<PuntoOrdenDia> listaPuntosOrdenDia,
            List<Tuple> listaNumeroDocumentosPorPuntoOrdenDiaId, List<Tuple> listaNumeroAcuerdosPorPuntoOrdenDiaId)
    {
        List<UIEntity> puntosOrdenDiaUI = new ArrayList<>();

        for (PuntoOrdenDia puntoOrdenDia : listaPuntosOrdenDia)
        {
            UIEntity puntoOrdenDiaUI = UIEntity.toUI(puntoOrdenDia);
            puntoOrdenDiaUI.put("numeroDocumentos", getNumeroDocumentosByPuntoOrdenDiaId(puntoOrdenDia.getId(),
                    listaNumeroDocumentosPorPuntoOrdenDiaId));
            puntoOrdenDiaUI.put("numeroAcuerdos",
                    getNumeroAcuerdosByPuntoOrdenDiaId(puntoOrdenDia.getId(), listaNumeroAcuerdosPorPuntoOrdenDiaId));
            puntosOrdenDiaUI.add(puntoOrdenDiaUI);
        }

        return puntosOrdenDiaUI;
    }

    private Long getNumeroDocumentosByPuntoOrdenDiaId(Long puntoOrdenDiaId,
            List<Tuple> listaNumeroDocumentosPorPuntoOrdenDiaId)
    {
        Long num = 0L;

        for (Tuple tupla : listaNumeroDocumentosPorPuntoOrdenDiaId)
        {
            Long id = tupla.get(QPuntoOrdenDiaDocumento.puntoOrdenDiaDocumento.puntoOrdenDia.id);

            if (id.equals(puntoOrdenDiaId))
            {
                return tupla.get(QPuntoOrdenDiaDocumento.puntoOrdenDiaDocumento.puntoOrdenDia.id.count());
            }
        }

        return num;
    }

    private Long getNumeroAcuerdosByPuntoOrdenDiaId(Long puntoOrdenDiaId,
            List<Tuple> listaNumeroAcuerdosPorPuntoOrdenDiaId)
    {
        Long num = 0L;

        for (Tuple tupla : listaNumeroAcuerdosPorPuntoOrdenDiaId)
        {
            Long id = tupla.get(QPuntoOrdenDiaAcuerdo.puntoOrdenDiaAcuerdo.puntoOrdenDia.id);

            if (id.equals(puntoOrdenDiaId))
            {
                return tupla.get(QPuntoOrdenDiaAcuerdo.puntoOrdenDiaAcuerdo.puntoOrdenDia.id.count());
            }
        }

        return num;
    }

    @DELETE
    @Path("{puntoOrdenDiaId}")
    public Response borrarPuntoOrdenDia(@PathParam("puntoOrdenDiaId") Long puntoOrdenDiaId)
            throws ReunionYaCompletadaException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        reunionService.compruebaReunionNoCompletada(reunionId);
        puntoOrdenDiaService.borrarPuntoOrdenDia(reunionId, puntoOrdenDiaId, connectedUserId);

        return Response.ok().build();
    }

    @PUT
    @Path("{puntoOrdenDiaId}/subir")
    public Response subePuntoOrdenDia(@PathParam("puntoOrdenDiaId") Long puntoOrdenDiaId)
            throws ReunionYaCompletadaException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        reunionService.compruebaReunionNoCompletada(reunionId);
        puntoOrdenDiaService.subePuntoOrdenDia(reunionId, puntoOrdenDiaId, connectedUserId);

        return Response.ok().build();
    }

    @PUT
    @Path("{puntoOrdenDiaId}/bajar")
    public Response bajaPuntoOrdenDia(@PathParam("puntoOrdenDiaId") Long puntoOrdenDiaId)
            throws ReunionYaCompletadaException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        reunionService.compruebaReunionNoCompletada(reunionId);
        puntoOrdenDiaService.bajaPuntoOrdenDia(reunionId, puntoOrdenDiaId, connectedUserId);

        return Response.ok().build();
    }

    @PUT
    @Path("{puntoOrdenDiaId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity modificaPuntoOrdenDia(@PathParam("puntoOrdenDiaId") Long puntoOrdenDiaId, UIEntity puntoOrdenDiaUI)
            throws ReunionNoDisponibleException, PuntoOrdenDiaNoDisponibleException, ReunionYaCompletadaException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        String titulo = puntoOrdenDiaUI.get("titulo");
        String tituloAlternativo = puntoOrdenDiaUI.get("tituloAlternativo");
        String descripcion = puntoOrdenDiaUI.get("descripcion");
        String descripcionAlternativa = puntoOrdenDiaUI.get("descripcionAlternativa");
        String deliberaciones = puntoOrdenDiaUI.get("deliberaciones");
        String deliberacionesAlternativas = puntoOrdenDiaUI.get("deliberacionesAlternativas");
        String acuerdos = puntoOrdenDiaUI.get("acuerdos");
        String acuerdosAlternativos = puntoOrdenDiaUI.get("acuerdosAlternativos");
        Boolean publico = new Boolean(puntoOrdenDiaUI.get("publico"));
        Long orden = ParamUtils.parseLong(puntoOrdenDiaUI.get("orden"));

        reunionService.compruebaReunionNoCompletada(reunionId);

        PuntoOrdenDia puntoOrdenDia =
                puntoOrdenDiaService.updatePuntoOrdenDia(puntoOrdenDiaId, titulo, tituloAlternativo, descripcion,
                        descripcionAlternativa, deliberaciones, deliberacionesAlternativas, acuerdos,
                        acuerdosAlternativos, orden, publico, connectedUserId);

        return UIEntity.toUI(puntoOrdenDia);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity addPuntoOrdenDia(UIEntity puntoOrdenDiaUI)
            throws ReunionYaCompletadaException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        PuntoOrdenDia puntoOrdenDia = puntoOrdenDiaUIToModel(puntoOrdenDiaUI);
        reunionService.compruebaReunionNoCompletada(puntoOrdenDia.getReunion().getId());
        puntoOrdenDia = puntoOrdenDiaService.addPuntoOrdenDia(puntoOrdenDia, connectedUserId);

        return UIEntity.toUI(puntoOrdenDia);
    }

    private PuntoOrdenDia puntoOrdenDiaUIToModel(UIEntity puntoOrdenDiaUI)
    {
        PuntoOrdenDia puntoOrdenDia = new PuntoOrdenDia();

        if (ParamUtils.parseLong(puntoOrdenDiaUI.get("id")) != null)
        {
            puntoOrdenDia.setId(new Long(puntoOrdenDiaUI.get("id")));
        }

        puntoOrdenDia.setTitulo((puntoOrdenDiaUI.get("titulo")));
        puntoOrdenDia.setTituloAlternativo((puntoOrdenDiaUI.get("tituloAlternativo")));
        puntoOrdenDia.setDescripcion(puntoOrdenDiaUI.get("descripcion"));
        puntoOrdenDia.setDescripcionAlternativa(puntoOrdenDiaUI.get("descripcionAlternativa"));
        puntoOrdenDia.setDeliberaciones(puntoOrdenDiaUI.get("deliberaciones"));
        puntoOrdenDia.setDeliberacionesAlternativas(puntoOrdenDiaUI.get("deliberacionesAlternativas"));
        puntoOrdenDia.setAcuerdos(puntoOrdenDiaUI.get("acuerdos"));
        puntoOrdenDia.setAcuerdosAlternativos(puntoOrdenDiaUI.get("acuerdosAlternativos"));
        puntoOrdenDia.setPublico(new Boolean(puntoOrdenDiaUI.get("publico")));

        Reunion reunion = new Reunion(Long.parseLong(puntoOrdenDiaUI.get("reunionId")));
        puntoOrdenDia.setReunion(reunion);

        return puntoOrdenDia;
    }

    @GET
    @Path("{puntoOrdenDiaId}/documentos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getPuntoOrdenDiaDocumentos(@PathParam("reunionId") Long reunionId,
            @PathParam("puntoOrdenDiaId") Long puntoOrdenDiaId)
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        List<PuntoOrdenDiaDocumento> documentos =
                puntoOrdenDiaDocumentoService.getDocumentosByPuntoOrdenDiaId(puntoOrdenDiaId, connectedUserId);

        return puntoOrdenDiaDocumentosToUI(documentos);
    }

    @GET
    @Path("{puntoOrdenDiaId}/acuerdos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getPuntoOrdenDiaAcuerdos(@PathParam("reunionId") Long reunionId,
            @PathParam("puntoOrdenDiaId") Long puntoOrdenDiaId)
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        List<PuntoOrdenDiaAcuerdo> acuerdos =
                puntoOrdenDiaDocumentoService.getAcuerdosByPuntoOrdenDiaId(puntoOrdenDiaId, connectedUserId);

        return puntoOrdenDiaAcuerdosToUI(acuerdos);
    }

    private List<UIEntity> puntoOrdenDiaDocumentosToUI(List<PuntoOrdenDiaDocumento> documentos)
    {
        List<UIEntity> listaUI = new ArrayList<>();

        for (PuntoOrdenDiaDocumento puntoOrdenDiaDocumento : documentos)
        {
            listaUI.add(puntoOrdenDiaDocumentoToUI(DocumentoUI.fromPuntoDiaDocumento(puntoOrdenDiaDocumento)));
        }

        return listaUI;
    }

    private List<UIEntity> puntoOrdenDiaAcuerdosToUI(List<PuntoOrdenDiaAcuerdo> acuerdos)
    {
        List<UIEntity> listaUI = new ArrayList<>();

        for (PuntoOrdenDiaAcuerdo puntoOrdenDiaAcuerdo : acuerdos)
        {
            listaUI.add(puntoOrdenDiaDocumentoToUI(DocumentoUI.fromPuntoDiaAcuerdos(puntoOrdenDiaAcuerdo)));
        }

        return listaUI;
    }

    private UIEntity puntoOrdenDiaDocumentoToUI(DocumentoUI documento)
    {
        UIEntity ui = new UIEntity();

        ui.put("id", documento.getId());
        ui.put("creadorId", documento.getCreadorId());
        ui.put("fechaAdicion", documento.getFechaAdicion());
        ui.put("descripcion", documento.getDescripcion());
        ui.put("descripcionAlternativa", documento.getDescripcionAlternativa());
        ui.put("mimeType", documento.getMimeType());
        ui.put("nombreFichero", documento.getNombreFichero());

        return ui;
    }

    @GET
    @Path("{puntoOrdenDiaId}/documentos/{documentoId}/descargar")
    public Response descargarPuntoOrdenDiaDocumento(@PathParam("puntoOrdenDiaId") Long puntoOrdenDiaId,
            @PathParam("documentoId") Long documentoId)
            throws DocumentoNoEncontradoException, IOException
    {
        PuntoOrdenDiaDocumento documento = puntoOrdenDiaDocumentoService.getDocumentoById(documentoId);

        if (documento == null)
        {
            throw new DocumentoNoEncontradoException();
        }

        DocumentoUI documentoAux = new DocumentoUI();

        documentoAux.setData(documento.getDatos());
        documentoAux.setNombreFichero(documento.getNombreFichero());
        documentoAux.setMimeType(documento.getMimeType());

        return sendDocumento(documentoAux);
    }

    @GET
    @Path("{puntoOrdenDiaId}/acuerdos/{acuerdoId}/descargar")
    public Response descargarPuntoOrdenDiaAcuerdo(@PathParam("puntoOrdenDiaId") Long puntoOrdenDiaId,
            @PathParam("acuerdoId") Long acuerdoId)
            throws DocumentoNoEncontradoException, IOException
    {
        PuntoOrdenDiaAcuerdo acuerdo = puntoOrdenDiaDocumentoService.getAcuerdoById(acuerdoId);

        if (acuerdo == null)
        {
            throw new DocumentoNoEncontradoException();
        }

        DocumentoUI documento = new DocumentoUI();

        documento.setData(acuerdo.getDatos());
        documento.setNombreFichero(acuerdo.getNombreFichero());
        documento.setMimeType(acuerdo.getMimeType());

        return sendDocumento(documento);
    }

    private Response sendDocumento(DocumentoUI documento)
            throws IOException
    {
        String nombreFichero = documento.getNombreFichero();
        String contentType = documento.getMimeType();
        byte[] data = documento.getData();

        return Response.ok(data)
                .header("Content-Disposition", "attachment; filename = \"" + nombreFichero + "\"")
                .header("Content-Length", data.length)
                .header("Content-Type", contentType)
                .build();

    }

    @DELETE
    @Path("{puntoOrdenDiaId}/documentos/{documentoId}")
    public Response borrarDocumentoPuntoOrdenDia(@PathParam("puntoOrdenDiaId") Long puntoOrdenDiaId,
            @PathParam("documentoId") Long documentoId)
            throws ReunionYaCompletadaException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        reunionService.compruebaReunionNoCompletada(reunionId);
        puntoOrdenDiaDocumentoService.borrarDocumento(documentoId, puntoOrdenDiaId, connectedUserId);

        return Response.ok().build();
    }

    @DELETE
    @Path("{puntoOrdenDiaId}/acuerdos/{acuerdoId}")
    public Response borrarAcuerdoPuntoOrdenDia(@PathParam("puntoOrdenDiaId") Long puntoOrdenDiaId,
            @PathParam("acuerdoId") Long acuerdoId)
            throws ReunionYaCompletadaException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        reunionService.compruebaReunionNoCompletada(reunionId);
        puntoOrdenDiaDocumentoService.borrarAcuerdo(acuerdoId, puntoOrdenDiaId, connectedUserId);

        return Response.ok().build();
    }

    @POST
    @Path("{puntoOrdenDiaId}/documentos")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public Response subirDocumentoPuntoOrdenDia(@PathParam("puntoOrdenDiaId") Long puntoOrdenDiaId,
            FormDataMultiPart multiPart)
            throws IOException, ReunionYaCompletadaException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        DocumentoUI documento = extractDocumentoFromMultipart(multiPart);

        puntoOrdenDiaDocumentoService.addDocumento(puntoOrdenDiaId, documento, connectedUserId);

        return Response.ok().entity("{\"success\":true}").build();
    }

    @POST
    @Path("{puntoOrdenDiaId}/acuerdos")
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public Response subirAcuerdoPuntoOrdenDia(@PathParam("puntoOrdenDiaId") Long puntoOrdenDiaId,
            FormDataMultiPart multiPart)
            throws IOException, ReunionYaCompletadaException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        DocumentoUI documento = extractDocumentoFromMultipart(multiPart);

        puntoOrdenDiaDocumentoService.addAcuerdo(puntoOrdenDiaId, documento, connectedUserId);

        return Response.ok().entity("{\"success\":true}").build();
    }

    private DocumentoUI extractDocumentoFromMultipart(FormDataMultiPart multiPart)
            throws IOException
    {
        String fileName = "";
        String mimeType = "";
        InputStream data = null;
        String descripcion = "";
        String descripcionAlternativa = "";
        DocumentoUI documento = new DocumentoUI();

        for (BodyPart bodyPart : multiPart.getBodyParts())
        {
            String mime = bodyPart.getHeaders().getFirst("Content-Type");
            if (mime != null && !mime.isEmpty())
            {
                mimeType = mime;
                String header = bodyPart.getHeaders().getFirst("Content-Disposition");
                Pattern fileNamePattern = Pattern.compile(".*filename=\"(.*)\"");
                Matcher m = fileNamePattern.matcher(header);
                if (m.matches())
                {
                    fileName = m.group(1);
                }
                BodyPartEntity bpe = (BodyPartEntity) bodyPart.getEntity();
                data = bpe.getInputStream();
            }
            else
            {
                ContentDisposition cd = bodyPart.getContentDisposition();
                Map<String, String> parameters = cd.getParameters();

                if (parameters.get("name").equals("descripcion"))
                {
                    descripcion = bodyPart.getEntityAs(String.class);
                }

                if (parameters.get("name").equals("descripcionAlternativa"))
                {
                    descripcionAlternativa = bodyPart.getEntityAs(String.class);
                }
            }
        }

        documento.setDescripcion(descripcion);
        documento.setDescripcionAlternativa(descripcionAlternativa);
        documento.setNombreFichero(fileName);
        documento.setMimeType(mimeType);
        documento.setData(StreamUtils.inputStreamToByteArray(data));

        return documento;
    }
}
