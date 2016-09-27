package es.uji.apps.goc.model;

import java.util.Set;

public class Cargo
{
    private String id;

    private String nombre;

    private Set<Miembro> miembros;

    public Cargo() {}

    public Cargo(String id)
    {
        this.id = id;
    }

    public Cargo(String id, String nombre)
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
