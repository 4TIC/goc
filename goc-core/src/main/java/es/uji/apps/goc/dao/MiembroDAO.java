package es.uji.apps.goc.dao;

import com.mysema.query.jpa.impl.JPAQuery;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import es.uji.apps.goc.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

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

        List<MiembroLocal> miembrosLocales = query.from(qMiembroLocal).list(qMiembroLocal);

        return creaListaMiembrosDesdeListaMiembrosLocales(miembrosLocales);
    }

    private List<Miembro> creaListaMiembrosDesdeListaMiembrosLocales(List<MiembroLocal> miembrosLocales)
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
            cargo.setCodigo(miembroLocal.getCargo().getCodigo());
            cargo.setNombre(miembroLocal.getCargo().getNombre());
            cargo.setNombreAlternativo(miembroLocal.getCargo().getNombreAlternativo());
            miembro.setCargo(cargo);
        }

        return miembro;
    }

    public List<Miembro> getMiembrosByOrganoId(Long organoId)
    {
        JPAQuery query = new JPAQuery(entityManager);
        QMiembroLocal qMiembroLocal = QMiembroLocal.miembroLocal;

        List<MiembroLocal> miembrosLocales =
                query.from(qMiembroLocal).where(qMiembroLocal.organo.id.eq(organoId)).list(qMiembroLocal);

        return creaListaMiembrosDesdeListaMiembrosLocales(miembrosLocales);
    }

    @Transactional
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

    public Miembro getMiembroById(Long miembroId)
    {
        JPAQuery query = new JPAQuery(entityManager);
        QMiembroLocal qMiembroLocal = QMiembroLocal.miembroLocal;
        QOrganoLocal qOrganoLocal = QOrganoLocal.organoLocal;

        List<MiembroLocal> miembrosLocales = query.from(qMiembroLocal)
                .join(qMiembroLocal.organo, qOrganoLocal)
                .fetch()
                .where(qMiembroLocal.id.eq(miembroId))
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


}
