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

    @JsonProperty("cargo_firma")
    private Boolean firma;

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

    public Boolean isFirma()
    {
        return firma;
    }

    public void setFirma(Boolean firma)
    {
        this.firma = firma;
    }
}
