package es.uji.apps.goc.services;

import es.uji.apps.goc.dao.CargoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import es.uji.apps.goc.dto.Cargo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CargoService
{
    @Autowired
    private CargoDAO cargoDAO;

    public List<Cargo> getCargos()
    {
        return cargoDAO.get(Cargo.class);
    }

    public Cargo addCargo(Cargo cargo)
    {
        return cargoDAO.insert(cargo);
    }

    public Cargo updateCargo(Cargo cargo)
    {
        return cargoDAO.update(cargo);
    }

    public void removeCargo(Long cargoId)
    {
        cargoDAO.delete(Cargo.class, cargoId);
    }
}
