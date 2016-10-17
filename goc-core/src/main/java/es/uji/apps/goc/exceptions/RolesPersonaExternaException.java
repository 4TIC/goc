package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class RolesPersonaExternaException extends CoreDataBaseException
{
    public RolesPersonaExternaException()
    {
        super("No se han podido obtener los roles de la persona");
    }

    public RolesPersonaExternaException(String message)
    {
        super(message);
    }
}
