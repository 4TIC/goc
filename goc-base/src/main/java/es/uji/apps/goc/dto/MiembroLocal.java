package es.uji.apps.goc.dto;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import es.uji.apps.goc.model.OrganoLocal;

@Entity
@Table(name = "GOC_MIEMBROS")
public class MiembroLocal implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;

    private String email;

    @ManyToOne
    @JoinColumn(name = "ORGANO_ID")
    private OrganoLocal organo;

    @ManyToOne
    @JoinColumn(name = "CARGO_ID")
    private Cargo cargo;

    public MiembroLocal() {}

    public MiembroLocal(Long id, String nombre, String email, OrganoLocal organo) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.organo = organo;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
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

    public OrganoLocal getOrgano()
    {
        return organo;
    }

    public void setOrgano(OrganoLocal organo)
    {
        this.organo = organo;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Cargo getCargo()
    {
        return cargo;
    }

    public void setCargo(Cargo cargo)
    {
        this.cargo = cargo;
    }
}