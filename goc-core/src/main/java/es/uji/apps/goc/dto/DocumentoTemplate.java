package es.uji.apps.goc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class DocumentoTemplate
{
    private Long id;
    private String descripcion;

    private String mimeType;

    private String nombreFichero;

    private Date fechaAdicion;

    private Long creadorId;

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
}
