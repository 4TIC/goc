package es.uji.apps.goc.services.rest;

import com.sun.jersey.api.core.InjectParam;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.uji.apps.goc.dto.Clave;
import es.uji.apps.goc.dto.Descriptor;
import es.uji.apps.goc.services.ClaveService;
import es.uji.apps.goc.services.DescriptorService;
import es.uji.commons.rest.UIEntity;

@Path("claves")
public class ClaveResource {

    @InjectParam
    private ClaveService claveService;

    @InjectParam
    private DescriptorService descriptorService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity addClave(UIEntity clave)
    {
        Descriptor descriptor = descriptorService.getDescriptor(clave.get("idDescriptor"));
        Clave claveModel = clave.toModel(Clave.class);
        claveModel.setDescriptor(descriptor);
        Clave newClave = claveService.addClave(claveModel);

        return UIEntity.toUI(newClave);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getClaves(
        @QueryParam("idDescriptor") Long idDescriptor
    )
    {
        List<Clave> claves = claveService.getClavesDescriptor(idDescriptor);
        return clavesToUI(claves);
    }

    private List<UIEntity> clavesToUI(List<Clave> claves)
    {
        List<UIEntity> ui = new ArrayList<>();
        for(Clave clave : claves)
        {
            ui.add(claveToUI(clave));
        }

        return ui;
    }

    private UIEntity claveToUI(Clave clave)
    {
        UIEntity ui = new UIEntity();
        ui.put("id", clave.getId());
        ui.put("clave", clave.getClave());
        ui.put("claveAlternativa", clave.getClaveAlternativa());
        ui.put("idDescriptor", clave.getDescriptor().getId());

        return ui;
    }

    @PUT
    @Path("{idClave}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity updateClave(
        @PathParam("idClave") Long idClave,
        UIEntity clave
    ) {
        Clave newClave = claveService.updateClave(clave.toModel(Clave.class));

        return UIEntity.toUI(newClave);
    }

    @DELETE
    @Path("{idClave}")
    public Response deleteClave(@PathParam("idClave") Long idClave)
    {
        claveService.deleteClave(idClave);

        return Response.ok().build();
    }
}
