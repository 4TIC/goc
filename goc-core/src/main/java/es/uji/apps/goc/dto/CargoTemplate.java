package es.uji.apps.goc.dto;

import es.uji.apps.goc.model.Miembro;

import java.util.Set;

public class CargoTemplate
{
    private String id;

    private String nombre;

    private Set<Miembro> miembros;

    public CargoTemplate() {}

    public CargoTemplate(String id)
    {
        this.id = id;
    }

    public CargoTemplate(String id, String nombre)
    {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
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
}
