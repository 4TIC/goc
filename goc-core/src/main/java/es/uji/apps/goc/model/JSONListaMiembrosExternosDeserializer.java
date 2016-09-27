package es.uji.apps.goc.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import es.uji.apps.goc.dto.CargoExterno;
import es.uji.apps.goc.dto.MiembroExterno;
import es.uji.apps.goc.dto.OrganoExterno;

public class JSONListaMiembrosExternosDeserializer extends JsonDeserializer<List<MiembroExterno>>
{
    @Override
    public List<MiembroExterno> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        List<MiembroExterno> listaMiembros = new ArrayList<>();

        ObjectCodec oc = p.getCodec();
        JsonNode nodes = oc.readTree(p);

        for (JsonNode node: nodes) {
            listaMiembros.add(getMiembroExternoDesdeJsonNode(node));
        }
        return listaMiembros;
    }

    private MiembroExterno getMiembroExternoDesdeJsonNode(JsonNode miembroNode) {
        MiembroExterno miembro = new MiembroExterno();
        miembro.setId(miembroNode.get("id").asLong());
        miembro.setNombre(miembroNode.get("nombre").asText());
        miembro.setEmail(miembroNode.get("email").asText());

        CargoExterno cargo = new CargoExterno();
        JsonNode cargoNode = miembroNode.get("cargo");
        cargo.setId(cargoNode.get("cargo_id").asLong());
        cargo.setNombre(cargoNode.get("cargo_nombre").asText());
        miembro.setCargo(cargo);

        OrganoExterno organo = new OrganoExterno();
        JsonNode organoNode = miembroNode.get("organo");
        organo.setId(organoNode.get("id").asText());
        organo.setNombre(organoNode.get("nombre").asText());
        organo.setTipoOrganoId(organoNode.get("tipo_id").asLong());
        organo.setTipoCodigo(organoNode.get("tipo_codigo").asText());
        organo.setTipoNombre(organoNode.get("tipo_nombre").asText());
        return miembro;
    }
}
