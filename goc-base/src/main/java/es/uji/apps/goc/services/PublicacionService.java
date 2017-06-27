package es.uji.apps.goc.services;

import com.sun.jersey.api.core.InjectParam;

import es.uji.apps.goc.dto.*;
import es.uji.apps.goc.model.AcuerdosSearch;
import es.uji.apps.goc.model.Persona;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import es.uji.apps.goc.auth.LanguageConfig;
import es.uji.apps.goc.auth.PersonalizationConfig;
import es.uji.apps.goc.dao.ReunionDAO;
import es.uji.apps.goc.exceptions.InvalidAccessException;
import es.uji.apps.goc.exceptions.MiembrosExternosException;
import es.uji.apps.goc.exceptions.OrganosExternosException;
import es.uji.apps.goc.exceptions.PersonasExternasException;
import es.uji.apps.goc.exceptions.ReunionNoDisponibleException;
import es.uji.apps.goc.templates.HTMLTemplate;
import es.uji.apps.goc.templates.Template;
import es.uji.commons.rest.CoreBaseService;
import es.uji.commons.sso.AccessManager;

import static java.util.stream.Collectors.toList;

@Service
@Path("publicacion")
public class PublicacionService extends CoreBaseService
{
    public static final int RESULTADOS_POR_PAGINA = 5;
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

    @InjectParam
    private PersonalizationConfig personalizationConfig;

