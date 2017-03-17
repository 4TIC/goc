package es.uji.apps.goc.dao;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;

import org.springframework.stereotype.Repository;

import java.util.List;

import es.uji.apps.goc.dto.Clave;
import es.uji.apps.goc.dto.QClave;
import es.uji.apps.goc.dto.QDescriptor;
import es.uji.apps.goc.dto.QPuntoOrdenDia;
import es.uji.apps.goc.dto.QPuntoOrdenDiaDescriptor;
import es.uji.apps.goc.dto.QReunion;
import es.uji.commons.db.BaseDAODatabaseImpl;

@Repository
public class ClaveDAO extends BaseDAODatabaseImpl{

    QDescriptor qDescriptor = QDescriptor.descriptor1;
    QClave qClave = QClave.clave1;
    QPuntoOrdenDiaDescriptor qPuntoOrdenDiaDescriptor = QPuntoOrdenDiaDescriptor.puntoOrdenDiaDescriptor;
    QPuntoOrdenDia qPuntoOrdenDia = QPuntoOrdenDia.puntoOrdenDia;
    QReunion qReunion = QReunion.reunion;

    public List<Clave> getClavesByDescriptor(Long idDescriptor){
        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qClave)
            .join(qClave.descriptor, qDescriptor)
            .where(qDescriptor.id.eq(idDescriptor))
            .distinct()
            .list(qClave);
    }

    public List<Clave> getClavesConReunionesPublicas(
        List<Long> idsReuniones,
        Long idsDescriptores,
        Integer anyo
    ) {
        BooleanExpression beWhere = null;
        if(anyo != null){
            beWhere = qReunion.fecha.year().eq(anyo);
        }
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qPuntoOrdenDiaDescriptor)
            .join(qPuntoOrdenDiaDescriptor.clave, qClave)
            .join(qClave.descriptor, qDescriptor)
            .join(qPuntoOrdenDiaDescriptor.puntoOrdenDia, qPuntoOrdenDia)
            .join(qPuntoOrdenDia.reunion, qReunion)
            .where(qReunion.id.in(idsReuniones)
                .and(qDescriptor.id.eq(idsDescriptores))
                .and(beWhere))
            .distinct()
            .list(qClave);
    }
}
