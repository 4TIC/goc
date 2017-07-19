package es.uji.apps.goc.services;

import es.uji.apps.goc.dao.ReunionDocumentoDAO;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.dto.ReunionDocumento;
import es.uji.commons.rest.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
@Component
public class ReunionDocumentoService
{
    @Autowired
    private ReunionDocumentoDAO reunionDocumentoDAO;

    public List<ReunionDocumento> getDocumentosByReunionId(Long reunionId, Long connectedUserId)
    {
        return reunionDocumentoDAO.getDocumentosByReunionId(reunionId);
    }

    public ReunionDocumento getDocumentoById(Long documentoId)
    {
        return reunionDocumentoDAO.getDocumentoById(documentoId);
    }

    public void borrarDocumento(Long documentoId, Long reunionId, Long connectedUserId)
    {
        reunionDocumentoDAO.delete(ReunionDocumento.class, documentoId);
    }

    public ReunionDocumento addDocumento(Long reunionId, String fileName, String descripcion,
                                         String descripcionAlternativa, String mimeType, InputStream data,
                                         Long connectedUserId)
            throws IOException
    {

        ReunionDocumento reunionDocumento = new ReunionDocumento();
        Reunion reunion = new Reunion(reunionId);
        reunionDocumento.setReunion(reunion);
        reunionDocumento.setDatos(StreamUtils.inputStreamToByteArray(data));
        reunionDocumento.setMimeType(mimeType);
        reunionDocumento.setNombreFichero(fileName);
        reunionDocumento.setDescripcion(descripcion);
        reunionDocumento.setDescripcionAlternativa(descripcionAlternativa);
        reunionDocumento.setFechaAdicion(new Date());
        reunionDocumento.setCreadorId(connectedUserId);

        return reunionDocumentoDAO.insert(reunionDocumento);
    }
}
