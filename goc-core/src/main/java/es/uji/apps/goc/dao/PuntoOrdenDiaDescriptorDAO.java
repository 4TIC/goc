package es.uji.apps.goc.dao;

import com.mysema.query.jpa.impl.JPAQuery;

import org.springframework.stereotype.Repository;

import java.util.List;

import es.uji.apps.goc.dto.PuntoOrdenDiaDescriptor;
import es.uji.apps.goc.dto.QClave;
import es.uji.apps.goc.dto.QDescriptor;
import es.uji.apps.goc.dto.QPuntoOrdenDia;
import es.uji.apps.goc.dto.QPuntoOrdenDiaDescriptor;
import es.uji.commons.db.BaseDAODatabaseImpl;

@Repository
public class PuntoOrdenDiaDescriptorDAO extends BaseDAODatabaseImpl{

    QPuntoOrdenDiaDescriptor qPuntoOrdenDiaDescriptor = QPuntoOrdenDiaDescriptor.puntoOrdenDiaDescriptor;
    QClave qClave = QClave.clave1;
    QPuntoOrdenDia qPuntoOrdenDia = QPuntoOrdenDia.puntoOrdenDia;

    public List<PuntoOrdenDiaDescriptor> getDescriptoresOrdenDia(
        Long idPuntoOrdenDia
    ) {
        JPAQuery query = new JPAQuery(entityManager);
        return query.from(qPuntoOrdenDiaDescriptor)
            .join(qPuntoOrdenDiaDescriptor.clave, qClave)
            .join(qPuntoOrdenDiaDescriptor.puntoOrdenDia, qPuntoOrdenDia)
            .where(qPuntoOrdenDia.id.eq(idPuntoOrdenDia))
            .list(qPuntoOrdenDiaDescriptor);
    }
}
