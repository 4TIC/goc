package es.uji.apps.goc.model;

public class RespuestaFirmaAsistencia
{
    private String personaId;
    private String urlAsistencia;
    private String urlAsistenciaAlternativa;

    public String getPersonaId()
    {
        return personaId;
    }

    public void setPersonaId(String personaId)
    {
        this.personaId = personaId;
    }

    public String getUrlAsistencia()
    {
        return urlAsistencia;
    }

    public void setUrlAsistencia(String urlAsistencia)
    {
        this.urlAsistencia = urlAsistencia;
    }

    public String getUrlAsistenciaAlternativa()
    {
        return urlAsistenciaAlternativa;
    }

    public void setUrlAsistenciaAlternativa(String urlAsistenciaAlternativa)
    {
        this.urlAsistenciaAlternativa = urlAsistenciaAlternativa;
    }
}
