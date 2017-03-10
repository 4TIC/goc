package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class UrlGrabacionException extends CoreDataBaseException
{
    public UrlGrabacionException()
    {
        super("appI18N.reuniones.urlInvalida");
    }

    public UrlGrabacionException(String message)
    {
        super(message);
    }
}
