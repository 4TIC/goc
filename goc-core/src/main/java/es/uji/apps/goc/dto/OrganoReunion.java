package es.uji.apps.goc.dto;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "GOC_ORGANOS_REUNIONES")
public class OrganoReunion implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ORGANO_ID")
    private String organoId;

    @Column(name = "EXTERNO")
    private Boolean externo;

    @ManyToOne
    @JoinColumn(name = "REUNION_ID")
    private Reunion reunion;

    @Column(name = "ORGANO_NOMBRE")
    private String organoNombre;

    @Column(name = "ORGANO_NOMBRE_ALT")
    private String organoNombreAlternativo;

    @Column(name = "TIPO_ORGANO_ID")
    private Long tipoOrganoId;

    @OneToMany(mappedBy = "organoReunion", cascade = CascadeType.REMOVE)
    private Set<OrganoReunionMiembro> miembros;

    @OneToMany(mappedBy = "organoReunion", cascade = CascadeType.REMOVE)
    private Set<OrganoReunionInvitado> invitados;

    public Reunion getReunion()
    {
        return reunion;
    }

    public void setReunion(Reunion reunion)
    {
        this.reunion = reunion;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Set<OrganoReunionMiembro> getMiembros() {
        return miembros;
    }

    public void setMiembros(Set<OrganoReunionMiembro> miembros) {
        this.miembros = miembros;
    }

    public String getOrganoNombre()
    {
        return organoNombre;
    }

    public void setOrganoNombre(String organoNombre)
    {
        this.organoNombre = organoNombre;
    }

    public Long getTipoOrganoId()
    {
        return tipoOrganoId;
    }

    public void setTipoOrganoId(Long tipoOrganoId)
    {
        this.tipoOrganoId = tipoOrganoId;
    }

    public String getOrganoId()
    {
        return organoId;
    }

    public void setOrganoId(String organoId)
    {
        this.organoId = organoId;
    }

    public Boolean isExterno()
    {
        return externo;
    }

    public void setExterno(Boolean externo)
    {
        this.externo = externo;
    }

    public String getOrganoNombreAlternativo()
    {
        return organoNombreAlternativo;
    }

    public void setOrganoNombreAlternativo(String organoNombreAlternativo)
    {
        this.organoNombreAlternativo = organoNombreAlternativo;
    }

    public Set<OrganoReunionInvitado> getInvitados()
    {
        return invitados;
    }

    public void setInvitados(Set<OrganoReunionInvitado> invitados)
    {
        this.invitados = invitados;
    }
}
