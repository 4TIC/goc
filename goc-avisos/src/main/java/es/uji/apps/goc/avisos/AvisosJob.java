package es.uji.apps.goc.avisos;

import es.uji.apps.goc.avisos.services.AvisosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Date;

public class AvisosJob implements Runnable
{
    public static Logger log = LoggerFactory.getLogger(AvisosJob.class);

    private static final int TIEMPO_ESPERA_ENTRE_CONSULTAS = 1000 * 60;

    private AvisosService avisosService;

    public AvisosJob(AvisosService avisosService)
    {
        this.avisosService = avisosService;
    }

    public void run()
    {
        while (true)
        {
            try
            {
                avisosService.procesarPendientes();

                sleep();
            }
            catch (Exception e)
            {
                log.error("Error general", e);
                sleep();
            }
        }
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

        AvisosService avisosService = context.getBean(AvisosService.class);

        AvisosJob job = new AvisosJob(avisosService);

        log.info("START AvisosJob " + new Date());

        new Thread(job).start();
    }
}