    @GET
    @Path("reuniones")
    @Produces(MediaType.TEXT_HTML)
    public Template reuniones(@QueryParam("lang") String lang)
            throws OrganosExternosException, MiembrosExternosException, ReunionNoDisponibleException,
            PersonasExternasException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);

        List<ReunionPermiso> reuniones = reunionService.getReunionesAccesiblesByPersonaId(connectedUserId);

        String applang = getLangCode(lang);
        Template template = new HTMLTemplate("reuniones-" + applang);
        template.put("logo", logoUrl);
        template.put("reuniones", reuniones);
        template.put("applang", applang);
        template.put("charset", charset);
        template.put("mainLanguage", languageConfig.mainLanguage);
        template.put("alternativeLanguage", languageConfig.alternativeLanguage);
        template.put("mainLanguageDescription", languageConfig.mainLanguageDescription);
        template.put("alternativeLanguageDescription", languageConfig.alternativeLanguageDescription);
        template.put("customCSS", (personalizationConfig.customCSS != null) ? personalizationConfig.customCSS : "");
        template.put("connectedUserId", connectedUserId);

        return template;
    }

    @GET
    @Path("acuerdos")
    @Produces(MediaType.TEXT_HTML)
    public Template acuerdos(@QueryParam("lang") String lang, @QueryParam("tipoOrganoId") Long tipoOrganoId,
            @QueryParam("organoId") Long organoId, @QueryParam("descriptorId") Long descriptorId,
            @QueryParam("claveId") Long claveId, @QueryParam("anyo") Integer anyo,
            @QueryParam("fInicio") String fInicio, @QueryParam("fFin") String fFin, @QueryParam("texto") String texto,
            @QueryParam("pagina") @DefaultValue("0") Integer pagina)
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        String applang = getLangCode(lang);

        List<Integer> anyos = reunionService.getAnyosConReunionesPublicas();
        List<TipoOrganoLocal> tiposOrganos = reunionService.getTiposOrganosConReunionesPublicas();
        List<OrganoLocal> organos = new ArrayList<>();
        List<Descriptor> descriptoresConReunionesPublicas = null;
        List<Clave> claves = null;
        List<Reunion> reuniones = new ArrayList<>();
        List<ReunionTemplate> reunionesTemplate = null;

        descriptoresConReunionesPublicas = reunionService.getDescriptoresConReunionesPublicas(anyo);

        if (tipoOrganoId != null)
        {
            organos = reunionService.getOrganosConReunionesPublicas(tipoOrganoId, anyo);
        }

        if (!descriptoresConReunionesPublicas.isEmpty() && descriptorId != null)
        {
            claves = reunionService.getClavesConReunionesPublicas(descriptorId, anyo);
        }

        AcuerdosSearch acuerdosSearch = new AcuerdosSearch(anyo, pagina * RESULTADOS_POR_PAGINA, RESULTADOS_POR_PAGINA);

        if (!descriptoresConReunionesPublicas.isEmpty())
        {
            acuerdosSearch.setClaveId(claveId);
            acuerdosSearch.setDescriptorId(descriptorId);
        }

        acuerdosSearch.setfInicio(getDate(fInicio));
        acuerdosSearch.setfFin(getDate(fFin));
        acuerdosSearch.setTexto(texto);
        acuerdosSearch.setTipoOrganoId(tipoOrganoId);
        acuerdosSearch.setOrganoId(organoId);

        acuerdosSearch.setIdiomaAlternatico(applang.equals(languageConfig.alternativeLanguage) ? true : false);

        reuniones = reunionService.getReunionesPublicas(acuerdosSearch);
        Integer numReuniones = reunionService.getNumReunionesPublicas(acuerdosSearch);

        reunionesTemplate = buildReunionTemplate(connectedUserId, reuniones);

        Template template = new HTMLTemplate("acuerdos-" + applang);
        template.put("logo", logoUrl);
        template.put("applang", applang);
        template.put("charset", charset);
        template.put("lang", lang);
        template.put("mainLanguage", languageConfig.mainLanguage);
        template.put("alternativeLanguage", languageConfig.alternativeLanguage);
        template.put("mainLanguageDescription", languageConfig.mainLanguageDescription);
        template.put("alternativeLanguageDescription", languageConfig.alternativeLanguageDescription);
        template.put("customCSS", (personalizationConfig.customCSS != null) ? personalizationConfig.customCSS : "");
        template.put("connectedUserId", connectedUserId);
        template.put("tiposOrganos", tiposOrganos);
        template.put("tipoOrganoId", tipoOrganoId);
        template.put("organoId", organoId);
        template.put("organos", organos);
        template.put("reuniones", reunionesTemplate);
        template.put("descriptores", descriptoresConReunionesPublicas);
        template.put("descriptorId", descriptorId);
        template.put("claves", claves);
        template.put("claveId", claveId);
        template.put("anyos", anyos);
        template.put("anyo", anyo);
        template.put("fInicio", fInicio);
        template.put("fFin", fFin);
        template.put("texto", texto);

        if (pagina > 0)
        {
            template.put("hasPrevPage", true);
        }

        if (numReuniones > ((pagina * RESULTADOS_POR_PAGINA) + RESULTADOS_POR_PAGINA))
        {
            template.put("hasNextPage", true);
        }

        template.put("pagina", pagina);

        return template;
    }

    public Date getDate(String value)
    {
        try
        {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            return formatter.parse(value);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private List<ReunionTemplate> buildReunionTemplate(Long connectedUserId, List<Reunion> reuniones)
    {
        List<ReunionTemplate> reunionesTemplate;
        reunionesTemplate = reuniones.stream()
                .map(r -> reunionService.getReunionTemplateDesdeReunion(r, connectedUserId, false))
                .collect(Collectors.toList());
        return reunionesTemplate;
    }

    @GET
    @Path("reuniones/{reunionId}")
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public Template reunion(@PathParam("reunionId") Long reunionId, @QueryParam("lang") String lang)
            throws OrganosExternosException, MiembrosExternosException, ReunionNoDisponibleException,
            PersonasExternasException, InvalidAccessException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        Reunion reunion = reunionDAO.getReunionConOrganosById(reunionId);

        if (reunion == null)
        {
            throw new ReunionNoDisponibleException();
        }

        if (!reunionDAO.tieneAcceso(reunionId, connectedUserId))
        {
            throw new InvalidAccessException("No se tiene acceso a esta reunión");
        }

        boolean permitirComentarios = reunion.isPermitirComentarios(connectedUserId);

        ReunionTemplate reunionTemplate = reunionService.getReunionTemplateDesdeReunion(reunion, connectedUserId, true);

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
        template.put("customCSS", (personalizationConfig.customCSS != null) ? personalizationConfig.customCSS : "");
        template.put("connectedUserId", connectedUserId);
        template.put("permitirComentarios", permitirComentarios);

        return template;
    }

    private String getLangCode(String lang)
    {
        if (lang == null || lang.isEmpty() || !(lang.toLowerCase()
                .equals(languageConfig.mainLanguage) || lang.toLowerCase().equals(languageConfig.alternativeLanguage)))
        {
            return languageConfig.mainLanguage;
        }

        return lang;
    }
}
