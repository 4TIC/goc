package es.uji.apps.goc.services;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import es.uji.apps.goc.exceptions.PersonasExternasException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.goc.dao.ReunionDAO;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.dto.ReunionTemplate;
import es.uji.apps.goc.exceptions.MiembrosExternosException;
import es.uji.apps.goc.exceptions.OrganosExternosException;
import es.uji.apps.goc.exceptions.ReunionNoDisponibleException;
import es.uji.commons.rest.CoreBaseService;
import es.uji.commons.sso.AccessManager;
import es.uji.commons.web.template.HTMLTemplate;
import es.uji.commons.web.template.Template;

import java.util.List;

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

        List<Reunion> reuniones = reunionService.getReunionesCompletadasByAsistenteIdOrSuplenteId(connectedUserId);

        String applang = getLangCode(lang);
        Template template = new HTMLTemplate("goc/reuniones-" + applang);
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
        Template template = new HTMLTemplate("goc/reunion-" + applang);
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
