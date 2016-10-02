package es.uji.apps.goc.avisos.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private ReunionDAO reunionDAO;

    @Autowired
    private AvisosReunion avisosReunion;

    @Autowired
    public AvisosService(ReunionDAO reunionDAO) {
        this.reunionDAO = reunionDAO;
    }


    @Value("${uji.deploy.defaultUserId}")
    private Long connectedUserId;

    @Value("${uji.smtp.defaultSender}")
    private String defaultSender;

    public void procesarPendientes()
    {

        Date tomorrow = getTomorrowDate();
        reunionDAO.getPendientesNotificacion(getTomorrowDate()).stream().forEach(reunion -> {
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

    @Transactional
    private void procesaEnvios(Reunion reunion)
            throws MiembrosExternosException, ReunionNoDisponibleException, NotificacionesException
    {
        avisosReunion.enviaAvisoReunionProxima(reunion.getId(), connectedUserId, defaultSender);

        reunion.setNotificada(true);
        reunionDAO.update(reunion);
    }

    public Date getTomorrowDate()
    {
        Date today = new Date();
        return new Date(today.getTime() + ONE_DAY);
    }
}
