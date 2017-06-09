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
}
