package es.uji.apps.goc.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrganoTemplate
{
    private static final String PRESIDENTE = "PR";
    private static final String SECRETARIO = "SE";

    private String id;
    private String nombre;
    private Boolean inactivo;

    private Long tipoOrganoId;
    private String tipoNombre;
    private String tipoCodigo;

    private List<MiembroTemplate> asistentes;
    private List<MiembroTemplate> ausentes;

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

    public OrganoTemplate()
    {
    }

    public OrganoTemplate(String id)
    {
        this.id = id;
    }

    public List<MiembroTemplate> getAsistentes()
    {
        return orderMiembros(asistentes);
    }

    public void setAsistentes(List<MiembroTemplate> asistentes)
    {
        this.asistentes = asistentes;
    }

    public Boolean getInactivo()
    {
        return inactivo;
    }

    public void setInactivo(Boolean inactivo)
    {
        this.inactivo = inactivo;
    }

    public List<MiembroTemplate> getAusentes()
    {
        return orderMiembros(ausentes);
    }

    public void setAusentes(List<MiembroTemplate> ausentes)
    {
        this.ausentes = ausentes;
    }

    private List<MiembroTemplate> orderMiembros(List<MiembroTemplate> miembros)
    {
        if (miembros == null) return null;

        List<MiembroTemplate> miembrosOrdenados = new ArrayList<>();

        miembros.sort((p1, p2) -> p1.getNombre().compareTo(p2.getNombre()));

        miembrosOrdenados.addAll(miembros.stream().filter(m -> m.getCargo().getCodigo().equalsIgnoreCase(PRESIDENTE)).collect(Collectors.toList()));
        miembrosOrdenados.addAll(miembros.stream().filter(m -> m.getCargo().getCodigo().equalsIgnoreCase(SECRETARIO)).collect(Collectors.toList()));
        miembrosOrdenados.addAll(miembros.stream().filter(m -> ! (m.getCargo().getCodigo().equalsIgnoreCase(SECRETARIO) || (m.getCargo().getCodigo().equalsIgnoreCase(PRESIDENTE)))).collect(Collectors.toList()));

        return miembrosOrdenados;
    }
}
