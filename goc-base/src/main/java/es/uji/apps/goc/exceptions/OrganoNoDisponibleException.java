package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class OrganoNoDisponibleException extends CoreDataBaseException
{
    public OrganoNoDisponibleException()
    {
        super("El órgano requerido no está disponible");
    }

    public OrganoNoDisponibleException(String message)
    {
        super(message);
    }
}
