package es.uji.apps.goc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import es.uji.apps.goc.dao.DescriptorDAO;
import es.uji.apps.goc.dto.Descriptor;

@Service
public class DescriptorService
{
    @Autowired
    private DescriptorDAO descriptorDAO;

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
}
