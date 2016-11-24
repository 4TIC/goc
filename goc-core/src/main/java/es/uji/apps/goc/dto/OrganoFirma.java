package es.uji.apps.goc.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OrganoFirma
{
    private String id;
    private String nombre;
    private Boolean inactivo;

    @JsonProperty("tipo_organo_id")
    private Long tipoOrganoId;

    @JsonProperty("tipo_nombre")
    private String tipoNombre;

    @JsonProperty("tipo_codigo")
    private String tipoCodigo;

    private List<MiembroFirma> asistentes;

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

    public OrganoFirma()
    {
    }

    public OrganoFirma(String id)
    {
        this.id = id;
    }

    public List<MiembroFirma> getAsistentes() {
        return asistentes;
    }

    public void setAsistentes(List<MiembroFirma> asistentes) {
        this.asistentes = asistentes;
    }

    public Boolean getInactivo() {
        return inactivo;
    }

    public void setInactivo(Boolean inactivo) {
        this.inactivo = inactivo;
    }
}
