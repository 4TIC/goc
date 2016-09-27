package es.uji.apps.goc.notifications;

import es.uji.apps.goc.dto.Reunion;

import java.text.SimpleDateFormat;

public class ReunionFormatter
{
    private final SimpleDateFormat formatter;
    private Reunion reunion;

    public ReunionFormatter(Reunion reunion)
    {
        this.formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.reunion = reunion;
    }

    public String format()
    {
        StringBuffer content = new StringBuffer();

        content.append("<h2>" + reunion.getAsunto() + "</h2>");

        content.append("<div>" + reunion.getDescripcion() + "</div>");

        content.append("<div>");
        content.append("<strong>Fecha y hora:</strong> " + formatter.format(reunion.getFecha()) + "<br/>");
        content.append("<strong>Duración:</strong> " + reunion.getDuracion() + " minutos<br/>");
        content.append("</div>");

        return content.toString();
    }
}
