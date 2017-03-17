package es.uji.apps.goc.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "GOC_CLAVES")
public class Clave {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String clave;

    @Column(name = "CLAVE_ALT")
    private String claveAlternativa;

    private Long estado;

    @ManyToOne
    @JoinColumn(name = "ID_DESCRIPTOR")
    private Descriptor descriptor;

    public Clave() {
    }

    public Clave(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getClaveAlternativa() {
        return claveAlternativa;
    }

    public void setClaveAlternativa(String claveAlternativa) {
        this.claveAlternativa = claveAlternativa;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    public Descriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(Descriptor descriptor) {
        this.descriptor = descriptor;
    }
}
