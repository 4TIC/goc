package es.uji.apps.goc.model;

import com.sun.jersey.api.client.ClientResponse;
import es.uji.commons.rest.UIEntity;

import java.util.ArrayList;
import java.util.List;

public class RespuestaFirma
{
    private String urlActa;
    private String urlActaAlternativa;

    private List<RespuestaFirmaPuntoOrdenDiaAcuerdo> puntoOrdenDiaAcuerdos;

    public RespuestaFirma()
    {
        puntoOrdenDiaAcuerdos = new ArrayList<>();
    }

    public String getUrlActa()
    {
        return urlActa;
    }

    public void setUrlActa(String urlActa)
    {
        this.urlActa = urlActa;
    }

    public String getUrlActaAlternativa()
    {
        return urlActaAlternativa;
    }

    public void setUrlActaAlternativa(String urlActaAlternativa)
    {
        this.urlActaAlternativa = urlActaAlternativa;
    }

    public List<RespuestaFirmaPuntoOrdenDiaAcuerdo> getPuntoOrdenDiaAcuerdos()
    {
        return puntoOrdenDiaAcuerdos;
    }

    public void setPuntoOrdenDiaAcuerdos(List<RespuestaFirmaPuntoOrdenDiaAcuerdo> puntoOrdenDiaAcuerdos)
    {
        this.puntoOrdenDiaAcuerdos = puntoOrdenDiaAcuerdos;
    }

    public void addPuntoOrdenDiaAcuerdos(RespuestaFirmaPuntoOrdenDiaAcuerdo puntoOrdenDiaAcuerdo)
    {
        this.puntoOrdenDiaAcuerdos.add(puntoOrdenDiaAcuerdo);
    }

    public static RespuestaFirma buildRespuestaFirma(ClientResponse response)
    {
        UIEntity entityResponse = response.getEntity(UIEntity.class);
        UIEntity entityData = entityResponse.getRelations().get("data").get(0);

        RespuestaFirma respuestaFirma = new RespuestaFirma();

        respuestaFirma.setUrlActa(entityData.get("urlActa"));
        respuestaFirma.setUrlActaAlternativa(entityData.get("urlActaAlternativa"));

        List<UIEntity> entityAcuerdos = entityData.getRelations().get("puntoOrdenDiaAcuerdos");

        if (entityAcuerdos != null)
        {
            for (UIEntity entityAcuerdo : entityAcuerdos)
            {
                RespuestaFirmaPuntoOrdenDiaAcuerdo respuestaFirmaPuntoOrdenDiaAcuerdo =
                        new RespuestaFirmaPuntoOrdenDiaAcuerdo();

                respuestaFirmaPuntoOrdenDiaAcuerdo.setUrlActa(entityAcuerdo.get("urlActa"));
                respuestaFirmaPuntoOrdenDiaAcuerdo.setUrlActaAlternativa(entityAcuerdo.get("urlActaAlternativa"));
                respuestaFirmaPuntoOrdenDiaAcuerdo.setId(entityAcuerdo.getLong("id"));

                respuestaFirma.addPuntoOrdenDiaAcuerdos(respuestaFirmaPuntoOrdenDiaAcuerdo);
            }
        }
        return respuestaFirma;
    }
}
