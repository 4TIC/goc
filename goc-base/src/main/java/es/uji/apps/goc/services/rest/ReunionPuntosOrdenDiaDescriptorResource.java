package es.uji.apps.goc.services.rest;

import com.sun.jersey.api.core.InjectParam;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.uji.apps.goc.dto.PuntoOrdenDiaDescriptor;
import es.uji.apps.goc.model.DescriptorOrdenDia;
import es.uji.apps.goc.services.PuntoOrdenDiaDescriptorService;
import es.uji.commons.rest.UIEntity;

@Path("reuniones/{idReunion}/puntosOrdenDia/{idPuntoOrdenDia}/descriptores")
public class ReunionPuntosOrdenDiaDescriptorResource {

    @InjectParam
    private PuntoOrdenDiaDescriptorService puntoOrdenDiaDescriptorService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public UIEntity addDescriptoresToPuntoDia(
        @PathParam("idReunion") String idReunion,
        @PathParam("idPuntoOrdenDia") String idPuntoOrdenDia,
        UIEntity descriptorOrdenDiaUI
    ) {
        DescriptorOrdenDia descriptorOrdenDia = descriptorOrdenDiaUI.toModel(DescriptorOrdenDia.class);
        descriptorOrdenDia.setIdPuntoOrdenDia(Long.valueOf(idPuntoOrdenDia));
        PuntoOrdenDiaDescriptor puntoOrdenDiaDescriptor =
            puntoOrdenDiaDescriptorService.addDescriptorToPuntoOrdenDia(descriptorOrdenDia);
        return UIEntity.toUI(puntoOrdenDiaDescriptor);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getDescriptoriesOrdenDia(
        @PathParam("idPuntoOrdenDia") String idPuntoOrdendia
    ) {
        List<DescriptorOrdenDia> descriptoresOrdenDia =
            puntoOrdenDiaDescriptorService.getDescriptoresOrdenDia(Long.valueOf(idPuntoOrdendia));
        return UIEntity.toUI(descriptoresOrdenDia);
    }

    @DELETE
    @Path("{idDescriptorOrdenDia}")
    public Response deleteDesriptorOrdenDia(
        @PathParam("idDescriptorOrdenDia") Long idDescriptorOrdenDia
    ) {
        puntoOrdenDiaDescriptorService.deleteDescriptorOrdenDia(idDescriptorOrdenDia);
        return Response.ok().build();
    }

    @PUT
    @Path("{idDescriptorOrdenDia}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity updateDescriptorOrdenDia(
        @PathParam("idDescriptorOrdenDia") Long idDescriptorOrdenDia,
        UIEntity descriptorOrdenDiaUI
    ) {
        DescriptorOrdenDia descriptorOrdenDia = descriptorOrdenDiaUI.toModel(DescriptorOrdenDia.class);
        DescriptorOrdenDia newDescriptorOrdenDia =
            puntoOrdenDiaDescriptorService.updateDescriptorOrdenDia(descriptorOrdenDia);
        return UIEntity.toUI(newDescriptorOrdenDia);
    }
}
