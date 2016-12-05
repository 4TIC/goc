package es.uji.apps.goc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrganoExterno
{
    private String id;
    private String nombre;

    @JsonProperty("nombre_alternativo")
    private String nombreAlternativo;

    private Boolean inactivo;

    @JsonProperty("tipo_id")
    private Long tipoOrganoId;

    @JsonProperty("tipo_nombre")
    private String tipoNombre;

    @JsonProperty("tipo_nombre_alternativo")
    private String tipoNombreAlternativo;

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

    public Boolean isInactivo() {
        return inactivo;
    }

    public void setInactivo(Boolean inactivo) {
        this.inactivo = inactivo;
    }

    public String getNombreAlternativo()
    {
        return nombreAlternativo;
    }

    public void setNombreAlternativo(String nombreAlternativo)
    {
        this.nombreAlternativo = nombreAlternativo;
    }

    public String getTipoNombreAlternativo()
    {
        return tipoNombreAlternativo;
    }

    public void setTipoNombreAlternativo(String tipoNombreAlternativo)
    {
        this.tipoNombreAlternativo = tipoNombreAlternativo;
    }
}