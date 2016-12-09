package es.uji.apps.goc.services.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mysema.query.Tuple;
import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.FormDataMultiPart;

import es.uji.apps.goc.dto.PuntoOrdenDia;
import es.uji.apps.goc.dto.PuntoOrdenDiaDocumento;
import es.uji.apps.goc.dto.QPuntoOrdenDiaDocumento;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.exceptions.DocumentoNoEncontradoException;
import es.uji.apps.goc.exceptions.PuntoOrdenDiaNoDisponibleException;
import es.uji.apps.goc.exceptions.ReunionNoDisponibleException;
import es.uji.apps.goc.exceptions.ReunionYaCompletadaException;
import es.uji.apps.goc.services.PuntoOrdenDiaDocumentoService;
import es.uji.apps.goc.services.PuntoOrdenDiaService;
import es.uji.apps.goc.services.ReunionService;
import es.uji.commons.rest.CoreBaseService;
import es.uji.commons.rest.ParamUtils;
import es.uji.commons.rest.UIEntity;
import es.uji.commons.sso.AccessManager;

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
        List<Tuple> listaNumeroDocumentosPorPuntoOrdenDiaId = puntoOrdenDiaDocumentoService.getNumeroDocumentosPorReunion(connectedUserId);

        return puntosOrdenDiaConNumeroDocumentosToUI(listaPuntosOrdenDia, listaNumeroDocumentosPorPuntoOrdenDiaId);
    }

    private List<UIEntity> puntosOrdenDiaConNumeroDocumentosToUI(
            List<PuntoOrdenDia> listaPuntosOrdenDia,
            List<Tuple> listaNumeroDocumentosPorPuntoOrdenDiaId)
    {
        List<UIEntity> puntosOrdenDiaUI = new ArrayList<>();

        for (PuntoOrdenDia puntoOrdenDia : listaPuntosOrdenDia)
        {
            UIEntity puntoOrdenDiaUI = UIEntity.toUI(puntoOrdenDia);
            puntoOrdenDiaUI.put("numeroDocumentos", getNumeroDocumentosByPuntoOrdenDiaId(
                    puntoOrdenDia.getId(), listaNumeroDocumentosPorPuntoOrdenDiaId));
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
    public UIEntity modificaPuntoOrdenDia(@PathParam("puntoOrdenDiaId") Long puntoOrdenDiaId,
            UIEntity puntoOrdenDiaUI) throws ReunionNoDisponibleException,
            PuntoOrdenDiaNoDisponibleException, ReunionYaCompletadaException
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

        PuntoOrdenDia puntoOrdenDia = puntoOrdenDiaService.updatePuntoOrdenDia(puntoOrdenDiaId, titulo,
                tituloAlternativo, descripcion, descripcionAlternativa, deliberaciones, deliberacionesAlternativas,
                acuerdos, acuerdosAlternativos, orden, publico, connectedUserId);

        return UIEntity.toUI(puntoOrdenDia);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity addPuntoOrdenDia(UIEntity puntoOrdenDiaUI) throws ReunionYaCompletadaException
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

        List<PuntoOrdenDiaDocumento> documentos = puntoOrdenDiaDocumentoService
                .getDocumentosByPuntoOrdenDiaId(puntoOrdenDiaId, connectedUserId);

        return puntoOrdenDiaDocumentosToUI(documentos);
    }

    private List<UIEntity> puntoOrdenDiaDocumentosToUI(List<PuntoOrdenDiaDocumento> documentos)
    {
        List<UIEntity> listaUI = new ArrayList<>();

        for (PuntoOrdenDiaDocumento puntoOrdenDiaDocumento : documentos)
        {
            listaUI.add(puntoOrdenDiaDocumentoToUI(puntoOrdenDiaDocumento));
        }

        return listaUI;
    }

    private UIEntity puntoOrdenDiaDocumentoToUI(PuntoOrdenDiaDocumento puntoOrdenDiaDocumento)
    {
        UIEntity ui = new UIEntity();

        ui.put("id", puntoOrdenDiaDocumento.getId());
        ui.put("creadorId", puntoOrdenDiaDocumento.getCreadorId());
        ui.put("fechaAdicion", puntoOrdenDiaDocumento.getFechaAdicion());
        ui.put("descripcion", puntoOrdenDiaDocumento.getDescripcion());
        ui.put("descripcionAlternativa", puntoOrdenDiaDocumento.getDescripcionAlternativa());
        ui.put("mimeType", puntoOrdenDiaDocumento.getMimeType());
        ui.put("nombreFichero", puntoOrdenDiaDocumento.getNombreFichero());
        return ui;
    }

    @GET
    @Path("{puntoOrdenDiaId}/documentos/{documentoId}/descargar")
    public Response descargarPuntoOrdenDiaDocumento(
            @PathParam("puntoOrdenDiaId") Long puntoOrdenDiaId,
            @PathParam("documentoId") Long documentoId) throws DocumentoNoEncontradoException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        PuntoOrdenDiaDocumento documento = puntoOrdenDiaDocumentoService.getDocumentoById(documentoId);

        String nombreFichero = null;
        String contentType = null;
        byte[] data = null;

        if (documento == null)
        {
            throw new DocumentoNoEncontradoException();
        }

        data = documento.getDatos();
        nombreFichero = documento.getNombreFichero();
        contentType = documento.getMimeType();

        return Response.ok(data)
                .header("Content-Disposition", "attachment; filename = \"" + nombreFichero + "\"")
                .header("Content-Length", data.length).header("Content-Type", contentType).build();
    }

    @DELETE
    @Path("{puntoOrdenDiaId}/documentos/{documentoId}")
    public Response borrarDocumentoPuntoOrdenDia(@PathParam("puntoOrdenDiaId") Long puntoOrdenDiaId,
            @PathParam("documentoId") Long documentoId) throws ReunionYaCompletadaException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        reunionService.compruebaReunionNoCompletada(reunionId);
        puntoOrdenDiaDocumentoService.borrarDocumento(documentoId, puntoOrdenDiaId, connectedUserId);

        return Response.ok().build();
    }

    @POST
    @Path("{puntoOrdenDiaId}/documentos")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_HTML)
    public UIEntity subirDocumentoPuntoOrdenDia(@PathParam("puntoOrdenDiaId") Long puntoOrdenDiaId,
            FormDataMultiPart multiPart) throws IOException, ReunionYaCompletadaException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        String fileName = "";
        String mimeType = "";
        InputStream data = null;
        String descripcion = "";
        String descripcionAlternativa = "";

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

        reunionService.compruebaReunionNoCompletada(reunionId);

        PuntoOrdenDiaDocumento puntoOrdenDiaDocumento = puntoOrdenDiaDocumentoService.addDocumento(
                puntoOrdenDiaId, fileName, descripcion, descripcionAlternativa, mimeType, data, connectedUserId);

        return UIEntity.toUI(puntoOrdenDiaDocumento);
    }
}
