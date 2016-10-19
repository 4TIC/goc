package es.uji.apps.goc.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;

import es.uji.apps.goc.dto.OrganoReunionMiembro;
import es.uji.apps.goc.dto.QOrganoReunionMiembro;
import es.uji.commons.db.BaseDAODatabaseImpl;

@Repository
public class OrganoReunionMiembroDAO extends BaseDAODatabaseImpl
{
    private QOrganoReunionMiembro qOrganoReunionMiembro = QOrganoReunionMiembro.organoReunionMiembro;

    public List<OrganoReunionMiembro> getOrganoReunionMiembroByOrganoReunionId(Long organoReunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qOrganoReunionMiembro)
                .where(qOrganoReunionMiembro.organoReunion.id.eq(organoReunionId))
                .list(qOrganoReunionMiembro);
    }

    public OrganoReunionMiembro getMiembroById(Long miembroId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        List<OrganoReunionMiembro> resultado = query.from(qOrganoReunionMiembro)
                .where(qOrganoReunionMiembro.id.eq(miembroId)).list(qOrganoReunionMiembro);

        if (resultado.size() == 0)
        {
            return null;
        }

        return resultado.get(0);
    }

    public List<OrganoReunionMiembro> getNuevosOrganoReunionMiembroByOrganoId(String organoId)
    {
        return null;
    }

    @Transactional
    public void updateAsistenciaMiembroByMiembrosIdsAndReunionIdAndOrganoId(
            List<String> asistentesIds, Boolean asistencia, Long reunionId, String organoId,
            Boolean externo)
    {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qOrganoReunionMiembro);
        update.set(qOrganoReunionMiembro.asistencia, asistencia)
                .where(qOrganoReunionMiembro.miembroId.in(asistentesIds)
                        .and(qOrganoReunionMiembro.reunionId.eq(reunionId)
                                .and(qOrganoReunionMiembro.organoId.eq(organoId)
                                        .and(qOrganoReunionMiembro.organoExterno.eq(externo)))));
        update.execute();

    }

    public List<OrganoReunionMiembro> getOrganoReunionMiembroByOrganoAndReunionId(String organoId,
            Boolean externo, Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qOrganoReunionMiembro)
                .where(qOrganoReunionMiembro.organoExterno.eq(externo)
                        .and(qOrganoReunionMiembro.organoId.eq(organoId)
                                .and(qOrganoReunionMiembro.reunionId.eq(reunionId))))
                .list(qOrganoReunionMiembro);
    }

    public List<OrganoReunionMiembro> getAsistenteReunionByOrganoAndReunionId(String organoId,
            Boolean externo, Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qOrganoReunionMiembro)
                .where(qOrganoReunionMiembro.organoExterno.eq(externo)
                        .and(qOrganoReunionMiembro.organoId.eq(organoId)
                                .and(qOrganoReunionMiembro.reunionId.eq(reunionId))
                                .and(qOrganoReunionMiembro.asistencia.eq(true))))
                .list(qOrganoReunionMiembro);
    }

    @Transactional
    public void updateAsistenteReunionByEmail(Long reunionId, String organoId, Boolean externo,
                                String asistenteEmail, Boolean asistencia, Long suplenteId, String suplenteNombre,
                                String suplenteEmail)
    {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qOrganoReunionMiembro);
        update.set(qOrganoReunionMiembro.asistencia, asistencia)
                .set(qOrganoReunionMiembro.suplenteId, suplenteId)
                .set(qOrganoReunionMiembro.suplenteNombre, suplenteNombre)
                .set(qOrganoReunionMiembro.suplenteEmail, suplenteEmail)
                .where(qOrganoReunionMiembro.email.eq(asistenteEmail)
                        .and(qOrganoReunionMiembro.reunionId.eq(reunionId)
                                .and(qOrganoReunionMiembro.organoId.eq(organoId)
                                        .and(qOrganoReunionMiembro.organoExterno.eq(externo)))));
        update.execute();
    }


    public List<OrganoReunionMiembro> getMiembroByAsistenteIdOrSuplenteId(Long reunionId,
            Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qOrganoReunionMiembro)
                .where(qOrganoReunionMiembro.reunionId.eq(reunionId)
                        .and(qOrganoReunionMiembro.suplenteId.eq(connectedUserId).or(
                                qOrganoReunionMiembro.miembroId.eq(connectedUserId.toString()))))
                .list(qOrganoReunionMiembro);
    }

    public List<OrganoReunionMiembro> getReunionesByAsistenteIdOrSuplenteId(Long userId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qOrganoReunionMiembro)
                .where(qOrganoReunionMiembro.suplenteId.eq(userId)
                        .or(qOrganoReunionMiembro.miembroId.eq(userId.toString())))
                .list(qOrganoReunionMiembro);
    }

    public List<OrganoReunionMiembro> getAsistentesByReunionId(Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qOrganoReunionMiembro)
                .where(qOrganoReunionMiembro.reunionId.eq(reunionId)).list(qOrganoReunionMiembro);
    }

    public List<OrganoReunionMiembro> getAsistentesConfirmadosByReunionId(Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qOrganoReunionMiembro)
                .where(qOrganoReunionMiembro.reunionId.eq(reunionId)
                        .and(qOrganoReunionMiembro.asistenciaConfirmada.eq(true)))
                .list(qOrganoReunionMiembro);
    }

    public List<OrganoReunionMiembro> getAsistentesReunionId(Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qOrganoReunionMiembro)
                .where(qOrganoReunionMiembro.reunionId.eq(reunionId))
                .list(qOrganoReunionMiembro);
    }
}
