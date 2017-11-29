package es.uji.apps.goc.builders;

import java.util.Date;

import es.uji.apps.goc.dto.Reunion;

public class ReunionBuilder {

    private Reunion reunion;

    public ReunionBuilder() {
        this.reunion = new Reunion();
    }

    public ReunionBuilder withAsunto(String asunto){
        this.reunion.setAsunto(asunto);
        return this;
    }

    public ReunionBuilder withFecha(Date fecha){
        this.reunion.setFecha(fecha);
        return this;
    }

    public ReunionBuilder withDuracion(Long duracionMinutos){
        this.reunion.setDuracion(duracionMinutos);
        return this;
    }

    public ReunionBuilder withId(Long id){
        this.reunion.setId(id);
        return this;
    }

    public ReunionBuilder withUbicacion(String ubicacion){
        this.reunion.setUbicacion(ubicacion);
        return this;
    }

    public ReunionBuilder withCreadorNombre(String creadorNombre){
        this.reunion.setCreadorNombre(creadorNombre);
        return this;
    }

    public ReunionBuilder withCreadorEmail(String creadorEmail){
        this.reunion.setCreadorEmail(creadorEmail);
        return this;
    }

    public Reunion build(){
        return this.reunion;
    }
}
