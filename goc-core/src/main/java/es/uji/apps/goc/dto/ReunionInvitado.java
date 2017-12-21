package es.uji.apps.goc.dto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "GOC_REUNIONES_INVITADOS")
public class ReunionInvitado implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "REUNION_ID")
    private Reunion reunion;

    @Column(name = "PERSONA_ID")
    private Long personaId;

    @Column(name = "PERSONA_NOMBRE")
    private String personaNombre;

    @Column(name = "PERSONA_EMAIL")
    private String personaEmail;

    @Column(name = "URL_ASISTENCIA")
    private String urlAsistencia;

    @Column(name = "URL_ASISTENCIA_ALT")
    private String urlAsistenciaAlternativa;

    @Column(name = "MOTIVO_INVITACION")
    private String motivoInvitacion;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Reunion getReunion()
    {
        return reunion;
    }

    public void setReunion(Reunion reunion)
    {
        this.reunion = reunion;
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

    public String getPersonaEmail()
    {
        return personaEmail;
    }

    public void setPersonaEmail(String personaEmail)
    {
        this.personaEmail = personaEmail;
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

    public String getMotivoInvitacion()
    {
        return motivoInvitacion;
    }

    public void setMotivoInvitacion(String motivoInvitacion)
    {
        this.motivoInvitacion = motivoInvitacion;
    }
}
