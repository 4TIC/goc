package es.uji.apps.goc.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import es.uji.apps.goc.model.Comentario;
import es.uji.apps.goc.model.Documento;

public class ReunionFirma implements Serializable
{
    private Long id;

    private String asunto;

    private Date fecha;

    @JsonProperty("numero_sesion")
    private Long numeroSesion;

    private Long duracion;

    private String ubicacion;

    private String descripcion;

    private String urlGrabacion;

    private String acuerdos;

    private Boolean telematica;

    private Boolean completada;

    private String creadorNombre;

    private String creadorEmail;

    private String telematicaDescripcion;

    private List<OrganoFirma> organos;

    private List<Comentario> comentarios;

    private List<Documento> documentos;

    @JsonProperty("puntos_orden_dia")
    private List<PuntoOrdenDiaFirma> puntosOrdenDia;

    public ReunionFirma() {}

    public ReunionFirma(Long id) {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getAsunto()
    {
        return asunto;
    }

    public void setAsunto(String asunto)
    {
        this.asunto = asunto;
    }

    public Date getFecha()
    {
        return fecha;
    }

    public void setFecha(Date fecha)
    {
        this.fecha = fecha;
    }

    public Long getNumeroSesion()
    {
        return numeroSesion;
    }

    public void setNumeroSesion(Long numeroSesion)
    {
        this.numeroSesion = numeroSesion;
    }

    public Long getDuracion()
    {
        return duracion;
    }

    public void setDuracion(Long duracion)
    {
        this.duracion = duracion;
    }

    public String getUbicacion()
    {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion)
    {
        this.ubicacion = ubicacion;
    }

    public String getAcuerdos()
    {
        return acuerdos;
    }

    public void setAcuerdos(String acuerdos)
    {
        this.acuerdos = acuerdos;
    }

    public List<PuntoOrdenDiaFirma> getPuntosOrdenDia()
    {
        return puntosOrdenDia;
    }

    public void setPuntosOrdenDia(List<PuntoOrdenDiaFirma> puntosOrdenDia)
    {
        this.puntosOrdenDia = puntosOrdenDia;
    }

    public String getUrlGrabacion() {
        return urlGrabacion;
    }

    public void setUrlGrabacion(String urlGrabacion) {
        this.urlGrabacion = urlGrabacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<OrganoFirma> getOrganos()
    {
        return organos;
    }

    public void setOrganos(List<OrganoFirma> organos)
    {
        this.organos = organos;
    }

    public List<Comentario> getComentarios()
    {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios)
    {
        this.comentarios = comentarios;
    }

    public List<Documento> getDocumentos()
    {
        return documentos;
    }

    public void setDocumentos(List<Documento> documentos)
    {
        this.documentos = documentos;
    }

    public Boolean isTelematica()
    {
        return telematica;
    }

    public void setTelematica(Boolean telematica)
    {
        this.telematica = telematica;
    }

    public String getTelematicaDescripcion()
    {
        return telematicaDescripcion;
    }

    public void setTelematicaDescripcion(String telematicaDescripcion)
    {
        this.telematicaDescripcion = telematicaDescripcion;
    }

    public Boolean isCompletada()
    {
        return completada;
    }

    public void setCompletada(Boolean completada)
    {
        this.completada = completada;
    }

    public String getCreadorNombre()
    {
        return creadorNombre;
    }

    public void setCreadorNombre(String creadorNombre)
    {
        this.creadorNombre = creadorNombre;
    }

    public String getCreadorEmail()
    {
        return creadorEmail;
    }

    public void setCreadorEmail(String creadorEmail)
    {
        this.creadorEmail = creadorEmail;
    }
}
