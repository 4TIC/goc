package es.uji.apps.goc.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "GOC_REUNIONES")
public class Reunion implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String asunto;

    private Date fecha;

    private Long duracion;

    private String descripcion;

    private String ubicacion;

    @Column(name="URL_GRABACION")
    private String urlGrabacion;

    @Column(name="CREADOR_ID")
    private Long creadorId;

    @Column(name="CREADOR_NOMBRE")
    private String creadorNombre;

    @Column(name="CREADOR_EMAIL")
    private String creadorEmail;

    @Column(name="FECHA_CREACION")
    private Date fechaCreacion;

    private Boolean completada;

    private Boolean telematica;

    @Column(name="TELEMATICA_DESCRIPCION")
    private String telematicaDescripcion;

    private Boolean publica;

    private String acuerdos;

    @Column(name="NUMERO_SESION")
    private Long numeroSesion;

    @Column(name="FECHA_COMPLETADA")
    private Date fechaCompletada;

    @Column(name="NOTIFICADA")
    private Boolean notificada;

    @OneToOne
    @JoinColumn(name="MIEMBRO_RESPONSABLE_ACTA_ID")
    private OrganoReunionMiembro miembroResponsableActa;

    @OneToMany(mappedBy = "reunion", cascade = CascadeType.REMOVE)
    private Set<OrganoReunion> reunionOrganos;

    @OneToMany(mappedBy = "reunion", cascade = CascadeType.REMOVE)
    private Set<ReunionDocumento> reunionDocumentos;

    @OneToMany(mappedBy = "reunion", cascade = CascadeType.REMOVE)
    private Set<PuntoOrdenDia> reunionPuntosOrdenDia;

    @OneToMany(mappedBy = "reunion", cascade = CascadeType.REMOVE)
    private Set<ReunionComentario> comentarios;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Reunion() {}

    public Reunion(Long reunionId) {
        this.id = reunionId;
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

    public Set<OrganoReunion> getReunionOrganos()
    {
        return reunionOrganos;
    }

    public void setReunionOrganos(Set<OrganoReunion> reunionOrganos)
    {
        this.reunionOrganos = reunionOrganos;
    }

    public Long getDuracion()
    {
        return duracion;
    }

    public void setDuracion(Long duracion)
    {
        this.duracion = duracion;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    public Long getCreadorId()
    {
        return creadorId;
    }

    public void setCreadorId(Long creadorId)
    {
        this.creadorId = creadorId;
    }

    public Date getFechaCreacion()
    {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion)
    {
        this.fechaCreacion = fechaCreacion;
    }

    public Boolean isCompletada()
    {
        return completada;
    }

    public void setCompletada(Boolean completada)
    {
        this.completada = completada;
    }

    public Date getFechaCompletada() {
        return fechaCompletada;
    }

    public void setFechaCompletada(Date fechaCompletada) {
        this.fechaCompletada = fechaCompletada;
    }

    public Boolean getCompletada()
    {
        return completada;
    }

    public Set<ReunionDocumento> getReunionDocumentos()
    {
        return reunionDocumentos;
    }

    public void setReunionDocumentos(Set<ReunionDocumento> reunionDocumentos)
    {
        this.reunionDocumentos = reunionDocumentos;
    }

    public Set<PuntoOrdenDia> getReunionPuntosOrdenDia()
    {
        return reunionPuntosOrdenDia;
    }

    public void setReunionPuntosOrdenDia(Set<PuntoOrdenDia> reunionPuntosOrdenDia)
    {
        this.reunionPuntosOrdenDia = reunionPuntosOrdenDia;
    }

    public String getAcuerdos() {
        return acuerdos;
    }

    public void setAcuerdos(String acuerdos) {
        this.acuerdos = acuerdos;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Long getNumeroSesion()
    {
        return numeroSesion;
    }

    public void setNumeroSesion(Long numeroSesion)
    {
        this.numeroSesion = numeroSesion;
    }

    public String getUrlGrabacion() {
        return urlGrabacion;
    }

    public void setUrlGrabacion(String urlGrabacion) {
        this.urlGrabacion = urlGrabacion;
    }

    public Boolean isPublica() {
        return publica;
    }

    public void setPublica(Boolean publica) {
        this.publica = publica;
    }

    public Set<ReunionComentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(Set<ReunionComentario> comentarios) {
        this.comentarios = comentarios;
    }

    public OrganoReunionMiembro getMiembroResponsableActa()
    {
        return miembroResponsableActa;
    }

    public void setMiembroResponsableActa(OrganoReunionMiembro miembroResponsableActa)
    {
        this.miembroResponsableActa = miembroResponsableActa;
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

    public Boolean isNotificada() {
        return notificada;
    }

    public void setNotificada(Boolean notificada) {
        this.notificada = notificada;
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
