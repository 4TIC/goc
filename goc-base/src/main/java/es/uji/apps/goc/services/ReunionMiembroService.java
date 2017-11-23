package es.uji.apps.goc.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import es.uji.apps.goc.dao.OrganoReunionDAO;
import es.uji.apps.goc.dao.OrganoReunionMiembroDAO;
import es.uji.apps.goc.dto.OrganoReunion;
import es.uji.apps.goc.dto.OrganoReunionMiembro;
import es.uji.apps.goc.exceptions.MiembroNoDisponibleException;
import es.uji.apps.goc.exceptions.MiembrosExternosException;
import es.uji.apps.goc.model.Miembro;

@Service
@Component
public class ReunionMiembroService
{
    @Autowired
    private OrganoReunionDAO organoReunionDAO;

    @Autowired
    private MiembroService miembroService;

    @Autowired
    private OrganoReunionMiembroDAO organoReunionMiembroDAO;

    public List<OrganoReunionMiembro> getMiembrosReunionByReunionIdAndOrganoId(Long reunionId,
            String organoId, Boolean externo, Long connectedUserId)
            throws MiembroNoDisponibleException, MiembrosExternosException
    {
        OrganoReunion organoReunion;
        if (externo)
        {
            organoReunion = organoReunionDAO
                    .getOrganoReunionByReunionIdAndOrganoExternoId(reunionId, organoId);
        }
        else
        {
            organoReunion = organoReunionDAO.getOrganoReunionByReunionIdAndOrganoLocalId(reunionId,
                    Long.parseLong(organoId));
        }

        if (organoReunion != null)
        {
            List<OrganoReunionMiembro> listaOrganoReunionMiembros = organoReunionMiembroDAO
                    .getOrganoReunionMiembroByOrganoReunionId(organoReunion.getId());

            if (listaOrganoReunionMiembros.size() > 0)
            {
                return listaOrganoReunionMiembros;
            }
        }

        List<Miembro> listaMiembros = new ArrayList<>();

        if (externo)
        {
            listaMiembros = miembroService.getMiembrosExternos(organoId, connectedUserId);
        }
        else
        {
            listaMiembros = miembroService.getMiembrosLocales(Long.parseLong(organoId), connectedUserId);
        }

        return creaOrganoReunionMiembroDesdeListaMiembros(listaMiembros, externo, reunionId);
    }

    private List<OrganoReunionMiembro> creaOrganoReunionMiembroDesdeListaMiembros(
            List<Miembro> listaMiembros, Boolean externo, Long reunionId)
    {
        List<OrganoReunionMiembro> listaOrganoReunionMiembros = new ArrayList<>();

        for (Miembro miembro : listaMiembros)
        {
            listaOrganoReunionMiembros.add(creaOrganoReunionMiembroDesdeMiembro(miembro, externo, reunionId));
        }

        return listaOrganoReunionMiembros;
    }

    private OrganoReunionMiembro creaOrganoReunionMiembroDesdeMiembro(Miembro miembro,
            Boolean externo, Long reunionId)
    {
        OrganoReunionMiembro organoReunionMiembro = new OrganoReunionMiembro();
        organoReunionMiembro.setId(miembro.getId());
        organoReunionMiembro.setNombre(miembro.getNombre());
        organoReunionMiembro.setEmail(miembro.getEmail());
        organoReunionMiembro.setCondicion(miembro.getCondicion());
        organoReunionMiembro.setCondicionAlternativa(miembro.getCondicionAlternativa());
        organoReunionMiembro.setAsistencia(true);
        organoReunionMiembro.setOrganoId(miembro.getOrgano().getId());
        organoReunionMiembro.setOrganoExterno(externo);
        organoReunionMiembro.setReunionId(reunionId);
        organoReunionMiembro.setMiembroId(miembro.getId().toString());
        organoReunionMiembro.setCargoId(miembro.getCargo().getId());
        organoReunionMiembro.setCargoCodigo(miembro.getCargo().getCodigo());
        organoReunionMiembro.setCargoNombre(miembro.getCargo().getNombre());
        organoReunionMiembro.setCargoNombreAlternativo(miembro.getCargo().getNombreAlternativo());

        return organoReunionMiembro;
    }

    public OrganoReunionMiembro updateReunionMiembro(Long miembroId, Boolean asistencia, Long connectedUserId)
            throws MiembroNoDisponibleException
    {
        OrganoReunionMiembro miembro = organoReunionMiembroDAO.getMiembroById(miembroId);

        if (miembro == null)
        {
            throw new MiembroNoDisponibleException();
        }

        miembro.setAsistencia(asistencia);

        return organoReunionMiembroDAO.update(miembro);
    }

    public List<OrganoReunionMiembro> getMiembrosReunionByReunionId(Long reunionId, Long connectedUserId)
            throws MiembrosExternosException
    {
        return organoReunionMiembroDAO.getMiembrosByReunionId(reunionId);
    }
}
