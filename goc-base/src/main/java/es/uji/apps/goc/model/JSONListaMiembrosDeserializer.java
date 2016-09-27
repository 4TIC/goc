package es.uji.apps.goc.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import es.uji.apps.goc.dto.MiembroExterno;

public class JSONListaMiembrosDeserializer
{
    @JsonProperty("success")
    public String success;

    @JsonProperty("data")
    @JsonDeserialize(as= ArrayList.class, contentAs=MiembroExterno.class)
    public List<MiembroExterno> data;

    public List<MiembroExterno> getMiembros()
    {
        return data;
    }
}
