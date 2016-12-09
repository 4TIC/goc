package es.uji.apps.goc.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import es.uji.apps.goc.dto.MiembroExterno;
import es.uji.apps.goc.dto.MiembroLocal;
import es.uji.apps.goc.dto.OrganoLocal;
import es.uji.apps.goc.dto.QMiembroLocal;
import es.uji.apps.goc.dto.QOrganoLocal;
import es.uji.apps.goc.exceptions.MiembrosExternosException;
import es.uji.apps.goc.model.Cargo;
import es.uji.apps.goc.model.JSONListaMiembrosDeserializer;
import es.uji.apps.goc.model.Miembro;
import es.uji.apps.goc.model.Organo;
import es.uji.commons.db.BaseDAODatabaseImpl;

@Repository
public class MiembroDAO extends BaseDAODatabaseImpl
{
    @Value("${goc.external.authToken}")
    private String authToken;

    @Value("${goc.external.miembrosEndpoint}")
    private String miembrosExternosEndpoint;

    public List<Miembro> getMiembros()
    {
        JPAQuery query = new JPAQuery(entityManager);
        QMiembroLocal qMiembroLocal = QMiembroLocal.miembroLocal;

        List<MiembroLocal> miembrosLocales = query.from(qMiembroLocal)
                .list(qMiembroLocal);

        return creaListaMiembrosDesdeListaMiembrosLocales(miembrosLocales);
    }

    private List<Miembro> creaListaMiembrosDesdeListaMiembrosLocales(
            List<MiembroLocal> miembrosLocales)
    {
        List<Miembro> listaMiembros = new ArrayList<>();

        for (MiembroLocal miembroLocal : miembrosLocales)
        {
            listaMiembros.add(creaMiembroDesdeMiembroLocal(miembroLocal));
        }

        return listaMiembros;
    }

    private Miembro creaMiembroDesdeMiembroLocal(MiembroLocal miembroLocal)
    {
        Miembro miembro = new Miembro();
        miembro.setId(miembroLocal.getId());
        miembro.setPersonaId(miembroLocal.getPersonaId());
        miembro.setNombre(miembroLocal.getNombre());
        miembro.setEmail(miembroLocal.getEmail());

        Organo organo = new Organo(miembroLocal.getOrgano().getId().toString());
        miembro.setOrgano(organo);

        if (miembroLocal.getCargo() != null)
        {
            Cargo cargo = new Cargo(miembroLocal.getCargo().getId().toString());
            cargo.setNombre(miembroLocal.getCargo().getNombre());
            miembro.setCargo(cargo);
        }

        return miembro;
    }

    public List<Miembro> getMiembrosByOrganoId(Long organoId)
    {
        JPAQuery query = new JPAQuery(entityManager);
        QMiembroLocal qMiembroLocal = QMiembroLocal.miembroLocal;

        List<MiembroLocal> miembrosLocales = query.from(qMiembroLocal)
                .where(qMiembroLocal.organo.id.eq(organoId))
                .list(qMiembroLocal);

        return creaListaMiembrosDesdeListaMiembrosLocales(miembrosLocales);
    }

    public Miembro insertMiembro(Miembro miembro, Long connectedUserId)
    {
        MiembroLocal miembroLocal = new MiembroLocal();
        miembroLocal.setPersonaId(miembro.getId());
        miembroLocal.setNombre(miembro.getNombre());
        miembroLocal.setEmail(miembro.getEmail());

        Long organoLocalId = Long.parseLong(miembro.getOrgano().getId());
        miembroLocal.setOrgano(new OrganoLocal(organoLocalId));

        Long cargoId = Long.parseLong(miembro.getCargo().getId());
        miembroLocal.setCargo(new es.uji.apps.goc.dto.Cargo(cargoId));

        miembroLocal = this.insert(miembroLocal);
        return creaMiembroDesdeMiembroLocal(miembroLocal);
    }

