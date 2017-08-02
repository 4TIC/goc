package es.uji.apps.goc.exceptions;

import es.uji.commons.rest.exceptions.CoreDataBaseException;

@SuppressWarnings("serial")
public class PuntoDelDiaNoTieneAcuerdosException extends CoreDataBaseException
{
    public PuntoDelDiaNoTieneAcuerdosException()
    {
        super("El punto del día no tiene acuerdos");
    }

    public PuntoDelDiaNoTieneAcuerdosException(String message)
    {
        super(message);
    }
}
