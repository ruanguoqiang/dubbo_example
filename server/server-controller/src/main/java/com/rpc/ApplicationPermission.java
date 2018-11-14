package com.rpc;

import com.common.model.RpcPermission;
import com.qiangge.interf.AuthenticationRpcService;

import java.util.List;
import java.util.Set;

public class ApplicationPermission {

    private Set<String> applicationPermissionSet=null;

    private List<com.common.model.RpcPermission> applicationMenu=null;

    private static Object monitor=new Object();

    public static void initApplicationPermissions(AuthenticationRpcService authenticationRpcService,
                                                  String ssoAppCode) {
        List<RpcPermission> dList=null;
        dList=authenticationRpcService.findPermissionList(null,ssoAppCode);
    }
}
