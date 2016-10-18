package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class ReunionNoAdmiteSuplenciaException extends CoreDataBaseException
{
    public ReunionNoAdmiteSuplenciaException()
    {
        super("La reunión no permite añadir suplentes");
    }

    public ReunionNoAdmiteSuplenciaException(String message)
    {
        super(message);
    }
}
