package es.uji.apps.goc.dto;

import java.util.List;

public class OrganoTemplate
{
    private String id;
    private String nombre;
    private Boolean inactivo;

    private Long tipoOrganoId;
    private String tipoNombre;
    private String tipoCodigo;

    private List<MiembroTemplate> asistentes;
    private List<MiembroTemplate> ausentes;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public Long getTipoOrganoId()
    {
        return tipoOrganoId;
    }

    public void setTipoOrganoId(Long tipoOrganoId)
    {
        this.tipoOrganoId = tipoOrganoId;
    }

    public String getTipoNombre()
    {
        return tipoNombre;
    }

    public void setTipoNombre(String tipoNombre)
    {
        this.tipoNombre = tipoNombre;
    }

    public String getTipoCodigo()
    {
        return tipoCodigo;
    }

    public void setTipoCodigo(String tipoCodigo)
    {
        this.tipoCodigo = tipoCodigo;
    }

    public OrganoTemplate()
    {
    }

    public OrganoTemplate(String id)
    {
        this.id = id;
    }

    public List<MiembroTemplate> getAsistentes() {
        return asistentes;
    }

    public void setAsistentes(List<MiembroTemplate> asistentes) {
        this.asistentes = asistentes;
    }

    public Boolean getInactivo() {
        return inactivo;
    }

    public void setInactivo(Boolean inactivo) {
        this.inactivo = inactivo;
    }

    public List<MiembroTemplate> getAusentes()
    {
        return ausentes;
    }

    public void setAusentes(List<MiembroTemplate> ausentes)
    {
        this.ausentes = ausentes;
    }
}
