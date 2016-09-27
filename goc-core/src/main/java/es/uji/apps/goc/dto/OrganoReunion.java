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

    @ManyToOne
    @JoinColumn(name = "ORGANO_ID")
    private OrganoLocal organo;

    @ManyToOne
    @JoinColumn(name = "REUNION_ID")
    private Reunion reunion;

    @Column(name = "ORGANO_EXTERNO_ID")
    private String organoExternoId;

    @OneToMany(mappedBy = "organoReunion", cascade = CascadeType.REMOVE)
    private Set<OrganoReunionMiembro> miembros;

    public OrganoLocal getOrganoLocal()
    {
        return organo;
    }

    public void setOrganoLocal(OrganoLocal organo)
    {
        this.organo = organo;
    }

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

    public String getOrganoExternoId()
    {
        return organoExternoId;
    }

    public void setOrganoExternoId(String organoExternoId)
    {
        this.organoExternoId = organoExternoId;
    }

    public Set<OrganoReunionMiembro> getMiembros() {
        return miembros;
    }

    public void setMiembros(Set<OrganoReunionMiembro> miembros) {
        this.miembros = miembros;
    }
}
