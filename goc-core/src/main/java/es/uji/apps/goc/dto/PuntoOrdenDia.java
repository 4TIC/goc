package es.uji.apps.goc.dto;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "GOC_REUNIONES_PUNTOS_ORDEN_DIA")
public class PuntoOrdenDia implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String titulo;

    @Column(name = "TITULO_ALT")
    private String tituloAlternativo;

    private String descripcion;

    @Column(name = "DESCRIPCION_ALT")
    private String descripcionAlternativa;

    private Long orden;

    private String acuerdos;

    @Column(name = "ACUERDOS_ALT")
    private String acuerdosAlternativos;

    private Boolean publico;

    private String deliberaciones;

    @Column(name = "DELIBERACIONES_ALT")
    private String deliberacionesAlternativas;

    @ManyToOne
    @JoinColumn(name = "REUNION_ID")
    private Reunion reunion;

    @OneToMany(mappedBy = "puntoOrdenDia", cascade = CascadeType.REMOVE)
    private Set<PuntoOrdenDiaDocumento> puntoOrdenDiaDocumentos;

    public PuntoOrdenDia() {}
    public PuntoOrdenDia(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getOrden() {
        return orden;
    }

    public void setOrden(Long orden) {
        this.orden = orden;
    }

    public Reunion getReunion() {
        return reunion;
    }

    public void setReunion(Reunion reunion) {
        this.reunion = reunion;
    }

    public Set<PuntoOrdenDiaDocumento> getPuntoOrdenDiaDocumentos()
    {
        return puntoOrdenDiaDocumentos;
    }

    public void setPuntoOrdenDiaDocumentos(Set<PuntoOrdenDiaDocumento> puntoOrdenDiaDocumentos)
    {
        this.puntoOrdenDiaDocumentos = puntoOrdenDiaDocumentos;
    }

    public String getAcuerdos()
    {
        return acuerdos;
    }

    public void setAcuerdos(String acuerdos)
    {
        this.acuerdos = acuerdos;
    }

    public String getDeliberaciones() {
        return deliberaciones;
    }

    public void setDeliberaciones(String deliberaciones) {
        this.deliberaciones = deliberaciones;
    }

    public Boolean isPublico()
    {
        return publico;
    }

    public void setPublico(Boolean publico)
    {
        this.publico = publico;
    }


    public String getTituloAlternativo()
    {
        return tituloAlternativo;
    }

    public void setTituloAlternativo(String tituloAlternativo)
    {
        this.tituloAlternativo = tituloAlternativo;
    }

    public String getDescripcionAlternativa()
    {
        return descripcionAlternativa;
    }

    public void setDescripcionAlternativa(String descripcionAlternativa)
    {
        this.descripcionAlternativa = descripcionAlternativa;
    }

    public String getAcuerdosAlternativos()
    {
        return acuerdosAlternativos;
    }

    public void setAcuerdosAlternativos(String acuerdosAlternativos)
    {
        this.acuerdosAlternativos = acuerdosAlternativos;
    }

    public String getDeliberacionesAlternativas()
    {
        return deliberacionesAlternativas;
    }

    public void setDeliberacionesAlternativas(String deliberacionesAlternativas)
    {
        this.deliberacionesAlternativas = deliberacionesAlternativas;
    }
}