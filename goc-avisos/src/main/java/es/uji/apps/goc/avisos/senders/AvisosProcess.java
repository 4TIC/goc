package es.uji.apps.goc.avisos.senders;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import es.uji.apps.goc.dao.ReunionDAO;
import es.uji.apps.goc.dto.Reunion;

public class AvisosProcess implements Runnable
{
    public static Logger log = LoggerFactory.getLogger(AvisosProcess.class);

    private static final int TIEMPO_ESPERA_ENTRE_CONSULTAS = 1000 * 60;

    private static final int ONE_DAY = 1000 * 60 * 60 * 24;

    private ReunionDAO reunionDAO;

    public AvisosProcess(ReunionDAO reunionDAO)
    {
        this.reunionDAO = reunionDAO;
    }

    public void run()
    {
        while (true)
        {
            try
            {
                Date tomorrow = getTomorrowDate();
                reunionDAO.getReunionesProximas(tomorrow).stream()
                        .forEach(reunionPendiente -> procesaEnvios(reunionPendiente));

                sleep();
            }
            catch (Exception e)
            {
                log.error("Error general", e);
                sleep();
            }
        }
    }

    private void procesaEnvios(Reunion reunion)
    {
        System.out.println(reunion.getId());
    }

    private void sleep()
    {
        try
        {
            log.info("No hay más peticiones a procesar");
            Thread.sleep(TIEMPO_ESPERA_ENTRE_CONSULTAS);
        }
        catch (InterruptedException e)
        {
            log.error("Error en la interrupción del thread principal", e);
        }
    }

    public static void main(String[] args) throws Exception
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");

        ReunionDAO reunionDAO = context.getBean(ReunionDAO.class);

        AvisosProcess job = new AvisosProcess(reunionDAO);

        log.info("START MatriculasEstudiosFueJob " + new Date());

        new Thread(job).start();
    }

    public Date getTomorrowDate()
    {
        Date today = new Date();
        return new Date(today.getTime() + ONE_DAY);
    }
}
