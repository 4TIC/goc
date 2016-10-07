package es.uji.apps.goc.avisos.services;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import es.uji.apps.goc.dao.ReunionDAO;
import es.uji.apps.goc.dto.Reunion;
import es.uji.apps.goc.notifications.AvisosReunion;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AvisosService
{
    private static Logger log = LoggerFactory.getLogger(AvisosService.class);
    private static final int ONE_DAY = 1000 * 60 * 60 * 24;

    @Value("${uji.smtp.defaultSender}")
    private String defaultSender;

    private AvisosReunion avisosReunion;
    private ReunionDAO reunionDAO;

    @Autowired
    public AvisosService(AvisosReunion avisosReunion, ReunionDAO reunionDAO)
    {
        this.avisosReunion = avisosReunion;
        this.reunionDAO = reunionDAO;
    }

    public void procesarPendientes()
    {
        Date tomorrow = getTomorrowDate();
        List<Reunion> pendientesNotificacion = reunionDAO.getPendientesNotificacion(tomorrow);

        pendientesNotificacion.stream()
                .forEach(reunion -> procesaEnvios(reunion));
    }

    @Transactional
    private void procesaEnvios(Reunion reunion)
    {
        Boolean notificados = false;

        try
        {
            notificados = avisosReunion.enviaAvisoReunionProxima(reunion);
        }
        catch (Exception e)
        {
            log.error("No se ha podido enviar el aviso de reunión para " + reunion.getId(), e);
        }

        if (notificados)
        {
            reunion.setNotificada(true);
            reunionDAO.update(reunion);
        }
    }

    public Date getTomorrowDate()
    {
        Date today = new Date();
        return new Date(today.getTime() + ONE_DAY);
    }
}
