package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class ReunionNoAdmiteDelegacionVotoException extends CoreDataBaseException
{
    public ReunionNoAdmiteDelegacionVotoException()
    {
        super("La reunión no permite añadir delegación de voto");
    }

    public ReunionNoAdmiteDelegacionVotoException(String message)
    {
        super(message);
    }
}
