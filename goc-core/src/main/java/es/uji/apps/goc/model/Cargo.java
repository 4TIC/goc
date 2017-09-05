package es.uji.apps.goc.model;

import java.util.Set;

public class Cargo
{
    private String id;

    private String codigo;

    private String nombre;

    private String nombreAlternativo;

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

    public String getNombreAlternativo()
    {
        return nombreAlternativo;
    }

    public void setNombreAlternativo(String nombreAlternativo)
    {
        this.nombreAlternativo = nombreAlternativo;
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
