package es.uji.apps.goc.dao;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import es.uji.apps.goc.dto.QReunionInvitado;
import es.uji.apps.goc.dto.ReunionInvitado;
import es.uji.commons.db.BaseDAODatabaseImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ReunionInvitadoDAO extends BaseDAODatabaseImpl
{
    QReunionInvitado qReunionInvitado = QReunionInvitado.reunionInvitado;

    public List<ReunionInvitado> getInvitadosByReunionId(Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qReunionInvitado).where(qReunionInvitado.reunion.id.eq(reunionId)).list(qReunionInvitado);
    }

    @Transactional
    public void deleteByReunionId(Long reunionId)
    {
        JPADeleteClause deleteClause = new JPADeleteClause(entityManager, qReunionInvitado);

        deleteClause.where(qReunionInvitado.reunion.id.eq(reunionId)).execute();
    }
}
