package es.uji.apps.goc.dto;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "GOC_DESCRIPTORES")
public class Descriptor implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String descriptor;

    @Column(name = "DESCRIPTOR_ALT")
    private String descriptorAlternativo;

    private String descripcion;

    @Column(name = "DESCRIPCION_ALT")
    private String descripcionAlternativa;

    private Long estado;

    @OneToMany(mappedBy = "descriptor", cascade = CascadeType.ALL)
    private Set<DescriptorTipoOrgano> descriptoresTiposOrgano;

    @OneToMany(mappedBy = "descriptor", cascade = CascadeType.ALL)
    private Set<Clave> claves;

    public Descriptor() {
    }

    public Descriptor(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public String getDescriptorAlternativo() {
        return descriptorAlternativo;
    }

    public void setDescriptorAlternativo(String descriptorAlternativo) {
        this.descriptorAlternativo = descriptorAlternativo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcionAlternativa() {
        return descripcionAlternativa;
    }

    public void setDescripcionAlternativa(String descripcionAlternativa) {
        this.descripcionAlternativa = descripcionAlternativa;
    }

    public Set<DescriptorTipoOrgano> getDescriptoresTiposOrgano()
    {
        return descriptoresTiposOrgano;
    }

    public void setDescriptoresTiposOrgano(Set<DescriptorTipoOrgano> descriptoresTiposOrgano)
    {
        this.descriptoresTiposOrgano = descriptoresTiposOrgano;
    }

    public Set<Clave> getClaves()
    {
        return claves;
    }

    public void setClaves(Set<Clave> claves)
    {
        this.claves = claves;
    }
}
