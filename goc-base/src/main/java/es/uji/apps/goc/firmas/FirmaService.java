package es.uji.apps.goc.firmas;

import es.uji.apps.goc.dto.ReunionFirma;
import es.uji.apps.goc.model.RespuestaFirma;
import org.springframework.stereotype.Component;

@Component
public class FirmaService
{
    public RespuestaFirma firmaReunion(ReunionFirma reunionFirma)
    {
        RespuestaFirma respuestaFirma = new RespuestaFirma();

        respuestaFirma.setUrlActa("http://www.uji.es/acta");

        return respuestaFirma;
    }
}