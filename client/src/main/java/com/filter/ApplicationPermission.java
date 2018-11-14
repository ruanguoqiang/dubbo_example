package com.filter;

import com.common.model.RpcPermission;
import com.qiangge.interf.AuthenticationRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApplicationPermission  {

    private static final Logger logger= LoggerFactory.getLogger(ApplicationPermission.class);
    private static Set<String> applicationPermissionSet = null;
    private static List<RpcPermission> applicationMenuList=null;

    private static Object monitor=new Object();

    public static void initApplicationPermissions(AuthenticationRpcService authenticationRpcService,
                                                  String ssoAppCode) {
       List<RpcPermission> dlist=null;
       try{
           dlist=authenticationRpcService.findPermissionList(null,ssoAppCode);
       }catch (Exception e){
           dlist=new ArrayList<RpcPermission>(0);
           logger.error("无法连接到单点登录服务端,请检查配置sso.server.url", e);
       }
       synchronized (monitor){
           applicationMenuList=new ArrayList<RpcPermission>();
           applicationPermissionSet=new HashSet<String>();
           for (RpcPermission rpcPermission:dlist){
               if (rpcPermission.getIsMenu()){
                   applicationMenuList.add(rpcPermission);
               }
               if (!StringUtils.isEmpty(rpcPermission.getUrl())) {
                   applicationPermissionSet.add(rpcPermission.getUrl());
               }
           }
       }
    }

    public static Set<String> getApplicationPermissionSet() {
        synchronized (monitor) {
            return applicationPermissionSet;
        }
    }


    public static List<RpcPermission> getApplicationMenuList() {
        synchronized (monitor) {
            return applicationMenuList;
        }
    }


}
