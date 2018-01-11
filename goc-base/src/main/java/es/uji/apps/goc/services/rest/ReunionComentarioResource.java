package es.uji.apps.goc.services.rest;

import com.sun.jersey.api.core.InjectParam;
import es.uji.apps.goc.dto.ReunionComentario;
import es.uji.apps.goc.exceptions.AsistenteNoEncontradoException;
import es.uji.apps.goc.exceptions.ReunionYaCompletadaException;
import es.uji.apps.goc.services.ReunionComentarioService;
import es.uji.apps.goc.services.ReunionService;
import es.uji.commons.rest.CoreBaseService;
import es.uji.commons.rest.UIEntity;
import es.uji.commons.sso.AccessManager;
import es.uji.commons.sso.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/reuniones/{reunionId}/comentarios/")
public class ReunionComentarioResource extends CoreBaseService
{
    @InjectParam
    private ReunionService reunionService;

    @InjectParam
    private ReunionComentarioService reunionComentarioService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getReunionComentarios(@PathParam("reunionId") Long reunionId)
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        List<UIEntity> entities = new ArrayList<>();

        for (ReunionComentario comentario : reunionComentarioService.getComentariosByReunionId(reunionId,
                connectedUserId))
        {
            UIEntity entity = UIEntity.toUI(comentario);
            entity.put("permiteBorrado",
                    reunionComentarioService.isPermiteBorrado(reunionId, comentario, connectedUserId));

            entities.add(entity);
        }

        return entities;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity addComentario(UIEntity comentarioUI)
            throws ReunionYaCompletadaException, AsistenteNoEncontradoException
    {
        User userConnected = AccessManager.getConnectedUser(request);

        reunionService.compruebaReunionNoCompletada(Long.parseLong(comentarioUI.get("reunionId")));
        ReunionComentario reunionComentario = reunionComentarioService.addComentario(comentarioUI, userConnected);

        return UIEntity.toUI(reunionComentario);
    }

    @DELETE
    @Path("{comentarioId}")
    public void deleteComentario(@PathParam("reunionId") Long reunionId, @PathParam("comentarioId") Long comentarioId)
            throws ReunionYaCompletadaException, AsistenteNoEncontradoException
    {
        User userConnected = AccessManager.getConnectedUser(request);

        reunionService.compruebaReunionNoCompletada(reunionId);
        reunionComentarioService.deleteComentario(reunionId, comentarioId, userConnected);
    }

    private List<UIEntity> reunionComentariosToUI(List<ReunionComentario> comentarios)
    {
        List<UIEntity> listaUI = new ArrayList<>();

        for (ReunionComentario reunionComentario : comentarios)
        {
            listaUI.add(reunionComentarioToUI(reunionComentario));
        }

        return listaUI;

    }

    private UIEntity reunionComentarioToUI(ReunionComentario reunionComentario)
    {
        UIEntity ui = new UIEntity();

        ui.put("id", reunionComentario.getId());
        ui.put("creadorId", reunionComentario.getCreadorId());
        ui.put("creadorNombre", reunionComentario.getCreadorNombre());
        ui.put("fecha", reunionComentario.getFecha());
        ui.put("comentario", reunionComentario.getComentario());
        return ui;
    }
}
