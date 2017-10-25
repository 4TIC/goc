package es.uji.apps.goc.dto;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "GOC_ORGANOS_INVITADOS")
public class OrganoInvitado implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "PERSONA_ID")
    private Long personaId;

    @Column(name = "PERSONA_NOMBRE")
    private String personaNombre;

    @Column(name = "PERSONA_EMAIL")
    private String personaEmail;

    @Column(name = "ORGANO_ID")
    private String organoId;

    @Column(name = "SOLO_CONSULTA")
    private Boolean soloConsulta;

    public OrganoInvitado()
    {
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getPersonaId()
    {
        return personaId;
    }

    public void setPersonaId(Long personaId)
    {
        this.personaId = personaId;
    }

    public String getOrganoId()
    {
        return organoId;
    }

    public void setOrganoId(String organoId)
    {
        this.organoId = organoId;
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

    public Boolean isSoloConsulta()
    {
        return soloConsulta;
    }

    public void setSoloConsulta(Boolean soloConsulta)
    {
        this.soloConsulta = soloConsulta;
    }
}
