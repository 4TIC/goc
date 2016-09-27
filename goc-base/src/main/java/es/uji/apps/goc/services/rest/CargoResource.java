package es.uji.apps.goc.services.rest;

import com.sun.jersey.api.core.InjectParam;
import es.uji.apps.goc.dto.Cargo;
import es.uji.apps.goc.exceptions.OrganoNoDisponibleException;
import es.uji.apps.goc.exceptions.OrganosExternosException;
import es.uji.apps.goc.model.Organo;
import es.uji.apps.goc.model.TipoOrgano;
import es.uji.apps.goc.services.CargoService;
import es.uji.apps.goc.services.OrganoService;
import es.uji.commons.rest.CoreBaseService;
import es.uji.commons.rest.ParamUtils;
import es.uji.commons.rest.UIEntity;
import es.uji.commons.sso.AccessManager;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("cargos")
public class CargoResource extends CoreBaseService
{
    @InjectParam
    private CargoService cargoService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<UIEntity> getCargos() throws OrganosExternosException
    {
        return UIEntity.toUI(cargoService.getCargos());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity addCargo(UIEntity cargo)
    {
        Cargo newCargo = cargoService.addCargo(cargo.toModel(Cargo.class));

        return UIEntity.toUI(newCargo);
    }

    @PUT
    @Path("{cargoId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UIEntity updateCargo(@PathParam("cargoId") Long cargoId, UIEntity cargo)
    {
        Cargo updatedCargo = cargoService.updateCargo(cargo.toModel(Cargo.class));

        return UIEntity.toUI(updatedCargo);
    }

    @DELETE
    @Path("{cargoId}")
    public Response borraCargo(@PathParam("cargoId") Long cargoId, UIEntity cargo)
    {
        cargoService.removeCargo(cargoId);

        return Response.ok().build();
    }
}
