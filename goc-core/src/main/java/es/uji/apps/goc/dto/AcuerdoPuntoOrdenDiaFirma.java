package es.uji.apps.goc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class AcuerdoPuntoOrdenDiaFirma
{
    private Long reunionId;

    private String reunionAsunto;

    @JsonProperty("asunto_alternativo")
    private String reunionAsuntoAlternativo;

    private Date fecha;

    private Long puntoOrdenDiaId;

    private String puntoOrdenDiaTitulo;

    @JsonProperty(value = "titulo_alternativo")
    private String puntoOrdenDiaTituloAlternativo;

    private String puntoOrdenDiaAcuerdos;

    @JsonProperty(value = "acuerdos_alternativos")
    private String puntoOrdenDiaAcuerdosAlternativos;

    private List<OrganoFirma> organos;

    public Long getReunionId()
    {
        return reunionId;
    }

    public void setReunionId(Long reunionId)
    {
        this.reunionId = reunionId;
    }

    public String getReunionAsunto()
    {
        return reunionAsunto;
    }

    public void setReunionAsunto(String reunionAsunto)
    {
        this.reunionAsunto = reunionAsunto;
    }

    public String getReunionAsuntoAlternativo()
    {
        return reunionAsuntoAlternativo;
    }

    public void setReunionAsuntoAlternativo(String reunionAsuntoAlternativo)
    {
        this.reunionAsuntoAlternativo = reunionAsuntoAlternativo;
    }

    public Date getFecha()
    {
        return fecha;
    }

    public void setFecha(Date fecha)
    {
        this.fecha = fecha;
    }

    public Long getPuntoOrdenDiaId()
    {
        return puntoOrdenDiaId;
    }

    public void setPuntoOrdenDiaId(Long puntoOrdenDiaId)
    {
        this.puntoOrdenDiaId = puntoOrdenDiaId;
    }

    public String getPuntoOrdenDiaTitulo()
    {
        return puntoOrdenDiaTitulo;
    }

    public void setPuntoOrdenDiaTitulo(String puntoOrdenDiaTitulo)
    {
        this.puntoOrdenDiaTitulo = puntoOrdenDiaTitulo;
    }

    public String getPuntoOrdenDiaTituloAlternativo()
    {
        return puntoOrdenDiaTituloAlternativo;
    }

    public void setPuntoOrdenDiaTituloAlternativo(String puntoOrdenDiaTituloAlternativo)
    {
        this.puntoOrdenDiaTituloAlternativo = puntoOrdenDiaTituloAlternativo;
    }

    public String getPuntoOrdenDiaAcuerdos()
    {
        return puntoOrdenDiaAcuerdos;
    }

    public void setPuntoOrdenDiaAcuerdos(String puntoOrdenDiaAcuerdos)
    {
        this.puntoOrdenDiaAcuerdos = puntoOrdenDiaAcuerdos;
    }

    public String getPuntoOrdenDiaAcuerdosAlternativos()
    {
        return puntoOrdenDiaAcuerdosAlternativos;
    }

    public void setPuntoOrdenDiaAcuerdosAlternativos(String puntoOrdenDiaAcuerdosAlternativos)
    {
        this.puntoOrdenDiaAcuerdosAlternativos = puntoOrdenDiaAcuerdosAlternativos;
    }

    public List<OrganoFirma> getOrganos()
    {
        return organos;
    }

    public void setOrganos(List<OrganoFirma> organos)
    {
        this.organos = organos;
    }


}
