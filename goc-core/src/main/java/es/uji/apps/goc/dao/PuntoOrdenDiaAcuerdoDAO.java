package es.uji.apps.goc.dao;

import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPAQuery;
import es.uji.apps.goc.dto.PuntoOrdenDiaAcuerdo;
import es.uji.apps.goc.dto.QPuntoOrdenDiaAcuerdo;
import es.uji.commons.db.BaseDAODatabaseImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PuntoOrdenDiaAcuerdoDAO extends BaseDAODatabaseImpl
{
    private QPuntoOrdenDiaAcuerdo qPuntoOrdenDiaAcuerdo = QPuntoOrdenDiaAcuerdo.puntoOrdenDiaAcuerdo;

    public List<Tuple> getNumeroAcuerdosPorPuntoOrdenDia()
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qPuntoOrdenDiaAcuerdo).groupBy(qPuntoOrdenDiaAcuerdo.puntoOrdenDia.id)
                .list(qPuntoOrdenDiaAcuerdo.puntoOrdenDia.id,
                        qPuntoOrdenDiaAcuerdo.puntoOrdenDia.id.count());
    }

    public PuntoOrdenDiaAcuerdo getAcuerdoById(Long acuerdoId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        query.from(qPuntoOrdenDiaAcuerdo).where(qPuntoOrdenDiaAcuerdo.id.eq(acuerdoId));

        List<PuntoOrdenDiaAcuerdo> resultado = query.list(qPuntoOrdenDiaAcuerdo);

        if (resultado.size() != 1)
        {
            return null;
        }

        return resultado.get(0);
    }

    public List<PuntoOrdenDiaAcuerdo> getAcuerdosByPuntoOrdenDiaId(Long puntoOrdenDiaId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        query.from(qPuntoOrdenDiaAcuerdo)
                .where(qPuntoOrdenDiaAcuerdo.puntoOrdenDia.id.eq(puntoOrdenDiaId))
                .orderBy(qPuntoOrdenDiaAcuerdo.fechaAdicion.desc());

        return query.list(qPuntoOrdenDiaAcuerdo);
    }
}