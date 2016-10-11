package es.uji.apps.goc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import es.uji.apps.goc.dao.PuntoOrdenDiaDAO;
import es.uji.apps.goc.dto.PuntoOrdenDia;
import es.uji.apps.goc.exceptions.PuntoOrdenDiaNoDisponibleException;
import org.springframework.transaction.annotation.Transactional;

@Service
@Component
public class PuntoOrdenDiaService
{
    @Autowired
    private PuntoOrdenDiaDAO puntoOrdenDiaDAO;

    public List<PuntoOrdenDia> getPuntosByReunionId(Long reunionId, Long connectedUserId)
    {
        return puntoOrdenDiaDAO.getPuntosByReunionId(reunionId);
    }

    public void borrarPuntoOrdenDia(Long reunionId, Long puntoOrdenDiaId, Long connectedUserId)
    {
        puntoOrdenDiaDAO.delete(PuntoOrdenDia.class, puntoOrdenDiaId);

    }

    public PuntoOrdenDia updatePuntoOrdenDia(Long puntoOrdenDiaId, String titulo,
            String descripcion, String deliberaciones, String acuerdos, Long orden, Boolean publico,
            Long connectedUserId) throws PuntoOrdenDiaNoDisponibleException
    {
        PuntoOrdenDia puntoOrdenDia = puntoOrdenDiaDAO.getPuntoOrdenDiaById(puntoOrdenDiaId);

        if (puntoOrdenDia == null)
        {
            throw new PuntoOrdenDiaNoDisponibleException();
        }

        puntoOrdenDia.setTitulo(titulo);
        puntoOrdenDia.setDescripcion(descripcion);
        puntoOrdenDia.setOrden(orden);
        puntoOrdenDia.setDeliberaciones(deliberaciones);
        puntoOrdenDia.setAcuerdos(acuerdos);
        puntoOrdenDia.setPublico(publico);

        return puntoOrdenDiaDAO.update(puntoOrdenDia);
    }

    public PuntoOrdenDia addPuntoOrdenDia(PuntoOrdenDia puntoOrdenDia, Long connectedUserId)
    {
        PuntoOrdenDia ultimo = puntoOrdenDiaDAO
                .getUltimoPuntoOrdenDiaByReunionId(puntoOrdenDia.getReunion().getId());

        if (ultimo != null)
        {
            puntoOrdenDia.setOrden(ultimo.getOrden() + 10L);
        }
        else
        {
            puntoOrdenDia.setOrden(10L);
        }
        return puntoOrdenDiaDAO.insert(puntoOrdenDia);
    }

    @Transactional
    public void subePuntoOrdenDia(Long reunionId, Long puntoOrdenDiaId, Long connectedUserId)
    {
        PuntoOrdenDia puntoOrdenDia = puntoOrdenDiaDAO.getPuntoOrdenDiaById(puntoOrdenDiaId);
        PuntoOrdenDia anteriorPuntoOrdenDia = puntoOrdenDiaDAO
                .getAnteriorPuntoOrdenDiaByOrden(puntoOrdenDia.getOrden());

        if (anteriorPuntoOrdenDia != null)
        {
            Long orden = puntoOrdenDia.getOrden();
            puntoOrdenDiaDAO.actualizaOrden(puntoOrdenDiaId, anteriorPuntoOrdenDia.getOrden());
            puntoOrdenDiaDAO.flush();
            puntoOrdenDiaDAO.actualizaOrden(anteriorPuntoOrdenDia.getId(), orden);
            puntoOrdenDiaDAO.flush();
            return;
        }

        List<PuntoOrdenDia> listaPuntosOrdenDia = puntoOrdenDiaDAO
                .getPuntosOrdenDiaMismoOrden(puntoOrdenDia.getOrden());

        if (listaPuntosOrdenDia.size() > 1)
        {
            puntoOrdenDiaDAO.actualizaOrden(puntoOrdenDiaId, puntoOrdenDia.getOrden() - 10L);
        }
    }

    @Transactional
    public void bajaPuntoOrdenDia(Long reunionId, Long puntoOrdenDiaId, Long connectedUserId)
    {
        PuntoOrdenDia puntoOrdenDia = puntoOrdenDiaDAO.getPuntoOrdenDiaById(puntoOrdenDiaId);
        PuntoOrdenDia siguientePuntoOrdenDia = puntoOrdenDiaDAO
                .getSiguientePuntoOrdenDiaByOrden(reunionId, puntoOrdenDia.getOrden());

        if (siguientePuntoOrdenDia != null)
        {
            Long orden = puntoOrdenDia.getOrden();
            puntoOrdenDiaDAO.actualizaOrden(puntoOrdenDiaId, siguientePuntoOrdenDia.getOrden());
            puntoOrdenDiaDAO.flush();
            puntoOrdenDiaDAO.actualizaOrden(siguientePuntoOrdenDia.getId(), orden);
            puntoOrdenDiaDAO.flush();
        }

        List<PuntoOrdenDia> listaPuntosOrdenDia = puntoOrdenDiaDAO
                .getPuntosOrdenDiaMismoOrden(puntoOrdenDia.getOrden());

        if (listaPuntosOrdenDia.size() > 1)
        {
            puntoOrdenDiaDAO.actualizaOrden(puntoOrdenDiaId, puntoOrdenDia.getOrden() + 10L);
        }

    }
}
