package es.uji.apps.goc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uji.apps.goc.dao.TipoOrganoDAO;
import es.uji.apps.goc.dto.TipoOrganoLocal;
import es.uji.apps.goc.model.TipoOrgano;

@Service
public class TipoOrganoService
{
    @Autowired
    private TipoOrganoDAO tipoOrganoDAO;

    public List<TipoOrgano> getTiposOrgano(Long connectedUserId) {
        return tipoOrganoDAO.getTiposOrgano();
    }

    public TipoOrgano addTipoOrgano(TipoOrgano tipoOrgano, Long connectedUserId)
    {
        return tipoOrganoDAO.insertTipoOrgano(tipoOrgano);
    }

    public TipoOrgano updateTipoOrgano(TipoOrgano tipoOrgano, Long connectedUserId)
    {
        return tipoOrganoDAO.updateTipoOrgano(tipoOrgano);
    }

    public void removeTipoOrganoById(Long tipoOrganoId) {
        tipoOrganoDAO.delete(TipoOrganoLocal.class, tipoOrganoId);
    }
}
