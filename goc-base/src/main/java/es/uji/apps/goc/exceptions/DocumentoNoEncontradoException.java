package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class DocumentoNoEncontradoException extends CoreDataBaseException
{
    public DocumentoNoEncontradoException()
    {
        super("El document no s'ha pogut trobar");
    }

    public DocumentoNoEncontradoException(String message)
    {
        super(message);
    }
}
