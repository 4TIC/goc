package es.uji.apps.goc.dao;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import es.uji.apps.goc.dto.*;
import es.uji.commons.db.BaseDAODatabaseImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public class ReunionDAO extends BaseDAODatabaseImpl
{
    private QReunion qReunion = QReunion.reunion;
    private QOrganoReunion qOrganoReunion = QOrganoReunion.organoReunion;
    private QPuntoOrdenDia qPuntoOrdenDia = QPuntoOrdenDia.puntoOrdenDia;
    private QOrganoReunionMiembro qOrganoReunionMiembro = QOrganoReunionMiembro.organoReunionMiembro;
    private QOrganoLocal qOrganoLocal = QOrganoLocal.organoLocal;

    public List<Reunion> getReunionesByUserId(Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qReunion)
                .where(qReunion.creadorId.eq(connectedUserId)
                        .and(qReunion.completada.eq(false).or(qReunion.completada.isNull())))
                .orderBy(qReunion.fechaCreacion.desc()).list(qReunion);
    }

    public Reunion getReunionConOrganosById(Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        List<Reunion> reuniones = query.from(qReunion)
                .leftJoin(qReunion.reunionOrganos, qOrganoReunion).fetch()
                .where(qReunion.id.eq(reunionId))
                .list(qReunion);

        if (reuniones.size() == 0)
        {
            return null;
        }

        return reuniones.get(0);
    }

    @Transactional
    public void marcarReunionComoCompletadaYActualizarAcuerdo(Long reunionId,
                                                              Long responsableActaId, String acuerdos,
                                                              String acuerdosAlternativos)
    {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qReunion);
        update.set(qReunion.completada, true).set(qReunion.fechaCompletada, new Date())
                .set(qReunion.miembroResponsableActa.id, responsableActaId)
                .set(qReunion.acuerdos, acuerdos)
                .set(qReunion.acuerdosAlternativos, acuerdosAlternativos)
                .where(qReunion.id.eq(reunionId));
        update.execute();
    }

    public List<Reunion> getReunionesByOrganoLocalIdAndUserId(Long organoId, Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qReunion).join(qReunion.reunionOrganos, qOrganoReunion)
                .where(qReunion.creadorId.eq(connectedUserId)
                        .and(qReunion.completada.isNull().or(qReunion.completada.eq(false)))
                        .and(qOrganoReunion.organoId.eq(organoId.toString())
                                .and(qOrganoReunion.externo.eq(false))))
                .orderBy(qReunion.fechaCreacion.desc()).list(qReunion);
    }

    public List<Reunion> getReunionesCompletadasByOrganoLocalIdAndUserId(Long organoId,
                                                                         Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qReunion).join(qReunion.reunionOrganos, qOrganoReunion)
                .where(qReunion.creadorId.eq(connectedUserId).and(qReunion.completada.eq(true))
                        .and(qOrganoReunion.organoId.eq(organoId.toString())
                                .and(qOrganoReunion.externo.eq(false))))
                .orderBy(qReunion.fechaCreacion.desc()).list(qReunion);
    }

    public List<Reunion> getReunionesByOrganoExternoIdAndUserId(String organoId,
                                                                Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qReunion).join(qReunion.reunionOrganos, qOrganoReunion)
                .where(qReunion.creadorId.eq(connectedUserId)
                        .and(qReunion.completada.isNull().or(qReunion.completada.eq(false)))
                        .and(qOrganoReunion.organoId.eq(organoId)
                                .and(qOrganoReunion.externo.eq(true))))
                .orderBy(qReunion.fechaCreacion.desc()).list(qReunion);
    }

    public List<Reunion> getReunionesCompletadasByOrganoExternoIdAndUserId(String organoId,
                                                                           Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qReunion).join(qReunion.reunionOrganos, qOrganoReunion)
                .where(qReunion.creadorId.eq(connectedUserId).and(qReunion.completada.eq(true)).and(
                        qOrganoReunion.organoId.eq(organoId).and(qOrganoReunion.externo.eq(true))))
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

    public List<Reunion> getReunionesTodasByListaIds(List<Long> reunionesIds)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qReunion).where(qReunion.id.in(reunionesIds))
                .orderBy(qReunion.fechaCreacion.desc()).list(qReunion);
    }

    public Reunion getReunionById(Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        List<Reunion> reuniones = query.from(qReunion)
                .where(qReunion.id.eq(reunionId))
                .list(qReunion);

        if (reuniones.size() == 0)
        {
            return null;
        }

        return reuniones.get(0);
    }

    public Reunion getReunionConMiembrosAndPuntosDiaById(Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        List<Reunion> reuniones = query.from(qReunion)
                .leftJoin(qReunion.reunionOrganos, qOrganoReunion).fetch()
                .leftJoin(qOrganoReunion.miembros, qOrganoReunionMiembro).fetch()
                .leftJoin(qReunion.reunionPuntosOrdenDia, qPuntoOrdenDia).fetch()
                .where(qReunion.id.eq(reunionId))
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

        Date now = new Date();
        return query.from(qReunion).where(qReunion.notificada.ne(true)
                .and(qReunion.fecha.after(now)).and(qReunion.fecha.before(fecha))).list(qReunion);
    }

    public List<Reunion> getReunionesCompletadasByUserId(Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qReunion)
                .where(qReunion.creadorId.eq(connectedUserId).and(qReunion.completada.eq(true)))
                .orderBy(qReunion.fechaCreacion.desc()).list(qReunion);
    }

    public List<Reunion> getReunionesByCreadorId(Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qReunion).where(qReunion.creadorId.eq(connectedUserId))
                .orderBy(qReunion.fechaCreacion.desc()).list(qReunion);
    }

    public List<Reunion> getReunionesPublicas(Long tipoOrganoId, Long organoId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qReunion)
                .leftJoin(qReunion.reunionOrganos, qOrganoReunion)
                .leftJoin(qReunion.reunionPuntosOrdenDia).fetch()
                .where(qOrganoReunion.tipoOrganoId.eq(tipoOrganoId)
                        .and(qOrganoReunion.organoId.eq(String.valueOf(organoId)))
                        .and(qReunion.publica.isTrue())
                        .and(qReunion.completada.isTrue()))
                .orderBy(qReunion.fechaCreacion.desc())
                .list(qReunion);
    }
}