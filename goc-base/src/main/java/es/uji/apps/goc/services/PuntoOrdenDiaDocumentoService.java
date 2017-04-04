package es.uji.apps.goc.services;

import com.mysema.query.Tuple;
import es.uji.apps.goc.dao.PuntoOrdenDiaAcuerdoDAO;
import es.uji.apps.goc.dao.PuntoOrdenDiaDocumentoDAO;
import es.uji.apps.goc.dto.PuntoOrdenDia;
import es.uji.apps.goc.dto.PuntoOrdenDiaAcuerdo;
import es.uji.apps.goc.dto.PuntoOrdenDiaDocumento;
import es.uji.apps.goc.model.DocumentoUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Component
public class PuntoOrdenDiaDocumentoService
{
    @Autowired
    private PuntoOrdenDiaDocumentoDAO puntoOrdenDiaDocumentoDAO;

    @Autowired
    private PuntoOrdenDiaAcuerdoDAO puntoOrdenDiaAcuerdoDAO;

    public List<Tuple> getNumeroDocumentosPorReunion(Long connectedUserId)
    {
        return puntoOrdenDiaDocumentoDAO.getNumeroDocumentosPorPuntoOrdenDia();
    }

    public List<Tuple> getNumeroAcuerdosPorReunion(Long connectedUserId)
    {
        return puntoOrdenDiaAcuerdoDAO.getNumeroAcuerdosPorPuntoOrdenDia();
    }

    public PuntoOrdenDiaDocumento addDocumento(Long puntoOrdenDiaId, DocumentoUI documento, Long connectedUserId)
    {
        PuntoOrdenDiaDocumento puntoOrdenDiaDocumento = new PuntoOrdenDiaDocumento();
        PuntoOrdenDia puntoOrdenDia = new PuntoOrdenDia(puntoOrdenDiaId);
        puntoOrdenDiaDocumento.setPuntoOrdenDia(puntoOrdenDia);
        puntoOrdenDiaDocumento.setDatos(documento.getData());
        puntoOrdenDiaDocumento.setMimeType(documento.getMimeType());
        puntoOrdenDiaDocumento.setNombreFichero(documento.getNombreFichero());
        puntoOrdenDiaDocumento.setDescripcion(documento.getDescripcion());
        puntoOrdenDiaDocumento.setDescripcionAlternativa(documento.getDescripcionAlternativa());
        puntoOrdenDiaDocumento.setFechaAdicion(new Date());
        puntoOrdenDiaDocumento.setCreadorId(connectedUserId);

        return puntoOrdenDiaDocumentoDAO.insert(puntoOrdenDiaDocumento);
    }

    public PuntoOrdenDiaAcuerdo addAcuerdo(Long puntoOrdenDiaId, DocumentoUI documento, Long connectedUserId)
    {
        PuntoOrdenDiaAcuerdo puntoOrdenDiaAcuerdo = new PuntoOrdenDiaAcuerdo();
        PuntoOrdenDia puntoOrdenDia = new PuntoOrdenDia(puntoOrdenDiaId);
        puntoOrdenDiaAcuerdo.setPuntoOrdenDia(puntoOrdenDia);
        puntoOrdenDiaAcuerdo.setDatos(documento.getData());
        puntoOrdenDiaAcuerdo.setMimeType(documento.getMimeType());
        puntoOrdenDiaAcuerdo.setNombreFichero(documento.getNombreFichero());
        puntoOrdenDiaAcuerdo.setDescripcion(documento.getDescripcion());
        puntoOrdenDiaAcuerdo.setDescripcionAlternativa(documento.getDescripcionAlternativa());
        puntoOrdenDiaAcuerdo.setFechaAdicion(new Date());
        puntoOrdenDiaAcuerdo.setCreadorId(connectedUserId);

        return puntoOrdenDiaAcuerdoDAO.insert(puntoOrdenDiaAcuerdo);
    }

    public void borrarDocumento(Long documentoId, Long puntoOrdenDiaId, Long connectedUserId)
    {
        puntoOrdenDiaDocumentoDAO.delete(PuntoOrdenDiaDocumento.class, documentoId);
    }

    public void borrarAcuerdo(Long documentoId, Long puntoOrdenDiaId, Long connectedUserId)
    {
        puntoOrdenDiaAcuerdoDAO.delete(PuntoOrdenDiaAcuerdo.class, documentoId);
    }

    public PuntoOrdenDiaDocumento getDocumentoById(Long documentoId)
    {
        return puntoOrdenDiaDocumentoDAO.getDocumentoById(documentoId);
    }

    public PuntoOrdenDiaAcuerdo getAcuerdoById(Long acuerdoId)
    {
        return puntoOrdenDiaAcuerdoDAO.getAcuerdoById(acuerdoId);
    }

    public List<PuntoOrdenDiaDocumento> getDocumentosByPuntoOrdenDiaId(Long puntoOrdenDiaId, Long connectedUserId)
    {
        return puntoOrdenDiaDocumentoDAO.getDocumentosByPuntoOrdenDiaId(puntoOrdenDiaId);
    }

    public List<PuntoOrdenDiaAcuerdo> getAcuerdosByPuntoOrdenDiaId(Long puntoOrdenDiaId, Long connectedUserId)
    {
        return puntoOrdenDiaAcuerdoDAO.getAcuerdosByPuntoOrdenDiaId(puntoOrdenDiaId);
    }
}
