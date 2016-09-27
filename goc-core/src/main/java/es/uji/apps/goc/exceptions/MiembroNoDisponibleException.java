package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class MiembroNoDisponibleException extends CoreDataBaseException
{
    public MiembroNoDisponibleException()
    {
        super("El miembro requerido no está disponible");
    }

    public MiembroNoDisponibleException(String message)
    {
        super(message);
    }
}
