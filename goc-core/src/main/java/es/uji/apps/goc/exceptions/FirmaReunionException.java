package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class FirmaReunionException extends CoreDataBaseException
{
    public FirmaReunionException()
    {
        super("No s'ha pogut firmar la reunió");
    }

    public FirmaReunionException(String message)
    {
        super(message);
    }
}
