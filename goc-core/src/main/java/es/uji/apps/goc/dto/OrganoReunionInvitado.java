package es.uji.apps.goc.dto;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "GOC_ORGANOS_REUNIONES_INVITS")
public class OrganoReunionInvitado implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;

    private String email;

    @Column(name = "ORGANO_ID")
    private String organoId;

    @Column(name = "ORGANO_EXTERNO")
    private Boolean organoExterno;

    @Column(name = "REUNION_ID")
    private Long reunionId;

    @Column(name = "PERSONA_ID")
    private String personaId;

    @Column(name = "URL_ASISTENCIA")
    private String urlAsistencia;

    @Column(name = "URL_ASISTENCIA_ALT")
    private String urlAsistenciaAlternativa;

    @Column(name = "SOLO_CONSULTA")
    private Boolean soloConsulta;

    @ManyToOne
    @JoinColumn(name = "ORGANO_REUNION_ID")
    private OrganoReunion organoReunion;

    public OrganoReunionInvitado()
    {
    }

    public OrganoReunionInvitado(Long id)
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

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public OrganoReunion getOrganoReunion()
    {
        return organoReunion;
    }

    public void setOrganoReunion(OrganoReunion organoReunion)
    {
        this.organoReunion = organoReunion;
    }

    public String getOrganoId()
    {
        return organoId;
    }

    public void setOrganoId(String organoId)
    {
        this.organoId = organoId;
    }

    public Long getReunionId()
    {
        return reunionId;
    }

    public void setReunionId(Long reunionId)
    {
        this.reunionId = reunionId;
    }

    public Boolean getOrganoExterno()
    {
        return organoExterno;
    }

    public void setOrganoExterno(Boolean organoExterno)
    {
        this.organoExterno = organoExterno;
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

    public String getPersonaId()
    {
        return personaId;
    }

    public void setPersonaId(String personaId)
    {
        this.personaId = personaId;
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
