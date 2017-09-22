package es.uji.apps.goc.dto;

import es.uji.apps.goc.model.Comentario;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ReunionTemplate implements Serializable
{
    private Long id;

    private String asunto;

    private Date fecha;

    private Date fechaSegundaConvocatoria;

    private Long numeroSesion;

    private String descripcion;

    private Long duracion;

    private String ubicacion;

    private String acuerdos;

    private Boolean telematica;

    private Boolean completada;

    private Boolean admiteSuplencia;

    private Boolean admiteDelegacionVoto;

    private Boolean admiteComentarios;

    private String telematicaDescripcion;

    private String creadorNombre;

    private String creadorEmail;

    private Long creadorId;

    private String urlGrabacion;

    private Boolean comoAsistente;

    private String responsableActa;

    private String cargoResponsableActa;

    private List<OrganoTemplate> organos;

    private List<Comentario> comentarios;

    private List<PuntoOrdenDiaTemplate> puntosOrdenDia;

    private List<DocumentoTemplate> documentos;

    private List<InvitadoTemplate> invitados;

    private String urlActa;

    public ReunionTemplate()
    {
    }

    public ReunionTemplate(Long id)
    {
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

    public List<OrganoTemplate> getOrganos()
    {
        return organos;
    }

    public void setOrganos(List<OrganoTemplate> organos)
    {
        this.organos = organos;
    }

    public List<DocumentoTemplate> getDocumentos()
    {
        return documentos;
    }

    public void setDocumentos(List<DocumentoTemplate> documentos)
    {
        this.documentos = documentos;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
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

    public String getUrlGrabacion()
    {
        return urlGrabacion;
    }

    public void setUrlGrabacion(String urlGrabacion)
    {
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

    public Boolean isAdmiteSuplencia()
    {
        return admiteSuplencia;
    }

    public void setAdmiteSuplencia(Boolean admiteSuplencia)
    {
        this.admiteSuplencia = admiteSuplencia;
    }

    public Boolean isComoAsistente()
    {
        return comoAsistente;
    }

    public void setComoAsistente(Boolean comoAsistente)
    {
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

    public String getCargoResponsableActa()
    {
        return cargoResponsableActa;
    }

    public void setCargoResponsableActa(String cargoResponsableActa)
    {
        this.cargoResponsableActa = cargoResponsableActa;
    }

    public Long getCreadorId()
    {
        return creadorId;
    }

    public void setCreadorId(Long creadorId)
    {
        this.creadorId = creadorId;
    }

    public List<InvitadoTemplate> getInvitados()
    {
        return invitados;
    }

    public void setInvitados(List<InvitadoTemplate> invitados)
    {
        this.invitados = invitados;
    }

    public Boolean getAdmiteDelegacionVoto()
    {
        return admiteDelegacionVoto;
    }

    public void setAdmiteDelegacionVoto(Boolean admiteDelegacionVoto)
    {
        this.admiteDelegacionVoto = admiteDelegacionVoto;
    }

    public String getUrlActa()
    {
        return urlActa;
    }

    public void setUrlActa(String urlActa)
    {
        this.urlActa = urlActa;
    }
}
