package es.uji.apps.goc.services;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.goc.dao.ReunionDAO;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.dto.ReunionTemplate;
import es.uji.apps.goc.exceptions.MiembrosExternosException;
import es.uji.apps.goc.exceptions.OrganosExternosException;
import es.uji.apps.goc.exceptions.PersonasExternasException;
import es.uji.apps.goc.exceptions.ReunionNoDisponibleException;
import es.uji.apps.goc.templates.HTMLTemplate;
import es.uji.apps.goc.templates.Template;
import es.uji.commons.rest.CoreBaseService;
import es.uji.commons.sso.AccessManager;

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

        List<Reunion> reuniones = reunionService.getReunionesTodasByAsistenteIdOrCreadorIdOrSuplenteId(connectedUserId);

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
    public Template reunion(@PathParam("reunionId") Long reunionId, @QueryParam("lang") String lang)
            throws OrganosExternosException, MiembrosExternosException,
            ReunionNoDisponibleException, PersonasExternasException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        Reunion reunion = reunionDAO.getReunionConOrganosById(reunionId);

        if (reunion == null)
        {
            throw new ReunionNoDisponibleException();
        }

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
