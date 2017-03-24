package es.uji.apps.goc.dao;

import com.mysema.query.jpa.impl.JPAQuery;

import org.springframework.stereotype.Repository;

import java.util.List;

import es.uji.apps.goc.dto.OrganoReunion;
import es.uji.apps.goc.dto.QOrganoReunion;
import es.uji.commons.db.BaseDAODatabaseImpl;

@Repository
public class OrganoReunionDAO extends BaseDAODatabaseImpl
{
    private QOrganoReunion qOrganoReunion = QOrganoReunion.organoReunion;

    public OrganoReunion getOrganoReunionById(Long organoReunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);
        List<OrganoReunion> resultado = query.from(qOrganoReunion)
                .where(qOrganoReunion.id.eq(organoReunionId)).list(qOrganoReunion);

        if (resultado.size() == 0)
        {
            return null;
        }

        return resultado.get(0);
    }

    public OrganoReunion getOrganoReunionByReunionIdAndOrganoExternoId(Long reunionId,
            String organoId)
    {
        JPAQuery query = new JPAQuery(entityManager);
        List<OrganoReunion> resultado = query.from(qOrganoReunion)
                .where(qOrganoReunion.reunion.id.eq(reunionId).and(
                        qOrganoReunion.organoId.eq(organoId).and(qOrganoReunion.externo.eq(true))))
                .list(qOrganoReunion);

        if (resultado.size() == 0)
        {
            return null;
        }

        return resultado.get(0);
    }

    public OrganoReunion getOrganoReunionByReunionIdAndOrganoLocalId(Long reunionId, Long organoId)
    {
        JPAQuery query = new JPAQuery(entityManager);
        List<OrganoReunion> resultado = query.from(qOrganoReunion)
                .where(qOrganoReunion.reunion.id.eq(reunionId).and(qOrganoReunion.organoId
                        .eq(organoId.toString()).and(qOrganoReunion.externo.eq(false))))
                .list(qOrganoReunion);

        if (resultado.size() == 0)
        {
            return null;
        }

        return resultado.get(0);
    }

    public List<OrganoReunion> getListaOrganoReunionPorReunionId(Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qOrganoReunion).where(qOrganoReunion.reunion.id.eq(reunionId))
                .list(qOrganoReunion);
    }

    public List<OrganoReunion> getOrganoReunionByOrganoExternoId(String organoExternoId)
    {
        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qOrganoReunion).where(
                qOrganoReunion.organoId.eq(organoExternoId).and(qOrganoReunion.externo.eq(true)))
                .list(qOrganoReunion);
    }

    public List<OrganoReunion> getOrganoReunionByOrganoLocalId(Long organoLocalId)
    {
        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qOrganoReunion).where(qOrganoReunion.organoId.eq(organoLocalId.toString())
                .and(qOrganoReunion.externo.eq(false))).list(qOrganoReunion);
    }

    public List<OrganoReunion> getOrganosReunionNoCompletadasByOrganoId(Long organoLocalId)
    {
        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qOrganoReunion).where(qOrganoReunion.organoId.eq(organoLocalId.toString())
            .and(qOrganoReunion.externo.eq(false)).and(qOrganoReunion.reunion.completada.isFalse())).list(qOrganoReunion);
    }
}
