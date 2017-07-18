package es.uji.apps.goc.dto;

import es.uji.apps.goc.model.Cargo;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MiembroTemplate
{
    private String id;

    private String nombre;

    private String email;

    private String suplente;
    private Long suplenteId;
    private String delegadoVoto;
    private Long delegadoVotoId;
    private String miembroId;

    private Boolean asistenciaConfirmada;
    private Boolean asistencia;

    private CargoTemplate cargo;

    private List<String> delegacionesDeVoto;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
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

    public MiembroTemplate()
    {
    }

    public CargoTemplate getCargo()
    {
        return cargo;
    }

    public void setCargo(CargoTemplate cargo)
    {
        this.cargo = cargo;
    }

    public String getSuplente()
    {
        return suplente;
    }

    public void setSuplente(String suplente)
    {
        this.suplente = suplente;
    }

    public Long getSuplenteId()
    {
        return suplenteId;
    }

    public void setSuplenteId(Long suplenteId)
    {
        this.suplenteId = suplenteId;
    }

    public Boolean getAsistenciaConfirmada()
    {
        return asistenciaConfirmada;
    }

    public void setAsistenciaConfirmada(Boolean asistenciaConfirmada)
    {
        this.asistenciaConfirmada = asistenciaConfirmada;
    }

    public String getMiembroId()
    {
        return miembroId;
    }

    public void setMiembroId(String miembroId)
    {
        this.miembroId = miembroId;
    }

    public Boolean getAsistencia()
    {
        return asistencia;
    }

    public void setAsistencia(Boolean asistencia)
    {
        this.asistencia = asistencia;
    }

    public String getDelegadoVoto()
    {
        return delegadoVoto;
    }

    public void setDelegadoVoto(String delegadoVoto)
    {
        this.delegadoVoto = delegadoVoto;
    }

    public Long getDelegadoVotoId()
    {
        return delegadoVotoId;
    }

    public void setDelegadoVotoId(Long delegadoVotoId)
    {
        this.delegadoVotoId = delegadoVotoId;
    }

    public List<String> getDelegacionesDeVoto()
    {
        return delegacionesDeVoto;
    }

    public void setDelegacionesDeVoto(List<String> delegacionesDeVoto)
    {
        this.delegacionesDeVoto = delegacionesDeVoto;
    }

    public void addDelegacionDeVoto(String delegadoVoto)
    {
        if (this.delegacionesDeVoto == null) delegacionesDeVoto = new ArrayList<>();

        delegacionesDeVoto.add(delegadoVoto);
    }

    public String getNombresDelegacionesDeVoto()
    {
        if (this.delegacionesDeVoto == null || this.delegacionesDeVoto.isEmpty()) return "";

        return StringUtils.join(this.delegacionesDeVoto, ", ");
    }
}
