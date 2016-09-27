package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class ReunionNoDisponibleException extends CoreDataBaseException
{
    public ReunionNoDisponibleException()
    {
        super("La reunión requerida no está disponible");
    }

    public ReunionNoDisponibleException(String message)
    {
        super(message);
    }
}
