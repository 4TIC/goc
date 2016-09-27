package es.uji.apps.goc.notifications;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.uji.apps.goc.dao.MiembroDAO;
import es.uji.apps.goc.dao.NotificacionesDAO;
import es.uji.apps.goc.dao.ReunionDAO;
import es.uji.apps.goc.dto.MiembroLocal;
import es.uji.apps.goc.dto.OrganoReunion;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.exceptions.MiembrosExternosException;
import es.uji.apps.goc.exceptions.NotificacionesException;
import es.uji.apps.goc.exceptions.ReunionNoDisponibleException;
import es.uji.apps.goc.model.Miembro;

@Component
public class AvisosReunion
{
    @Autowired
    private ReunionDAO reunionDAO;

    @Autowired
    private MiembroDAO miembroDAO;

    @Autowired
    private NotificacionesDAO notificacionesDAO;

    @Transactional
    public void enviaAvisoNuevaReunion(Long reunionId, Long connectedUserId)
            throws ReunionNoDisponibleException, MiembrosExternosException, NotificacionesException
    {
        Reunion reunion = getReunion(reunionId, connectedUserId);
        List<String> miembros = getMiembros(reunion, connectedUserId);

        Mensaje mensaje = new Mensaje();
        mensaje.setAsunto("[GOC] Nueva reunión: Se te ha incluido como miembro en una nueva convocatoria de reunión");
        mensaje.setContentType("text/html");

        ReunionFormatter formatter = new ReunionFormatter(reunion);
        mensaje.setCuerpo(formatter.format());

        mensaje.setFrom("e-ujier@uji.es");
        mensaje.setDestinos(miembros);

        notificacionesDAO.enviaNotificacion(mensaje);
    }

    private Reunion getReunion(Long reunionId, Long connectedUserId) throws ReunionNoDisponibleException
    {
        Reunion reunion = reunionDAO.getReunionConOrganosById(reunionId);

        if (reunion == null)
        {
            throw new ReunionNoDisponibleException();
        }
        return reunion;
    }

    private List<String> getMiembros(Reunion reunion, Long connectedUserId)
            throws ReunionNoDisponibleException, MiembrosExternosException
    {
        List<String> miembros = new ArrayList<>();

        for (OrganoReunion organoReunion : reunion.getReunionOrganos())
        {
            if (organoReunion.getOrganoLocal() != null)
            {
                for (MiembroLocal miembroLocal : organoReunion.getOrganoLocal().getMiembros())
                {
                    miembros.add(miembroLocal.getEmail());
                }
            }

            String organoExternoId = organoReunion.getOrganoExternoId();

            if (organoExternoId != null)
            {
                for (Miembro miembroExterno : miembroDAO.getMiembrosExternos(organoExternoId, connectedUserId))
                {
                    miembros.add(miembroExterno.getEmail());
                }
            }
        }

        return miembros;
    }
}
