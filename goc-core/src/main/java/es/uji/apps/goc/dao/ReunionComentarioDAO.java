package es.uji.apps.goc.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;

import es.uji.apps.goc.dto.QReunionComentario;
import es.uji.apps.goc.dto.ReunionComentario;
import es.uji.commons.db.BaseDAODatabaseImpl;

@Repository
public class ReunionComentarioDAO extends BaseDAODatabaseImpl
{
    QReunionComentario qReunionComentario = QReunionComentario.reunionComentario;

    public List<ReunionComentario> getComentariosByReunionId(Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        query.from(qReunionComentario).where(qReunionComentario.reunion.id.eq(reunionId))
                .orderBy(qReunionComentario.fecha.desc());

        return query.list(qReunionComentario);
    }

    public ReunionComentario getComentarioById(Long comentarioId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        query.from(qReunionComentario).where(qReunionComentario.id.eq(comentarioId));

        return query.uniqueResult(qReunionComentario);
    }
}