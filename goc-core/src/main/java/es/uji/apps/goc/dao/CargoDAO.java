package es.uji.apps.goc.dao;

import com.mysema.query.jpa.impl.JPAQuery;
import es.uji.apps.goc.dto.Cargo;
import es.uji.apps.goc.dto.QCargo;
import es.uji.commons.db.BaseDAODatabaseImpl;
import org.springframework.stereotype.Repository;

@Repository
public class CargoDAO extends BaseDAODatabaseImpl
{
    public String getCargoCodigoById(Long cargoId)
    {
        QCargo qCargo = QCargo.cargo;
        JPAQuery query = new JPAQuery(entityManager);

        Cargo cargo = query.from(qCargo).where(qCargo.id.eq(cargoId)).uniqueResult(qCargo);

        if (cargo != null) return cargo.getCodigo();

        return null;
    }
}
