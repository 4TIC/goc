package es.uji.apps.goc.dao;

import java.util.ArrayList;
import java.util.List;

import es.uji.apps.goc.dto.ReunionDocumento;
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

    public List<PuntoOrdenDiaDocumento> getDatosDocumentosByPuntoOrdenDiaId(Long puntoOrdenDiaId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        query.from(qPuntoOrdenDiaDocumento)
                .where(qPuntoOrdenDiaDocumento.puntoOrdenDia.id.eq(puntoOrdenDiaId))
                .orderBy(qPuntoOrdenDiaDocumento.fechaAdicion.desc());

        List<Tuple> tuplas = query.list(qPuntoOrdenDiaDocumento.creadorId, qPuntoOrdenDiaDocumento.descripcion,
                qPuntoOrdenDiaDocumento.descripcionAlternativa, qPuntoOrdenDiaDocumento.fechaAdicion, qPuntoOrdenDiaDocumento.id,
                qPuntoOrdenDiaDocumento.mimeType, qPuntoOrdenDiaDocumento.nombreFichero);

        List<PuntoOrdenDiaDocumento> documentos = new ArrayList<>();

        for (Tuple tupla : tuplas)
        {
            PuntoOrdenDiaDocumento puntoOrdenDiaDocumento = new PuntoOrdenDiaDocumento();

            puntoOrdenDiaDocumento.setId(tupla.get(qPuntoOrdenDiaDocumento.id));
            puntoOrdenDiaDocumento.setCreadorId(tupla.get(qPuntoOrdenDiaDocumento.creadorId));
            puntoOrdenDiaDocumento.setDescripcion(tupla.get(qPuntoOrdenDiaDocumento.descripcion));
            puntoOrdenDiaDocumento.setDescripcionAlternativa(tupla.get(qPuntoOrdenDiaDocumento.descripcionAlternativa));
            puntoOrdenDiaDocumento.setFechaAdicion(tupla.get(qPuntoOrdenDiaDocumento.fechaAdicion));
            puntoOrdenDiaDocumento.setMimeType(tupla.get(qPuntoOrdenDiaDocumento.mimeType));
            puntoOrdenDiaDocumento.setNombreFichero(tupla.get(qPuntoOrdenDiaDocumento.nombreFichero));

            documentos.add(puntoOrdenDiaDocumento);
        }

        return documentos;
    }
}