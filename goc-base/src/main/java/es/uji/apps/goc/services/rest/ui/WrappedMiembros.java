package es.uji.apps.goc.services.rest.ui;

import es.uji.apps.goc.model.Miembro;
import es.uji.apps.goc.model.Organo;

import java.util.List;

public class WrappedMiembros
{
    private Boolean success;
    private List<Miembro> data;

    public Boolean getSuccess()
    {
        return success;
    }

    public void setSuccess(Boolean success)
    {
        this.success = success;
    }

    public List<Miembro> getData()
    {
        return data;
    }

    public void setData(List<Miembro> data)
    {
        this.data = data;
    }
}
