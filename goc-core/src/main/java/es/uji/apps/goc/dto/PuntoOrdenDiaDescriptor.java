package es.uji.apps.goc.dto;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "GOC_P_ORDEN_DIA_DESCRIPTORES")
public class PuntoOrdenDiaDescriptor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_CLAVE")
    private Clave clave;

    @ManyToOne
    @JoinColumn(name = "ID_P_ORDEN_DIA")
    private PuntoOrdenDia puntoOrdenDia;

    public PuntoOrdenDiaDescriptor() {
    }

    public PuntoOrdenDiaDescriptor(Long id){
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Clave getClave() {
        return clave;
    }

    public void setClave(Clave clave) {
        this.clave = clave;
    }

    public PuntoOrdenDia getPuntoOrdenDia() {
        return puntoOrdenDia;
    }

    public void setPuntoOrdenDia(PuntoOrdenDia puntoOrdenDia) {
        this.puntoOrdenDia = puntoOrdenDia;
    }
}
