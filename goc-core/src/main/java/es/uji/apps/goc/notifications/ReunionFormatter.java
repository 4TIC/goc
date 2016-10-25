package es.uji.apps.goc.notifications;

import java.text.SimpleDateFormat;

import es.uji.apps.goc.dto.Reunion;

public class ReunionFormatter
{
    private final SimpleDateFormat formatter;
    private Reunion reunion;

    public ReunionFormatter(Reunion reunion)
    {
        this.formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.reunion = reunion;
    }

    public String format()
    {
        StringBuffer content = new StringBuffer();

        content.append("<h2>" + reunion.getAsunto() + "</h2>");

        if (!reunion.getDescripcion().isEmpty())
        {
            content.append("<div>" + reunion.getDescripcion() + "</div>");
        }

        content.append("<div>");
        content.append("<strong>Fecha y hora:</strong> " + formatter.format(reunion.getFecha()) + "<br/>");
        content.append("<strong>Duración:</strong> " + reunion.getDuracion() + " minutos<br/>");
        content.append("<br/>Para más información, consultar el detalle de la reunión en <a href=\"http://devel.uji.es/goc/rest/publicacion/reuniones/" + reunion.getId() + "\">" + "http://localhost:9007/goc/rest/publicacion/reuniones/" + reunion.getId() + "<br/>");
        content.append("</div>");

        return content.toString();
    }
}
