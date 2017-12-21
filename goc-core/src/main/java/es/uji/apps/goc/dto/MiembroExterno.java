package es.uji.apps.goc.dto;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import es.uji.apps.goc.model.MiembroExternoDeserializer;

@JsonDeserialize(using = MiembroExternoDeserializer.class)
public class MiembroExterno implements Serializable
{
    private Long id;

    private String nombre;

    private String email;

    private CargoExterno cargo;

    private OrganoExterno organo;

    private String condicion;

    private String condicionAlternativa;

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

    public OrganoExterno getOrgano()
    {
        return organo;
    }

    public void setOrgano(OrganoExterno organo)
    {
        this.organo = organo;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public MiembroExterno() {}

    public CargoExterno getCargo()
    {
        return cargo;
    }

    public void setCargo(CargoExterno cargo)
    {
        this.cargo = cargo;
    }

    public String getCondicion()
    {
        return condicion;
    }

    public void setCondicion(String condicion)
    {
        this.condicion = condicion;
    }

    public String getCondicionAlternativa()
    {
        return condicionAlternativa;
    }

    public void setCondicionAlternativa(String condicionAlternativa)
    {
        this.condicionAlternativa = condicionAlternativa;
    }
}
