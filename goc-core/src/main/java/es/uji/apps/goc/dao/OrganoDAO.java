package es.uji.apps.goc.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;

import es.uji.apps.goc.dto.OrganoLocal;
import es.uji.apps.goc.dto.QOrganoAutorizado;
import es.uji.apps.goc.dto.QOrganoLocal;
import es.uji.apps.goc.dto.QOrganoReunion;
import es.uji.apps.goc.dto.TipoOrganoLocal;
import es.uji.apps.goc.model.Organo;
import es.uji.apps.goc.model.TipoOrgano;
import es.uji.commons.db.BaseDAODatabaseImpl;

@Repository
public class OrganoDAO extends BaseDAODatabaseImpl
{
    public List<Organo> getOrganosByUserId(Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);
        QOrganoLocal qOrganoLocal = QOrganoLocal.organoLocal;

        List<OrganoLocal> organos = query.from(qOrganoLocal)
                .where(qOrganoLocal.creadorId.eq(connectedUserId))
                .orderBy(qOrganoLocal.fechaCreacion.desc()).list(qOrganoLocal);

        return organosLocalesToOrganos(organos);
    }

    public List<Organo> getOrganosByAutorizadoId(Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);
        QOrganoLocal qOrganoLocal = QOrganoLocal.organoLocal;
        QOrganoAutorizado qOrganoAutorizado = QOrganoAutorizado.organoAutorizado;

        List<OrganoLocal> organos = query.from(qOrganoLocal, qOrganoAutorizado)
                .where(qOrganoAutorizado.organoExterno.eq(false)
                        .and(qOrganoAutorizado.organoId.eq(qOrganoLocal.id.stringValue()))
                        .and(qOrganoAutorizado.personaId.eq(connectedUserId)))
                .orderBy(qOrganoLocal.fechaCreacion.desc()).list(qOrganoLocal);

        return organosLocalesToOrganos(organos);
    }

    private List<Organo> organosLocalesToOrganos(List<OrganoLocal> organosDTO)
    {
        List<Organo> organos = new ArrayList<>();
        for (OrganoLocal organoLocalDTO : organosDTO)
        {
            Organo organo = organoLocalToOrgano(organoLocalDTO);
            organos.add(organo);
        }
        return organos;
    }

    private Organo organoLocalToOrgano(OrganoLocal organoLocalDTO)
    {
        Organo organo = new Organo();

        organo.setId(organoLocalDTO.getId().toString());
        organo.setNombre(organoLocalDTO.getNombre());
        organo.setCreadorId(organoLocalDTO.getCreadorId());
        organo.setFechaCreacion(organoLocalDTO.getFechaCreacion());

        TipoOrgano tipoOrgano = new TipoOrgano(organoLocalDTO.getTipoOrganoLocal().getId());
        organo.setTipoOrgano(tipoOrgano);

        return organo;
    }

    public List<Organo> getOrganosByReunionIdAndUserId(Long reunionId, Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);
        QOrganoLocal qOrganoLocal = QOrganoLocal.organoLocal;
        QOrganoReunion qOrganoReunion = QOrganoReunion.organoReunion;

        query.from(qOrganoLocal).join(qOrganoLocal.organoReuniones, qOrganoReunion)
                .where(qOrganoReunion.reunion.id.eq(reunionId)
                        .and(qOrganoLocal.creadorId.eq(connectedUserId)))
                .orderBy(qOrganoLocal.fechaCreacion.desc());

        return organosLocalesToOrganos(query.list(qOrganoLocal));
    }

    public Organo getOrganoByIdAndUserId(Long organoId, Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);
        QOrganoLocal qOrganoLocal = QOrganoLocal.organoLocal;

        List<OrganoLocal> organos = query.from(qOrganoLocal)
                .where(qOrganoLocal.id.eq(organoId).and(qOrganoLocal.creadorId.eq(connectedUserId)))
                .orderBy(qOrganoLocal.fechaCreacion.desc()).list(qOrganoLocal);

        if (organos.size() == 0)
        {
            return null;
        }

        return organoLocalToOrgano(organos.get(0));
    }

    public Organo insertOrgano(Organo organo, Long connectedUserId)
    {
        OrganoLocal organoLocal = organoLocalDesdeOrgano(organo);
        organoLocal.setCreadorId(connectedUserId);
        organoLocal.setFechaCreacion(new Date());
        organoLocal = this.insert(organoLocal);

        return organoLocalToOrgano(organoLocal);
    }

    public Organo updateOrgano(Organo organo)
    {
        OrganoLocal organoLocal = organoLocalDesdeOrgano(organo);
        organoLocal = this.update(organoLocal);

        return organoLocalToOrgano(organoLocal);

    }

    private OrganoLocal organoLocalDesdeOrgano(Organo organo)
    {
        OrganoLocal organoLocal = new OrganoLocal();

        if (organo.getId() != null)
        {
            organoLocal.setId(Long.parseLong(organo.getId()));
        }

        organoLocal.setNombre(organo.getNombre());
        organoLocal.setCreadorId(organo.getCreadorId());
        organoLocal.setFechaCreacion(organo.getFechaCreacion());

        if (organo.getTipoOrgano() != null)
        {
            TipoOrganoLocal tipoOrganoLocal = new TipoOrganoLocal(organo.getTipoOrgano().getId());
            organoLocal.setTipoOrganoLocal(tipoOrganoLocal);
        }

        return organoLocal;
    }
}
