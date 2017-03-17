package es.uji.apps.goc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import es.uji.apps.goc.dao.OrganoReunionMiembroDAO;
import es.uji.apps.goc.dao.ReunionComentarioDAO;
import es.uji.apps.goc.dao.ReunionDAO;
import es.uji.apps.goc.dto.OrganoReunionMiembro;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.dto.ReunionComentario;
import es.uji.apps.goc.exceptions.AsistenteNoEncontradoException;
import es.uji.commons.rest.UIEntity;

@Service
@Component
public class ReunionComentarioService
{
    @Autowired
    private ReunionComentarioDAO reunionComentarioDAO;

    @Autowired
    private OrganoReunionMiembroDAO organoReunionMiembroDAO;

    @Autowired
    private ReunionDAO reunionDAO;

    public List<ReunionComentario> getComentariosByReunionId(Long reunionId, Long connectedUserId)
    {
        return reunionComentarioDAO.getComentariosByReunionId(reunionId);

    }

    @Transactional
    public ReunionComentario addComentario(UIEntity comentarioUI, Long connectedUserId)
        throws AsistenteNoEncontradoException {
        Long reunionId = Long.parseLong(comentarioUI.get("reunionId"));

        List<OrganoReunionMiembro> listaAsistentes = organoReunionMiembroDAO
            .getMiembroByAsistenteIdOrSuplenteId(reunionId, connectedUserId);
        Reunion reunionBD = reunionDAO.getReunionById(reunionId);
        List<OrganoReunionMiembro> listaAstenteFiltrada =
            listaAsistentes.stream().filter(l -> l.getMiembroId().equals(connectedUserId)).collect(Collectors.toList());
        if(listaAstenteFiltrada.isEmpty() && !reunionBD.getCreadorId().equals(connectedUserId)){
            throw new AsistenteNoEncontradoException();
        }

        ReunionComentario reunionComentario = new ReunionComentario();
        reunionComentario.setComentario((comentarioUI.get("comentario")));

        Reunion reunion = new Reunion(reunionId);
        reunionComentario.setReunion(reunion);

        reunionComentario.setCreadorId(connectedUserId);
        reunionComentario.setCreadorNombre(getNombreCreadorByReunionId(reunion.getId(), connectedUserId));
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
