package es.uji.apps.goc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import es.uji.apps.goc.dao.OrganoAutorizadoDAO;
import es.uji.apps.goc.dao.OrganoDAO;
import es.uji.apps.goc.dao.ReunionDAO;
import es.uji.apps.goc.dto.OrganoAutorizado;
import es.uji.apps.goc.dto.OrganoExterno;
import es.uji.apps.goc.dto.OrganoLocal;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.exceptions.OrganoNoDisponibleException;
import es.uji.apps.goc.exceptions.OrganosExternosException;
import es.uji.apps.goc.model.JSONListaOrganosExternosDeserializer;
import es.uji.apps.goc.model.Organo;
import es.uji.apps.goc.model.TipoOrgano;

@Service
@Component
public class OrganoService
{
    @Autowired
    private OrganoDAO organoDAO;

    @Autowired
    private ReunionDAO reunionDAO;

    @Autowired
    private OrganoAutorizadoDAO organoAutorizadoDAO;

    @Value("${goc.external.authToken}")
    private String authToken;

    @Value("${goc.external.organosEndpoint}")
    private String organosExternosEndpoint;

    public List<Organo> getOrganos(Long connectedUserId) throws OrganosExternosException
    {
        List<Organo> organos = new ArrayList<>();
        organos.addAll(getOrganosExternos());
        organos.addAll(organoDAO.getOrganosByUserId(connectedUserId));
        return organos;
    }

    public List<Organo> getOrganosPorAutorizadoId(Long connectedUserId)
            throws OrganosExternosException
    {
        List<Organo> organos = new ArrayList<>();
        List<Organo> organosExternos = getOrganosExternos();
        List<String> listaOrganosIdsPermitidos = getOrganosIdsPermitidosAutorizado(connectedUserId);
        organos.addAll(
                organosExternos.stream().filter(o -> listaOrganosIdsPermitidos.contains(o.getId()))
                        .collect(Collectors.toList()));
        List<Organo> organosLocales = organoDAO.getOrganosByAutorizadoId(connectedUserId);
        organos.addAll(organosLocales);
        return organos;
    }

    private List<String> getOrganosIdsPermitidosAutorizado(Long connectedUserId)
    {
        List<OrganoAutorizado> organoAutorizados = organoAutorizadoDAO
                .getAutorizadosByUserId(connectedUserId);
        return organoAutorizados.stream().filter(c -> c.isOrganoExterno()).map(c -> c.getOrganoId())
                .collect(Collectors.toList());
    }

    public Organo addOrgano(Organo organo, Long connectedUserId)
    {
        return organoDAO.insertOrgano(organo, connectedUserId);
    }

    public Organo updateOrgano(Long organoId, String nombre, Long tipoOrganoId,
            Long connectedUserId) throws OrganoNoDisponibleException
    {
        Organo organo = organoDAO.getOrganoByIdAndUserId(organoId, connectedUserId);

        if (organo == null)
        {
            throw new OrganoNoDisponibleException();
        }

        organo.setNombre(nombre);
        TipoOrgano tipoOrgano = new TipoOrgano(tipoOrganoId);
        organo.setTipoOrgano(tipoOrgano);
        return organoDAO.updateOrgano(organo);
    }

    public void removeOrganoById(Long organoId, Long connectedUserId)
            throws OrganoNoDisponibleException
    {
        Organo organo = organoDAO.getOrganoByIdAndUserId(organoId, connectedUserId);

        if (organo == null)
        {
            throw new OrganoNoDisponibleException();
        }

        organoDAO.delete(OrganoLocal.class, organoId);
    }

    public List<Organo> getOrganosExternos() throws OrganosExternosException
    {
        WebResource getOrganosResource = Client.create().resource(this.organosExternosEndpoint);

        ClientResponse response = getOrganosResource.type(MediaType.APPLICATION_JSON)
                .header("X-UJI-AuthToken", authToken).get(ClientResponse.class);
        if (response.getStatus() != 200)
        {
            throw new OrganosExternosException();
        }

        JSONListaOrganosExternosDeserializer jsonDeserializer = response
                .getEntity(JSONListaOrganosExternosDeserializer.class);

        List<OrganoExterno> listaOrganosExternos = jsonDeserializer.getOrganos();
        return organosExternosDTOToOrgano(listaOrganosExternos);
    }

    public List<Organo> getOrganosByReunionIdAndUserId(Long reunionId, Long connectedUserId)
            throws OrganosExternosException
    {
        List<Organo> organos = new ArrayList<>();
        List<Organo> organosExternos = getOrganosExternos();

        organos.addAll(organoDAO.getOrganosByReunionIdAndUserId(reunionId, connectedUserId));

        Reunion reunion = reunionDAO.getReunionConOrganosById(reunionId);
        List<String> listaIdsOrganosExternos = reunion.getReunionOrganos().stream()
                .map(el -> el.getOrganoExternoId()).collect(Collectors.toList());

        for (Organo organoExterno : organosExternos)
        {
            if (listaIdsOrganosExternos.contains(organoExterno.getId()))
            {
                organos.add(organoExterno);
            }
        }

        return organos;
    }

    private List<Organo> organosExternosDTOToOrgano(List<OrganoExterno> listaOrganosExternos)
    {
        List<Organo> organos = new ArrayList<>();

        for (OrganoExterno organoExterno : listaOrganosExternos)
        {
            Organo organo = organoExternoDTOToOrgano(organoExterno);
            organos.add(organo);
        }

        return organos;
    }

    private Organo organoExternoDTOToOrgano(OrganoExterno organoExternoDTO)
    {
        Organo organo = new Organo();

        organo.setId(organoExternoDTO.getId());
        organo.setNombre(organoExternoDTO.getNombre());
        organo.setExterno(true);

        TipoOrgano tipoOrgano = new TipoOrgano();
        tipoOrgano.setId(organoExternoDTO.getTipoOrganoId());
        tipoOrgano.setNombre(organoExternoDTO.getTipoNombre());
        tipoOrgano.setCodigo(organoExternoDTO.getTipoCodigo());

        organo.setTipoOrgano(tipoOrgano);
        return organo;
    }

    public List<OrganoAutorizado> getAutorizados(String organoId, Boolean externo)
    {
        return organoAutorizadoDAO.getAutorizadosByOrgano(organoId, externo);
    }

    public OrganoAutorizado addAutorizado(OrganoAutorizado organoAutorizado)
    {
        return organoAutorizadoDAO.insert(organoAutorizado);
    }

    @Transactional
    public void removeAutorizado(Long organoAutorizadoId)
    {
        organoAutorizadoDAO.delete(OrganoAutorizado.class, organoAutorizadoId);
    }

    public boolean usuarioConPermisosParaConvocarOrganos(List<Organo> organos, Long connectedUserId)
    {
        Boolean permisosAdecuados = true;
        List<OrganoAutorizado> listaPermisosOrganoAutorizado = organoAutorizadoDAO
                .getAutorizadosByUserId(connectedUserId);

        for (Organo organo : organos)
        {
            Boolean encontrado = false;
            for (OrganoAutorizado organoAutorizado : listaPermisosOrganoAutorizado)
            {
                if (organoAutorizado.getOrganoId().equals(organo.getId().toString())
                        && organoAutorizado.isOrganoExterno().equals(organo.isExterno()))
                {
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado)
            {
                permisosAdecuados = false;
                break;
            }
        }

        return permisosAdecuados;
    }

}
