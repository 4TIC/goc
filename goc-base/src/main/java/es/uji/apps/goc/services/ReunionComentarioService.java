package es.uji.apps.goc.services;

import es.uji.apps.goc.dao.OrganoReunionMiembroDAO;
import es.uji.apps.goc.dao.ReunionComentarioDAO;
import es.uji.apps.goc.dao.ReunionDAO;
import es.uji.apps.goc.dto.OrganoReunionMiembro;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.dto.ReunionComentario;
import es.uji.apps.goc.exceptions.AsistenteNoEncontradoException;
import es.uji.commons.rest.UIEntity;
import es.uji.commons.sso.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    public ReunionComentario addComentario(UIEntity comentarioUI, User userConnected)
            throws AsistenteNoEncontradoException
    {
        Long reunionId = Long.parseLong(comentarioUI.get("reunionId"));
        Reunion reunionBD = reunionDAO.getReunionById(reunionId);
        Long connectedUserId = userConnected.getId();

        checkIfUsuarioInReunion(reunionBD, connectedUserId);

        ReunionComentario reunionComentario = new ReunionComentario();
        reunionComentario.setComentario((comentarioUI.get("comentario")));

        Reunion reunion = new Reunion(reunionId);
        reunionComentario.setReunion(reunion);

        reunionComentario.setCreadorId(connectedUserId);
        String nombreCreadorByReunionId = getNombreCreadorByReunionId(reunion.getId(), connectedUserId);
        nombreCreadorByReunionId = (nombreCreadorByReunionId == null || nombreCreadorByReunionId.equals("")) ? reunionBD
                .getCreadorNombre() : nombreCreadorByReunionId;
        reunionComentario.setCreadorNombre(nombreCreadorByReunionId);
        reunionComentario.setFecha(new Date());

        return reunionComentarioDAO.insert(reunionComentario);
    }

    public void deleteComentario(Long reunionId, Long comentarioId, User userConnected)
            throws AsistenteNoEncontradoException
    {
        Reunion reunionBD = reunionDAO.getReunionById(reunionId);

        checkIfUsuarioInReunion(reunionBD, userConnected.getId());

        reunionComentarioDAO.delete(ReunionComentario.class, comentarioId);
    }

    private void checkIfUsuarioInReunion(Reunion reunionBD, Long connectedUserId)
            throws AsistenteNoEncontradoException
    {
        List<OrganoReunionMiembro> listaAsistentes =
                organoReunionMiembroDAO.getMiembroByAsistenteIdOrSuplenteId(reunionBD.getId(), connectedUserId);
        List<OrganoReunionMiembro> listaAstenteFiltrada = listaAsistentes.stream()
                .filter(l -> l.getMiembroId().equals(connectedUserId))
                .collect(Collectors.toList());

        if (listaAstenteFiltrada.isEmpty() && !reunionBD.getCreadorId().equals(connectedUserId))
        {
            throw new AsistenteNoEncontradoException();
        }
    }

    private String getNombreCreadorByReunionId(Long reunionId, Long connectedUserId)
    {
        List<OrganoReunionMiembro> listaAsistentes =
                organoReunionMiembroDAO.getMiembroByAsistenteIdOrSuplenteId(reunionId, connectedUserId);

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
