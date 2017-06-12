package es.uji.apps.goc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Miembro updateMiembro(Long miembroId, String nombre, String email, String cargoId, Long connectedUserId)
            throws OrganoNoDisponibleException, MiembroNoDisponibleException
    {
        Miembro miembro = miembroDAO.getMiembroByIdAndUserId(miembroId, connectedUserId);
        miembro.setNombre(nombre);
        miembro.setEmail(email);

        if (cargoId != null)
        {
            miembro.setCargo(new Cargo(cargoId));
        }

        if (miembro == null)
        {
            throw new MiembroNoDisponibleException();
        }

        return miembroDAO.updateMiembro(miembro);
    }

    public void removeMiembroById(Long miembroId, Long connectedUserId)
            throws MiembroNoDisponibleException
    {
        Miembro miembro = miembroDAO.getMiembroByIdAndUserId(miembroId, connectedUserId);

        if (miembro == null)
        {
            throw new MiembroNoDisponibleException();
        }

        miembroDAO.delete(MiembroLocal.class, miembroId);
    }

    public List<Miembro> getMiembrosExternos(String organoId, Long connectedUserId)
            throws MiembrosExternosException
    {
        return miembroDAO.getMiembrosExternos(organoId, connectedUserId);
    }
}
