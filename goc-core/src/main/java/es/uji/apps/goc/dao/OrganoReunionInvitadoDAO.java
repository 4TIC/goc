package es.uji.apps.goc.dao;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import es.uji.apps.goc.dto.Cargo;
import es.uji.apps.goc.dto.OrganoReunionMiembro;
import es.uji.apps.goc.dto.QOrganoReunionInvitado;
import es.uji.apps.goc.dto.QOrganoReunionMiembro;
import es.uji.commons.db.BaseDAODatabaseImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class OrganoReunionInvitadoDAO extends BaseDAODatabaseImpl
{
    private QOrganoReunionInvitado qOrganoReunionInvitado = QOrganoReunionInvitado.organoReunionInvitado;

    @Transactional
    public void deleteAllByReunionId(Long reunionId)
    {
        JPADeleteClause deleteClause = new JPADeleteClause(entityManager, qOrganoReunionInvitado);

        deleteClause.where(qOrganoReunionInvitado.reunionId.eq(reunionId)).execute();
    }
}
