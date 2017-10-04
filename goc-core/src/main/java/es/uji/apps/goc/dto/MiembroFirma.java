package es.uji.apps.goc.dto;

import es.uji.apps.goc.model.Cargo;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MiembroFirma
{
    private String id;

    private String nombre;

    private String email;

    private String suplente;
    private Long suplenteId;
    private Boolean asistencia;
    private Boolean asistenciaConfirmada;
    private String delegadoVoto;
    private Long delegadoVotoId;

    private Cargo cargo;

    private List<String> delegacionesDeVoto;
    private String nombresDelegacionesDeVoto;

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

    public MiembroFirma()
    {
    }

    public Cargo getCargo()
    {
        return cargo;
    }

    public void setCargo(Cargo cargo)
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
        return nombresDelegacionesDeVoto;
    }

    public void setNombresDelegacionesDeVoto(String nombresDelegacionesDeVoto)
    {
        this.nombresDelegacionesDeVoto = nombresDelegacionesDeVoto;
    }

    public void buildNombresDelegacionesDeVoto()
    {
        if (this.delegacionesDeVoto == null || this.delegacionesDeVoto.isEmpty())
        {
            nombresDelegacionesDeVoto = "";
            return;
        }

        nombresDelegacionesDeVoto = StringUtils.join(this.delegacionesDeVoto, ", ");
    }

    public Boolean getAsistencia()
    {
        return asistencia;
    }

    public void setAsistencia(Boolean asistencia)
    {
        this.asistencia = asistencia;
    }
}


