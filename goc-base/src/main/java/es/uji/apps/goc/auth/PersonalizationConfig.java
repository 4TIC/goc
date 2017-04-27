package es.uji.apps.goc.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PersonalizationConfig
{
    @Value("${goc.logo}")
    public String logo;

    @Value("${goc.customCSS:}")
    public String customCSS;

    @Value("${goc.rolAdministrador:ADMIN}")
    public String rolAdministrador;

    @Value("${goc.rolUsuario:USUARIO}")
    public String rolUsuario;

    @Value("${goc.rolGestor:GESTOR}")
    public String rolGestor;

    @Value("${goc.disconnectLink:#}")
    public String disconnectLink;
}
