package es.uji.apps.goc.services;

import es.uji.apps.goc.dao.ReunionInvitadoDAO;
import es.uji.apps.goc.dao.TipoOrganoDAO;
import es.uji.apps.goc.dto.ReunionInvitado;
import es.uji.apps.goc.dto.TipoOrganoLocal;
import es.uji.apps.goc.model.TipoOrgano;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvitadosService
{
    @Autowired
    private ReunionInvitadoDAO reunionInvitadoDAO;

    public List<ReunionInvitado> getInvitadosByReunionId(Long reunionId, Long connectedUserId)
    {
        return reunionInvitadoDAO.getInvitadosByReunionId(reunionId);
    }
}
