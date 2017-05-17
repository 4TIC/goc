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

        content.append("<h2>" + reunion.getAsuntoAlternativo() + "</h2>");

        if (reunion.getDescripcionAlternativa() != null && !reunion.getDescripcionAlternativa().isEmpty())
        {
            content.append("<div><strong>Descripció:</strong><span>" + reunion.getDescripcionAlternativa() + "</span></div>");
        }

        content.append("<div><strong>Data i hora:</strong> " + formatter.format(reunion.getFecha()) + "</div>");

        if (reunion.getFechaSegundaConvocatoria() != null)
        {
            content.append("<div><strong>Segona convocatòria:</strong> " + formatter.format(reunion.getFechaSegundaConvocatoria()) + "</div>");
        }

        content.append("<div><strong>Duració:</strong> " + reunion.getDuracion() + " minuts</div><br/>");
        content.append("<div>Per a més informació, podeu consultar el detall de la reunió a <a href=\"" + publicUrl + "/goc/rest/publicacion/reuniones/" + reunion.getId() + "\">" + publicUrl + "/goc/rest/publicacion/reuniones/" + reunion.getId() + "</div>");

        return content.toString();
    }
}
