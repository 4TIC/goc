package es.uji.apps.goc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import es.uji.apps.goc.model.Comentario;
import es.uji.apps.goc.model.Documento;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ReunionFirma implements Serializable
{
    private Long id;

    private String asunto;

    @JsonProperty("asunto_alternativo")
    private String asuntoAlternativo;

    private Date fecha;

    @JsonProperty("numero_sesion")
    private Long numeroSesion;

    private Long duracion;

    private String ubicacion;

    @JsonProperty("ubicacion_alternativa")
    private String ubicacionAlternativa;

    private String descripcion;

    @JsonProperty("descripcion_alternativa")
    private String descripcionAlternativa;

    @JsonProperty("url_grabacion")
    private String urlGrabacion;

    private String acuerdos;

    @JsonProperty("acuerdos_alternativos")
    private String acuerdosAlternativos;

    private Boolean telematica;

    @JsonProperty("admite_suplencia")
    private Boolean admiteSuplencia;

    private Boolean completada;

    @JsonProperty("creador_nombre")
    private String creadorNombre;

    @JsonProperty("creador_email")
    private String creadorEmail;

    @JsonProperty("telematica_descripcion")
    private String telematicaDescripcion;

    @JsonProperty("telematica_descripcion_alternativa")
    private String telematicaDescripcionAlternativa;

    private List<OrganoFirma> organos;

    private List<Comentario> comentarios;

    private List<Documento> documentos;

    @JsonProperty("puntos_orden_dia")
    private List<PuntoOrdenDiaFirma> puntosOrdenDia;

    private String responsableActa;

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

    public Boolean isAdmiteSuplencia() {
        return admiteSuplencia;
    }

    public void setAdmiteSuplencia(Boolean admiteSuplencia) {
        this.admiteSuplencia = admiteSuplencia;
    }

    public String getAsuntoAlternativo()
    {
        return asuntoAlternativo;
    }

    public void setAsuntoAlternativo(String asuntoAlternativo)
    {
        this.asuntoAlternativo = asuntoAlternativo;
    }

    public String getUbicacionAlternativa()
    {
        return ubicacionAlternativa;
    }

    public void setUbicacionAlternativa(String ubicacionAlternativa)
    {
        this.ubicacionAlternativa = ubicacionAlternativa;
    }

    public String getDescripcionAlternativa()
    {
        return descripcionAlternativa;
    }

    public void setDescripcionAlternativa(String descripcionAlternativa)
    {
        this.descripcionAlternativa = descripcionAlternativa;
    }

    public String getAcuerdosAlternativos()
    {
        return acuerdosAlternativos;
    }

    public void setAcuerdosAlternativos(String acuerdosAlternativos)
    {
        this.acuerdosAlternativos = acuerdosAlternativos;
    }

    public String getTelematicaDescripcionAlternativa()
    {
        return telematicaDescripcionAlternativa;
    }

    public void setTelematicaDescripcionAlternativa(String telematicaDescripcionAlternativa)
    {
        this.telematicaDescripcionAlternativa = telematicaDescripcionAlternativa;
    }

    public String getResponsableActa()
    {
        return responsableActa;
    }

    public void setResponsableActa(String responsableActa)
    {
        this.responsableActa = responsableActa;
    }
}