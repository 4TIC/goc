package es.uji.apps.goc.notifications;

import java.util.List;

public class Mensaje
{
    private String asunto;
    private String contentType;
    private String cuerpo;
    private String from;
    private String replyTo;
    private List<String> destinos;
    private List<String> autorizados;
    private byte[] fileBase64;
    private String fileContentType;

    public String getAsunto()
    {
        return asunto;
    }

    public void setAsunto(String asunto)
    {
        this.asunto = asunto;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public String getCuerpo()
    {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo)
    {
        this.cuerpo = cuerpo;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getReplyTo()
    {
        return replyTo;
    }

    public void setReplyTo(String replyTo)
    {
        this.replyTo = replyTo;
    }

    public List<String> getDestinos()
    {
        return destinos;
    }

    public void setDestinos(List<String> destinos)
    {
        this.destinos = destinos;
    }

    public List<String> getAutorizados()
    {
        return autorizados;
    }

    public void setAutorizados(List<String> autorizados)
    {
        this.autorizados = autorizados;
    }

    public byte[] getFileBase64() {
        return fileBase64;
    }

    public void setFileBase64(byte[] fileBase64) {
        this.fileBase64 = fileBase64;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }
}