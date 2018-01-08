package es.uji.apps.goc.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import es.uji.apps.goc.config.CustomRoutingDataSource;

public class MultiClienteDefaultSessionFromPropertiesAuth implements Filter
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

        CustomRoutingDataSource.setDataSourceKey(clientRequest.getServerName());

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy()
    {
    }
}