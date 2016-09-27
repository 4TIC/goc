package es.uji.apps.goc.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import es.uji.apps.goc.model.MiembroExternoDeserializer;
import es.uji.apps.goc.model.PersonaExternaDeserializer;

@JsonDeserialize(using = PersonaExternaDeserializer.class)
public class PersonaExterna implements Serializable
{
    private Long id;

    private String nombre;

    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
