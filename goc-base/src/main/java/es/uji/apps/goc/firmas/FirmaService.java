package es.uji.apps.goc.firmas;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import es.uji.apps.goc.dto.ReunionFirma;
import es.uji.apps.goc.model.RespuestaFirma;

@Component
public class FirmaService
{
    @Value("${goc.publicUrl}")
    private String publicUrl;

    public RespuestaFirma firmaReunion(ReunionFirma reunionFirma)
    {
        RespuestaFirma respuestaFirma = new RespuestaFirma();

        respuestaFirma.setUrlActa(publicUrl + "/goc/rest/actas/" + reunionFirma.getId());

        return respuestaFirma;
    }
}