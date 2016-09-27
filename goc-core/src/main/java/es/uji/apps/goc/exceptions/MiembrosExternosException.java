package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class MiembrosExternosException extends CoreDataBaseException
{
    public MiembrosExternosException()
    {
        super("No se ha podido descargar la lista de miembros externos");
    }

    public MiembrosExternosException(String message)
    {
        super(message);
    }
}
