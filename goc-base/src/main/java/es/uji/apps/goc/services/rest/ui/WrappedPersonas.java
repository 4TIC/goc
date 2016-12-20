package es.uji.apps.goc.services.rest.ui;

import es.uji.apps.goc.model.Miembro;
import es.uji.apps.goc.model.Persona;

import java.util.List;

public class WrappedPersonas
{
    private Boolean success;
    private List<Persona> data;

    public Boolean getSuccess()
    {
        return success;
    }

    public void setSuccess(Boolean success)
    {
        this.success = success;
    }

    public List<Persona> getData()
    {
        return data;
    }

    public void setData(List<Persona> data)
    {
        this.data = data;
    }
}
