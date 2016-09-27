package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class UrlGrabacionException extends CoreDataBaseException
{
    public UrlGrabacionException()
    {
        super("La URL de gravació de la reunió no té un format vàlid.");
    }

    public UrlGrabacionException(String message)
    {
        super(message);
    }
}
