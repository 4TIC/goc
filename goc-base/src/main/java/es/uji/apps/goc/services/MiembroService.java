package es.uji.apps.goc.services;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import es.uji.apps.goc.dto.MiembroExterno;
import es.uji.apps.goc.model.JSONListaMiembrosDeserializer;
import es.uji.apps.goc.model.Organo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import es.uji.apps.goc.dao.CargoDAO;
import es.uji.apps.goc.dao.MiembroDAO;
import es.uji.apps.goc.dao.OrganoReunionDAO;
import es.uji.apps.goc.dao.OrganoReunionMiembroDAO;
import es.uji.apps.goc.dto.MiembroLocal;
import es.uji.apps.goc.dto.OrganoReunion;
import es.uji.apps.goc.dto.OrganoReunionMiembro;
import es.uji.apps.goc.exceptions.MiembroNoDisponibleException;
import es.uji.apps.goc.exceptions.MiembrosExternosException;
import es.uji.apps.goc.exceptions.OrganoNoDisponibleException;
import es.uji.apps.goc.model.Cargo;
import es.uji.apps.goc.model.Miembro;

import javax.ws.rs.core.MediaType;

@Service
@Component
public class MiembroService
{
    @Autowired
    private MiembroDAO miembroDAO;

    @Autowired
    private CargoDAO cargoDAO;

    @Autowired
    private OrganoReunionDAO organoReunionDAO;

    @Autowired
    private OrganoReunionMiembroDAO organoReunionMiembroDAO;

    @Value("${goc.external.authToken}")
    private String authToken;

    @Value("${goc.external.miembrosEndpoint}")
    private String miembrosExternosEndpoint;

    public List<Miembro> getMiembrosLocales(Long organoId, Long connectedUserId)
    {
        if (organoId != null)
        {
            return miembroDAO.getMiembrosByOrganoId(organoId);
        }

        return miembroDAO.getMiembros();
    }

    @Transactional
    public Miembro addMiembro(Miembro miembro, Long connectedUserId)
            throws OrganoNoDisponibleException
    {
        miembro = miembroDAO.insertMiembro(miembro, connectedUserId);

        es.uji.apps.goc.dto.Cargo cargo =
                cargoDAO.get(es.uji.apps.goc.dto.Cargo.class, Long.parseLong(miembro.getCargo().getId())).get(0);
        miembro.getCargo().setCodigo(cargo.getCodigo());
        miembro.getCargo().setNombre(cargo.getNombre());
        miembro.getCargo().setNombreAlternativo(cargo.getNombreAlternativo());

        List<OrganoReunion> organoReuniones =
                organoReunionDAO.getOrganosReunionNoCompletadasByOrganoId(Long.parseLong(miembro.getOrgano().getId()));

        for (OrganoReunion organoReunion : organoReuniones)
        {
            OrganoReunionMiembro organoReunionMiembro = miembro.toOrganoReunionMiembro(organoReunion);
            organoReunionMiembroDAO.insert(organoReunionMiembro);
        }

        return miembro;
    }

    @Transactional
    public Miembro updateMiembro(Long miembroId, String nombre, String email, String cargoId, Long connectedUserId)
            throws OrganoNoDisponibleException, MiembroNoDisponibleException
    {
        Miembro miembro = miembroDAO.getMiembroById(miembroId);

        if (miembro == null)
        {
            throw new MiembroNoDisponibleException();
        }

        miembro.setNombre(nombre);
        miembro.setEmail(email);
        String oldCargoId = miembro.getCargo().getId();

        es.uji.apps.goc.dto.Cargo cargo =
                cargoDAO.get(es.uji.apps.goc.dto.Cargo.class, Long.parseLong(cargoId)).get(0);

        Cargo newCargo = new Cargo();

        newCargo.setId(cargo.getId().toString());
        newCargo.setCodigo(cargo.getCodigo());
        newCargo.setNombre(cargo.getNombre());
        newCargo.setNombreAlternativo(cargo.getNombreAlternativo());

        miembro.setCargo(newCargo);

        List<OrganoReunion> organoReuniones =
                organoReunionDAO.getOrganosReunionNoCompletadasByOrganoId(Long.parseLong(miembro.getOrgano().getId()));

        for (OrganoReunion organoReunion : organoReuniones)
        {
            organoReunionMiembroDAO.ByOrganoReunionIdPersonaIdAndCargoId(organoReunion.getId(),
                    miembro.getPersonaId().toString(), oldCargoId, nombre, email, newCargo);
        }

        return miembroDAO.updateMiembro(miembro);
    }

    @Transactional
    public void removeMiembroById(Long miembroId, Long connectedUserId)
            throws MiembroNoDisponibleException
    {
        Miembro miembro = miembroDAO.getMiembroById(miembroId);

        if (miembro == null)
        {
            throw new MiembroNoDisponibleException();
        }

        miembroDAO.delete(MiembroLocal.class, miembroId);

        List<OrganoReunion> organoReuniones =
                organoReunionDAO.getOrganosReunionNoCompletadasByOrganoId(Long.parseLong(miembro.getOrgano().getId()));

        for (OrganoReunion organoReunion : organoReuniones)
        {
            organoReunionMiembroDAO.deleteByOrganoReunionIdPersonaIdAndCargoId(organoReunion.getId(),
                    miembro.getPersonaId().toString(), miembro.getCargo().getId());
        }
    }

    public List<Miembro> getMiembrosExternos(String organoId, Long connectedUserId)
            throws MiembrosExternosException
    {
        WebResource getMiembrosResource =
                Client.create().resource(this.miembrosExternosEndpoint.replace("{organoId}", organoId));

        ClientResponse response = getMiembrosResource.type(MediaType.APPLICATION_JSON)
                .header("X-UJI-AuthToken", authToken)
                .get(ClientResponse.class);

        if (response.getStatus() != 200)
        {
            throw new MiembrosExternosException();
        }

        JSONListaMiembrosDeserializer jsonDeserializer = response.getEntity(JSONListaMiembrosDeserializer.class);

        List<MiembroExterno> listaMiembrosExternos = jsonDeserializer.getMiembros();
        return creaListaMiembroDesdeListaMiembrosExternos(listaMiembrosExternos);
    }

    private List<Miembro> creaListaMiembroDesdeListaMiembrosExternos(List<MiembroExterno> listaMiembrosExternos)
    {
        List<Miembro> listaMiembros = new ArrayList<>();

        for (MiembroExterno miembroExterno : listaMiembrosExternos)
        {
            listaMiembros.add(creaMiembroDesdeMiembroExterno(miembroExterno));
        }

        return listaMiembros;
    }

    private Miembro creaMiembroDesdeMiembroExterno(MiembroExterno miembroExterno)
    {
        Long cargoId = miembroExterno.getCargo().getId();

        Cargo cargo = new Cargo(cargoId.toString());
        cargo.setCodigo(cargoDAO.getCargoCodigoById(cargoId));
        cargo.setNombre(miembroExterno.getCargo().getNombre());
        cargo.setNombreAlternativo(miembroExterno.getCargo().getNombreAlternativo());

        Miembro miembro = new Miembro();
        miembro.setId(miembroExterno.getId());
        miembro.setPersonaId(miembroExterno.getId());
        miembro.setNombre(miembroExterno.getNombre());
        miembro.setEmail(miembroExterno.getEmail());
        miembro.setCargo(cargo);
        miembro.setCondicion(miembroExterno.getCondicion());
        miembro.setCondicionAlternativa(miembroExterno.getCondicionAlternativa());

        miembro.setOrgano(new Organo(miembroExterno.getOrgano().getId()));

        return miembro;
    }
}
