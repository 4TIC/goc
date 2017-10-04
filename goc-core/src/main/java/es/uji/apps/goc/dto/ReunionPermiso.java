package es.uji.apps.goc.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "GOC_VW_REUNIONES_PERMISOS")
public class ReunionPermiso
{
    @Id
    private Long id;

    private String asunto;

    @Column(name = "ASUNTO_ALT")
    private String asuntoAlternativo;

    private Date fecha;

    private Boolean completada;

    private Boolean asistente;

    @Column(name = "PERSONA_ID")
    private Long personaId;

    @Column(name = "PERSONA_NOMBRE")
    private String personaNombre;

    @Column(name = "URL_ACTA")
    private String urlActa;

    @Column(name = "URL_ACTA_ALT")
    private String urlActaAlternativa;

    @Column(name = "URL_ASISTENCIA")
    private String urlAsistencia;

    @Column(name = "URL_ASISTENCIA_ALT")
    private String urlAsistenciaAlternativa;

    public ReunionPermiso()
    {
    }

    public ReunionPermiso(Long reunionId)
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

    public Boolean getCompletada()
    {
        return completada;
    }

    public void setCompletada(Boolean completada)
    {
        this.completada = completada;
    }

    public Boolean getAsistente()
    {
        return asistente;
    }

    public void setAsistente(Boolean asistente)
    {
        this.asistente = asistente;
    }

    public Long getPersonaId()
    {
        return personaId;
    }

    public void setPersonaId(Long personaId)
    {
        this.personaId = personaId;
    }

    public String getPersonaNombre()
    {
        return personaNombre;
    }

    public void setPersonaNombre(String personaNombre)
    {
        this.personaNombre = personaNombre;
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

    public String getUrlAsistencia()
    {
        return urlAsistencia;
    }

    public void setUrlAsistencia(String urlAsistencia)
    {
        this.urlAsistencia = urlAsistencia;
    }

    public String getUrlAsistenciaAlternativa()
    {
        return urlAsistenciaAlternativa;
    }

    public void setUrlAsistenciaAlternativa(String urlAsistenciaAlternativa)
    {
        this.urlAsistenciaAlternativa = urlAsistenciaAlternativa;
    }
}
