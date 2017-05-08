package es.uji.apps.goc.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import es.uji.apps.goc.model.Documento;

public class PuntoOrdenDiaFirma
{
    private Long id;

    private String titulo;

    @JsonProperty(value = "titulo_alternativo")
    private String tituloAlternativo;

    private String descripcion;

    @JsonProperty(value = "descripcion_alternativa")
    private String descripcionAlternativa;

    private Long orden;

    private String acuerdos;

    @JsonProperty(value = "acuerdos_alternativos")
    private String acuerdosAlternativos;

    private String deliberaciones;

    @JsonProperty(value = "deliberaciones_alternativas")
    private String deliberacionesAlternativas;

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
