package es.uji.apps.goc.model;

import com.mysema.query.types.expr.BooleanExpression;

import java.util.Date;

public class AcuerdosSearch
{
    private Integer startSearch;
    private Integer numResults;
    private Integer anyo;
    private Long tipoOrganoId;
    private Long organoId;
    private Long descriptorId;
    private Long claveId;
    private Date fInicio;
    private Date fFin;
    private String texto;
    private Boolean idiomaAlternatico;

    public AcuerdosSearch(Integer anyo, Integer startSearch, Integer numResults)
    {
        this.anyo = anyo;
        this.startSearch = startSearch;
        this.numResults = numResults;
        this.idiomaAlternatico = false;
    }

    public Integer getStartSearch()
    {
        return startSearch;
    }

    public void setStartSearch(Integer startSearch)
    {
        this.startSearch = startSearch;
    }

    public Integer getNumResults()
    {
        return numResults;
    }

    public void setNumResults(Integer numResults)
    {
        this.numResults = numResults;
    }

    public Integer getAnyo()
    {
        return anyo;
    }

    public void setAnyo(Integer anyo)
    {
        this.anyo = anyo;
    }

    public Long getTipoOrganoId()
    {
        return tipoOrganoId;
    }

    public void setTipoOrganoId(Long tipoOrganoId)
    {
        this.tipoOrganoId = tipoOrganoId;
    }

    public Long getOrganoId()
    {
        return organoId;
    }

    public void setOrganoId(Long organoId)
    {
        this.organoId = organoId;
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

    public Date getfInicio()
    {
        return fInicio;
    }

    public void setfInicio(Date fInicio)
    {
        this.fInicio = fInicio;
    }

    public Date getfFin()
    {
        return fFin;
    }

    public void setfFin(Date fFin)
    {
        this.fFin = fFin;
    }

    public String getTexto()
    {
        return texto;
    }

    public void setTexto(String texto)
    {
        this.texto = texto;
    }

    public Boolean getIdiomaAlternatico()
    {
        return idiomaAlternatico;
    }

    public void setIdiomaAlternatico(Boolean idiomaAlternatico)
    {
        this.idiomaAlternatico = idiomaAlternatico;
    }
}
