package es.uji.apps.goc.dao;

import com.mysema.query.jpa.impl.JPAQuery;
import es.uji.apps.goc.dto.OrganoAutorizado;
import es.uji.apps.goc.dto.OrganoInvitado;
import es.uji.apps.goc.dto.QOrganoAutorizado;
import es.uji.apps.goc.dto.QOrganoInvitado;
import es.uji.commons.db.BaseDAODatabaseImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrganoInvitadoDAO extends BaseDAODatabaseImpl
{
    QOrganoInvitado qOrganoInvitado = QOrganoInvitado.organoInvitado;

    public List<OrganoInvitado> getInvitadosByOrgano(String organoId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qOrganoInvitado).where(qOrganoInvitado.organoId.eq(organoId)).list(qOrganoInvitado);
    }
}
