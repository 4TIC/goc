package es.uji.apps.goc.dto;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "GOC_TIPOS_ORGANO")
public class TipoOrganoLocal implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String codigo;

    private String nombre;

    @Column(name = "NOMBRE_ALT")
    private String nombreAlternativo;

    @OneToMany(mappedBy = "tipoOrgano")
    private Set<OrganoLocal> organos;

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

    public TipoOrganoLocal() {}

    public TipoOrganoLocal(Long id) {
        this.id = id;
    }

    public TipoOrganoLocal(Long id, String codigo, String nombre) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public Set<OrganoLocal> getOrganos() {
        return organos;
    }

    public void setOrganos(Set<OrganoLocal> organos) {
        this.organos = organos;
    }

    public String getCodigo()
    {
        return codigo;
    }

    public void setCodigo(String codigo)
    {
        this.codigo = codigo;
    }

    public String getNombreAlternativo()
    {
        return nombreAlternativo;
    }

    public void setNombreAlternativo(String nombreAlternativo)
    {
        this.nombreAlternativo = nombreAlternativo;
    }
}
