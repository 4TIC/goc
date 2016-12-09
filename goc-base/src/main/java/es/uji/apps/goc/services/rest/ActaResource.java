package es.uji.apps.goc.services.rest;

import com.sun.jersey.api.core.InjectParam;
import es.uji.apps.goc.auth.LanguageConfig;
import es.uji.apps.goc.dao.ReunionDAO;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.dto.ReunionTemplate;
import es.uji.apps.goc.exceptions.*;
import es.uji.apps.goc.model.Persona;
import es.uji.apps.goc.services.PersonaService;
import es.uji.apps.goc.services.ReunionService;
import es.uji.apps.goc.templates.PDFTemplate;
import es.uji.apps.goc.templates.Template;
import es.uji.commons.sso.AccessManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

@Service
@Path("actas")
public class ActaResource
{
    @InjectParam
    private ReunionService reunionService;

    @InjectParam
    private LanguageConfig languageConfig;

    @InjectParam
    private PersonaService personaService;

    @InjectParam
    private ReunionDAO reunionDAO;

    @Value("${goc.logo}")
    private String logoUrl;

    @GET
    @Path("{reunionId}")
    @Produces("application/pdf")
    @Transactional
    public Template reunion(@PathParam("reunionId") Long reunionId, @QueryParam("lang") String lang,
                            @Context HttpServletRequest request)
            throws OrganosExternosException, MiembrosExternosException, ReunionNoDisponibleException,
            PersonasExternasException, InvalidAccessException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        Reunion reunion = reunionDAO.getReunionConOrganosById(reunionId);

        if (reunion == null)
        {
            throw new ReunionNoDisponibleException();
        }

        reunion.tieneAcceso(connectedUserId);

        Persona convocante = personaService
                .getPersonaFromDirectoryByPersonaId(reunion.getCreadorId());

        ReunionTemplate reunionTemplate = reunionService.getReunionTemplateDesdeReunion(reunion,
                connectedUserId);

        String applang = getLangCode(lang);

        Template template = new PDFTemplate("acta-" + applang);
        template.put("logo", logoUrl);
        template.put("reunion", reunionTemplate);
        template.put("convocante", convocante);
        template.put("applang", applang);

        return template;
    }

    private String getLangCode(String lang)
    {
        if (lang == null || lang.isEmpty() ||
                !(lang.toLowerCase().equals(languageConfig.mainLanguage) || lang.toLowerCase().equals(languageConfig.alternativeLanguage)))
        {
            return languageConfig.mainLanguage;
        }

        return lang;
    }
}
