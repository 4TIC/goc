package es.uji.apps.goc.dao;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import es.uji.apps.goc.dto.*;
import es.uji.commons.db.BaseDAODatabaseImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DescriptorTipoOrganoDAO extends BaseDAODatabaseImpl
{
    private QDescriptorTipoOrgano qDescriptorTipoOrgano = QDescriptorTipoOrgano.descriptorTipoOrgano;

    public List<DescriptorTipoOrgano> getByDescriptorId(Long descriptorId)
    {
        JPAQuery jpaQuery = new JPAQuery(entityManager);

        return jpaQuery.from(qDescriptorTipoOrgano)
                .where(qDescriptorTipoOrgano.descriptor.id.eq(descriptorId))
                .list(qDescriptorTipoOrgano);
    }
}