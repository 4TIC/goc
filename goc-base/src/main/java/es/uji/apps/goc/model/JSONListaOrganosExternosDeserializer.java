package es.uji.apps.goc.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import es.uji.apps.goc.dto.OrganoExterno;

public class JSONListaOrganosExternosDeserializer
{
    @JsonProperty("success")
    public String success;

    @JsonProperty("data")
    @JsonDeserialize(as= ArrayList.class, contentAs=OrganoExterno.class)
    public List<OrganoExterno> data;

    public List<OrganoExterno> getOrganos()
    {
        return data;
    }
}
