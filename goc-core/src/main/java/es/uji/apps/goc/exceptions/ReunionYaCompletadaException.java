package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class ReunionYaCompletadaException extends CoreDataBaseException
{
    public ReunionYaCompletadaException()
    {
        super("Els acords de la reunió ya s'han establert previament");
    }

    public ReunionYaCompletadaException(String message)
    {
        super(message);
    }
}
