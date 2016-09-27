package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class PuntoOrdenDiaNoDisponibleException extends CoreDataBaseException
{
    public PuntoOrdenDiaNoDisponibleException()
    {
        super("El punto del orden del día requerido no está disponible");
    }

    public PuntoOrdenDiaNoDisponibleException(String message)
    {
        super(message);
    }
}
