package es.uji.apps.goc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import es.uji.apps.goc.dao.ClaveDAO;
import es.uji.apps.goc.dto.Clave;

@Service
public class ClaveService {

    @Autowired
    ClaveDAO claveDAO;

    public Clave addClave(Clave clave) {
        return claveDAO.insert(clave);
    }

    public Clave updateClave(Clave clave){
        return claveDAO.update(clave);
    }

    public void deleteClave(Long idClave){
        claveDAO.delete(Clave.class, idClave);
    }

    public List<Clave> getClavesDescriptor(Long idDescriptor){
        return claveDAO.getClavesByDescriptor(idDescriptor);
    }
}
