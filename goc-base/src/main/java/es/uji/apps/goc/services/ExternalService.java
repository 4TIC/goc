package es.uji.apps.goc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import es.uji.apps.goc.model.Cargo;
import es.uji.apps.goc.model.Miembro;
import es.uji.apps.goc.model.Organo;
import es.uji.apps.goc.model.Persona;
import es.uji.apps.goc.model.TipoOrgano;
import es.uji.commons.rest.StringUtils;

@Service
public class ExternalService
{
    public List<Organo> getOrganosExternos(Long connectedUserId)
    {
        List<Organo> listaOrganos = new ArrayList<>();

        TipoOrgano t1 = new TipoOrgano(5L, "98", "Departamento Externo");
        TipoOrgano t2 = new TipoOrgano(6L, "121", "Unidad de Gestión Externa");

        for (Integer i=1; i<5; i++) {
            Organo organo = new Organo("E" + i.toString(), "Organo " + i.toString() + " Externo", t1);
            organo.setInactivo(i % 2 == 0);
            listaOrganos.add(organo);

        }

        for (Integer i=5; i<10; i++)
        {
            Organo organo = new Organo("E" + i.toString(), "Organo " + i.toString() + " Externo", t2);
            organo.setInactivo(i % 2 == 0);
            listaOrganos.add(organo);
        }

        return listaOrganos;
    }

    public List<Miembro> getMiembrosByOrganoId(String organoId, Long connectedUserId)
    {
        List<Miembro> listaMiembros = new ArrayList<>();
        Organo organo = new Organo();
        organo.setId(organoId.toString());
        organo.setNombre("Organo Externo 1");

        Cargo c1 = new Cargo("1");
        c1.setNombre("Presidente");

        Miembro m1 = new Miembro(1L, "Miembro 1 Organo " + organo.getId(),
                "miembro1@organo" + organo.getId() + ".com", organo, c1);

        listaMiembros.add(m1);

        Cargo c2 = new Cargo("2");
        c2.setNombre("Vocal");

        for (Long i=2L; i<10L; i++)
        {
            Miembro miembro = new Miembro(i, "Miembro " + i.toString() + " Organo " + organo.getId(),
                    "miembro" + i.toString() + "@organo" + organo.getId() + ".com", organo, c2);
            listaMiembros.add(miembro);
        }

        return listaMiembros;
    }

    public List<Persona> getPersonasByQueryString(String query, Long connectedUserId)
    {
        Persona p1 = new Persona(1L, "Javier Pérez", "jperez@uji.es");
        Persona p2 = new Persona(2L, "Joaquin Rodríguez", "jrodriguez@uv.es");
        Persona p3 = new Persona(3L, "Antonio Fernández", "afernandez@upv.es");
        Persona p4 = new Persona(4L, "Luis Domínguez", "ldominguez@ua.es");
        Persona p5 = new Persona(5L, "Jose Ruíz", "jruiz@umh.es");
        Persona p6 = new Persona(6L, "Ana Esteve", "aesteve@uv.es");
        Persona p7 = new Persona(7L, "Sonia Rovira", "srovira@upv.es");
        Persona p8 = new Persona(8L, "Natalia Soraya", "nsoraya@uji.es");
        Persona p9 = new Persona(9L, "Andrea Miguelez", "amiguelez@ua.es");
        Persona p10 = new Persona(10L, "María Suárez", "msuarez@ua.es");
        Persona p11 = new Persona(88849L, "Nicolás Manero", "nmanero@uji.es");

        List<Persona> personas = new ArrayList<>();
        personas.add(p1);
        personas.add(p2);
        personas.add(p3);
        personas.add(p4);
        personas.add(p5);
        personas.add(p6);
        personas.add(p7);
        personas.add(p8);
        personas.add(p9);
        personas.add(p10);
        personas.add(p11);

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

    public Persona getPersonaById(Long personaId, Long connectedUserId) {
        Persona persona = new Persona(1L, "Javier Pérez", "jperez@uji.es");

        return persona;
    }
}
