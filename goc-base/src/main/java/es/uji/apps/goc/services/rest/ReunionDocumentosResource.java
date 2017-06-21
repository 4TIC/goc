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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.api.core.InjectParam;
import com.sun.jersey.core.header.ContentDisposition;
import com.sun.jersey.multipart.BodyPart;
import com.sun.jersey.multipart.BodyPartEntity;
import com.sun.jersey.multipart.FormDataMultiPart;

import es.uji.apps.goc.dto.ReunionDocumento;
import es.uji.apps.goc.exceptions.ReunionYaCompletadaException;
import es.uji.apps.goc.services.ReunionDocumentoService;
import es.uji.apps.goc.services.ReunionService;
import es.uji.commons.rest.CoreBaseService;
import es.uji.commons.rest.UIEntity;
import es.uji.commons.sso.AccessManager;

@Path("/reuniones/{reunionId}/documentos")
public class ReunionDocumentosResource extends CoreBaseService
{
    @InjectParam
    private ReunionService reunionService;

    @InjectParam
    private ReunionDocumentoService reunionDocumentoService;

    @PathParam("reunionId")
    Long reunionId;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getReunionDocumentos()
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        List<ReunionDocumento> documentos = reunionDocumentoService
                .getDocumentosByReunionId(reunionId, connectedUserId);

        return reunionDocumentosToUI(documentos);
    }

    private List<UIEntity> reunionDocumentosToUI(List<ReunionDocumento> documentos)
    {
        List<UIEntity> listaUI = new ArrayList<>();

        for (ReunionDocumento reunionDocumento : documentos)
        {
            listaUI.add(reunionDocumentoToUI(reunionDocumento));
        }

        return listaUI;
    }

    private UIEntity reunionDocumentoToUI(ReunionDocumento reunionDocumento)
    {
        UIEntity ui = new UIEntity();
        ui.put("id", reunionDocumento.getId());
        ui.put("creadorId", reunionDocumento.getCreadorId());
        ui.put("fechaAdicion", reunionDocumento.getFechaAdicion());
        ui.put("descripcion", reunionDocumento.getDescripcion());
        ui.put("descripcionAlternativa", reunionDocumento.getDescripcionAlternativa());
        ui.put("mimeType", reunionDocumento.getMimeType());
        ui.put("nombreFichero", reunionDocumento.getNombreFichero());

        return ui;
    }

    @GET
    @Path("{documentoId}/descargar")
    public Response descargarDocumento(@PathParam("documentoId") Long documentoId)
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        ReunionDocumento documento = reunionDocumentoService.getDocumentoById(documentoId);

        String nombreFichero = null;
        String contentType = null;

        byte[] data = documento.getDatos();

        if (data != null)
        {
            nombreFichero = documento.getNombreFichero();
            contentType = documento.getMimeType();
        }

        return Response.ok(data)
                .header("Content-Disposition", "attachment; filename = \"" + nombreFichero + "\"")
                .header("Content-Length", data.length).header("Content-Type", contentType).build();
    }

    @DELETE
    @Path("{documentoId}")
    public Response borrarDocumento(@PathParam("documentoId") Long documentoId)
            throws ReunionYaCompletadaException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        reunionService.compruebaReunionNoCompletada(reunionId);
        reunionDocumentoService.borrarDocumento(documentoId, reunionId, connectedUserId);

        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity subirDocumento(FormDataMultiPart multiPart)
            throws IOException, ReunionYaCompletadaException
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

        ReunionDocumento reunionDocumento = reunionDocumentoService.addDocumento(reunionId,
                fileName, descripcion, descripcionAlternativa, mimeType, data, connectedUserId);
        return UIEntity.toUI(reunionDocumento);
    }
}
