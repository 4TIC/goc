package es.uji.apps.goc.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultUser
{
    @Value("${uji.deploy.defaultUserName}")
    public String defaultUsername;

    @Value("${uji.deploy.defaultUserId}")
    public String defaultUserId;
}
