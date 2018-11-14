package com.filter;


import com.qiangge.interf.AuthenticationRpcService;
import org.springframework.beans.factory.annotation.Autowired;

public class ParamFilter {
    public String ssoServerUrl;

    @Autowired
    protected AuthenticationRpcService authenticationRpcService;

    public void setSsoServerUrl(String ssoServerUrl) {
        this.ssoServerUrl = ssoServerUrl;
    }

    public AuthenticationRpcService getAuthenticationRpcService() {
        return authenticationRpcService;
    }

    public void setAuthenticationRpcService(AuthenticationRpcService authenticationRpcService) {
        this.authenticationRpcService = authenticationRpcService;
    }
}
