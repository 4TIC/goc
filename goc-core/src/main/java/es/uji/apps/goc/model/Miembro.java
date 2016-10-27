package es.uji.apps.goc.model;

public class Miembro
{
    private Long id;

    private Long personaId;

    private String nombre;

    private String email;

    private Organo organo;
    private Cargo cargo;

    public Miembro()
    {
    }

    public Miembro(Long id, String nombre, String email, Organo organo, Cargo cargo)
    {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.organo = organo;
        this.cargo = cargo;
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

    public Organo getOrgano()
    {
        return organo;
    }

    public void setOrgano(Organo organo)
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

    public Long getPersonaId()
    {
        return personaId;
    }

    public void setPersonaId(Long personaId)
    {
        this.personaId = personaId;
    }
}