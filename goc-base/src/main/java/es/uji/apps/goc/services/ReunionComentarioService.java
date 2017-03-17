package es.uji.apps.goc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import es.uji.apps.goc.dao.OrganoReunionMiembroDAO;
import es.uji.apps.goc.dao.ReunionComentarioDAO;
import es.uji.apps.goc.dto.OrganoReunionMiembro;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.dto.ReunionComentario;
import es.uji.commons.rest.UIEntity;
import es.uji.commons.sso.User;

@Service
@Component
public class ReunionComentarioService
{
    @Autowired
    private ReunionComentarioDAO reunionComentarioDAO;

    @Autowired
    private OrganoReunionMiembroDAO organoReunionMiembroDAO;

    public List<ReunionComentario> getComentariosByReunionId(Long reunionId, Long connectedUserId)
    {
        return reunionComentarioDAO.getComentariosByReunionId(reunionId);

    }

    @Transactional
    public ReunionComentario addComentario(UIEntity comentarioUI, User connectedUser)
    {
        ReunionComentario reunionComentario = new ReunionComentario();
        reunionComentario.setComentario((comentarioUI.get("comentario")));

        Reunion reunion = new Reunion(Long.parseLong(comentarioUI.get("reunionId")));
        reunionComentario.setReunion(reunion);

        reunionComentario.setCreadorId(connectedUser.getId());
        String nombreCreadorByReunionId = getNombreCreadorByReunionId(reunion.getId(), connectedUser.getId());
        nombreCreadorByReunionId = (nombreCreadorByReunionId == null || nombreCreadorByReunionId.equals("")) ?
            connectedUser.getName() : nombreCreadorByReunionId;
        reunionComentario.setCreadorNombre(nombreCreadorByReunionId);
        reunionComentario.setFecha(new Date());

        return reunionComentarioDAO.insert(reunionComentario);
    }

    private String getNombreCreadorByReunionId(Long reunionId, Long connectedUserId)
    {
        List<OrganoReunionMiembro> listaAsistentes = organoReunionMiembroDAO
                .getMiembroByAsistenteIdOrSuplenteId(reunionId, connectedUserId);

        if (listaAsistentes.size() == 0)
        {
            return "";
        }

        OrganoReunionMiembro creador = listaAsistentes.get(0);

        if (connectedUserId.equals(Long.parseLong(creador.getMiembroId())))
        {
            return creador.getNombre();
        }
        else if (creador.getSuplenteId().equals(connectedUserId))
        {
            return creador.getSuplenteNombre();
        }

        return "";
    }
}
