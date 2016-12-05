package es.uji.apps.goc.model;

import java.util.Date;
import java.util.Set;

import es.uji.apps.goc.dto.OrganoReunion;

public class Organo
{
    private String id;

    private String nombre;

    private String nombreAlternativo;

    private Set<OrganoReunion> organoReuniones;

    private Set<Miembro> miembros;

    private TipoOrgano tipoOrgano;

    private Boolean externo;

    private Boolean inactivo;

    private Long creadorId;

    private Date fechaCreacion;

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

    public TipoOrgano getTipoOrgano()
    {
        return tipoOrgano;
    }

    public void setTipoOrgano(TipoOrgano tipoOrgano)
    {
        this.tipoOrgano = tipoOrgano;
    }

    public Organo() {
        this.externo = false;
    }

    public Organo(String id, String nombre, String nombreAlternativo, TipoOrgano tipoOrgano) {
        this.id = id;
        this.nombre = nombre;
        this.nombreAlternativo = nombreAlternativo;
        this.tipoOrgano = tipoOrgano;
        this.externo = true;
    }

    public Organo(String id) {
        this.id = id;
    }

    public Organo(String id, String nombre, String nombreAlternativo, TipoOrgano tipoOrgano, Boolean externo) {
        this.id = id;
        this.nombre = nombre;
        this.nombreAlternativo = nombreAlternativo;
        this.externo = externo;
        this.tipoOrgano = tipoOrgano;
    }

    public Boolean isExterno()
    {
        return externo;
    }

    public void setExterno(Boolean externo)
    {
        this.externo = externo;
    }

    public Set<OrganoReunion> getOrganoReuniones()
    {
        return organoReuniones;
    }

    public void setOrganoReuniones(Set<OrganoReunion> organoReuniones)
    {
        this.organoReuniones = organoReuniones;
    }

    public Set<Miembro> getMiembros() {
        return miembros;
    }

    public void setMiembros(Set<Miembro> miembros) {
        this.miembros = miembros;
    }

    public Long getCreadorId() {
        return creadorId;
    }

    public void setCreadorId(Long creadorId) {
        this.creadorId = creadorId;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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
}
