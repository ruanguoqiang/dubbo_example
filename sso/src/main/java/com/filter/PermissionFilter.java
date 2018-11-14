package com.filter;

import com.config.SessionUtils;
import com.config.StringUtils;
import com.common.model.RpcPermission;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class PermissionFilter extends ClientFilter {
    private String ssoAppCode;

    private static Set<String> sessionPermissionCache=new CopyOnWriteArraySet<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        if (StringUtils.isBlank(ssoAppCode)){
            throw new IllegalArgumentException("ssoAppCode不能为空");
        }
        ApplicationPermission.initApplicationPermissions(authenticationRpcService,ssoAppCode);
    }

    @Override
    public boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path=request.getServletPath();
        if (isPermitted(request, path))
            return true;
        else if (!ApplicationPermission.getApplicationPermissionSet().contains(path))
            return true;
        else {
            responseJson(response, SsoResultCode.SSO_PERMISSION_ERROR, "没有访问权限");
            return false;
        }
    }
    private boolean isPermitted(HttpServletRequest request, String path) {
        Set<String> permissionSet=getLocalPermissionSet(request);
        return permissionSet.contains(path);
    }
    private Set<String> getLocalPermissionSet(HttpServletRequest request) {
        SessionPermission sessionPermission= SessionUtils.getSessionPermission(request);
        String token=SessionUtils.getSessionUser(request).getToken();
        if (StringUtils.isBlank(token)||!sessionPermissionCache.contains(token)){
            sessionPermission=invokePermissionInSession(request,token);
        }
        return sessionPermission.getPermissionSet();
    }
    public SessionPermission invokePermissionInSession(HttpServletRequest request, String token) {
        List<RpcPermission> dbList=authenticationRpcService.findPermissionList(token,ssoAppCode);
        List<RpcPermission> menuList = new ArrayList<RpcPermission>();
        Set<String> operateSet = new HashSet<String>();
        for (RpcPermission rpcPermission:dbList){
             if (rpcPermission.getIsMenu()){
                 menuList.add(rpcPermission);
             }
             if (rpcPermission.getUrl()!=null){
                 operateSet.add(rpcPermission.getUrl());
             }
       }
        SessionPermission sessionPermission = new SessionPermission();
       Set<String> noPermissionSet=new HashSet<String>(ApplicationPermission.getApplicationPermissionSet());
        noPermissionSet.removeAll(operateSet);
        sessionPermission.setNoPermissions(org.springframework.util.StringUtils.arrayToDelimitedString(noPermissionSet.toArray(), ","));

        // 保存登录用户权限列表
        sessionPermission.setPermissionSet(operateSet);
        SessionUtils.setSessionPermission(request, sessionPermission);

        // 添加权限监控集合，当前session已更新最新权限
        sessionPermissionCache.add(token);
        return sessionPermission;
    }
    public static void invalidateSessionPermissions() {
        sessionPermissionCache.clear();
    }
    public String getSsoAppCode() {
        return ssoAppCode;
    }

    public void setSsoAppCode(String ssoAppCode) {
        this.ssoAppCode = ssoAppCode;
    }
}
