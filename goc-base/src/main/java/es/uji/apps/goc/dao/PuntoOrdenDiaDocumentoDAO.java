package es.uji.apps.goc.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPAQuery;

import es.uji.apps.goc.dto.PuntoOrdenDiaDocumento;
import es.uji.apps.goc.dto.QPuntoOrdenDiaDocumento;
import es.uji.commons.db.BaseDAODatabaseImpl;

@Repository
public class PuntoOrdenDiaDocumentoDAO extends BaseDAODatabaseImpl
{
    private QPuntoOrdenDiaDocumento qPuntoOrdenDiaDocumento = QPuntoOrdenDiaDocumento.puntoOrdenDiaDocumento;

    public List<Tuple> getNumeroDocumentosPorPuntoOrdenDia()
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qPuntoOrdenDiaDocumento).groupBy(qPuntoOrdenDiaDocumento.puntoOrdenDia.id)
                .list(qPuntoOrdenDiaDocumento.puntoOrdenDia.id,
                        qPuntoOrdenDiaDocumento.puntoOrdenDia.id.count());
    }

    public PuntoOrdenDiaDocumento getDocumentoById(Long documentoId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        query.from(qPuntoOrdenDiaDocumento).where(qPuntoOrdenDiaDocumento.id.eq(documentoId));

        List<PuntoOrdenDiaDocumento> resultado = query.list(qPuntoOrdenDiaDocumento);

        if (resultado.size() != 1)
        {
            return null;
        }

        return resultado.get(0);
    }

    public List<PuntoOrdenDiaDocumento> getDocumentosByPuntoOrdenDiaId(Long puntoOrdenDiaId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        query.from(qPuntoOrdenDiaDocumento)
                .where(qPuntoOrdenDiaDocumento.puntoOrdenDia.id.eq(puntoOrdenDiaId))
                .orderBy(qPuntoOrdenDiaDocumento.fechaAdicion.desc());

        return query.list(qPuntoOrdenDiaDocumento);
    }
}