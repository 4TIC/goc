package es.uji.apps.goc.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
}
