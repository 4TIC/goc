package es.uji.apps.goc.dao;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import com.mysema.query.types.expr.BooleanExpression;
import es.uji.apps.goc.dto.*;
import es.uji.commons.db.BaseDAODatabaseImpl;
import es.uji.commons.rest.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class ReunionDAO extends BaseDAODatabaseImpl
{
    private QReunion qReunion = QReunion.reunion;
    private QReunionBusqueda qReunionBusqueda = QReunionBusqueda.reunionBusqueda;
    private QReunionEditor qReunionEditor = QReunionEditor.reunionEditor;
    private QOrganoReunion qOrganoReunion = QOrganoReunion.organoReunion;
    private QPuntoOrdenDia qPuntoOrdenDia = QPuntoOrdenDia.puntoOrdenDia;
    private QOrganoReunionMiembro qOrganoReunionMiembro = QOrganoReunionMiembro.organoReunionMiembro;
    private QPuntoOrdenDiaDescriptor qPuntoOrdenDiaDescriptor = QPuntoOrdenDiaDescriptor.puntoOrdenDiaDescriptor;
    private QDescriptor qDescriptor = QDescriptor.descriptor1;
    private QClave qClave = QClave.clave1;
    private QReunionComentario qReunionComentario = QReunionComentario.reunionComentario;
    private QReunionDocumento qReunionDocumento = QReunionDocumento.reunionDocumento;

    public List<ReunionEditor> getReunionesByEditorId(Long connectedUserId, String organodId, Long tipoOrganoId,
            Boolean externo, Boolean completada)
    {
        JPAQuery query = new JPAQuery(entityManager);

        query.from(qReunionEditor).where((qReunionEditor.editorId.eq(connectedUserId)).
                and(qReunionEditor.completada.eq(completada)));

        if (organodId != null)
        {
            query.where(qReunionEditor.organoId.eq(organodId).and(qReunionEditor.externo.eq(externo)));
        }

        if (tipoOrganoId != null)
        {
            query.where(qReunionEditor.tipoOrganoId.eq(tipoOrganoId));
        }

        return query.orderBy(qReunionEditor.fecha.desc()).list(qReunionEditor);
    }

    public ReunionEditor getReunionByIdAndEditorId(Long reunionId, Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qReunionEditor).where((qReunionEditor.editorId.eq(connectedUserId)).
                and(qReunionEditor.id.eq(reunionId))).uniqueResult(qReunionEditor);
    }

    public List<Reunion> getReunionesTodasByListaIds(List<Long> reunionesIds)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qReunion)
                .where(qReunion.id.in(reunionesIds))
                .orderBy(qReunion.fechaCreacion.desc())
                .list(qReunion);
    }

    public Reunion getReunionConOrganosById(Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        List<Reunion> reuniones = query.from(qReunion)
                .leftJoin(qReunion.reunionOrganos, qOrganoReunion)
                .fetch()
                .where(qReunion.id.eq(reunionId))
                .list(qReunion);

        if (reuniones.size() == 0)
        {
            return null;
        }

        return reuniones.get(0);
    }

    @Transactional
    public void marcarReunionComoCompletadaYActualizarAcuerdo(Long reunionId, Long responsableActaId, String acuerdos,
            String acuerdosAlternativos)
    {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qReunion);
        update.set(qReunion.completada, true)
                .set(qReunion.fechaCompletada, new Date())
                .set(qReunion.miembroResponsableActa.id, responsableActaId)
                .set(qReunion.acuerdos, acuerdos)
                .set(qReunion.acuerdosAlternativos, acuerdosAlternativos)
                .where(qReunion.id.eq(reunionId));
        update.execute();
    }

    public Reunion getReunionById(Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        List<Reunion> reuniones = query.from(qReunion).where(qReunion.id.eq(reunionId)).list(qReunion);

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
                .leftJoin(qReunion.reunionOrganos, qOrganoReunion)
                .fetch()
                .leftJoin(qOrganoReunion.miembros, qOrganoReunionMiembro)
                .fetch()
                .leftJoin(qReunion.reunionPuntosOrdenDia, qPuntoOrdenDia)
                .fetch()
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
        return query.from(qReunion)
                .where(qReunion.notificada.ne(true).and(qReunion.fecha.after(now)).and(qReunion.fecha.before(fecha)))
                .list(qReunion);
    }


    public List<Reunion> getReunionesByCreadorId(Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qReunion)
                .where(qReunion.creadorId.eq(connectedUserId))
                .orderBy(qReunion.fechaCreacion.desc())
                .list(qReunion);
    }

    public List<Integer> getAnyosConReunionesPublicas()
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qReunion)
                .where(qReunion.publica.isTrue().and(qReunion.completada.isTrue()))
                .distinct()
                .list(qReunion.fecha.year());
    }

    public List<Reunion> getReunionesPublicas(Integer anyo)
    {
        return getReunionesPublicas(null, null, null, null, anyo, null, null, null, false);
    }

    public List<Reunion> getReunionesPublicas(Long tipoOrganoId, Long organoId, Long descriptorId, Long claveId,
            Integer anyo, Date fInicio, Date fFin, String texto, Boolean idiomaAlternativo)
    {
        return getReunionesPublicasPaginated(tipoOrganoId, organoId, descriptorId, claveId, anyo, fInicio, fFin, texto,
                idiomaAlternativo, null, null);
    }

    public List<Reunion> getReunionesPublicasPaginated(Long tipoOrganoId, Long organoId, Long descriptorId,
            Long claveId, Integer anyo, Date fInicio, Date fFin, String texto, Boolean idiomaAlternativo,
            Integer startSeach, Integer numResults)
    {
        BooleanExpression whereAnyo = null;
        BooleanExpression whereOrgano = null;
        BooleanExpression whereTipoOrgano = null;
        BooleanExpression whereDescriptor = null;
        BooleanExpression whereClave = null;

        if (anyo != null)
        {
            whereAnyo = qReunion.fecha.year().eq(anyo);
        }

        if (organoId != null)
        {
            whereOrgano = qOrganoReunion.organoId.eq(String.valueOf(organoId));
        }

        if (tipoOrganoId != null)
        {
            whereTipoOrgano = qOrganoReunion.tipoOrganoId.eq(tipoOrganoId);
        }

        if (claveId != null)
        {
            whereClave = qClave.id.eq(claveId);
        }

        if (descriptorId != null)
        {
            whereDescriptor = qDescriptor.id.eq(descriptorId);
        }

        JPAQuery query = new JPAQuery(entityManager);

        query.from(qReunion)
                .join(qReunion.reunionBusqueda, qReunionBusqueda)
                .leftJoin(qReunion.reunionOrganos, qOrganoReunion)
                .leftJoin(qReunion.reunionPuntosOrdenDia)
                .fetch();

        if (claveId != null || descriptorId != null)
        {
            query.join(qReunion.reunionPuntosOrdenDia, qPuntoOrdenDia)
                    .fetch()
                    .join(qPuntoOrdenDia.puntoOrdenDiaDescriptores, qPuntoOrdenDiaDescriptor)
                    .join(qPuntoOrdenDiaDescriptor.clave, qClave)
                    .leftJoin(qClave.descriptor, qDescriptor);
        }

        query.where(qReunion.publica.isTrue()
                .and(qReunion.completada.isTrue())
                .and(whereDescriptor)
                .and(whereClave)
                .and(whereTipoOrgano)
                .and(whereOrgano)
                .and(whereAnyo)).orderBy(qReunion.fechaCreacion.desc());

        if (fInicio != null)
        {
            query.where(qReunion.fecha.goe(fInicio));
        }

        if (fFin != null)
        {
            query.where(qReunion.fecha.lt(add1Day(fFin)));
        }

        if (texto != null)
        {
            if (idiomaAlternativo)
            {
                query.where(qReunionBusqueda.asuntoAlternativoReunion.like("%" + StringUtils.limpiaAcentos(texto) + "%")
                        .or((qReunionBusqueda.descripcionAlternativaReunion.like(
                                "%" + StringUtils.limpiaAcentos(texto) + "%")
                                .or(qReunionBusqueda.tituloAlternativoPunto.like(
                                        "%" + StringUtils.limpiaAcentos(texto) + "%")
                                        .or(qReunionBusqueda.descripcionAlternativaPunto.like(
                                                "%" + StringUtils.limpiaAcentos(texto) + "%")
                                                .or(qReunionBusqueda.acuerdosAlternativosPunto.like(
                                                        "%" + StringUtils.limpiaAcentos(texto) + "%")
                                                        .or(qReunionBusqueda.deliberacionesAlternativasPunto.like(
                                                                "%" + StringUtils.limpiaAcentos(texto) + "%"))))))));
            }
            else
            {
                query.where(qReunionBusqueda.asuntoReunion.like("%" + StringUtils.limpiaAcentos(texto) + "%")
                        .or((qReunionBusqueda.descripcionReunion.like("%" + StringUtils.limpiaAcentos(texto) + "%")
                                .or(qReunionBusqueda.tituloPunto.like("%" + StringUtils.limpiaAcentos(texto) + "%")
                                        .or(qReunionBusqueda.descripcionPunto.like(
                                                "%" + StringUtils.limpiaAcentos(texto) + "%")
                                                .or(qReunionBusqueda.acuerdosPunto.like(
                                                        "%" + StringUtils.limpiaAcentos(texto) + "%")
                                                        .or(qReunionBusqueda.deliberacionesPunto.like(
                                                                "%" + StringUtils.limpiaAcentos(texto) + "%"))))))));
            }
        }

        if (startSeach != null && numResults != null)
        {
            query.offset(startSeach).limit(numResults);
        }

        return query.list(qReunion);
    }

    private Date add1Day(Date fecha)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        c.add(Calendar.DATE, 1);

        return c.getTime();
    }

    @Transactional
    public void deleteByReunionId(Long reunionId)
    {
        JPADeleteClause deleteClause = new JPADeleteClause(entityManager, qOrganoReunionMiembro);
        deleteClause.where(qOrganoReunionMiembro.reunionId.eq(reunionId)).execute();

        deleteClause = new JPADeleteClause(entityManager, qOrganoReunion);
        deleteClause.where(qOrganoReunion.reunion.id.eq(reunionId)).execute();

        deleteClause = new JPADeleteClause(entityManager, qReunionDocumento);
        deleteClause.where(qReunionDocumento.reunion.id.eq(reunionId)).execute();

        deleteClause = new JPADeleteClause(entityManager, qReunionComentario);
        deleteClause.where(qReunionComentario.reunion.id.eq(reunionId)).execute();

        deleteClause = new JPADeleteClause(entityManager, qReunion);
        deleteClause.where(qReunion.id.eq(reunionId)).execute();
    }

    public List<OrganoReunion> getOrganosReunionByReunionId(Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qOrganoReunion)
                .where(qOrganoReunion.reunion.id.eq(reunionId))
                .list(qOrganoReunion);
    }
}