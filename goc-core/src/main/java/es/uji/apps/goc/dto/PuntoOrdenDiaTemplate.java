package es.uji.apps.goc.dto;

import java.util.List;

import es.uji.apps.goc.model.Documento;

public class PuntoOrdenDiaTemplate
{
    private Long id;

    private String titulo;

    private String descripcion;

    private Long orden;

    private String acuerdos;

    private String deliberaciones;

    private boolean publico;

    private List<DocumentoTemplate> documentos;

    private List<DocumentoTemplate> documentosAcuerdos;

    private List<DescriptorTemplate> descriptores;

    private String urlActa;

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

    public List<DocumentoTemplate> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<DocumentoTemplate> documentos) {
        this.documentos = documentos;
    }

    public boolean isPublico() {
        return publico;
    }

    public void setPublico(boolean publico) {
        this.publico = publico;
    }

    public List<DocumentoTemplate> getDocumentosAcuerdos()
    {
        return documentosAcuerdos;
    }

    public void setDocumentosAcuerdos(List<DocumentoTemplate> documentosAcuerdos)
    {
        this.documentosAcuerdos = documentosAcuerdos;
    }

    public List<DescriptorTemplate> getDescriptores()
    {
        return descriptores;
    }

    public void setDescriptores(List<DescriptorTemplate> descriptores)
    {
        this.descriptores = descriptores;
    }

    public String getUrlActa()
    {
        return urlActa;
    }

    public void setUrlActa(String urlActa)
    {
        this.urlActa = urlActa;
    }
}
