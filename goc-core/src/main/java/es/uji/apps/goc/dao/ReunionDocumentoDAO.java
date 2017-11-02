package es.uji.apps.goc.dao;

import com.mysema.query.Tuple;
import com.mysema.query.jpa.impl.JPAQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import es.uji.apps.goc.dto.QReunionDocumento;
import es.uji.apps.goc.dto.ReunionDocumento;
import es.uji.commons.db.BaseDAODatabaseImpl;

@Repository
public class ReunionDocumentoDAO extends BaseDAODatabaseImpl
{
    private QReunionDocumento qReunionDocumento = QReunionDocumento.reunionDocumento;

    @Transactional
    public List<ReunionDocumento> getDocumentosByReunionId(Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        query.from(qReunionDocumento)
                .where(qReunionDocumento.reunion.id.eq(reunionId))
                .orderBy(qReunionDocumento.fechaAdicion.desc());

        return query.list(qReunionDocumento);
    }

    public List<ReunionDocumento> getDatosDocumentosByReunionId(Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        query.from(qReunionDocumento)
                .where(qReunionDocumento.reunion.id.eq(reunionId))
                .orderBy(qReunionDocumento.fechaAdicion.desc());

        List<Tuple> tuplas = query.list(qReunionDocumento.creadorId, qReunionDocumento.descripcion,
                qReunionDocumento.descripcionAlternativa, qReunionDocumento.fechaAdicion, qReunionDocumento.id,
                qReunionDocumento.mimeType, qReunionDocumento.nombreFichero);

        List<ReunionDocumento> documentos = new ArrayList<>();

        for (Tuple tupla : tuplas)
        {
            ReunionDocumento reunionDocumento = new ReunionDocumento();

            reunionDocumento.setId(tupla.get(qReunionDocumento.id));
            reunionDocumento.setCreadorId(tupla.get(qReunionDocumento.creadorId));
            reunionDocumento.setDescripcion(tupla.get(qReunionDocumento.descripcion));
            reunionDocumento.setDescripcionAlternativa(tupla.get(qReunionDocumento.descripcionAlternativa));
            reunionDocumento.setFechaAdicion(tupla.get(qReunionDocumento.fechaAdicion));
            reunionDocumento.setMimeType(tupla.get(qReunionDocumento.mimeType));
            reunionDocumento.setNombreFichero(tupla.get(qReunionDocumento.nombreFichero));

            documentos.add(reunionDocumento);
        }

        return documentos;
    }

    @Transactional
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