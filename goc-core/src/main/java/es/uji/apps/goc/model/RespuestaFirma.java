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

    private List<RespuestaFirmaAsistencia> asistencias;

    public RespuestaFirma()
    {
        puntoOrdenDiaAcuerdos = new ArrayList<>();
        asistencias = new ArrayList<>();
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

    public List<RespuestaFirmaAsistencia> getAsistencias()
    {
        return asistencias;
    }

    public void setAsistencias(List<RespuestaFirmaAsistencia> asistencias)
    {
        this.asistencias = asistencias;
    }

    public void addAsistencia(RespuestaFirmaAsistencia respuestaFirmaAsistencia)
    {
        this.asistencias.add(respuestaFirmaAsistencia);
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

        List<UIEntity> entityAsistencias = entityData.getRelations().get("asistencias");

        if (entityAsistencias != null)
        {
            for (UIEntity entityAsistencia : entityAsistencias)
            {
                RespuestaFirmaAsistencia respuestaFirmaAsistencia = new RespuestaFirmaAsistencia();

                respuestaFirmaAsistencia.setPersonaId(entityAsistencia.get("personaId"));
                respuestaFirmaAsistencia.setUrlAsistencia(entityAsistencia.get("urlAsistencia"));

                respuestaFirma.addAsistencia(respuestaFirmaAsistencia);
            }
        }

        return respuestaFirma;
    }
}
