package es.uji.apps.goc.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.impl.JPAQuery;

import es.uji.apps.goc.dto.QTipoOrganoLocal;
import es.uji.apps.goc.dto.TipoOrganoLocal;
import es.uji.apps.goc.model.TipoOrgano;
import es.uji.commons.db.BaseDAODatabaseImpl;

@Repository
public class TipoOrganoDAO extends BaseDAODatabaseImpl {
    public List<TipoOrgano> getTiposOrgano() {
        JPAQuery query = new JPAQuery(entityManager);
        QTipoOrganoLocal qTipoOrganoLocal = QTipoOrganoLocal.tipoOrganoLocal;

        List<TipoOrganoLocal> tiposOrgano = query.from(qTipoOrganoLocal).list(qTipoOrganoLocal);
        return tiposOrganoDTOToTipoOrgano(tiposOrgano);
    }

    private List<TipoOrgano> tiposOrganoDTOToTipoOrgano(List<TipoOrganoLocal> tiposOrganoDTO)
    {
        List<TipoOrgano> listaTipoOrgano = new ArrayList<>();
        for (TipoOrganoLocal tipoOrganoDTO: tiposOrganoDTO) {
            TipoOrgano tipoOrgano = tipoOrganoDTOToTipoOrgano(tipoOrganoDTO);
            listaTipoOrgano.add(tipoOrgano);
        }

        return listaTipoOrgano;
    }

    private TipoOrgano tipoOrganoDTOToTipoOrgano(TipoOrganoLocal tipoOrganoDTO)
    {
        TipoOrgano tipoOrgano = new TipoOrgano();
        tipoOrgano.setId(tipoOrganoDTO.getId());
        tipoOrgano.setCodigo(tipoOrganoDTO.getCodigo());
        tipoOrgano.setNombre(tipoOrganoDTO.getNombre());

        return tipoOrgano;
    }

    public TipoOrgano insertTipoOrgano(TipoOrgano tipoOrgano)
    {
        TipoOrganoLocal tipoOrganoLocal = creaTipoOrganoLocalDesdeTipoOrgano(tipoOrgano);
        tipoOrganoLocal = this.insert(tipoOrganoLocal);

        return creaTipoOrganoDesdeTipoOrganoLocal(tipoOrganoLocal);
    }

    private TipoOrgano creaTipoOrganoDesdeTipoOrganoLocal(TipoOrganoLocal tipoOrganoLocal)
    {
        TipoOrgano tipoOrgano = new TipoOrgano();

        tipoOrgano.setId(tipoOrganoLocal.getId());
        tipoOrgano.setNombre(tipoOrganoLocal.getNombre());
        tipoOrgano.setCodigo(tipoOrganoLocal.getCodigo());
        return tipoOrgano;
    }

    public TipoOrgano updateTipoOrgano(TipoOrgano tipoOrgano)
    {
        TipoOrganoLocal tipoOrganoLocal = creaTipoOrganoLocalDesdeTipoOrgano(tipoOrgano);
        tipoOrganoLocal = this.update(tipoOrganoLocal);

        return creaTipoOrganoDesdeTipoOrganoLocal(tipoOrganoLocal);
    }

    private TipoOrganoLocal creaTipoOrganoLocalDesdeTipoOrgano(TipoOrgano tipoOrgano)
    {
        TipoOrganoLocal tipoOrganoLocal = new TipoOrganoLocal();

        tipoOrganoLocal.setId(tipoOrgano.getId());
        tipoOrganoLocal.setNombre(tipoOrgano.getNombre());
        tipoOrganoLocal.setCodigo(tipoOrgano.getCodigo());
        return tipoOrganoLocal;
    }

}
