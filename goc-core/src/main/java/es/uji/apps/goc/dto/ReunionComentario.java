package es.uji.apps.goc.dto;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "GOC_REUNIONES_COMENTARIOS")
public class ReunionComentario implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String comentario;

    @ManyToOne
    @JoinColumn(name = "REUNION_ID")
    private Reunion reunion;

    @Column(name="FECHA")
    private Date fecha;

    @Column(name="CREADOR_ID")
    private Long creadorId;

    @Column(name="CREADOR_NOMBRE")
    private String creadorNombre;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getComentario()
    {
        return comentario;
    }

    public void setComentario(String comentario)
    {
        this.comentario = comentario;
    }

    public Reunion getReunion()
    {
        return reunion;
    }

    public void setReunion(Reunion reunion)
    {
        this.reunion = reunion;
    }

    public Date getFecha()
    {
        return fecha;
    }

    public void setFecha(Date fecha)
    {
        this.fecha = fecha;
    }

    public Long getCreadorId()
    {
        return creadorId;
    }

    public void setCreadorId(Long creadorId)
    {
        this.creadorId = creadorId;
    }

    public String getCreadorNombre()
    {
        return creadorNombre;
    }

    public void setCreadorNombre(String creadorNombre)
    {
        this.creadorNombre = creadorNombre;
    }
}
