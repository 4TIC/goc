package es.uji.apps.goc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import es.uji.apps.goc.dao.PuntoOrdenDiaDescriptorDAO;
import es.uji.apps.goc.dto.Clave;
import es.uji.apps.goc.dto.PuntoOrdenDia;
import es.uji.apps.goc.dto.PuntoOrdenDiaDescriptor;
import es.uji.apps.goc.model.DescriptorOrdenDia;

@Service
public class PuntoOrdenDiaDescriptorService
{

    @Autowired
    PuntoOrdenDiaDescriptorDAO puntoOrdenDiaDescriptorDAO;

    public List<DescriptorOrdenDia> getDescriptoresOrdenDia(Long idPuntoOrdenDia)
    {
        List<PuntoOrdenDiaDescriptor> puntoOrdenDiaDescriptors =
                puntoOrdenDiaDescriptorDAO.getDescriptoresOrdenDia(idPuntoOrdenDia);
        return puntoOrdenDiaDescriptors.stream().map(p ->
        {
            return createDescriptorOrdenDiaFromDTO(p);
        }).collect(Collectors.toList());
    }

    public void deleteDescriptorOrdenDia(Long idDescriptorOrdenDia)
    {
        puntoOrdenDiaDescriptorDAO.delete(PuntoOrdenDiaDescriptor.class, idDescriptorOrdenDia);
    }

    public PuntoOrdenDiaDescriptor addDescriptorToPuntoOrdenDia(DescriptorOrdenDia descriptorOrdenDia)
    {
        PuntoOrdenDiaDescriptor puntoOrdenDiaDescriptor = createPuntoOrdenDiaDescriptorFromUI(descriptorOrdenDia);
        return puntoOrdenDiaDescriptorDAO.insert(puntoOrdenDiaDescriptor);
    }

    public DescriptorOrdenDia updateDescriptorOrdenDia(DescriptorOrdenDia descriptorOrdenDia)
    {
        PuntoOrdenDiaDescriptor puntoOrdenDiaDescriptor = createPuntoOrdenDiaDescriptorFromUI(descriptorOrdenDia);
        puntoOrdenDiaDescriptor.setId(descriptorOrdenDia.getId());
        puntoOrdenDiaDescriptor = puntoOrdenDiaDescriptorDAO.update(puntoOrdenDiaDescriptor);
        return createDescriptorOrdenDiaFromDTO(puntoOrdenDiaDescriptor);
    }

    private DescriptorOrdenDia createDescriptorOrdenDiaFromDTO(PuntoOrdenDiaDescriptor puntoOrdenDiaDescriptor)
    {
        DescriptorOrdenDia descriptorOrdenDia = new DescriptorOrdenDia();
        descriptorOrdenDia.setId(puntoOrdenDiaDescriptor.getId());
        descriptorOrdenDia.setIdPuntoOrdenDia(puntoOrdenDiaDescriptor.getPuntoOrdenDia().getId());
        descriptorOrdenDia.setIdClave(puntoOrdenDiaDescriptor.getClave().getId());
        descriptorOrdenDia.setClave(puntoOrdenDiaDescriptor.getClave().getClave());
        descriptorOrdenDia.setIdDescriptor(puntoOrdenDiaDescriptor.getClave().getDescriptor().getId());
        descriptorOrdenDia.setDescriptor(puntoOrdenDiaDescriptor.getClave().getDescriptor().getDescriptor());
        return descriptorOrdenDia;
    }

    private PuntoOrdenDiaDescriptor createPuntoOrdenDiaDescriptorFromUI(DescriptorOrdenDia descriptorOrdenDia)
    {
        PuntoOrdenDiaDescriptor puntoOrdenDiaDescriptor = new PuntoOrdenDiaDescriptor();
        puntoOrdenDiaDescriptor.setClave(new Clave(descriptorOrdenDia.getIdClave()));
        puntoOrdenDiaDescriptor.setPuntoOrdenDia(new PuntoOrdenDia(descriptorOrdenDia.getIdPuntoOrdenDia()));
        return puntoOrdenDiaDescriptor;
    }
}
