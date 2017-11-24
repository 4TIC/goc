package es.uji.apps.goc.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "GOC_VW_REUNIONES_EDITORES")
public class ReunionEditor
{
    @Id
    private Long id;

    private String asunto;

    @Column(name = "ASUNTO_ALT")
    private String asuntoAlternativo;

    private Date fecha;

    private Long duracion;

    @Column(name = "EDITOR_ID")
    private Long editorId;

    private Boolean completada;

    private Boolean externo;

    @Column(name = "ORGANO_ID")
    private String organoId;

    @Column(name = "TIPO_ORGANO_ID")
    private Long tipoOrganoId;

    @Column(name = "NUM_DOCUMENTOS")
    private Long numeroDocumentos;

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

    public ReunionEditor()
    {
    }

    public ReunionEditor(Long reunionId)
    {
        this.id = reunionId;
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

    public String getAsuntoAlternativo()
    {
        return asuntoAlternativo;
    }

    public void setAsuntoAlternativo(String asuntoAlternativo)
    {
        this.asuntoAlternativo = asuntoAlternativo;
    }

    public Date getFecha()
    {
        return fecha;
    }

    public void setFecha(Date fecha)
    {
        this.fecha = fecha;
    }

    public Long getDuracion()
    {
        return duracion;
    }

    public void setDuracion(Long duracion)
    {
        this.duracion = duracion;
    }

    public Boolean getCompletada()
    {
        return completada;
    }

    public void setCompletada(Boolean completada)
    {
        this.completada = completada;
    }

    public Boolean getExterno()
    {
        return externo;
    }

    public void setExterno(Boolean externo)
    {
        this.externo = externo;
    }

    public String getOrganoId()
    {
        return organoId;
    }

    public void setOrganoId(String organoId)
    {
        this.organoId = organoId;
    }

    public Long getTipoOrganoId()
    {
        return tipoOrganoId;
    }

    public void setTipoOrganoId(Long tipoOrganoId)
    {
        this.tipoOrganoId = tipoOrganoId;
    }

    public Long getNumeroDocumentos()
    {
        return numeroDocumentos;
    }

    public void setNumeroDocumentos(Long nummeroDocumentos)
    {
        this.numeroDocumentos = nummeroDocumentos;
    }

    public Long getEditorId()
    {
        return editorId;
    }

    public void setEditorId(Long editorId)
    {
        this.editorId = editorId;
    }

    public Boolean isAvisoPrimeraReunion()
    {
        return avisoPrimeraReunion;
    }

    public void setAvisoPrimeraReunion(Boolean avisoPrimeraReunion)
    {
        this.avisoPrimeraReunion = avisoPrimeraReunion;
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
}
