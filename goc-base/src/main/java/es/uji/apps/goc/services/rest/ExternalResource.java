package es.uji.apps.goc.services.rest;

import com.sun.jersey.api.core.InjectParam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import es.uji.apps.goc.auth.LanguageConfig;
import es.uji.apps.goc.auth.PersonalizationConfig;
import es.uji.apps.goc.dto.ReunionFirma;
import es.uji.apps.goc.exceptions.RolesPersonaExternaException;
import es.uji.apps.goc.firmas.FirmaService;
import es.uji.apps.goc.model.Menu;
import es.uji.apps.goc.model.MenuItem;
import es.uji.apps.goc.model.Miembro;
import es.uji.apps.goc.model.Organo;
import es.uji.apps.goc.model.Persona;
import es.uji.apps.goc.model.Role;
import es.uji.apps.goc.notifications.CanNotSendException;
import es.uji.apps.goc.notifications.MailSender;
import es.uji.apps.goc.notifications.Mensaje;
import es.uji.apps.goc.services.ExternalService;
import es.uji.apps.goc.services.PersonaService;
import es.uji.apps.goc.services.rest.ui.WrappedMiembros;
import es.uji.apps.goc.services.rest.ui.WrappedOrganos;
import es.uji.apps.goc.services.rest.ui.WrappedPersona;
import es.uji.apps.goc.services.rest.ui.WrappedPersonas;
import es.uji.commons.rest.CoreBaseService;
import es.uji.commons.rest.UIEntity;
import es.uji.commons.sso.AccessManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import static java.util.stream.Collectors.toList;

@Path("external")
@Api(value = "/external", tags = "external")
public class ExternalResource extends CoreBaseService
{
    @InjectParam
    private ExternalService externalService;

    @InjectParam
    private LanguageConfig languageConfig;

    @InjectParam
    private PersonaService personaService;

    @InjectParam
    private MailSender mailSender;

    @InjectParam
    private FirmaService firmaService;

    @GET
    @Path("organos")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Órganos disponibles", notes = "Obtiene una lista completa de los organos disponibles", response = WrappedOrganos.class)
    public List<UIEntity> getOrganosExternos()
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        List<Organo> listaOrganos = externalService.getOrganosExternos(connectedUserId);

