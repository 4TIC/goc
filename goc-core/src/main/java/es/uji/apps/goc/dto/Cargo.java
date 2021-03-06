package es.uji.apps.goc.dto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "GOC_CARGOS")
public class Cargo implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String codigo;

    private String nombre;

    @Column(name = "NOMBRE_ALT")
    private String nombreAlternativo;

    @OneToMany(mappedBy = "cargo", cascade = CascadeType.ALL)
    private Set<MiembroLocal> miembrosLocales;

    public Cargo()
    {
    }

    public Cargo(Long id)
    {
        this.id = id;
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

    public Set<MiembroLocal> getMiembrosLocales()
    {
        return miembrosLocales;
    }

    public void setMiembrosLocales(Set<MiembroLocal> miembrosLocales)
    {
        this.miembrosLocales = miembrosLocales;
    }

    public String getNombreAlternativo()
    {
        return nombreAlternativo;
    }

    public void setNombreAlternativo(String nombreAlternativo)
    {
        this.nombreAlternativo = nombreAlternativo;
    }

    public String getCodigo()
    {
        return codigo;
    }

    public void setCodigo(String codigo)
    {
        this.codigo = codigo;
    }
}
