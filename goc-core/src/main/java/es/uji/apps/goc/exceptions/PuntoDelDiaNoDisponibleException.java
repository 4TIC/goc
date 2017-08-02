package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class PuntoDelDiaNoDisponibleException extends CoreDataBaseException
{
    public PuntoDelDiaNoDisponibleException()
    {
        super("El punto del día no está disponible");
    }

    public PuntoDelDiaNoDisponibleException(String message)
    {
        super(message);
    }
}
