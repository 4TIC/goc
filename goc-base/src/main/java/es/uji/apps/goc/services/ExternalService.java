package es.uji.apps.goc.services;

import es.uji.apps.goc.model.*;
import es.uji.commons.rest.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExternalService
{
    public List<Organo> getOrganosExternos(Long connectedUserId)
    {
        List<Organo> listaOrganos = new ArrayList<>();

        TipoOrgano t1 = new TipoOrgano(5L, "98", "Departamento Interno");
        TipoOrgano t2 = new TipoOrgano(6L, "121", "Unidad de Gestión Interno");

        for (Integer i = 1; i < 10; i++)
        {
            Organo organo = new Organo("E" + i.toString(), "Organo " + i.toString() + " Interno", t1);
            listaOrganos.add(organo);
        }

        return listaOrganos;
    }

    public List<Miembro> getMiembrosByOrganoId(String organoId, Long connectedUserId)
    {
        List<Miembro> listaMiembros = new ArrayList<>();
        Organo organo = new Organo();
        organo.setId(organoId);
        organo.setNombre("Organo Interno " + organoId);

        Cargo c1 = new Cargo("1");
        c1.setNombre("Presidente");

        Miembro m1 = new Miembro(1L, "Miembro 1 Organo " + organo.getId(),
                "miembro1@organo" + organo.getId() + ".com", organo, c1);

        listaMiembros.add(m1);

        Cargo c2 = new Cargo("2");
        c2.setNombre("Vocal");

        for (Long i = 2L; i < 10L; i++)
        {
            Miembro miembro = new Miembro(i, "Miembro " + i.toString() + " Organo " + organo.getId(),
                    "miembro" + i.toString() + "@organo" + organo.getId() + ".com", organo, c2);
            listaMiembros.add(miembro);
        }

        return listaMiembros;
    }

    public List<Persona> getPersonasByQueryString(String query, Long connectedUserId)
    {

        List<Persona> personas = new ArrayList<>();
        personas.add(new Persona(1L, "Javier Pérez", "jperez@uji.es"));
        personas.add(new Persona(2L, "Joaquin Rodríguez", "jrodriguez@uv.es"));
        personas.add(new Persona(3L, "Antonio Fernández", "afernandez@upv.es"));
        personas.add(new Persona(4L, "Luis Domínguez", "ldominguez@ua.es"));
        personas.add(new Persona(5L, "Jose Ruíz", "jruiz@umh.es"));
        personas.add(new Persona(6L, "Ana Esteve", "aesteve@uv.es"));
        personas.add(new Persona(7L, "Sonia Rovira", "srovira@upv.es"));
        personas.add(new Persona(8L, "Natalia Soraya", "nsoraya@uji.es"));
        personas.add(new Persona(9L, "Andrea Miguelez", "amiguelez@ua.es"));
        personas.add(new Persona(10L, "María Suárez", "msuarez@ua.es"));
        personas.add(new Persona(88849L, "Nicolás Manero", "nmanero@uji.es"));
        personas.add(new Persona(9792L, "Ricardo Borillo Domenech", "borillo@uji.es"));

        if (query == null)
        {
            return personas;
        }

        return personas.stream()
                .filter(persona -> StringUtils.limpiaAcentos(persona.getNombre()).toLowerCase()
                        .contains(StringUtils.limpiaAcentos(query).toLowerCase())
                        || StringUtils.limpiaAcentos(persona.getEmail().toLowerCase())
                        .contains(StringUtils.limpiaAcentos(query).toLowerCase()))
                .collect(Collectors.toList());
    }

    public Persona getPersonaById(Long personaId, Long connectedUserId)
    {
        Persona persona = new Persona(88849L, "Nicolás Manero", "nmanero@uji.es");

        return persona;
    }

    public List<Role> getRolesByPersonaId(Long userId)
    {
        return Arrays.asList(Role.values());
    }
}
