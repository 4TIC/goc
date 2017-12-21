package es.uji.apps.goc.services;

import com.sun.jersey.api.core.InjectParam;
import es.uji.apps.goc.auth.LanguageConfig;
import es.uji.apps.goc.auth.PersonalizationConfig;
import es.uji.apps.goc.dao.OrganoAutorizadoDAO;
import es.uji.apps.goc.dao.ReunionDAO;
import es.uji.apps.goc.dto.*;
import es.uji.apps.goc.exceptions.*;
import es.uji.apps.goc.model.AcuerdosSearch;
import es.uji.apps.goc.templates.HTMLTemplate;
import es.uji.apps.goc.templates.PDFTemplate;
import es.uji.apps.goc.templates.Template;
import es.uji.commons.rest.CoreBaseService;
import es.uji.commons.sso.AccessManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Path("publicacion")
public class PublicacionService extends CoreBaseService
{
    public static final int RESULTADOS_POR_PAGINA = 5;

    @InjectParam
    private ReunionDAO reunionDAO;

    @InjectParam
    private OrganoAutorizadoDAO organoAutorizadoDAO;

    @InjectParam
    private LanguageConfig languageConfig;

    @Value("${goc.logo}")
    private String logoUrl;

    @Value("${goc.logoPublic}")
    private String logoPublic;

    @Value("${goc.logoDocumentos}")
    private String logoDocumentosUrl;

    @Value("${goc.charset}")
    private String charset;

    @Value("${goc.nombreInstitucion}")
    private String nombreInstitucion;

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

        String applang = languageConfig.getLangCode(lang);
        Template template = new HTMLTemplate("reuniones-" + applang);
        template.put("logo", logoUrl);
        template.put("logoPublic", (logoPublic != null) ? logoPublic : logoUrl);
        template.put("reuniones", setLanguageFields(reuniones, lang));
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