    public Miembro getMiembroByIdAndUserId(Long miembroId, Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);
        QMiembroLocal qMiembroLocal = QMiembroLocal.miembroLocal;
        QOrganoLocal qOrganoLocal = QOrganoLocal.organoLocal;

        List<MiembroLocal> miembrosLocales = query.from(qMiembroLocal)
                .join(qMiembroLocal.organo, qOrganoLocal).fetch()
                .where(qMiembroLocal.id.eq(miembroId)
                        .and(qMiembroLocal.organo.creadorId.eq(connectedUserId)))
                .list(qMiembroLocal);

        if (miembrosLocales.size() == 0)
        {
            return null;
        }

        return creaMiembroDesdeMiembroLocal(miembrosLocales.get(0));
    }

    public Miembro updateMiembro(Miembro miembro)
    {
        MiembroLocal miembroLocal = creaMiembroLocalDesdeMiembro(miembro);
        miembroLocal = this.update(miembroLocal);

        return creaMiembroDesdeMiembroLocal(miembroLocal);
    }

    private MiembroLocal creaMiembroLocalDesdeMiembro(Miembro miembro)
    {
        MiembroLocal miembroLocal = new MiembroLocal();
        miembroLocal.setId(miembro.getId());
        miembroLocal.setPersonaId(miembro.getPersonaId());
        miembroLocal.setNombre(miembro.getNombre());
        miembroLocal.setEmail(miembro.getEmail());

        OrganoLocal organoLocal = new OrganoLocal(Long.parseLong(miembro.getOrgano().getId()));
        miembroLocal.setOrgano(organoLocal);

        es.uji.apps.goc.dto.Cargo cargo = new es.uji.apps.goc.dto.Cargo();
        cargo.setId(Long.parseLong(miembro.getCargo().getId()));
        miembroLocal.setCargo(cargo);

        return miembroLocal;
    }

    public List<Miembro> getMiembrosExternos(String organoId, Long connectedUserId)
            throws MiembrosExternosException
    {
        WebResource getMiembrosResource = Client.create().resource(
                this.miembrosExternosEndpoint.replace("{organoId}", organoId));

        ClientResponse response = getMiembrosResource.type(MediaType.APPLICATION_JSON)
                .header("X-UJI-AuthToken", authToken).get(ClientResponse.class);

        if (response.getStatus() != 200)
        {
            throw new MiembrosExternosException();
        }

        JSONListaMiembrosDeserializer jsonDeserializer = response
                .getEntity(JSONListaMiembrosDeserializer.class);

        List<MiembroExterno> listaMiembrosExternos = jsonDeserializer.getMiembros();
        return creaListaMiembroDesdeListaMiembrosExternos(listaMiembrosExternos);
    }

    private List<Miembro> creaListaMiembroDesdeListaMiembrosExternos(
            List<MiembroExterno> listaMiembrosExternos)
    {
        List<Miembro> listaMiembros = new ArrayList<>();

        for (MiembroExterno miembroExterno : listaMiembrosExternos)
        {
            listaMiembros.add(creaMiembroDesdeMiembroExterno(miembroExterno));
        }

        return listaMiembros;
    }

    private Miembro creaMiembroDesdeMiembroExterno(MiembroExterno miembroExterno)
    {
        Cargo cargo = new Cargo(miembroExterno.getCargo().getId().toString());
        cargo.setNombre(miembroExterno.getCargo().getNombre());
        cargo.setNombreAlternativo(miembroExterno.getCargo().getNombreAlternativo());

        Miembro miembro = new Miembro();
        miembro.setId(miembroExterno.getId());
        miembro.setPersonaId(miembroExterno.getId());
        miembro.setNombre(miembroExterno.getNombre());
        miembro.setEmail(miembroExterno.getEmail());
        miembro.setCargo(cargo);

        miembro.setOrgano(new Organo(miembroExterno.getOrgano().getId()));

        return miembro;
    }
}
