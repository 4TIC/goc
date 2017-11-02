package es.uji.apps.goc.dao;

import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPAQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import es.uji.apps.goc.dto.PuntoOrdenDiaAcuerdo;
import es.uji.apps.goc.dto.QPuntoOrdenDiaAcuerdo;
import es.uji.commons.db.BaseDAODatabaseImpl;

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

    @Transactional
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

    @Transactional
    public List<PuntoOrdenDiaAcuerdo> getAcuerdosByPuntoOrdenDiaId(Long puntoOrdenDiaId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        query.from(qPuntoOrdenDiaAcuerdo)
                .where(qPuntoOrdenDiaAcuerdo.puntoOrdenDia.id.eq(puntoOrdenDiaId))
                .orderBy(qPuntoOrdenDiaAcuerdo.fechaAdicion.desc());

        return query.list(qPuntoOrdenDiaAcuerdo);
    }

    public List<PuntoOrdenDiaAcuerdo> getDatosAcuerdosByPuntoOrdenDiaId(Long puntoOrdenDiaId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        query.from(qPuntoOrdenDiaAcuerdo)
                .where(qPuntoOrdenDiaAcuerdo.puntoOrdenDia.id.eq(puntoOrdenDiaId))
                .orderBy(qPuntoOrdenDiaAcuerdo.fechaAdicion.desc());

        List<Tuple> tuplas = query.list(qPuntoOrdenDiaAcuerdo.creadorId, qPuntoOrdenDiaAcuerdo.descripcion,
                qPuntoOrdenDiaAcuerdo.descripcionAlternativa, qPuntoOrdenDiaAcuerdo.fechaAdicion, qPuntoOrdenDiaAcuerdo.id,
                qPuntoOrdenDiaAcuerdo.mimeType, qPuntoOrdenDiaAcuerdo.nombreFichero);

        List<PuntoOrdenDiaAcuerdo> documentos = new ArrayList<>();

        for (Tuple tupla : tuplas)
        {
            PuntoOrdenDiaAcuerdo puntoOrdenDiaAcuerdo = new PuntoOrdenDiaAcuerdo();

            puntoOrdenDiaAcuerdo.setId(tupla.get(qPuntoOrdenDiaAcuerdo.id));
            puntoOrdenDiaAcuerdo.setCreadorId(tupla.get(qPuntoOrdenDiaAcuerdo.creadorId));
            puntoOrdenDiaAcuerdo.setDescripcion(tupla.get(qPuntoOrdenDiaAcuerdo.descripcion));
            puntoOrdenDiaAcuerdo.setDescripcionAlternativa(tupla.get(qPuntoOrdenDiaAcuerdo.descripcionAlternativa));
            puntoOrdenDiaAcuerdo.setFechaAdicion(tupla.get(qPuntoOrdenDiaAcuerdo.fechaAdicion));
            puntoOrdenDiaAcuerdo.setMimeType(tupla.get(qPuntoOrdenDiaAcuerdo.mimeType));
            puntoOrdenDiaAcuerdo.setNombreFichero(tupla.get(qPuntoOrdenDiaAcuerdo.nombreFichero));

            documentos.add(puntoOrdenDiaAcuerdo);
        }

        return documentos;
    }
}