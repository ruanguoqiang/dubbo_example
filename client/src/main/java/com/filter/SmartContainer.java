package com.filter;


import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SmartContainer extends ParamFilter implements Filter {
    public Boolean isServer=false;
    private ClientFilter[] filters;

    private PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (isServer){
            ssoServerUrl="http://localhost:8082";
        }
        else if (StringUtils.isEmpty(ssoServerUrl)) {
            throw new IllegalArgumentException("ssoServerUrl不能为空");
        }
      /*  if (authenticationRpcService==null){

            try {
                authenticationRpcService = (AuthenticationRpcService) new HessianProxyFactory()
                        .create(AuthenticationRpcService.class, ssoServerUrl + "/rpc/authenticationRpcService");
            }
            catch (MalformedURLException e) {
                new IllegalArgumentException("authenticationRpcService初始化失败");
            }
        }*/
        if (filters == null || filters.length == 0) {
            throw new IllegalArgumentException("filters不能为空");
        }
        for (ClientFilter filter:filters){
            filter.setSsoServerUrl(ssoServerUrl);
            filter.setAuthenticationRpcService(authenticationRpcService);
            filter.init(filterConfig);
        }

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest=(HttpServletRequest)servletRequest;
        HttpServletResponse httpServletResponse=(HttpServletResponse)servletResponse;
        for (ClientFilter filter:filters){
            if (matchPath(filter.getPattern(), httpServletRequest.getServletPath())
                    &&!filter.isAccessAllowed(httpServletRequest,httpServletResponse)){
                return;
            }
            else{
                filterChain.doFilter(servletRequest,servletResponse);
            }

        }
    }
    private boolean matchPath(String pattern, String path) {
        return StringUtils.isEmpty(pattern) || pathMatcher.match(pattern, path);
    }

    public void setIsServer(boolean isServer) {
        this.isServer = isServer;
    }

    public ClientFilter[] getFilters() {
        return filters;
    }

    public void setFilters(ClientFilter[] filters) {
        this.filters = filters;
    }

    @Override
    public void destroy() {
       if (filters==null||filters.length==0){
           return;
       }
       for (ClientFilter filter: filters){
           filter.destroy();
       }
    }

}
