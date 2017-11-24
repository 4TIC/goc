package es.uji.apps.goc.notifications;

import java.text.SimpleDateFormat;

import es.uji.apps.goc.dto.PuntoOrdenDia;
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

    public String format(String publicUrl, String textoAux)
    {
        StringBuffer content = new StringBuffer();

        content.append("<h2>" + reunion.getAsunto() + "</h2>");

        if (textoAux != null)
        {
            content.append("<div>" + textoAux + "</div><br/>");
        }

        if (reunion.getNumeroSesion() != null)
        {
            content.append(
                    "<div><strong>Número de la sessió: </strong><span>" + reunion.getNumeroSesion() + "</span></div>");
        }

        if (reunion.getDescripcion() != null && !reunion.getDescripcion().isEmpty())
        {
            content.append("<div><strong>Descripció: </strong><span>" + reunion.getDescripcion() + "</span></div>");
        }

        if (reunion.getFechaSegundaConvocatoria() == null)
        {
            content.append("<div><strong>Data i hora: </strong>" + formatter.format(reunion.getFecha()) + "</div>");
        }
        else
        {
            content.append(
                    "<div><strong>Primera convocatòria: </strong>" + formatter.format(reunion.getFecha()) + "</div>");
            content.append("<div><strong>Segona convocatòria: </strong>" + formatter.format(
                    reunion.getFechaSegundaConvocatoria()) + "</div>");
        }

        if (reunion.getUbicacion() != null && !reunion.getUbicacion().isEmpty())
        {
            content.append("<div><strong>Ubicació: </strong>" + reunion.getUbicacion() + "</div>");
        }

        if (reunion.getDuracion() != null && reunion.getDuracion() > 0)
        {
            content.append("<div><strong>Duració: </strong>" + reunion.getDuracion() + " minuts</div>");
        }

        if (reunion.getReunionPuntosOrdenDia() != null && !reunion.getReunionPuntosOrdenDia().isEmpty())
        {
            content.append("<h4>Ordre del dia</h4>");
            content.append("<ol>");

            for (PuntoOrdenDia puntoOrdenDia : reunion.getReunionPuntosOrdenDia())
            {
                content.append("<li>" + puntoOrdenDia.getTitulo() + "</li>");
            }

            content.append("</ol>");
        }

        content.append(
                "<div>Per a més informació, podeu consultar el detall de la reunió a <a href=\"" + publicUrl + "/goc/rest/publicacion/reuniones/" + reunion
                        .getId() + "\">" + publicUrl + "/goc/rest/publicacion/reuniones/" + reunion.getId() + "</div>");

        return content.toString();
    }
}
