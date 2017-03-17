package es.uji.apps.goc.services.rest;

import com.sun.jersey.api.core.InjectParam;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.uji.apps.goc.dto.Descriptor;
import es.uji.apps.goc.services.DescriptorService;
import es.uji.commons.rest.UIEntity;

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
}
