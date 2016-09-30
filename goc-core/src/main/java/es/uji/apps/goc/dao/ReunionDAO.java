package es.uji.apps.goc.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

import es.uji.apps.goc.dto.QOrganoReunion;
import es.uji.apps.goc.dto.QReunion;
import es.uji.apps.goc.dto.Reunion;
import es.uji.commons.db.BaseDAODatabaseImpl;

@Repository
public class ReunionDAO extends BaseDAODatabaseImpl
{
    private QReunion qReunion = QReunion.reunion;
    private QOrganoReunion qOrganoReunion = QOrganoReunion.organoReunion;

    public List<Reunion> getReunionesByUserId(Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qReunion).where(qReunion.creadorId.eq(connectedUserId))
                .orderBy(qReunion.fechaCreacion.desc()).list(qReunion);
    }

    public Reunion getReunionConOrganosById(Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        List<Reunion> reuniones = query.from(qReunion)
                .leftJoin(qReunion.reunionOrganos, qOrganoReunion).fetch()
                .where(qReunion.id.eq(reunionId)).list(qReunion);

        if (reuniones.size() == 0)
        {
            return null;
        }

        return reuniones.get(0);
    }

    @Transactional
    public void marcarReunionComoCompletadaYActualizarAcuerdo(Long reunionId,
            Long responsableActaId, String acuerdos)
    {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qReunion);
        update.set(qReunion.completada, true).set(qReunion.fechaCompletada, new Date())
                .set(qReunion.miembroResponsableActa.id, responsableActaId)
                .set(qReunion.acuerdos, acuerdos).where(qReunion.id.eq(reunionId));
        update.execute();
    }

    public List<Reunion> getReunionesByOrganoLocalIdAndUserId(Long organoId, Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qReunion).join(qReunion.reunionOrganos, qOrganoReunion)
                .where(qReunion.creadorId.eq(connectedUserId)
                        .and(qOrganoReunion.organo.id.eq(organoId)))
                .orderBy(qReunion.fechaCreacion.desc()).list(qReunion);
    }

    public List<Reunion> getReunionesByOrganoExternoIdAndUserId(String organoId,
            Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qReunion).join(qReunion.reunionOrganos, qOrganoReunion)
                .where(qReunion.creadorId.eq(connectedUserId)
                        .and(qOrganoReunion.organoExternoId.eq(organoId)))
                .orderBy(qReunion.fechaCreacion.desc()).list(qReunion);
    }

    public List<Reunion> getReunionesProximas(Date fechaProxima)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qReunion).leftJoin(qReunion.reunionOrganos, qOrganoReunion).fetch()
                // .where(qReunion.completada.eq(false))
                // .and(qReunion.fecha.after(new Date()).and(qReunion.fecha.before(fechaProxima))))
                .list(qReunion);
    }

    public List<Reunion> getReunionesCompletadasByListaIds(List<Long> reunionesIds)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qReunion)
                .where(qReunion.id.in(reunionesIds).and(qReunion.completada.eq(true)))
                .orderBy(qReunion.fechaCreacion.desc()).list(qReunion);
    }

    public Reunion getReunionById(Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        List<Reunion> reuniones = query.from(qReunion).where(qReunion.id.eq(reunionId))
                .list(qReunion);

        if (reuniones.size() == 0)
        {
            return null;
        }

        return reuniones.get(0);
    }

    public List<Reunion> getPendientesNotificacion(Date fecha)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query
                .from(qReunion).where(qReunion.notificada.ne(true)
                        .and(qReunion.fecha.after(new Date())).and(qReunion.fecha.before(fecha)))
                .list(qReunion);
    }
}