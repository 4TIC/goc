package es.uji.apps.goc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrganoExterno
{
    private String id;
    private String nombre;

    @JsonProperty("tipo_id")
    private Long tipoOrganoId;

    @JsonProperty("tipo_nombre")
    private String tipoNombre;

    @JsonProperty("tipo_codigo")
    private String tipoCodigo;

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

    public OrganoExterno() {}
    public OrganoExterno(String id) {
        this.id = id;
    }
}
