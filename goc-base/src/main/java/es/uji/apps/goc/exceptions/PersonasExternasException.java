package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class PersonasExternasException extends CoreDataBaseException
{
    public PersonasExternasException()
    {
        super("No se ha podido consultar el directorio de personas");
    }

    public PersonasExternasException(String message)
    {
        super(message);
    }
}
