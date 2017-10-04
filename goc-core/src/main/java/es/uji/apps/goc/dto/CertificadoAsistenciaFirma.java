package es.uji.apps.goc.dto;

import java.util.Date;

public class CertificadoAsistenciaFirma
{
    private Long reunionId;

    private String reunionAsunto;

    private String reunionAsuntoAlternativo;

    private Date fecha;

    private String asistenteId;

    private String asistenteNombre;

    private String convocanteNombre;

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

    public String getAsistenteId()
    {
        return asistenteId;
    }

    public void setAsistenteId(String asistenteId)
    {
        this.asistenteId = asistenteId;
    }

    public String getAsistenteNombre()
    {
        return asistenteNombre;
    }

    public void setAsistenteNombre(String asistenteNombre)
    {
        this.asistenteNombre = asistenteNombre;
    }

    public String getConvocanteNombre()
    {
        return convocanteNombre;
    }

    public void setConvocanteNombre(String convocanteNombre)
    {
        this.convocanteNombre = convocanteNombre;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CertificadoAsistenciaFirma that = (CertificadoAsistenciaFirma) o;

        if (!reunionId.equals(that.reunionId)) return false;
        return asistenteId.equals(that.asistenteId);
    }

    @Override
    public int hashCode()
    {
        int result = reunionId.hashCode();
        result = 31 * result + asistenteId.hashCode();
        return result;
    }
}
