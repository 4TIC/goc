package es.uji.apps.goc.services.rest.ui;

import es.uji.apps.goc.model.Persona;

import java.util.List;

public class WrappedPersona
{
    private Boolean success;
    private Persona data;

    public Boolean getSuccess()
    {
        return success;
    }

    public void setSuccess(Boolean success)
    {
        this.success = success;
    }

    public Persona getData()
    {
        return data;
    }

    public void setData(Persona data)
    {
        this.data = data;
    }
}
