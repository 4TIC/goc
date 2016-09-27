package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class AsistenteNoEncontradoException extends CoreDataBaseException
{
    public AsistenteNoEncontradoException()
    {
        super("El assistent a la reunió no s'ha pogut trobar");
    }

    public AsistenteNoEncontradoException(String message)
    {
        super(message);
    }
}
