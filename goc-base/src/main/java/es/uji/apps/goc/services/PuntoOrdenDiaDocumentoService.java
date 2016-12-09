package es.uji.apps.goc.services;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.mysema.query.Tuple;

import es.uji.apps.goc.dao.PuntoOrdenDiaDocumentoDAO;
import es.uji.apps.goc.dto.PuntoOrdenDia;
import es.uji.apps.goc.dto.PuntoOrdenDiaDocumento;

@Service
@Component
public class PuntoOrdenDiaDocumentoService
{
    @Autowired
    private PuntoOrdenDiaDocumentoDAO puntoOrdenDiaDocumentoDAO;

    public List<Tuple> getNumeroDocumentosPorReunion(Long connectedUserId) {
        return puntoOrdenDiaDocumentoDAO.getNumeroDocumentosPorPuntoOrdenDia();
    }

    public PuntoOrdenDiaDocumento addDocumento(Long puntoOrdenDiaId, String fileName, String descripcion,
                                               String descripcionAlternativa, String mimeType, InputStream data,
                                               Long connectedUserId)
    {
        PuntoOrdenDiaDocumento puntoOrdenDiaDocumento = new PuntoOrdenDiaDocumento();
        PuntoOrdenDia puntoOrdenDia = new PuntoOrdenDia(puntoOrdenDiaId);
        puntoOrdenDiaDocumento.setPuntoOrdenDia(puntoOrdenDia);
        puntoOrdenDiaDocumento.setDatos(this.inputStreamToByteArray(data));
        puntoOrdenDiaDocumento.setMimeType(mimeType);
        puntoOrdenDiaDocumento.setNombreFichero(fileName);
        puntoOrdenDiaDocumento.setDescripcion(descripcion);
        puntoOrdenDiaDocumento.setDescripcionAlternativa(descripcionAlternativa);
        puntoOrdenDiaDocumento.setFechaAdicion(new Date());
        puntoOrdenDiaDocumento.setCreadorId(connectedUserId);

        return puntoOrdenDiaDocumentoDAO.insert(puntoOrdenDiaDocumento);

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

    public void borrarDocumento(Long documentoId, Long puntoOrdenDiaId, Long connectedUserId)
    {
        puntoOrdenDiaDocumentoDAO.delete(PuntoOrdenDiaDocumento.class, documentoId);
    }

    public PuntoOrdenDiaDocumento getDocumentoById(Long documentoId)
    {
        return puntoOrdenDiaDocumentoDAO.getDocumentoById(documentoId);
    }

    public List<PuntoOrdenDiaDocumento> getDocumentosByPuntoOrdenDiaId(Long puntoOrdenDiaId, Long connectedUserId)
    {
        return puntoOrdenDiaDocumentoDAO.getDocumentosByPuntoOrdenDiaId(puntoOrdenDiaId);
    }
}
