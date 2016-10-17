package es.uji.apps.goc.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class JSONListaRolesPersonaDeserializer extends JsonDeserializer<List<Rol>>
{
    @JsonProperty("success")
    public String success;

    @JsonProperty("data")
    @JsonDeserialize(as= ArrayList.class, contentAs=Rol.class)
    public List<Rol> data;

    public List<Rol> getRoles()
    {
        return data;
    }

    @Override
    public List<Rol> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        List<Rol> listaRoles = new ArrayList<>();

        ObjectCodec oc = p.getCodec();
        JsonNode nodes = oc.readTree(p);

        for (JsonNode node: nodes) {
            listaRoles.add(getRolPersonaDesdeJsonNode(node));
        }
        return listaRoles;
    }

    private Rol getRolPersonaDesdeJsonNode(JsonNode rolNode) {
        Rol rol = new Rol();
        rol.setId(rolNode.get("id").asLong());
        rol.setNombre(rolNode.get("nombre").asText());

        return rol;
    }
}
