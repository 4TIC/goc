package es.uji.apps.goc.dto;

public class DescriptorTemplate
{
    private Long id;

    private Long reunionId;

    private Long puntoOrdenDiaId;

    private Long descriptorId;

    private Long claveId;

    private String descriptorNombre;

    private String descriptorDescripcion;

    private String claveNombre;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getReunionId()
    {
        return reunionId;
    }

    public void setReunionId(Long reunionId)
    {
        this.reunionId = reunionId;
    }

    public Long getPuntoOrdenDiaId()
    {
        return puntoOrdenDiaId;
    }

    public void setPuntoOrdenDiaId(Long puntoOrdenDiaId)
    {
        this.puntoOrdenDiaId = puntoOrdenDiaId;
    }

    public Long getDescriptorId()
    {
        return descriptorId;
    }

    public void setDescriptorId(Long descriptorId)
    {
        this.descriptorId = descriptorId;
    }

    public Long getClaveId()
    {
        return claveId;
    }

    public void setClaveId(Long claveId)
    {
        this.claveId = claveId;
    }

    public String getDescriptorNombre()
    {
        return descriptorNombre;
    }

    public void setDescriptorNombre(String descriptorNombre)
    {
        this.descriptorNombre = descriptorNombre;
    }

    public String getDescriptorDescripcion()
    {
        return descriptorDescripcion;
    }

    public void setDescriptorDescripcion(String descriptorDescripcion)
    {
        this.descriptorDescripcion = descriptorDescripcion;
    }

    public String getClaveNombre()
    {
        return claveNombre;
    }

    public void setClaveNombre(String claveNombre)
    {
        this.claveNombre = claveNombre;
    }
}
