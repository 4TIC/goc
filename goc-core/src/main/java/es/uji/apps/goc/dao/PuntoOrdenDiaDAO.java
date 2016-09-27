package es.uji.apps.goc.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

import es.uji.apps.goc.dto.PuntoOrdenDia;
import es.uji.apps.goc.dto.QPuntoOrdenDia;
import es.uji.commons.db.BaseDAODatabaseImpl;

@Repository
public class PuntoOrdenDiaDAO extends BaseDAODatabaseImpl
{
    private QPuntoOrdenDia qPuntoOrdenDia = QPuntoOrdenDia.puntoOrdenDia;

    public List<PuntoOrdenDia> getPuntosByReunionId(Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        query.from(qPuntoOrdenDia).where(qPuntoOrdenDia.reunion.id.eq(reunionId))
                .orderBy(qPuntoOrdenDia.orden.asc());

        return query.list(qPuntoOrdenDia);

    }

    public PuntoOrdenDia getPuntoOrdenDiaById(Long puntoOrdenDiaId)
    {
        JPAQuery query = new JPAQuery(entityManager);
        List<PuntoOrdenDia> puntosOrdenDia = query.from(qPuntoOrdenDia)
                .where(qPuntoOrdenDia.id.eq(puntoOrdenDiaId)).list(qPuntoOrdenDia);

        if (puntosOrdenDia.size() == 0)
        {
            return null;
        }

        return puntosOrdenDia.get(0);

    }

    public PuntoOrdenDia getSiguientePuntoOrdenDiaByOrden(Long orden)
    {
        JPAQuery query = new JPAQuery(entityManager);
        List<PuntoOrdenDia> puntosOrdenDia = query.from(qPuntoOrdenDia)
                .where(qPuntoOrdenDia.orden.gt(orden)).orderBy(qPuntoOrdenDia.orden.asc())
                .list(qPuntoOrdenDia);

        if (puntosOrdenDia.size() == 0)
        {
            return null;
        }

        return puntosOrdenDia.get(0);
    }

    public PuntoOrdenDia getAnteriorPuntoOrdenDiaByOrden(Long orden)
    {
        JPAQuery query = new JPAQuery(entityManager);
        List<PuntoOrdenDia> puntosOrdenDia = query.from(qPuntoOrdenDia)
                .where(qPuntoOrdenDia.orden.lt(orden)).orderBy(qPuntoOrdenDia.orden.desc())
                .list(qPuntoOrdenDia);

        if (puntosOrdenDia.size() == 0)
        {
            return null;
        }
        return puntosOrdenDia.get(0);
    }

    public List<PuntoOrdenDia> getPuntosOrdenDiaMismoOrden(Long orden)
    {
        JPAQuery query = new JPAQuery(entityManager);
        List<PuntoOrdenDia> puntosOrdenDia = query.from(qPuntoOrdenDia)
                .where(qPuntoOrdenDia.orden.eq(orden)).list(qPuntoOrdenDia);

        return puntosOrdenDia;
    }

    @Transactional
    public void actualizaOrden(Long puntoOrdenDiaId, Long orden)
    {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qPuntoOrdenDia);
        update.set(qPuntoOrdenDia.orden, orden).where(qPuntoOrdenDia.id.eq(puntoOrdenDiaId));
        update.execute();
    }

    public PuntoOrdenDia getUltimoPuntoOrdenDiaByReunionId(Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);
        List<PuntoOrdenDia> puntosOrdenDia = query.from(qPuntoOrdenDia)
                .where(qPuntoOrdenDia.reunion.id.eq(reunionId)).orderBy(qPuntoOrdenDia.orden.desc())
                .list(qPuntoOrdenDia);

        if (puntosOrdenDia.size() == 0)
        {
            return null;
        }

        return puntosOrdenDia.get(0);
    }
}