package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class OrganoConvocadoNoPermitidoException extends CoreDataBaseException
{
    public OrganoConvocadoNoPermitidoException()
    {
        super("No té permissos suficients per a convocar el òrgan sol·licitat");
    }

    public OrganoConvocadoNoPermitidoException(String message)
    {
        super(message);
    }
}
