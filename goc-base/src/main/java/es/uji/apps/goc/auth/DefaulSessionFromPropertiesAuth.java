package es.uji.apps.goc.auth;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import es.uji.commons.sso.User;

public class DefaulSessionFromPropertiesAuth implements Filter
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

        String url = clientRequest.getRequestURI();
        String headerAuthToken = clientRequest.getHeader("X-UJI-AuthToken");

        if (isCorrectExternalAPICall(url, headerAuthToken) ||
            sessionAlreadyRegistered(clientRequest) ||
            isForbidenPage(url))
        {
            filterChain.doFilter(request, response);
            return;
        }

        DefaultUser defaultUser = getDefaultUser();

        String userName = defaultUser.defaultUsername;
        String userId = defaultUser.defaultUserId;

        User user = createUserFromDefaulLocalValues(userName, userId);
        registerUserInHttpSession(clientRequest, user);

        filterChain.doFilter(request, response);
    }

    private boolean isForbidenPage(String url)
    {
        return (url != null && url.startsWith("/goc/forbidden.jsp"));
    }

    private boolean sessionAlreadyRegistered(HttpServletRequest clientRequest)
    {
        return clientRequest.getSession().getAttribute(User.SESSION_USER) != null;
    }

    private User createUserFromDefaulLocalValues(String userName, String userId)
            throws IOException
    {
        try
        {
            User user = new User();
            user.setId(Long.parseLong(userId));
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
        serverSession.setAttribute(User.SESSION_USER, user);
    }

    private DefaultUser getDefaultUser()
    {
        WebApplicationContext context = WebApplicationContextUtils
                .getWebApplicationContext(filterConfig.getServletContext());
        return context.getBean(DefaultUser.class);
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