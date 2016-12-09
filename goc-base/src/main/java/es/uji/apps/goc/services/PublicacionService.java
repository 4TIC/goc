package es.uji.apps.goc.services;

import com.sun.jersey.api.core.InjectParam;
import es.uji.apps.goc.auth.LanguageConfig;
import es.uji.apps.goc.dao.ReunionDAO;
import es.uji.apps.goc.dto.OrganoLocal;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.dto.ReunionTemplate;
import es.uji.apps.goc.dto.TipoOrganoLocal;
import es.uji.apps.goc.exceptions.*;
import es.uji.apps.goc.model.Organo;
import es.uji.apps.goc.model.TipoOrgano;
import es.uji.apps.goc.templates.HTMLTemplate;
import es.uji.apps.goc.templates.Template;
import es.uji.commons.rest.CoreBaseService;
import es.uji.commons.sso.AccessManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Service
@Path("publicacion")
public class PublicacionService extends CoreBaseService
{
    @InjectParam
    private ReunionDAO reunionDAO;

    @InjectParam
    private LanguageConfig languageConfig;

    @Value("${goc.logo}")
    private String logoUrl;

    @Value("${goc.charset}")
    private String charset;

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
                .collect(toList());

        List<ReunionTemplate> reunionesConvocante = reunionService
                .getReunionesByCreadorId(connectedUserId).stream()
                .filter(r -> !reunionesIds.contains(r.getId())).collect(toList());

        List<ReunionTemplate> reuniones = Stream
                .concat(reunionesAsistente.stream(), reunionesConvocante.stream())
                .sorted((r1, r2) -> r2.getFecha().compareTo(r1.getFecha()))
                .collect(toList());

        String applang = getLangCode(lang);
        Template template = new HTMLTemplate("reuniones-" + applang);
        template.put("logo", logoUrl);
        template.put("reuniones", reuniones);
        template.put("applang", applang);
        template.put("mainLanguage", languageConfig.mainLanguage);
        template.put("alternativeLanguage", languageConfig.alternativeLanguage);
        template.put("mainLanguageDescription", languageConfig.mainLanguageDescription);
        template.put("alternativeLanguageDescription", languageConfig.alternativeLanguageDescription);
        template.put("connectedUserId", connectedUserId);

        return template;
    }

    @GET
    @Path("acuerdos")
    @Produces(MediaType.TEXT_HTML)
    public Template acuerdos(@QueryParam("lang") String lang, @QueryParam("tipoOrganoId") Long tipoOrganoId,
                             @QueryParam("organoId") Long organoId)
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        String applang = getLangCode(lang);

        List<TipoOrganoLocal> tiposOrganos = reunionService.getTiposOrganosConReunionesPublicas();
        List<OrganoLocal> organos = new ArrayList<>();
        List<Reunion> reuniones = new ArrayList<>();

        if (tipoOrganoId != null)
        {
            organos = reunionService.getOrganosConReunionesPublicas(tipoOrganoId);

            if (organoId != null)
            {
                reuniones = reunionService.getReunionesPublicas(tipoOrganoId, organoId);
            }
        }

        Template template = new HTMLTemplate("acuerdos-" + applang);
        template.put("logo", logoUrl);
        template.put("applang", applang);
        template.put("mainLanguage", languageConfig.mainLanguage);
        template.put("alternativeLanguage", languageConfig.alternativeLanguage);
        template.put("mainLanguageDescription", languageConfig.mainLanguageDescription);
        template.put("alternativeLanguageDescription", languageConfig.alternativeLanguageDescription);
        template.put("connectedUserId", connectedUserId);
        template.put("tiposOrganos", tiposOrganos);
        template.put("tipoOrganoId", tipoOrganoId);
        template.put("organoId", organoId);
        template.put("organos", organos);
        template.put("reuniones", reuniones);

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
        template.put("logo", logoUrl);
        template.put("charset", charset);
        template.put("reunion", reunionTemplate);
        template.put("applang", applang);
        template.put("mainLanguage", languageConfig.mainLanguage);
        template.put("alternativeLanguage", languageConfig.alternativeLanguage);
        template.put("mainLanguageDescription", languageConfig.mainLanguageDescription);
        template.put("alternativeLanguageDescription", languageConfig.alternativeLanguageDescription);
        template.put("connectedUserId", connectedUserId);

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
