package es.uji.apps.goc.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;

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
        List<OrganoReunion> resultado = query.from(qOrganoReunion).where(qOrganoReunion.reunion.id
                .eq(reunionId).and(qOrganoReunion.organoExternoId.eq(organoId)))
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
        List<OrganoReunion> resultado = query.from(qOrganoReunion).where(
                qOrganoReunion.reunion.id.eq(reunionId).and(qOrganoReunion.organo.id.eq(organoId)))
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
}
