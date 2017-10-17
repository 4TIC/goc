package es.uji.apps.goc.dto;

import es.uji.apps.goc.exceptions.InvalidAccessException;
import es.uji.apps.goc.model.Persona;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
@Table(name = "GOC_REUNIONES")
public class Reunion implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "reunion")
    private Set<ReunionBusqueda> reunionBusqueda;

    private String asunto;

    @Column(name = "ASUNTO_ALT")
    private String asuntoAlternativo;

    private Date fecha;

    @Column(name = "FECHA_SEGUNDA_CONVOCATORIA")
    private Date fechaSegundaConvocatoria;

    private Long duracion;

    private String descripcion;

    @Column(name = "DESCRIPCION_ALT")
    private String descripcionAlternativa;

    private String ubicacion;

    @Column(name = "UBICACION_ALT")
    private String ubicacionAlternativa;

    @Column(name = "URL_GRABACION")
    private String urlGrabacion;

    @Column(name = "CREADOR_ID")
    private Long creadorId;

    @Column(name = "CREADOR_NOMBRE")
    private String creadorNombre;

    @Column(name = "CREADOR_EMAIL")
    private String creadorEmail;

    @Column(name = "FECHA_CREACION")
    private Date fechaCreacion;

    private Boolean completada;

    @Column(name = "ADMITE_SUPLENCIA")
    private Boolean admiteSuplencia;

    @Column(name = "ADMITE_COMENTARIOS")
    private Boolean admiteComentarios;

    @Column(name = "ADMITE_DELEGACION_VOTO")
    private Boolean admiteDelegacionVoto;

    private Boolean telematica;

    @Column(name = "TELEMATICA_DESCRIPCION")
    private String telematicaDescripcion;

    @Column(name = "TELEMATICA_DESCRIPCION_ALT")
    private String telematicaDescripcionAlternativa;

    private Boolean publica;

    private String acuerdos;

    @Column(name = "ACUERDOS_ALT")
    private String acuerdosAlternativos;

    @Column(name = "NUMERO_SESION")
    private Long numeroSesion;

    @Column(name = "FECHA_COMPLETADA")
    private Date fechaCompletada;

    @Column(name = "NOTIFICADA")
    private Boolean notificada;

    @Column(name = "AVISO_PRIMERA_REUNION")
    private Boolean avisoPrimeraReunion;

    @Column(name = "AVISO_PRIMERA_REUNION_USER")
    private String avisoPrimeraReunionUser;

    @Column(name = "AVISO_PRIMERA_REUNION_FECHA")
    private Date avisoPrimeraReunionFecha;

    @Column(name = "URL_ACTA")
    private String urlActa;

    @Column(name = "URL_ACTA_ALT")
    private String urlActaAlternativa;

    @OneToOne
    @JoinColumn(name = "MIEMBRO_RESPONSABLE_ACTA_ID")
    private OrganoReunionMiembro miembroResponsableActa;

    @OneToMany(mappedBy = "reunion", cascade = CascadeType.REMOVE)
    private Set<OrganoReunion> reunionOrganos;

    @OneToMany(mappedBy = "reunion", cascade = CascadeType.REMOVE)
    private Set<ReunionDocumento> reunionDocumentos;

    @OneToMany(mappedBy = "reunion", cascade = CascadeType.REMOVE)
    private Set<PuntoOrdenDia> reunionPuntosOrdenDia;

    @OneToMany(mappedBy = "reunion", cascade = CascadeType.REMOVE)
    private Set<ReunionComentario> comentarios;

    @OneToMany(mappedBy = "reunion", cascade = CascadeType.REMOVE)
    private Set<ReunionInvitado> reunionInvitados;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Reunion()
    {
    }

    public Reunion(Long reunionId)
    {
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

    public Date getFechaCompletada()
    {
        return fechaCompletada;
    }

    public void setFechaCompletada(Date fechaCompletada)
    {
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

    public String getAcuerdos()
    {
        return acuerdos;
    }

    public void setAcuerdos(String acuerdos)
    {
        this.acuerdos = acuerdos;
    }

    public String getUbicacion()
    {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion)
    {
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

    public String getUrlGrabacion()
    {
        return urlGrabacion;
    }

    public void setUrlGrabacion(String urlGrabacion)
    {
        this.urlGrabacion = urlGrabacion;
    }

    public Boolean isPublica()
    {
        return publica;
    }

    public void setPublica(Boolean publica)
    {
        this.publica = publica;
    }

    public Set<ReunionComentario> getComentarios()
    {
        return comentarios;
    }

    public void setComentarios(Set<ReunionComentario> comentarios)
    {
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

    public Boolean isNotificada()
    {
        return notificada;
    }

    public void setNotificada(Boolean notificada)
    {
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

    public Boolean isAdmiteSuplencia()
    {
        return admiteSuplencia;
    }

    public void setAdmiteSuplencia(Boolean admiteSuplencia)
    {
        this.admiteSuplencia = admiteSuplencia;
    }

    public Boolean isAdmiteComentarios()
    {
        return admiteComentarios;
    }

    public void setAdmiteComentarios(Boolean admiteComentarios)
    {
        this.admiteComentarios = admiteComentarios;
    }

    public Boolean isAdmiteDelegacionVoto()
    {
        return admiteDelegacionVoto;
    }

    public void setAdmiteDelegacionVoto(Boolean admiteDelegacionVoto)
    {
        this.admiteDelegacionVoto = admiteDelegacionVoto;
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

    public String getTelematicaDescripcionAlternativa()
    {
        return telematicaDescripcionAlternativa;
    }

    public void setTelematicaDescripcionAlternativa(String telematicaDescripcionAlternativa)
    {
        this.telematicaDescripcionAlternativa = telematicaDescripcionAlternativa;
    }

    public String getAcuerdosAlternativos()
    {
        return acuerdosAlternativos;
    }

    public void setAcuerdosAlternativos(String acuerdosAlternativos)
    {
        this.acuerdosAlternativos = acuerdosAlternativos;
    }

    public boolean noContieneMiembros()
    {
        return !contieneMiembros();
    }

    private boolean contieneMiembros()
    {
        if (reunionOrganos == null || reunionOrganos.isEmpty()) return false;

        long numeroOrganosConMiembros =
                reunionOrganos.stream().filter(organo -> organo.getMiembros().size() > 0).count();

        return (numeroOrganosConMiembros > 0);
    }

    public boolean isPermitirComentarios(Long connectedUserId)
    {
        Set<OrganoReunion> reunionOrganos = getReunionOrganos();

        boolean permitirComentarios = false;
        for (OrganoReunion organoReunion : reunionOrganos)
        {
            Set<OrganoReunionMiembro> miembros = organoReunion.getMiembros();

            for (OrganoReunionMiembro miembro : miembros)
            {
                if (miembro.getMiembroId().toString().equals(connectedUserId.toString()) || connectedUserId.equals(
                        miembro.getSuplenteId()))
                {
                    permitirComentarios = true;
                }
            }
        }

        if (getCreadorId().equals(connectedUserId))
        {
            permitirComentarios = true;
        }

        return permitirComentarios;
    }

    public Boolean getAvisoPrimeraReunion()
    {
        return avisoPrimeraReunion;
    }

    public void setAvisoPrimeraReunion(Boolean avisoPrimeraReunion)
    {
        this.avisoPrimeraReunion = avisoPrimeraReunion;
    }

    public Set<ReunionInvitado> getReunionInvitados()
    {
        return reunionInvitados;
    }

    public void setReunionInvitados(Set<ReunionInvitado> reunionInvitados)
    {
        this.reunionInvitados = reunionInvitados;
    }

    public Set<ReunionBusqueda> getReunionBusqueda()
    {
        return reunionBusqueda;
    }

    public void setReunionBusqueda(Set<ReunionBusqueda> reunionBusqueda)
    {
        this.reunionBusqueda = reunionBusqueda;
    }

    public String getUrlActa()
    {
        return urlActa;
    }

    public void setUrlActa(String urlActa)
    {
        this.urlActa = urlActa;
    }

    public String getUrlActaAlternativa()
    {
        return urlActaAlternativa;
    }

    public void setUrlActaAlternativa(String urlActaAlternativa)
    {
        this.urlActaAlternativa = urlActaAlternativa;
    }

    public String getAvisoPrimeraReunionUser()
    {
        return avisoPrimeraReunionUser;
    }

    public void setAvisoPrimeraReunionUser(String avisoPrimeraReunionUser)
    {
        this.avisoPrimeraReunionUser = avisoPrimeraReunionUser;
    }

    public Date getAvisoPrimeraReunionFecha()
    {
        return avisoPrimeraReunionFecha;
    }

    public void setAvisoPrimeraReunionFecha(Date avisoPrimeraReunionFecha)
    {
        this.avisoPrimeraReunionFecha = avisoPrimeraReunionFecha;
    }
}
