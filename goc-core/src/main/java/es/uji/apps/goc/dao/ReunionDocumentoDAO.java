package es.uji.apps.goc.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPAQuery;

import es.uji.apps.goc.dto.QReunionDocumento;
import es.uji.apps.goc.dto.ReunionDocumento;
import es.uji.commons.db.BaseDAODatabaseImpl;

@Repository
public class ReunionDocumentoDAO extends BaseDAODatabaseImpl
{
    private QReunionDocumento qReunionDocumento = QReunionDocumento.reunionDocumento;

    public List<ReunionDocumento> getDocumentosByReunionId(Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        query.from(qReunionDocumento).where(qReunionDocumento.reunion.id.eq(reunionId))
                .orderBy(qReunionDocumento.fechaAdicion.desc());

        return query.list(qReunionDocumento);
    }

    public ReunionDocumento getDocumentoById(Long documentoId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        query.from(qReunionDocumento).where(qReunionDocumento.id.eq(documentoId));

        List<ReunionDocumento> resultado = query.list(qReunionDocumento);

        if (resultado.size() != 1)
        {
            return null;
        }

        return resultado.get(0);
    }
}