package es.uji.apps.goc.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthConfig
{
    @Value("${goc.saml.metadata.username}")
    public String userNameAttribute;
}