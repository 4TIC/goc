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

        Organo c1 = new Organo("E1", "Organo 1 Externo", t1);
        Organo c2 = new Organo("E2", "Organo 2 Externo", t1);
        Organo c3 = new Organo("E3", "Organo 3 Externo", t2);
        Organo c4 = new Organo("E4", "Organo 4 Externo", t2);

        listaOrganos.add(c1);
        listaOrganos.add(c2);
        listaOrganos.add(c3);
        listaOrganos.add(c4);

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

        Cargo c2 = new Cargo("2");
        c2.setNombre("Vocal");

        Miembro m1 = new Miembro(1L, "Miembro 1 Organo " + organo.getId(),
                "miembro1@organo" + organo.getId() + ".com", organo, c1);
        Miembro m2 = new Miembro(2L, "Miembro 2 Organo " + organo.getId(),
                "miembro2@organo" + organo.getId() + ".com", organo, c2);
        Miembro m3 = new Miembro(3L, "Miembro 3 Organo " + organo.getId(),
                "miembro3@organo" + organo.getId() + ".com", organo, c2);
        Miembro m4 = new Miembro(4L, "Miembro 4 Organo " + organo.getId(),
                "miembro4@organo" + organo.getId() + ".com", organo, c2);
        Miembro m5 = new Miembro(5L, "Miembro 5 Organo " + organo.getId(),
                "miembro5@organo" + organo.getId() + ".com", organo, c2);

        listaMiembros.add(m1);
        listaMiembros.add(m2);
        listaMiembros.add(m3);
        listaMiembros.add(m4);
        listaMiembros.add(m5);

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
