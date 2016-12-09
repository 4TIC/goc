package es.uji.apps.goc.dao;

import java.util.List;

import es.uji.apps.goc.dto.QOrganoAutorizado;
import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;

import es.uji.apps.goc.dto.OrganoAutorizado;
import es.uji.commons.db.BaseDAODatabaseImpl;

@Repository
public class OrganoAutorizadoDAO extends BaseDAODatabaseImpl
{
    QOrganoAutorizado qOrganoAutorizado = QOrganoAutorizado.organoAutorizado;

    public List<OrganoAutorizado> getAutorizadosByUserId(Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qOrganoAutorizado).where(qOrganoAutorizado.personaId.eq(connectedUserId))
                .list(qOrganoAutorizado);
    }

    public List<OrganoAutorizado> getAutorizadosByOrgano(String organoId, Boolean externo)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qOrganoAutorizado).where(qOrganoAutorizado.organoId
                .eq(organoId).and(qOrganoAutorizado.organoExterno.eq(externo)))
                .list(qOrganoAutorizado);
    }
}