        return organoToUI(listaOrganos);
    }

    private List<UIEntity> organoToUI(List<Organo> listaOrganos)
    {
        List<UIEntity> organosUI = new ArrayList<>();

        for (Organo organo : listaOrganos)
        {
            UIEntity organoUI = new UIEntity();

            organoUI.put("id", organo.getId());
            organoUI.put("nombre", organo.getNombre());
            organoUI.put("nombre_alternativo", organo.getNombreAlternativo());
            organoUI.put("tipo_id", organo.getTipoOrgano().getId());
            organoUI.put("tipo_codigo", organo.getTipoOrgano().getCodigo());
            organoUI.put("tipo_nombre", organo.getTipoOrgano().getNombre());
            organoUI.put("tipo_nombre_alternativo", organo.getTipoOrgano().getNombreAlternativo());

            organosUI.add(organoUI);
        }

        return organosUI;
    }

    @GET
    @Path("organos/{organoId}/miembros")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Miembros pertenecientes a un órgano determinado", notes = "Obtiene una lista de todos los miembros pertenecientes a un determinado órgano", response = WrappedMiembros.class)
    public List<UIEntity> getMiembrosByOrganoExternoId(@PathParam("organoId") String organoId)
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        List<Miembro> listaMiembros = externalService.getMiembrosByOrganoId(organoId, connectedUserId);

        return miembroToUI(listaMiembros);
    }

    private List<UIEntity> miembroToUI(List<Miembro> listaMiembros)
    {
        List<UIEntity> miembrosUI = new ArrayList<>();

        for (Miembro miembro : listaMiembros)
        {
            UIEntity miembroUI = new UIEntity();

            miembroUI.put("id", miembro.getId());
            miembroUI.put("nombre", miembro.getNombre());
            miembroUI.put("email", miembro.getEmail());
            miembroUI.put("organo_id", miembro.getOrgano().getId());
            miembroUI.put("organo_nombre", miembro.getOrgano().getNombre());
            miembroUI.put("organo_nombre_alternativo", miembro.getOrgano().getNombreAlternativo());
            miembroUI.put("cargo_id", miembro.getCargo().getId());
            miembroUI.put("cargo_nombre", miembro.getCargo().getNombre());
            miembroUI.put("cargo_nombre_alternativo", miembro.getCargo().getNombreAlternativo());

            miembrosUI.add(miembroUI);
        }
        return miembrosUI;
    }

    @GET
    @Path("personas")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Directorio de personas de la institución", notes = "A partir de una consulta devuelve un listado de personas pertenecientes a la institución", response = WrappedPersonas.class)
    public List<UIEntity> getPersonaByQueryString(@QueryParam("query") String query)
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        List<Persona> listaPersonas = externalService.getPersonasByQueryString(query, connectedUserId);

        return personasToUI(listaPersonas);
    }

    private List<UIEntity> personasToUI(List<Persona> listaPersonas)
    {
        List<UIEntity> personasUI = new ArrayList<>();

        for (Persona persona : listaPersonas)
        {
            personasUI.add(personaToUI(persona));
        }
        return personasUI;
    }

    private UIEntity personaToUI(Persona persona)
    {
        UIEntity personaUI = new UIEntity();

        personaUI.put("id", persona.getId());
        personaUI.put("nombre", persona.getNombre());
        personaUI.put("email", persona.getEmail());

        return personaUI;
    }

    @GET
    @Path("personas/{personaId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Información de una persona a partir de su id", notes = "Obtiene una persona del directorio de personas a partir de su Id", response = WrappedPersona.class)
    public UIEntity getPersonaById(@PathParam("personaId") Long personaId)
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        Persona persona = externalService.getPersonaById(personaId, connectedUserId);

        return personaToUI(persona);
    }

    @GET
    @Path("personas/{personaId}/roles")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Información de los roles de una persona a partir de su id", notes = "Obtiene una persona del directorio de personas a partir de su Id")
    public List<String> getRolesByPersonaId(@PathParam("personaId") Long personaId)
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        List<Role> rolesByPersonaId = externalService.getRolesByPersonaId(personaId);

        if (rolesByPersonaId == null && rolesByPersonaId.isEmpty()) return Collections.EMPTY_LIST;

        return rolesByPersonaId.stream().map(role -> role.toString()).collect(toList());
    }

    @POST
    @Path("notificaciones")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Envío de notificaciones", notes = "Permite el envío de correos de aviso a los miembros involucrados en las reuniones")
    public void enviaNotificacion(Mensaje mensaje)
            throws CanNotSendException
    {
        mailSender.send(mensaje);
    }

    @POST
    @Path("firmas")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Firma del acta de una reunión", notes = "Firma el acta de una reunión ya concluida con todos los puntos acordados en ella")
    public UIEntity firmaReunion(ReunionFirma reunionFirma)
    {
        return UIEntity.toUI(firmaService.firmaReunion(reunionFirma));
    }

    @GET
    @Path("config/menus")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Listado de menús de acceso a las distintas funcionalidades de GOC", notes = "Permite la carga del árbol lateral de navegación con las opciones principales de navegación")
    public Menu menus(@QueryParam("lang") String lang)
            throws RolesPersonaExternaException
    {
        Long connectedUserId = AccessManager.getConnectedUserId(request);
        Menu menu = new Menu();
        List<MenuItem> items = new ArrayList<>();
        String currentLanguage = (lang != null) ? lang : languageConfig.mainLanguage;

        List<String> roles = personaService.getRolesFromPersonaId(connectedUserId);

        if (roles.isEmpty()) return new Menu();

        if (personaService.isAdmin(roles))
        {
            items.add(menuEntry("goc.view.organo.Main", "appI18N.menu.organos"));
            items.add(menuEntry("goc.view.tipoOrgano.Main", "appI18N.menu.tiposDeOrganos"));
            items.add(menuEntry("goc.view.miembro.Main", "appI18N.menu.miembros"));
            items.add(menuEntry("goc.view.reunion.Main", "appI18N.menu.reuniones"));
            items.add(menuEntry("goc.view.historicoReunion.Main", "appI18N.menu.historicoDeReuniones"));
            items.add(menuEntry("goc.view.cargo.Main", "appI18N.menu.cargos"));
            items.add(menuEntry("goc.view.descriptor.Main", "appI18N.menu.descriptores"));

            menu.setMenuItems(items);
            return menu;
        }

        if (personaService.isGestor(roles))
        {
            items.add(menuEntry("goc.view.organo.Main", "appI18N.menu.organos"));
        }

        items.add(menuEntry("goc.view.miembro.Main", "appI18N.menu.miembros"));
        items.add(menuEntry("goc.view.reunion.Main", "appI18N.menu.reuniones"));
        items.add(menuEntry("goc.view.historicoReunion.Main", "appI18N.menu.historicoDeReuniones"));

        menu.setMenuItems(items);
        return menu;
    }

    @GET
    @Path("cuentas/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Recuperacion de la persona asociada a una cuenta", notes = "A partir del username del usuario, permite extraer el valor del identificador de persona")
    public Persona personaAsociadaACuenta(@PathParam("username") String username)
            throws RolesPersonaExternaException
    {
        if (username == null)
        {
            throw new RuntimeException("Cuenta de usuario no definida");
        }

        Persona persona = new Persona();

        if (username.equals("nmanero")) persona.setId(88849L);

        if (username.equals("borillo")) persona.setId(9792L);

        return persona;
    }

    private MenuItem menuEntry(String className, String title)
    {
        MenuItem menuItem = new MenuItem();

        menuItem.setId(className);
        menuItem.setTitle(title);
        menuItem.setText(title);
        menuItem.setLeaf("true");
        menuItem.setChecked(null);

        return menuItem;
    }
}
