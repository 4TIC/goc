package es.uji.apps.goc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

import es.uji.apps.goc.dto.PersonaExterna;

public class JSONListaPersonasExternasDeserializer
{
    @JsonProperty("success")
    public String success;

    @JsonProperty("data")
    @JsonDeserialize(as= ArrayList.class, contentAs=PersonaExterna.class)
    public List<PersonaExterna> data;

    public List<PersonaExterna> getPersonas()
    {
        return data;
    }
}
