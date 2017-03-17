package es.uji.apps.goc.model;

public class DescriptorOrdenDia {

    private Long id;
    private Long idPuntoOrdenDia;
    private Long idReunion;
    private Long idDescriptor;
    private Long idClave;
    private String descriptor;
    private String clave;

    public DescriptorOrdenDia() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPuntoOrdenDia() {
        return idPuntoOrdenDia;
    }

    public void setIdPuntoOrdenDia(Long idPuntoOrdenDia) {
        this.idPuntoOrdenDia = idPuntoOrdenDia;
    }

    public Long getIdReunion() {
        return idReunion;
    }

    public void setIdReunion(Long idReunion) {
        this.idReunion = idReunion;
    }

    public Long getIdDescriptor() {
        return idDescriptor;
    }

    public void setIdDescriptor(Long idDescriptor) {
        this.idDescriptor = idDescriptor;
    }

    public Long getIdClave() {
        return idClave;
    }

    public void setIdClave(Long idClave) {
        this.idClave = idClave;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getClave() {
        return clave;
    }
}
