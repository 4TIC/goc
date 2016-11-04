package es.uji.apps.goc.services;

import com.sun.jersey.api.core.InjectParam;
import es.uji.apps.goc.dao.ReunionDAO;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.dto.ReunionTemplate;
import es.uji.apps.goc.exceptions.*;
import es.uji.apps.goc.templates.HTMLTemplate;
import es.uji.apps.goc.templates.Template;
import es.uji.commons.rest.CoreBaseService;
import es.uji.commons.sso.AccessManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Path("publicacion")
public class PublicacionService extends CoreBaseService
{
    @Autowired
    private ReunionDAO reunionDAO;

    @InjectParam
    private ReunionService reunionService;

    @GET
    @Path("reuniones")
    @Produces(MediaType.TEXT_HTML)
    public Template reuniones(@QueryParam("lang") String lang) throws OrganosExternosException,
            MiembrosExternosException, ReunionNoDisponibleException, PersonasExternasException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        List<ReunionTemplate> reunionesAsistente = reunionService
                .getReunionesByAsistenteIdOrSuplenteId(connectedUserId);

        List<Long> reunionesIds = reunionesAsistente.stream().map(r -> r.getId())
                .collect(Collectors.toList());

        List<ReunionTemplate> reunionesConvocante = reunionService
                .getReunionesByCreadorId(connectedUserId).stream()
                .filter(r -> !reunionesIds.contains(r.getId())).collect(Collectors.toList());

        List<ReunionTemplate> reuniones = Stream
                .concat(reunionesAsistente.stream(), reunionesConvocante.stream())
                .sorted((r1, r2) -> r2.getFecha().compareTo(r1.getFecha()))
                .collect(Collectors.toList());

        String applang = getLangCode(lang);
        Template template = new HTMLTemplate("reuniones-" + applang);
        template.put("reuniones", reuniones);
        template.put("applang", applang);
        template.put("connectedUserId", connectedUserId);

        return template;
    }

    @GET
    @Path("reuniones/{reunionId}")
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public Template reunion(@PathParam("reunionId") Long reunionId, @QueryParam("lang") String lang)
            throws OrganosExternosException, MiembrosExternosException,
            ReunionNoDisponibleException, PersonasExternasException, InvalidAccessException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        Reunion reunion = reunionDAO.getReunionConOrganosById(reunionId);

        if (reunion == null)
        {
            throw new ReunionNoDisponibleException();
        }

        reunion.tieneAcceso(connectedUserId);

        ReunionTemplate reunionTemplate = reunionService.getReunionTemplateDesdeReunion(reunion,
                connectedUserId);

        String applang = getLangCode(lang);
        Template template = new HTMLTemplate("reunion-" + applang);
        template.put("reunion", reunionTemplate);
        template.put("applang", applang);
        template.put("connectedUserId", connectedUserId);

        return template;
    }

    private String getLangCode(String lang)
    {

        if (lang == null || lang.isEmpty()
                || !(lang.toLowerCase().equals("ca") || lang.toLowerCase().equals("es")))
        {
            return "ca";
        }

        return lang;
    }
}