    public List<ReunionPermiso> setLanguageFields(List<ReunionPermiso> reuniones, String lang)
    {
        for (ReunionPermiso reunionPermiso : reuniones)
        {
            reunionPermiso.setAsunto(languageConfig.isMainLangauge(
                    lang) ? reunionPermiso.getAsunto() : reunionPermiso.getAsuntoAlternativo());

            reunionPermiso.setUrlActa(languageConfig.isMainLangauge(lang) ? reunionPermiso.getUrlActa() : reunionPermiso
                    .getUrlActaAlternativa());

            reunionPermiso.setUrlAsistencia(languageConfig.isMainLangauge(lang) ? reunionPermiso.getUrlAsistencia() : reunionPermiso
                    .getUrlAsistenciaAlternativa());
        }

        return reuniones;
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
        String applang = languageConfig.getLangCode(lang);

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

        acuerdosSearch.setIdiomaAlternativo(!languageConfig.isMainLangauge(lang));

        reuniones = reunionService.getReunionesPublicas(acuerdosSearch);
        Integer numReuniones = reunionService.getNumReunionesPublicas(acuerdosSearch);

        reunionesTemplate = buildReunionTemplate(connectedUserId, reuniones, lang);

        Template template = new HTMLTemplate("acuerdos-" + applang);
        template.put("logo", logoUrl);
        template.put("logoPublic", (logoPublic != null) ? logoPublic : logoUrl);
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

    private List<ReunionTemplate> buildReunionTemplate(Long connectedUserId, List<Reunion> reuniones, String lang)
    {
        List<ReunionTemplate> reunionesTemplate;
        reunionesTemplate = reuniones.stream()
                .map(r -> reunionService.getReunionTemplateDesdeReunion(r, connectedUserId, false,
                        languageConfig.isMainLangauge(lang)))
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

        boolean permitirComentarios = reunion.isPermitirComentarios(connectedUserId, organoAutorizadoDAO.getAutorizadosByReunionId(reunionId));

        ReunionTemplate reunionTemplate = reunionService.getReunionTemplateDesdeReunion(reunion, connectedUserId, true,
                languageConfig.isMainLangauge(lang));

        String applang = languageConfig.getLangCode(lang);

        Template template = new HTMLTemplate("reunion-" + applang);
        template.put("logo", logoUrl);
        template.put("logoPublic", (logoPublic != null) ? logoPublic : logoUrl);
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

    @GET
    @Path("reuniones/{reunionId}/acuerdos")
    @Produces(MediaType.TEXT_HTML)
    public Template reunionAcuerdos(@PathParam("reunionId") Long reunionId, @QueryParam("lang") String lang)
            throws OrganosExternosException, MiembrosExternosException, ReunionNoDisponibleException,
            PersonasExternasException, InvalidAccessException, ReunionNoCompletadaException
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

        if (!reunion.getCompletada())
        {
            throw new ReunionNoCompletadaException();
        }

        ReunionTemplate reunionTemplate = reunionService.getReunionTemplateDesdeReunion(reunion, connectedUserId, true,
                languageConfig.isMainLangauge(lang));

        String applang = languageConfig.getLangCode(lang);

        Template template = new HTMLTemplate("reunion-acuerdos-" + applang);
        template.put("logo", logoUrl);
        template.put("logoPublic", (logoPublic != null) ? logoPublic : logoUrl);
        template.put("charset", charset);
        template.put("reunion", reunionTemplate);
        template.put("applang", applang);
        template.put("mainLanguage", languageConfig.mainLanguage);
        template.put("alternativeLanguage", languageConfig.alternativeLanguage);
        template.put("mainLanguageDescription", languageConfig.mainLanguageDescription);
        template.put("alternativeLanguageDescription", languageConfig.alternativeLanguageDescription);
        template.put("customCSS", (personalizationConfig.customCSS != null) ? personalizationConfig.customCSS : "");
        template.put("connectedUserId", connectedUserId);

        return template;
    }

    @GET
    @Path("reuniones/{reunionId}/acuerdos/{puntoOrdenDiaId}")
    @Produces("application/pdf")
    public Template reunionAcuerdoCertificado(@PathParam("reunionId") Long reunionId,
            @PathParam("puntoOrdenDiaId") Long puntoOrdenDiaId, @QueryParam("lang") String lang)
            throws OrganosExternosException, MiembrosExternosException, ReunionNoDisponibleException,
            PersonasExternasException, InvalidAccessException, ReunionNoCompletadaException,
            PuntoDelDiaNoDisponibleException, PuntoDelDiaNoTieneAcuerdosException
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

        if (!reunion.getCompletada())
        {
            throw new ReunionNoCompletadaException();
        }

        ReunionTemplate reunionTemplate = reunionService.getReunionTemplateDesdeReunion(reunion, connectedUserId, true,
                languageConfig.isMainLangauge(lang));

        PuntoOrdenDiaTemplate puntoOrdenDiaTemplate = reunionTemplate.getPuntosOrdenDia()
                .stream()
                .filter(p -> p.getId().equals(puntoOrdenDiaId))
                .findFirst()
                .orElse(null);

        if (puntoOrdenDiaTemplate == null)
        {
            throw new PuntoDelDiaNoDisponibleException();
        }

        if (puntoOrdenDiaTemplate.getAcuerdos() == null || puntoOrdenDiaTemplate.getAcuerdos().isEmpty())
        {
            throw new PuntoDelDiaNoTieneAcuerdosException();
        }

        String applang = languageConfig.getLangCode(lang);

        Template template = new PDFTemplate("reunion-acuerdo-" + applang);
        template.put("logo", logoDocumentosUrl);
        template.put("puntoOrdenDia", puntoOrdenDiaTemplate);
        template.put("nombreInstitucion", nombreInstitucion);
        template.put("fechaReunion", getFechaReunion(reunionTemplate.getFecha()));
        template.put("tituloReunion", reunionTemplate.getAsunto());
        template.put("organos", getNombreOrganos(reunionTemplate));

        return template;
    }

    private String getFechaReunion(Date fecha)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        return sdf.format(fecha);
    }

    private String getNombreOrganos(ReunionTemplate reunionTemplate)
    {
        List<String> nombreOrganos = new ArrayList<>();

        for (OrganoTemplate organo : reunionTemplate.getOrganos())
        {
            nombreOrganos.add(organo.getNombre());
        }

        return StringUtils.join(nombreOrganos, ", ");
    }
}
