package es.uji.apps.goc.auth;

import es.uji.apps.goc.integrations.CuentasClient;
import es.uji.commons.sso.User;
import es.uji.commons.sso.dao.SessionDAO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml.SAMLCredential;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

public class SpringSecurityAuth implements Filter
{
    private FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException
    {
        HttpServletRequest clientRequest = (HttpServletRequest) request;
        HttpServletResponse clientResponse = (HttpServletResponse) response;

        String url = clientRequest.getRequestURI();
        String headerAuthToken = clientRequest.getHeader("X-UJI-AuthToken");

        if (isCorrectExternalAPICall(url, headerAuthToken) ||
            sessionAlreadyRegistered(clientRequest) ||
            isForbidenPage(url))
        {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null)
        {
            clientResponse.sendRedirect("/goc/forbidden.jsp");
            filterChain.doFilter(request, response);
            return;
        }

        SAMLCredential credential = (SAMLCredential) authentication.getCredentials();

        AuthConfig authConfig = getAuthConfig();

        String userName = credential.getAttributeAsString(authConfig.userNameAttribute);
        User user = createUserFromDefaulLocalValues(userName);
        registerUserInHttpSession(clientRequest, user);

        filterChain.doFilter(request, response);
    }

    private boolean isForbidenPage(String url)
    {
        return (url != null && url.startsWith("/goc/forbidden.jsp"));
    }

    private boolean sessionAlreadyRegistered(HttpServletRequest clientRequest)
    {
        return clientRequest.getSession().getAttribute("www$persona") != null;
    }

    private User createUserFromDefaulLocalValues(String userName)
            throws IOException
    {
        try
        {
            User user = new User();
            user.setId(getCuentasClient().obtainPersonaIdFrom(userName));
            user.setName(userName);
            user.setActiveSession(UUID.randomUUID().toString());

            return user;
        }
        catch (Exception e)
        {
            throw new IOException("No se ha podido recuperar el id del usuario conectado");
        }
    }

    private void registerUserInHttpSession(HttpServletRequest clientRequest, User user)
    {
        HttpSession serverSession = clientRequest.getSession();
        serverSession.setAttribute("www$persona", user);
    }

    private SessionDAO getSessionDAO()
    {
        WebApplicationContext context = WebApplicationContextUtils
                .getWebApplicationContext(filterConfig.getServletContext());
        return context.getBean(SessionDAO.class);
    }

    private CuentasClient getCuentasClient()
    {
        WebApplicationContext context = WebApplicationContextUtils
                .getWebApplicationContext(filterConfig.getServletContext());
        return context.getBean(CuentasClient.class);
    }

    private AuthConfig getAuthConfig()
    {
        WebApplicationContext context = WebApplicationContextUtils
                .getWebApplicationContext(filterConfig.getServletContext());
        return context.getBean(AuthConfig.class);
    }

    private boolean isCorrectExternalAPICall(String url, String headerAuthToken)
    {
        if (url.startsWith("/goc/rest/external"))
        {
            if (headerAuthToken != null)
            {
                String token = filterConfig.getInitParameter("authToken");

                if (token != null && headerAuthToken.equals(token))
                {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void destroy()
    {

    }
}
