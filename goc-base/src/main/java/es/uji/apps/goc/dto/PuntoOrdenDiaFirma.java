package es.uji.apps.goc.dto;

import java.util.List;

import es.uji.apps.goc.model.Documento;

public class PuntoOrdenDiaFirma
{
    private Long id;

    private String titulo;

    private String descripcion;

    private Long orden;

    private String acuerdos;

    private String deliberaciones;

    private List<Documento> documentos;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getTitulo()
    {
        return titulo;
    }

    public void setTitulo(String titulo)
    {
        this.titulo = titulo;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    public Long getOrden()
    {
        return orden;
    }

    public void setOrden(Long orden)
    {
        this.orden = orden;
    }

    public String getAcuerdos()
    {
        return acuerdos;
    }

    public void setAcuerdos(String acuerdos)
    {
        this.acuerdos = acuerdos;
    }

    public String getDeliberaciones()
    {
        return deliberaciones;
    }

    public void setDeliberaciones(String deliberaciones)
    {
        this.deliberaciones = deliberaciones;
    }

    public List<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<Documento> documentos) {
        this.documentos = documentos;
    }
}
