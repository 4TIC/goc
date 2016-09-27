package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class OrganosExternosException extends CoreDataBaseException
{
    public OrganosExternosException()
    {
        super("No se ha podido descargar la lista de órganos externos");
    }

    public OrganosExternosException(String message)
    {
        super(message);
    }
}
