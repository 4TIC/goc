package es.uji.apps.goc.services;

import es.uji.apps.goc.dao.DescriptorTipoOrganoDAO;
import es.uji.apps.goc.dto.DescriptorTipoOrgano;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import es.uji.apps.goc.dao.DescriptorDAO;
import es.uji.apps.goc.dto.Descriptor;
import sun.security.krb5.internal.crypto.Des;

@Service
public class DescriptorService
{
    @Autowired
    private DescriptorDAO descriptorDAO;

    @Autowired
    private DescriptorTipoOrganoDAO descriptorTipoOrganoDAO;

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

    public List<Descriptor> getDescriptores() {
        return descriptorDAO.getDescriptores();
    }

    public Descriptor getDescriptor(String idDescriptor) {
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
}
