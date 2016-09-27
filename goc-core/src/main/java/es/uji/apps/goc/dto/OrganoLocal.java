package es.uji.apps.goc.dto;

import java.io.Serializable;
import java.util.Date;
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
@Table(name = "GOC_ORGANOS")
public class OrganoLocal implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;

    @Column(name="CREADOR_ID")
    private Long creadorId;

    @Column(name="FECHA_CREACION")
    private Date fechaCreacion;

    @OneToMany(mappedBy = "organo", cascade = CascadeType.REMOVE)
    private Set<OrganoReunion> organoReuniones;

    @OneToMany(mappedBy = "organo", cascade = CascadeType.REMOVE)
    private Set<MiembroLocal> miembros;

    @ManyToOne
    @JoinColumn(name = "TIPO_ORGANO_ID")
    private TipoOrganoLocal tipoOrgano;

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

    public TipoOrganoLocal getTipoOrganoLocal()
    {
        return tipoOrgano;
    }

    public void setTipoOrganoLocal(TipoOrganoLocal tipoOrgano)
    {
        this.tipoOrgano = tipoOrgano;
    }

    public OrganoLocal() {}

    public OrganoLocal(Long id, String nombre, TipoOrganoLocal tipoOrgano) {
        this.id = id;
        this.nombre = nombre;
        this.tipoOrgano = tipoOrgano;
    }

    public OrganoLocal(Long id) {
        this.id = id;
    }

    public Set<OrganoReunion> getOrganoReuniones()
    {
        return organoReuniones;
    }

    public void setOrganoReuniones(Set<OrganoReunion> organoReuniones)
    {
        this.organoReuniones = organoReuniones;
    }

    public Long getCreadorId()
    {
        return creadorId;
    }

    public void setCreadorId(Long creadorId)
    {
        this.creadorId = creadorId;
    }

    public Date getFechaCreacion()
    {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion)
    {
        this.fechaCreacion = fechaCreacion;
    }

    public Set<MiembroLocal> getMiembros()
    {
        return miembros;
    }

    public void setMiembros(Set<MiembroLocal> miembros)
    {
        this.miembros = miembros;
    }

    public TipoOrganoLocal getTipoOrgano()
    {
        return tipoOrgano;
    }

    public void setTipoOrgano(TipoOrganoLocal tipoOrgano)
    {
        this.tipoOrgano = tipoOrgano;
    }
}
