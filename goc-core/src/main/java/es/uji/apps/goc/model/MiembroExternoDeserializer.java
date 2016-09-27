package es.uji.apps.goc.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import es.uji.apps.goc.dto.CargoExterno;
import es.uji.apps.goc.dto.MiembroExterno;
import es.uji.apps.goc.dto.OrganoExterno;

public class MiembroExternoDeserializer extends JsonDeserializer<MiembroExterno>
{
    @Override
    public MiembroExterno deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException
    {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        Long id = node.get("id").asLong();
        String nombre = node.get("nombre").asText();
        String email = node.get("email").asText();
        String organoId = node.get("organo_id").asText();
        String organoNombre = node.get("organo_nombre").asText();

        Long cargoId = node.get("cargo_id").asLong();
        String cargoNombre = node.get("cargo_nombre").asText();

        CargoExterno cargo = new CargoExterno();
        cargo.setId(cargoId);
        cargo.setNombre(cargoNombre);

        OrganoExterno organo = new OrganoExterno();
        organo.setId(organoId);
        organo.setNombre(organoNombre);

        MiembroExterno miembro = new MiembroExterno();
        miembro.setId(id);
        miembro.setNombre(nombre);
        miembro.setEmail(email);
        miembro.setOrgano(organo);
        miembro.setCargo(cargo);

        return miembro;
    }

}
