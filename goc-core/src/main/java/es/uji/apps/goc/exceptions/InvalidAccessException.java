package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class InvalidAccessException extends CoreDataBaseException
{
    public InvalidAccessException(String message)
    {
        super(message);
    }
}
