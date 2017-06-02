package es.uji.apps.goc.services.rest;

import com.sun.jersey.api.core.InjectParam;
import es.uji.apps.goc.dto.Descriptor;
import es.uji.apps.goc.dto.DescriptorTipoOrgano;
import es.uji.apps.goc.dto.TipoOrganoLocal;
import es.uji.apps.goc.services.DescriptorService;
import es.uji.commons.rest.UIEntity;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("descriptores")
public class DescriptorResource
{

    @InjectParam
    private DescriptorService descriptorService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity addDescriptor(UIEntity descriptor)
    {
        Descriptor newDescriptor = descriptorService.addDescriptor(descriptor.toModel(Descriptor.class));

        return UIEntity.toUI(newDescriptor);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getDescriptores()
    {
        List<Descriptor> descriptores = descriptorService.getDescriptores();
        return UIEntity.toUI(descriptores);
    }

    @PUT
    @Path("{idDescriptor}")
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity updateDescriptor(UIEntity descriptor, @PathParam("idDescriptor") Long idDescriptor)
    {
        Descriptor newDescriptor = descriptorService.updateDescriptor(descriptor.toModel(Descriptor.class));

        return UIEntity.toUI(newDescriptor);
    }

    @DELETE
    @Path("{idDescriptor}")
    public Response deleteDescriptor(@PathParam("idDescriptor") Long idDescriptor)
    {
        descriptorService.removeDescriptor(idDescriptor);

        return Response.ok().build();
    }

    @GET
    @Path("{idDescriptor}/tiposorgano")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getTiposOrganoByDescriptorId(@PathParam("idDescriptor") Long idDescriptor)
    {
        List<UIEntity> entities = new ArrayList<>();

        for (DescriptorTipoOrgano descriptorTipoOrgano : descriptorService.getDescriptoresTiposOrganoByDescriptorId(idDescriptor))
        {
            UIEntity entity = new UIEntity();

            entity.put("idDescriptor", idDescriptor);
            entity.put("idTipoOrgano", descriptorTipoOrgano.getTipoOrgano().getId());
            entity.put("id", descriptorTipoOrgano.getId());

            entities.add(entity);
        }

        return entities;
    }

    @POST
    @Path("{idDescriptor}/tiposorgano")
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity setTiposOrganoByDescriptorId(@PathParam("idDescriptor") Long idDescriptor, UIEntity entity)
    {
        DescriptorTipoOrgano descriptorTipoOrgano = new DescriptorTipoOrgano();
        Descriptor descriptor = new Descriptor(entity.getLong("idDescriptor"));
        TipoOrganoLocal tipoOrgano = new TipoOrganoLocal(entity.getLong("idTipoOrgano"));

        descriptorTipoOrgano.setDescriptor(descriptor);
        descriptorTipoOrgano.setTipoOrgano(tipoOrgano);

        return UIEntity.toUI(descriptorService.addDescriptorTipoOrgano(descriptorTipoOrgano));
    }

    @PUT
    @Path("{idDescriptor}/tiposorgano/{idDescriptorTipoOrgano}")
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity updateTiposOrganoByDescriptorId(@PathParam("idDescriptor") Long idDescriptor, UIEntity entity)
    {
        DescriptorTipoOrgano descriptorTipoOrgano = new DescriptorTipoOrgano(entity.getLong("id"));
        Descriptor descriptor = new Descriptor(entity.getLong("idDescriptor"));
        TipoOrganoLocal tipoOrgano = new TipoOrganoLocal(entity.getLong("idTipoOrgano"));

        descriptorTipoOrgano.setDescriptor(descriptor);
        descriptorTipoOrgano.setTipoOrgano(tipoOrgano);

        descriptorService.updateDescriptorTipoOrgano(descriptorTipoOrgano);

        return entity;
    }

    @DELETE
    @Path("{idDescriptor}/tiposorgano/{idDescriptorTipoOrgano}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteTiposOrgano(@PathParam("idDescriptor") Long idDescriptor,
            @PathParam("idDescriptorTipoOrgano") Long idDescriptorTipoOrgano)
    {
        descriptorService.deleteDescriptorTipoOrgano(idDescriptorTipoOrgano);

        return Response.ok().build();
    }
}
