package es.uji.apps.goc.model;

import java.util.Set;

public class TipoOrgano
{
    private Long id;

    private String codigo;

    private String nombre;

    private Set<Organo> organos;

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

    public TipoOrgano() {}

    public TipoOrgano(Long id) {
        this.id = id;
    }

    public TipoOrgano(Long id, String codigo, String nombre) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public Set<Organo> getOrganos() {
        return organos;
    }

    public void setOrganos(Set<Organo> organos) {
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
}
