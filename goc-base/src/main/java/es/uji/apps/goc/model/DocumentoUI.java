package es.uji.apps.goc.model;

import es.uji.apps.goc.dto.PuntoOrdenDiaAcuerdo;
import es.uji.apps.goc.dto.PuntoOrdenDiaDocumento;

import java.util.Date;

public class DocumentoUI
{
    private Long id;
    private Long creadorId;
    private Date fechaAdicion;
    private String descripcion;
    private String descripcionAlternativa;
    private String mimeType;
    private String nombreFichero;

    private byte[] data;

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    public String getDescripcionAlternativa()
    {
        return descripcionAlternativa;
    }

    public void setDescripcionAlternativa(String descripcionAlternativa)
    {
        this.descripcionAlternativa = descripcionAlternativa;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public void setMimeType(String mimeType)
    {
        this.mimeType = mimeType;
    }

    public String getNombreFichero()
    {
        return nombreFichero;
    }

    public void setNombreFichero(String nombreFichero)
    {
        this.nombreFichero = nombreFichero;
    }

    public Date getFechaAdicion()
    {
        return fechaAdicion;
    }

    public void setFechaAdicion(Date fechaAdicion)
    {
        this.fechaAdicion = fechaAdicion;
    }

    public Long getCreadorId()
    {
        return creadorId;
    }

    public void setCreadorId(Long creadorId)
    {
        this.creadorId = creadorId;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setData(byte[] data)
    {
        this.data = data;
    }

    public byte[] getData()
    {
        return this.data;
    }

    public static DocumentoUI fromPuntoDiaDocumento(PuntoOrdenDiaDocumento puntoOrdenDiaDocumento)
    {
        DocumentoUI documento = new DocumentoUI();

        documento.setId(puntoOrdenDiaDocumento.getId());
        documento.setCreadorId(puntoOrdenDiaDocumento.getCreadorId());
        documento.setFechaAdicion(puntoOrdenDiaDocumento.getFechaAdicion());
        documento.setDescripcion(puntoOrdenDiaDocumento.getDescripcion());
        documento.setDescripcionAlternativa(puntoOrdenDiaDocumento.getDescripcionAlternativa());
        documento.setMimeType(puntoOrdenDiaDocumento.getMimeType());
        documento.setNombreFichero(puntoOrdenDiaDocumento.getNombreFichero());

        return documento;
    }

    public static DocumentoUI fromPuntoDiaAcuerdos(PuntoOrdenDiaAcuerdo puntoOrdenDiaAcuerdo)
    {
        DocumentoUI documento = new DocumentoUI();

        documento.setId(puntoOrdenDiaAcuerdo.getId());
        documento.setCreadorId(puntoOrdenDiaAcuerdo.getCreadorId());
        documento.setFechaAdicion(puntoOrdenDiaAcuerdo.getFechaAdicion());
        documento.setDescripcion(puntoOrdenDiaAcuerdo.getDescripcion());
        documento.setDescripcionAlternativa(puntoOrdenDiaAcuerdo.getDescripcionAlternativa());
        documento.setMimeType(puntoOrdenDiaAcuerdo.getMimeType());
        documento.setNombreFichero(puntoOrdenDiaAcuerdo.getNombreFichero());

        return documento;
    }

}
