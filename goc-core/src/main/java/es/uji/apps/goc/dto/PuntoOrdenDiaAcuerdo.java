package es.uji.apps.goc.dto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "GOC_P_ORDEN_DIA_ACUERDOS")
public class PuntoOrdenDiaAcuerdo implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String descripcion;

    @Column(name = "DESCRIPCION_ALT")
    private String descripcionAlternativa;

    @Column(name="MIME_TYPE")
    private String mimeType;

    @Column(name="NOMBRE_FICHERO")
    private String nombreFichero;

    @Column(name="FECHA_ADICION")
    private Date fechaAdicion;

    @Column(name="CREADOR_ID")
    private Long creadorId;

    @ManyToOne
    @JoinColumn(name = "PUNTO_ID")
    private PuntoOrdenDia puntoOrdenDia;

    public Long getId()
    {
        return id;
    }

    @Lob()
    private byte[] datos;

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public void setMimeType(String mimeType)
    {
        this.mimeType = mimeType;
    }

    public String getNombreFichero()
    {
        return nombreFichero;
    }

    public void setNombreFichero(String nombreFichero)
    {
        this.nombreFichero = nombreFichero;
    }

    public Date getFechaAdicion()
    {
        return fechaAdicion;
    }

    public void setFechaAdicion(Date fechaAdicion)
    {
        this.fechaAdicion = fechaAdicion;
    }

    public byte[] getDatos()
    {
        return datos;
    }

    public void setDatos(byte[] datos)
    {
        this.datos = datos;
    }

    public Long getCreadorId() {
        return creadorId;
    }

    public void setCreadorId(Long creadorId) {
        this.creadorId = creadorId;
    }

    public PuntoOrdenDia getPuntoOrdenDia() {
        return puntoOrdenDia;
    }

    public void setPuntoOrdenDia(PuntoOrdenDia puntoOrdenDia) {
        this.puntoOrdenDia = puntoOrdenDia;
    }

    public String getDescripcionAlternativa()
    {
        return descripcionAlternativa;
    }

    public void setDescripcionAlternativa(String descripcionAlternativa)
    {
        this.descripcionAlternativa = descripcionAlternativa;
    }
}
