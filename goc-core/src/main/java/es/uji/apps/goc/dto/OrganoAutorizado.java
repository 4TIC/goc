package es.uji.apps.goc.dto;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "GOC_ORGANOS_AUTORIZADOS")
public class OrganoAutorizado implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "PERSONA_ID")
    private Long personaId;

    @Column(name = "PERSONA_NOMBRE")
    private String personaNombre;

    @Column(name = "ORGANO_ID")
    private String organoId;

    @Column(name = "ORGANO_EXTERNO")
    private Boolean organoExterno;


    public OrganoAutorizado()
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

    public Boolean isOrganoExterno()
    {
        return organoExterno;
    }

    public void setOrganoExterno(Boolean organoExterno)
    {
        this.organoExterno = organoExterno;
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
}
