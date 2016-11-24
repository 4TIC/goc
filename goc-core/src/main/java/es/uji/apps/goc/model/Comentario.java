package es.uji.apps.goc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Comentario
{
    private Long id;
    private String comentario;
    private Date fecha;

    @JsonProperty("creador_id")
    private Long creadorId;

    @JsonProperty("creador_nombre")
    private String creadorNombre;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getComentario()
    {
        return comentario;
    }

    public void setComentario(String comentario)
    {
        this.comentario = comentario;
    }

    public Date getFecha()
    {
        return fecha;
    }

    public void setFecha(Date fecha)
    {
        this.fecha = fecha;
    }

    public Long getCreadorId()
    {
        return creadorId;
    }

    public void setCreadorId(Long creadorId)
    {
        this.creadorId = creadorId;
    }

    public String getCreadorNombre()
    {
        return creadorNombre;
    }

    public void setCreadorNombre(String creadorNombre)
    {
        this.creadorNombre = creadorNombre;
    }
}
