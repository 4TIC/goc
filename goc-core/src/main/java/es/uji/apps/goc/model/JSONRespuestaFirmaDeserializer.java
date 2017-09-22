package es.uji.apps.goc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import es.uji.apps.goc.dto.MiembroExterno;

import java.util.ArrayList;
import java.util.List;

public class JSONRespuestaFirmaDeserializer
{
    @JsonProperty("success")
    public String success;

    @JsonProperty("data")
    @JsonDeserialize(contentAs=RespuestaFirma.class)
    public RespuestaFirma data;

    public RespuestaFirma getRespuestaFirma()
    {
        return data;
    }
}
