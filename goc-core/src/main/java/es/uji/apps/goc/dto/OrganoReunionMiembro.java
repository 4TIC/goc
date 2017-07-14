package es.uji.apps.goc.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "GOC_ORGANOS_REUNIONES_MIEMBROS")
public class OrganoReunionMiembro implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;

    private String email;

    private Boolean asistencia;

    @Column(name = "ORGANO_ID")
    private String organoId;

    @Column(name = "ORGANO_EXTERNO")
    private Boolean organoExterno;

    @Column(name = "REUNION_ID")
    private Long reunionId;

    @Column(name = "MIEMBRO_ID")
    private String miembroId;

    @Column(name = "CARGO_ID")
    private String cargoId;

    @Column(name = "CARGO_NOMBRE")
    private String cargoNombre;

    @Column(name = "CARGO_NOMBRE_ALT")
    private String cargoNombreAlternativo;

    @Column(name = "SUPLENTE_ID")
    private Long suplenteId;

    @Column(name = "SUPLENTE_NOMBRE")
    private String suplenteNombre;

    @Column(name = "SUPLENTE_EMAIL")
    private String suplenteEmail;

    @Column(name = "ASISTENCIA_CONFIRMADA")
    private Boolean asistenciaConfirmada;

    @Column(name = "DELEGADO_VOTO_ID")
    private Long delegadoVotoId;

    @Column(name = "DELEGADO_VOTO_NOMBRE")
    private String delegadoVotoNombre;

    @Column(name = "DELEGADO_VOTO_EMAIL")
    private String delegadoVotoEmail;

    @ManyToOne
    @JoinColumn(name = "ORGANO_REUNION_ID")
    private OrganoReunion organoReunion;

    public OrganoReunionMiembro()
    {
    }

    public OrganoReunionMiembro(Long id)
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

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public OrganoReunion getOrganoReunion()
    {
        return organoReunion;
    }

    public void setOrganoReunion(OrganoReunion organoReunion)
    {
        this.organoReunion = organoReunion;
    }

    public Boolean isAsistencia()
    {
        return asistencia;
    }

    public void setAsistencia(Boolean asistencia)
    {
        this.asistencia = asistencia;
    }

    public Boolean getAsistencia()
    {
        return asistencia;
    }

    public String getOrganoId()
    {
        return organoId;
    }

    public void setOrganoId(String organoId)
    {
        this.organoId = organoId;
    }

    public Long getReunionId()
    {
        return reunionId;
    }

    public void setReunionId(Long reunionId)
    {
        this.reunionId = reunionId;
    }

    public Boolean getOrganoExterno()
    {
        return organoExterno;
    }

    public void setOrganoExterno(Boolean organoExterno)
    {
        this.organoExterno = organoExterno;
    }

    public String getMiembroId()
    {
        return miembroId;
    }

    public void setMiembroId(String miembroId)
    {
        this.miembroId = miembroId;
    }

    public String getCargoId()
    {
        return cargoId;
    }

    public void setCargoId(String cargoId)
    {
        this.cargoId = cargoId;
    }

    public String getCargoNombre()
    {
        return cargoNombre;
    }

    public void setCargoNombre(String cargoNombre)
    {
        this.cargoNombre = cargoNombre;
    }

    public Long getSuplenteId()
    {
        return suplenteId;
    }

    public void setSuplenteId(Long suplenteId)
    {
        this.suplenteId = suplenteId;
    }

    public String getSuplenteNombre()
    {
        return suplenteNombre;
    }

    public void setSuplenteNombre(String suplenteNombre)
    {
        this.suplenteNombre = suplenteNombre;
    }

    public Boolean getAsistenciaConfirmada()
    {
        return asistenciaConfirmada;
    }

    public void setAsistenciaConfirmada(Boolean asistenciaConfirmada)
    {
        this.asistenciaConfirmada = asistenciaConfirmada;
    }

    public String getSuplenteEmail()
    {
        return suplenteEmail;
    }

    public void setSuplenteEmail(String suplenteEmail)
    {
        this.suplenteEmail = suplenteEmail;
    }

    public String getCargoNombreAlternativo()
    {
        return cargoNombreAlternativo;
    }

    public void setCargoNombreAlternativo(String cargoNombreAlternativo)
    {
        this.cargoNombreAlternativo = cargoNombreAlternativo;
    }

    public Long getDelegadoVotoId()
    {
        return delegadoVotoId;
    }

    public void setDelegadoVotoId(Long delegadoVotoId)
    {
        this.delegadoVotoId = delegadoVotoId;
    }

    public String getDelegadoVotoNombre()
    {
        return delegadoVotoNombre;
    }

    public void setDelegadoVotoNombre(String delegadoVotoNombre)
    {
        this.delegadoVotoNombre = delegadoVotoNombre;
    }

    public String getDelegadoVotoEmail()
    {
        return delegadoVotoEmail;
    }

    public void setDelegadoVotoEmail(String delegadoVotoEmail)
    {
        this.delegadoVotoEmail = delegadoVotoEmail;
    }
}
