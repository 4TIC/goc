package es.uji.apps.goc.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CargoExterno implements Serializable
{
    @JsonProperty("cargo_id")
    private Long id;

    @JsonProperty("cargo_nombre")
    private String nombre;

    @JsonProperty("cargo_nombre_alternativo")
    private String nombreAlternativo;

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

    public String getNombreAlternativo()
    {
        return nombreAlternativo;
    }

    public void setNombreAlternativo(String nombreAlternativo)
    {
        this.nombreAlternativo = nombreAlternativo;
    }
}
