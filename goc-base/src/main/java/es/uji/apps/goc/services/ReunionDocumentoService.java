package es.uji.apps.goc.services;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.mysema.query.Tuple;

import es.uji.apps.goc.dao.ReunionDocumentoDAO;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.dto.ReunionDocumento;

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

    public ReunionDocumento addDocumento(Long reunionId, String fileName, String descripcion, String mimeType, InputStream data, Long connectedUserId)
    {

        ReunionDocumento reunionDocumento = new ReunionDocumento();
        Reunion reunion = new Reunion(reunionId);
        reunionDocumento.setReunion(reunion);
        reunionDocumento.setDatos(this.inputStreamToByteArray(data));
        reunionDocumento.setMimeType(mimeType);
        reunionDocumento.setNombreFichero(fileName);
        reunionDocumento.setDescripcion(descripcion);
        reunionDocumento.setFechaAdicion(new Date());
        reunionDocumento.setCreadorId(connectedUserId);

        return reunionDocumentoDAO.insert(reunionDocumento);
    }

    public byte[] inputStreamToByteArray(InputStream inputStream)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int size = 1024;
        byte[] buffer = new byte[size];
        int len;

        try
        {
            while ((len = inputStream.read(buffer, 0, size)) != -1)
            {
                outputStream.write(buffer, 0, len);
            }

            return outputStream.toByteArray();
        }
        catch (Exception e)
        {
        }

        return null;
    }

    public List<Tuple> getNumeroDocumentosPorReunion(Long connectedUserId) {
        return reunionDocumentoDAO.getNumeroDocumentosPorReunion();
    }
}
