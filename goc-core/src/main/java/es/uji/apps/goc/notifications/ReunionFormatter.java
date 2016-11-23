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

    public String format(String publicUrl)
    {
        StringBuffer content = new StringBuffer();

        content.append("<h2>" + reunion.getAsunto() + "</h2>");

        if (reunion.getDescripcion() != null && !reunion.getDescripcion().isEmpty())
        {
            content.append("<div><strong>Descripción:</strong><span>" + reunion.getDescripcion() + "</span></div>");
        }

        content.append("<div><strong>Fecha y hora:</strong> " + formatter.format(reunion.getFecha()) + "</div>");

        if (reunion.getFechaSegundaConvocatoria() != null)
        {
            content.append("<div><strong>Segunda convocatoria:</strong> " + formatter.format(reunion.getFechaSegundaConvocatoria()) + "</div>");
        }

        content.append("<div><strong>Duración:</strong> " + reunion.getDuracion() + " minutos</div><br/>");
        content.append("<div>Para más información, consultar el detalle de la reunión en <a href=\"" + publicUrl + "/goc/rest/publicacion/reuniones/" + reunion.getId() + "\">" + publicUrl + "/goc/rest/publicacion/reuniones/" + reunion.getId() + "</div>");

        return content.toString();
    }
}
