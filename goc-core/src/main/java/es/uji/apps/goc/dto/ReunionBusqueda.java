package es.uji.apps.goc.dto;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "GOC_VW_REUNIONES_BUSQUEDA")
public class ReunionBusqueda implements Serializable
{
    @Id
    private Long id;

    @JoinColumn(name = "ID")
    @OneToOne
    @MapsId
    private Reunion reunion;

    @Column(name = "ASUNTO_REUNION_BUSQ")
    private String asuntoReunion;

    @Column(name = "ASUNTO_ALT_REUNION_BUSQ")
    private String asuntoAlternativoReunion;

    @Column(name = "DESCRIPCION_REUNION_BUSQ")
    private String descripcionReunion;

    @Column(name = "DESCRIPCION_ALT_REUNION_BUSQ")
    private String descripcionAlternativaReunion;

    @Column(name = "TITULO_PUNTO_BUSQ")
    private String tituloPunto;

    @Column(name = "DESCRIPCION_PUNTO_BUSQ")
    private String descripcionPunto;

    @Column(name = "ACUERDOS_PUNTO_BUSQ")
    private String acuerdosPunto;

    @Column(name = "DELIBERACIONES_PUNTO_BUSQ")
    private String deliberacionesPunto;

    @Column(name = "TITULO_ALT_PUNTO_BUSQ")
    private String tituloAlternativoPunto;

    @Column(name = "DESCRIPCION_ALT_PUNTO_BUSQ")
    private String descripcionAlternativaPunto;

    @Column(name = "ACUERDOS_ALT_PUNTO_BUSQ")
    private String acuerdosAlternativosPunto;

    @Column(name = "DELIBERACIONES_ALT_PUNTO_BUSQ")
    private String deliberacionesAlternativasPunto;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public ReunionBusqueda()
    {
    }

    public String getAsuntoReunion()
    {
        return asuntoReunion;
    }

    public void setAsuntoReunion(String asuntoReunion)
    {
        this.asuntoReunion = asuntoReunion;
    }

    public String getAsuntoAlternativoReunion()
    {
        return asuntoAlternativoReunion;
    }

    public void setAsuntoAlternativoReunion(String asuntoAlternativoReunion)
    {
        this.asuntoAlternativoReunion = asuntoAlternativoReunion;
    }

    public String getDescripcionReunion()
    {
        return descripcionReunion;
    }

    public void setDescripcionReunion(String descripcionReunion)
    {
        this.descripcionReunion = descripcionReunion;
    }

    public String getDescripcionAlternativaReunion()
    {
        return descripcionAlternativaReunion;
    }

    public void setDescripcionAlternativaReunion(String descripcionAlternativaReunion)
    {
        this.descripcionAlternativaReunion = descripcionAlternativaReunion;
    }

    public Reunion getReunion()
    {
        return reunion;
    }

    public void setReunion(Reunion reunion)
    {
        this.reunion = reunion;
    }

    public String getTituloPunto()
    {
        return tituloPunto;
    }

    public void setTituloPunto(String tituloPunto)
    {
        this.tituloPunto = tituloPunto;
    }

    public String getDescripcionPunto()
    {
        return descripcionPunto;
    }

    public void setDescripcionPunto(String descripcionPunto)
    {
        this.descripcionPunto = descripcionPunto;
    }

    public String getAcuerdosPunto()
    {
        return acuerdosPunto;
    }

    public void setAcuerdosPunto(String acuerdosPunto)
    {
        this.acuerdosPunto = acuerdosPunto;
    }

    public String getDeliberacionesPunto()
    {
        return deliberacionesPunto;
    }

    public void setDeliberacionesPunto(String deliberacionesPunto)
    {
        this.deliberacionesPunto = deliberacionesPunto;
    }

    public String getTituloAlternativoPunto()
    {
        return tituloAlternativoPunto;
    }

    public void setTituloAlternativoPunto(String tituloAlternativoPunto)
    {
        this.tituloAlternativoPunto = tituloAlternativoPunto;
    }

    public String getDescripcionAlternativaPunto()
    {
        return descripcionAlternativaPunto;
    }

    public void setDescripcionAlternativaPunto(String descripcionAlternativaPunto)
    {
        this.descripcionAlternativaPunto = descripcionAlternativaPunto;
    }

    public String getAcuerdosAlternativosPunto()
    {
        return acuerdosAlternativosPunto;
    }

    public void setAcuerdosAlternativosPunto(String acuerdosAlternativosPunto)
    {
        this.acuerdosAlternativosPunto = acuerdosAlternativosPunto;
    }

    public String getDeliberacionesAlternativasPunto()
    {
        return deliberacionesAlternativasPunto;
    }

    public void setDeliberacionesAlternativasPunto(String deliberacionesAlternativasPunto)
    {
        this.deliberacionesAlternativasPunto = deliberacionesAlternativasPunto;
    }
}


       