package es.uji.apps.goc.templates;

public interface Template
{
    void put(String key, Object value);

    byte[] process() throws Exception;
}