package es.uji.apps.goc.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrganoFirma
{
    private static final String PRESIDENTE = "PR";
    private static final String SECRETARIO = "SE";

    private String id;
    private String nombre;

    @JsonProperty("nombre_alternativo")
    private String nombreAlternativo;

    private Boolean inactivo;

    @JsonProperty("tipo_organo_id")
    private Long tipoOrganoId;

    @JsonProperty("tipo_nombre")
    private String tipoNombre;

    @JsonProperty("tipo_nombre_alternativo")
    private String tipoNombreAlternativo;

    @JsonProperty("tipo_codigo")
    private String tipoCodigo;

    private List<MiembroFirma> asistentes;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public Long getTipoOrganoId()
    {
        return tipoOrganoId;
    }

    public void setTipoOrganoId(Long tipoOrganoId)
    {
        this.tipoOrganoId = tipoOrganoId;
    }

    public String getTipoNombre()
    {
        return tipoNombre;
    }

    public void setTipoNombre(String tipoNombre)
    {
        this.tipoNombre = tipoNombre;
    }

    public String getTipoCodigo()
    {
        return tipoCodigo;
    }

    public void setTipoCodigo(String tipoCodigo)
    {
        this.tipoCodigo = tipoCodigo;
    }

    public OrganoFirma()
    {
    }

    public OrganoFirma(String id)
    {
        this.id = id;
    }

    public List<MiembroFirma> getAsistentes()
    {
        return orderMiembros(asistentes);
    }

    public void setAsistentes(List<MiembroFirma> asistentes) {
        this.asistentes = asistentes;
    }

    public Boolean getInactivo() {
        return inactivo;
    }

    public void setInactivo(Boolean inactivo) {
        this.inactivo = inactivo;
    }

    public String getNombreAlternativo()
    {
        return nombreAlternativo;
    }

    public void setNombreAlternativo(String nombreAlternativo)
    {
        this.nombreAlternativo = nombreAlternativo;
    }

    public String getTipoNombreAlternativo()
    {
        return tipoNombreAlternativo;
    }

    public void setTipoNombreAlternativo(String tipoNombreAlternativo)
    {
        this.tipoNombreAlternativo = tipoNombreAlternativo;
    }

    private List<MiembroFirma> orderMiembros(List<MiembroFirma> miembros)
    {
        if (miembros == null) return null;

        List<MiembroFirma> miembrosOrdenados = new ArrayList<>();

        miembros.sort((p1, p2) -> p1.getNombre().compareTo(p2.getNombre()));

        miembrosOrdenados.addAll(miembros.stream().filter(m -> m.getCargo().getCodigo().equalsIgnoreCase(PRESIDENTE)).collect(
                Collectors.toList()));
        miembrosOrdenados.addAll(miembros.stream().filter(m -> m.getCargo().getCodigo().equalsIgnoreCase(SECRETARIO)).collect(Collectors.toList()));
        miembrosOrdenados.addAll(miembros.stream().filter(m -> ! (m.getCargo().getCodigo().equalsIgnoreCase(SECRETARIO) || (m.getCargo().getCodigo().equalsIgnoreCase(PRESIDENTE)))).collect(Collectors.toList()));

        return miembrosOrdenados;
    }

}
