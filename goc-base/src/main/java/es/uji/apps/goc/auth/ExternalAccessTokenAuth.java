package es.uji.apps.goc.auth;

import es.uji.commons.sso.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

public class ExternalAccessTokenAuth implements Filter
{
    public static Logger log = LoggerFactory.getLogger(ExternalAccessTokenAuth.class);

    private FilterConfig filterConfig = null;

    public ExternalAccessTokenAuth()
    {
        super();
    }

    public void init(FilterConfig filterConfig) throws ServletException
    {
        this.filterConfig = filterConfig;
    }

    public void destroy()
    {
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        HttpServletRequest clientRequest = (HttpServletRequest) request;

        String url = clientRequest.getRequestURI();
        String headerAuthToken = clientRequest.getHeader("X-UJI-AuthToken");

        if (isCorrectExternalAPICall(url, headerAuthToken))
        {
            chain.doFilter(request, response);
            return;
        }

        registerDefaulLocalSession(clientRequest);
        chain.doFilter(request, response);
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

    private void registerDefaulLocalSession(HttpServletRequest clientRequest)
    {
        Long userId = Long.parseLong(this.filterConfig.getInitParameter("defaultUserId"));
        String userName = this.filterConfig.getInitParameter("defaultUserName");

        User user = createUserFromDefaulLocalValues(userId, userName);
        registerUserInHttpSession(clientRequest, user);
    }

    private User createUserFromDefaulLocalValues(Long userId, String userName)
    {
        User user = new User();
        user.setId(userId);
        user.setActiveSession(UUID.randomUUID().toString());
        user.setName(userName);

        return user;
    }

    private void registerUserInHttpSession(HttpServletRequest clientRequest, User user)
    {
        HttpSession serverSession = clientRequest.getSession();
        serverSession.setAttribute("www$persona", user);
    }
}
