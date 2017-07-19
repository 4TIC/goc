package es.uji.apps.goc.dto;

import java.util.Date;

public class DocumentoFirma
{
    private Long id;
    private String descripcion;
    private String descripcionAlternativa;

    private String mimeType;

    private String nombreFichero;

    private Date fechaAdicion;

    private Long creadorId;

    private String hash;

    private String urlDescarga;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
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

    public Long getCreadorId() {
        return creadorId;
    }

    public void setCreadorId(Long creadorId) {
        this.creadorId = creadorId;
    }

    public String getHash()
    {
        return hash;
    }

    public void setHash(String hash)
    {
        this.hash = hash;
    }

    public String getUrlDescarga()
    {
        return urlDescarga;
    }

    public void setUrlDescarga(String urlDescarga)
    {
        this.urlDescarga = urlDescarga;
    }

    public String getDescripcionAlternativa()
    {
        return descripcionAlternativa;
    }

    public void setDescripcionAlternativa(String descripcionAlternativa)
    {
        this.descripcionAlternativa = descripcionAlternativa;
    }
}
