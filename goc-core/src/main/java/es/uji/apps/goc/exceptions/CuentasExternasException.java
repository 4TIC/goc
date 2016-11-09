package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class CuentasExternasException extends CoreDataBaseException
{
    public CuentasExternasException()
    {
        super("No se ha podido consultar la persona que equivale a dicha cuenta");
    }

    public CuentasExternasException(String message)
    {
        super(message);
    }
}
