package es.uji.apps.goc.notifications;

public class CanNotSendException extends Exception
{
    public CanNotSendException()
    {
        this(null);
    }

    public CanNotSendException(Throwable e)
    {
        super("The message could not be sent", e);
    }
}