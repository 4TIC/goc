package es.uji.apps.goc.dao;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;

import es.uji.apps.goc.dto.*;
import org.springframework.stereotype.Repository;

import java.util.List;

import es.uji.commons.db.BaseDAODatabaseImpl;

@Repository
public class DescriptorDAO extends BaseDAODatabaseImpl
{

    private QDescriptor qDescriptor = QDescriptor.descriptor1;
    private QReunion qReunion = QReunion.reunion;
    private QPuntoOrdenDiaDescriptor qPuntoOrdenDiaDescriptor = QPuntoOrdenDiaDescriptor.puntoOrdenDiaDescriptor;
    private QPuntoOrdenDia qPuntoOrdenDia = QPuntoOrdenDia.puntoOrdenDia;
    private QClave qClave = QClave.clave1;

    public List<Descriptor> getDescriptores()
    {
        JPAQuery jpaQuery = new JPAQuery(entityManager);
        return jpaQuery.from(qDescriptor).list(qDescriptor);
    }

    public Descriptor getDescriptor(String idDescriptor)
    {
        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qDescriptor).where(qDescriptor.id.eq(Long.valueOf(idDescriptor))).uniqueResult(qDescriptor);
    }

    public List<Descriptor> getDescriptoresConReunionesPublicas(List<Long> idsReuniones, Integer anyo)
    {
        BooleanExpression beWhere = null;
        if (anyo != null)
        {
            beWhere = qReunion.fecha.year().eq(anyo);
        }
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qPuntoOrdenDiaDescriptor)
                .join(qPuntoOrdenDiaDescriptor.puntoOrdenDia, qPuntoOrdenDia)
                .join(qPuntoOrdenDia.reunion, qReunion)
                .join(qPuntoOrdenDiaDescriptor.clave, qClave)
                .join(qClave.descriptor, qDescriptor)
                .where(qReunion.id.in(idsReuniones).and(beWhere))
                .distinct()
                .list(qDescriptor);
    }
}