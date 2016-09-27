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
import es.uji.apps.goc.dto.PersonaExterna;

public class PersonaExternaDeserializer extends JsonDeserializer<PersonaExterna>
{
    @Override
    public PersonaExterna deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException
    {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        JsonNode data = node.get("data");

        Long id = data.get("id").asLong();
        String nombre = data.get("nombre").asText();
        String email = data.get("email").asText();

        PersonaExterna persona = new PersonaExterna();
        persona.setId(id);
        persona.setNombre(nombre);
        persona.setEmail(email);

        return persona;
    }

}
