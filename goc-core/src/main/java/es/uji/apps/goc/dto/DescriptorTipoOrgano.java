package es.uji.apps.goc.dto;

import es.uji.apps.goc.model.TipoOrgano;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "GOC_DESCRIPTORES_TIPOS_ORGANO")
public class DescriptorTipoOrgano implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "DESCRIPTOR_ID")
    private Descriptor descriptor;

    @ManyToOne
    @JoinColumn(name = "TIPO_ORGANO_ID")
    private TipoOrganoLocal tipoOrgano;

    public DescriptorTipoOrgano() {
    }

    public DescriptorTipoOrgano(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Descriptor getDescriptor()
    {
        return descriptor;
    }

    public void setDescriptor(Descriptor descriptor)
    {
        this.descriptor = descriptor;
    }

    public TipoOrganoLocal getTipoOrgano()
    {
        return tipoOrgano;
    }

    public void setTipoOrgano(TipoOrganoLocal tipoOrgano)
    {
        this.tipoOrgano = tipoOrgano;
    }
}
