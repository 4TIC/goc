package es.uji.apps.goc.services;

import es.uji.apps.goc.dao.DescriptorDAO;
import es.uji.apps.goc.dao.DescriptorTipoOrganoDAO;
import es.uji.apps.goc.dto.Descriptor;
import es.uji.apps.goc.dto.DescriptorTipoOrgano;
import es.uji.apps.goc.dto.OrganoReunion;
import es.uji.apps.goc.exceptions.OrganosExternosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DescriptorService
{
    @Autowired
    private DescriptorDAO descriptorDAO;

    @Autowired
    private DescriptorTipoOrganoDAO descriptorTipoOrganoDAO;

    @Autowired
    private ReunionService reunionService;

    public Descriptor addDescriptor(Descriptor descriptor)
    {
        return descriptorDAO.insert(descriptor);
    }

    public Descriptor updateDescriptor(Descriptor descriptor)
    {
        return descriptorDAO.update(descriptor);
    }

    public void removeDescriptor(Long descriptorId)
    {
        descriptorDAO.delete(Descriptor.class, descriptorId);
    }

    public List<Descriptor> getDescriptores()
    {
        return descriptorDAO.getDescriptores();
    }

    public Descriptor getDescriptor(String idDescriptor)
    {
        return descriptorDAO.getDescriptor(idDescriptor);
    }

    public List<DescriptorTipoOrgano> getDescriptoresTiposOrganoByDescriptorId(Long idDescriptor)
    {
        return descriptorTipoOrganoDAO.getByDescriptorId(idDescriptor);
    }

    public void addDescriptorTipoOrgano(DescriptorTipoOrgano descriptorTipoOrgano)
    {
        descriptorTipoOrganoDAO.insert(descriptorTipoOrgano);
    }

    public void deleteDescriptorTipoOrgano(Long idDescriptorTipoOrgano)
    {
        descriptorTipoOrganoDAO.delete(DescriptorTipoOrgano.class, idDescriptorTipoOrgano);
    }

    public DescriptorTipoOrgano updateDescriptorTipoOrgano(DescriptorTipoOrgano descriptorTipoOrgano)
    {
        return descriptorTipoOrganoDAO.update(descriptorTipoOrgano);
    }

    public List<Descriptor> getDescriptoresByReunionId(Long reunionId, Long connectedUserId)
            throws OrganosExternosException
    {
        List<OrganoReunion> organosReunion = reunionService.getOrganosReunionByReunionId(reunionId);
        List<Descriptor> descriptores = new ArrayList<>();

        if (organosReunion == null || organosReunion.isEmpty()) return descriptorDAO.getDescriptores();

        for (OrganoReunion organoReunion : organosReunion)
        {
            List<Descriptor> descriptoresPorTipoOrgano = descriptorDAO.getDescriptoresByTipoOrganoId(organoReunion.getTipoOrganoId());

            if (descriptoresPorTipoOrgano == null || descriptoresPorTipoOrgano.isEmpty()) return descriptorDAO.getDescriptores();

            descriptores.addAll(descriptoresPorTipoOrgano);
        }

        return filtrarDuplicadosDescriptores(descriptores);
    }

    private List<Descriptor> filtrarDuplicadosDescriptores(List<Descriptor> descriptoresTotal)
    {
        List<Descriptor> descriptores = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (Descriptor descriptor : descriptoresTotal)
        {
            if (!ids.contains(descriptor.getId()))
            {
                ids.add(descriptor.getId());
                descriptores.add(descriptor);
            }
        }

        return descriptores;
    }
}
