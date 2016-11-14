package es.uji.apps.goc.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.uji.apps.goc.dto.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPAQuery;

import es.uji.apps.goc.model.Organo;
import es.uji.apps.goc.model.TipoOrgano;
import es.uji.commons.db.BaseDAODatabaseImpl;

@Repository
public class OrganoDAO extends BaseDAODatabaseImpl
{
    private QOrganoLocal qOrganoLocal = QOrganoLocal.organoLocal;
    private QOrganoAutorizado qOrganoAutorizado = QOrganoAutorizado.organoAutorizado;
    private QReunion qReunion = QReunion.reunion;
    private QOrganoReunion qOrganoReunion = QOrganoReunion.organoReunion;
    private QTipoOrganoLocal qTipoOrganoLocal = QTipoOrganoLocal.tipoOrganoLocal;

    public List<Organo> getOrganosByUserId(Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        List<OrganoLocal> organos = query.from(qOrganoLocal)
                .where(qOrganoLocal.creadorId.eq(connectedUserId))
                .orderBy(qOrganoLocal.fechaCreacion.desc()).list(qOrganoLocal);

        return organosLocalesToOrganos(organos);
    }

    public List<Organo> getOrganosByAutorizadoId(Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);

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
        organo.setInactivo(organoLocalDTO.isInactivo());
        organo.setCreadorId(organoLocalDTO.getCreadorId());
        organo.setFechaCreacion(organoLocalDTO.getFechaCreacion());

        TipoOrgano tipoOrgano = new TipoOrgano(organoLocalDTO.getTipoOrganoLocal().getId());
        organo.setTipoOrgano(tipoOrgano);

        return organo;
    }

    public Organo getOrganoByIdAndUserId(Long organoId, Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);

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

    @Transactional
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

        organoLocal.setInactivo(organo.isInactivo());
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

    public List<OrganoLocal> getOrganosConReunionesPublicas(Long tipoOrganoId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qReunion, qOrganoLocal)
                .leftJoin(qReunion.reunionOrganos, qOrganoReunion)
                .where(qOrganoReunion.organoId.eq(qOrganoLocal.id.stringValue())
                        .and(qOrganoReunion.tipoOrganoId.eq(tipoOrganoId))
                        .and(qReunion.publica.isTrue())
                        .and(qReunion.completada.isTrue()))
                .list(qOrganoLocal);
    }
}
