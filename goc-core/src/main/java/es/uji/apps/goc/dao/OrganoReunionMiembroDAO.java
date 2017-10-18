package es.uji.apps.goc.dao;

import com.mysema.query.jpa.impl.JPADeleteClause;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPAUpdateClause;
import es.uji.apps.goc.dto.Cargo;
import es.uji.apps.goc.dto.OrganoReunionMiembro;
import es.uji.apps.goc.dto.QOrganoReunionMiembro;
import es.uji.commons.db.BaseDAODatabaseImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
                .where(qOrganoReunionMiembro.id.eq(miembroId))
                .list(qOrganoReunionMiembro);

        if (resultado.size() == 0)
        {
            return null;
        }

        return resultado.get(0);
    }

    public List<OrganoReunionMiembro> getMiembroReunionByOrganoAndReunionId(String organoId, Boolean externo,
            Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qOrganoReunionMiembro)
                .where(qOrganoReunionMiembro.organoExterno.eq(externo)
                        .and(qOrganoReunionMiembro.organoId.eq(organoId)
                                .and(qOrganoReunionMiembro.reunionId.eq(reunionId))))
                .list(qOrganoReunionMiembro);
    }

    public List<OrganoReunionMiembro> getAsistenteReunionByOrganoAndReunionId(String organoId, Boolean externo,
            Long reunionId)
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
    public void updateAsistenteReunionByEmail(Long reunionId, String organoId, Boolean externo, String asistenteEmail,
            Boolean asistencia, Long suplenteId, String suplenteNombre, String suplenteEmail, Boolean guardarAsistencia)
    {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qOrganoReunionMiembro);

        if (guardarAsistencia)
        {
            update.set(qOrganoReunionMiembro.asistencia, asistencia)
                    .set(qOrganoReunionMiembro.asistenciaConfirmada, asistencia);
        }

        update.set(qOrganoReunionMiembro.suplenteId, suplenteId)
                .set(qOrganoReunionMiembro.suplenteNombre, suplenteNombre)
                .set(qOrganoReunionMiembro.suplenteEmail, suplenteEmail)
                .where(qOrganoReunionMiembro.email.eq(asistenteEmail)
                        .and(qOrganoReunionMiembro.reunionId.eq(reunionId)
                                .and(qOrganoReunionMiembro.organoId.eq(organoId)
                                        .and(qOrganoReunionMiembro.organoExterno.eq(externo)))));
        update.execute();
    }

    public List<OrganoReunionMiembro> getMiembroByAsistenteIdOrSuplenteId(Long reunionId, Long connectedUserId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qOrganoReunionMiembro)
                .where(qOrganoReunionMiembro.reunionId.eq(reunionId)
                        .and(qOrganoReunionMiembro.suplenteId.eq(connectedUserId)
                                .or(qOrganoReunionMiembro.miembroId.eq(connectedUserId.toString()))))
                .list(qOrganoReunionMiembro);
    }

    public List<OrganoReunionMiembro> getAsistentesByReunionId(Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qOrganoReunionMiembro)
                .where(qOrganoReunionMiembro.reunionId.eq(reunionId)
                        .and(qOrganoReunionMiembro.asistencia.eq(true)))
                .list(qOrganoReunionMiembro);
    }

    public List<OrganoReunionMiembro> getMiembrosByReunionId(Long reunionId)
    {
        JPAQuery query = new JPAQuery(entityManager);

        return query.from(qOrganoReunionMiembro)
                .where(qOrganoReunionMiembro.reunionId.eq(reunionId))
                .list(qOrganoReunionMiembro);
    }

    public OrganoReunionMiembro getByReunionAndOrganoAndEmail(Long reunionId, String organoId, Boolean externo,
            String email)
    {
        JPAQuery query = new JPAQuery(entityManager);

        List<OrganoReunionMiembro> miembros = query.from(qOrganoReunionMiembro)
                .where(qOrganoReunionMiembro.email.eq(email)
                        .and(qOrganoReunionMiembro.reunionId.eq(reunionId)
                                .and(qOrganoReunionMiembro.organoId.eq(organoId)
                                        .and(qOrganoReunionMiembro.organoExterno.eq(externo)))))
                .list(qOrganoReunionMiembro);

        if (miembros.size() == 0) return null;

        return miembros.get(0);
    }

    @Transactional
    public void deleteByOrganoReunionIdPersonaIdAndCargoId(Long organoReunionId, String personaId, String cargoId)
    {
        JPADeleteClause deleteClause = new JPADeleteClause(entityManager, qOrganoReunionMiembro);

        deleteClause.where(qOrganoReunionMiembro.organoReunion.id.eq(organoReunionId)
                .and(qOrganoReunionMiembro.miembroId.eq(personaId).and(qOrganoReunionMiembro.cargoId.eq(cargoId))))
                .execute();
    }

    public void ByOrganoReunionIdPersonaIdAndCargoId(Long organoReunionId, String personaId, String cargoId,
            String nombre, String email, Cargo cargo)
    {
        JPAUpdateClause update = new JPAUpdateClause(entityManager, qOrganoReunionMiembro);

        update.set(qOrganoReunionMiembro.nombre, nombre).
                set(qOrganoReunionMiembro.email, email).
                set(qOrganoReunionMiembro.cargoId, cargo.getId().toString()).
                set(qOrganoReunionMiembro.cargoCodigo, cargo.getCodigo()).
                set(qOrganoReunionMiembro.cargoNombre, cargo.getNombre()).
                set(qOrganoReunionMiembro.cargoNombreAlternativo, cargo.getNombreAlternativo()).
                where(qOrganoReunionMiembro.organoReunion.id.eq(organoReunionId)
                        .and(qOrganoReunionMiembro.miembroId.eq(personaId)
                                .and(qOrganoReunionMiembro.cargoId.eq(cargoId))));

        update.execute();
    }
}
