package es.uji.apps.goc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Documento
{
    private Long id;
    private String descripcion;

    @JsonProperty("mime_type")
    private String mimeType;

    @JsonProperty("nombre_fichero")
    private String nombreFichero;

    @JsonProperty("fecha_adicion")
    private Date fechaAdicion;

    @JsonProperty("creador_id")
    private Long creadorId;

    @JsonProperty("batos_base64")
    private String datosBase64;

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

    public String getDatosBase64() {
        return datosBase64;
    }

    public void setDatosBase64(String datosBase64) {
        this.datosBase64 = datosBase64;
    }
}
