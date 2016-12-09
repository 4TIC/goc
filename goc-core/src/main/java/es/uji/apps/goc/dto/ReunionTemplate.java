package es.uji.apps.goc.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import es.uji.apps.goc.model.Comentario;
import es.uji.apps.goc.model.Documento;

public class ReunionTemplate implements Serializable
{
    private Long id;

    private String asunto;

    private String asuntoAlternativo;

    private Date fecha;

    private Date fechaSegundaConvocatoria;

    private Long numeroSesion;

    private String descripcion;

    private String descripcionAlternativa;

    private Long duracion;

    private String ubicacion;

    private String ubicacionAlternativa;

    private String acuerdos;

    private String acuerdosAlternativos;

    private Boolean telematica;

    private Boolean completada;

    private Boolean admiteSuplencia;

    private Boolean admiteComentarios;

    private String telematicaDescripcion;

    private String telematicaDescripcionAlternativa;

    private String creadorNombre;

    private String creadorEmail;

    private String urlGrabacion;

    private Boolean comoAsistente;

    private String responsableActa;

    private List<OrganoTemplate> organos;

    private List<Comentario> comentarios;

    private List<PuntoOrdenDiaTemplate> puntosOrdenDia;

    private List<Documento> documentos;

    public ReunionTemplate() {}

    public ReunionTemplate(Long id) {
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

    public List<PuntoOrdenDiaTemplate> getPuntosOrdenDia()
    {
        return puntosOrdenDia;
    }

    public void setPuntosOrdenDia(List<PuntoOrdenDiaTemplate> puntosOrdenDia)
    {
        this.puntosOrdenDia = puntosOrdenDia;
    }

    public List<OrganoTemplate> getOrganos() {
        return organos;
    }

    public void setOrganos(List<OrganoTemplate> organos) {
        this.organos = organos;
    }

    public List<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<Documento> documentos) {
        this.documentos = documentos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Comentario> getComentarios()
    {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios)
    {
        this.comentarios = comentarios;
    }

    public String getUrlGrabacion() {
        return urlGrabacion;
    }

    public void setUrlGrabacion(String urlGrabacion) {
        this.urlGrabacion = urlGrabacion;
    }

    public String getResponsableActa()
    {
        return responsableActa;
    }

    public void setResponsableActa(String responsableActa)
    {
        this.responsableActa = responsableActa;
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

    public Boolean isCompletada()
    {
        return completada;
    }

    public void setCompletada(Boolean completada)
    {
        this.completada = completada;
    }

    public Boolean isAdmiteSuplencia() {
        return admiteSuplencia;
    }

    public void setAdmiteSuplencia(Boolean admiteSuplencia) {
        this.admiteSuplencia = admiteSuplencia;
    }

    public Boolean isComoAsistente() {
        return comoAsistente;
    }

    public void setComoAsistente(Boolean comoAsistente) {
        this.comoAsistente = comoAsistente;
    }

    public void setAdmiteComentarios(Boolean admiteComentarios)
    {
        this.admiteComentarios = admiteComentarios;
    }

    public Boolean isAdmiteComentarios()
    {
        return admiteComentarios;
    }

    public Date getFechaSegundaConvocatoria()
    {
        return fechaSegundaConvocatoria;
    }

    public void setFechaSegundaConvocatoria(Date fechaSegundaConvocatoria)
    {
        this.fechaSegundaConvocatoria = fechaSegundaConvocatoria;
    }

    public String getAsuntoAlternativo()
    {
        return asuntoAlternativo;
    }

    public void setAsuntoAlternativo(String asuntoAlternativo)
    {
        this.asuntoAlternativo = asuntoAlternativo;
    }

    public String getDescripcionAlternativa()
    {
        return descripcionAlternativa;
    }

    public void setDescripcionAlternativa(String descripcionAlternativa)
    {
        this.descripcionAlternativa = descripcionAlternativa;
    }

    public String getUbicacionAlternativa()
    {
        return ubicacionAlternativa;
    }

    public void setUbicacionAlternativa(String ubicacionAlternativa)
    {
        this.ubicacionAlternativa = ubicacionAlternativa;
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
}
