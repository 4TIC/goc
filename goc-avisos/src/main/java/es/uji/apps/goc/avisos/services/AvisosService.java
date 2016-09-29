package es.uji.apps.goc.avisos.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import es.uji.apps.goc.dao.ReunionDAO;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.exceptions.MiembrosExternosException;
import es.uji.apps.goc.exceptions.NotificacionesException;
import es.uji.apps.goc.exceptions.ReunionNoDisponibleException;
import es.uji.apps.goc.notifications.AvisosReunion;

@Service
@Component
public class AvisosService
{
    private static final int ONE_DAY = 1000 * 60 * 60 * 24;

    @Autowired
    private AvisosReunion avisosReunion;

    @Autowired
    private ReunionDAO reunionDAO;

    @Value("${uji.deploy.defaultUserId}")
    private Long connectedUserId;

    public void procesarPendientes()
    {

        Date tomorrow = getTomorrowDate();
        reunionDAO.getPendientesNotificacion(getTomorrowDate()).stream()
                .forEach(reunion ->
                {
                    try
                    {
                        procesaEnvios(reunion);
                    }
                    catch (MiembrosExternosException e)
                    {
                        e.printStackTrace();
                    }
                    catch (ReunionNoDisponibleException e)
                    {
                        e.printStackTrace();
                    }
                    catch (NotificacionesException e)
                    {
                        e.printStackTrace();
                    }
                });
    }

    private void procesaEnvios(Reunion reunion)
            throws MiembrosExternosException, ReunionNoDisponibleException, NotificacionesException
    {
        avisosReunion.enviaAvisoReunionProxima(reunion.getId(), connectedUserId);
        reunionDAO.marcaNotificada(reunion.getId());
    }

    public Date getTomorrowDate()
    {
        Date today = new Date();
        return new Date(today.getTime() + ONE_DAY);
    }
}
