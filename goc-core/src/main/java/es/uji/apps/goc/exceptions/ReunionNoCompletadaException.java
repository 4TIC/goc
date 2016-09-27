package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class ReunionNoCompletadaException extends CoreDataBaseException
{
    public ReunionNoCompletadaException()
    {
        super("La reunió encara no ha finalitzat.");
    }

    public ReunionNoCompletadaException(String message)
    {
        super(message);
    }
}
