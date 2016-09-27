package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class NotificacionesException extends CoreDataBaseException
{
    public NotificacionesException()
    {
        super("No se ha podido enviar la notificación de aviso");
    }

    public NotificacionesException(String message)
    {
        super(message);
    }
}
