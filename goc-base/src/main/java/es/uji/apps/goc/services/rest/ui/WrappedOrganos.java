package es.uji.apps.goc.services.rest.ui;

import es.uji.apps.goc.model.Organo;

import java.util.List;

public class WrappedOrganos
{
    private Boolean success;
    private List<Organo> data;

    public Boolean getSuccess()
    {
        return success;
    }

    public void setSuccess(Boolean success)
    {
        this.success = success;
    }

    public List<Organo> getData()
    {
        return data;
    }

    public void setData(List<Organo> data)
    {
        this.data = data;
    }
}
