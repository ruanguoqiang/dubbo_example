package com.controller;

import com.qiangge.interf.AuthenticationRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "admin")
public class TestSsoController {
    @Autowired
    AuthenticationRpcService authenticationRpcService;
    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/logout",method = RequestMethod.GET)
    public String logout(){
        return "loginout";
    }

    @RequestMapping(value = "/app",method = RequestMethod.GET)
    public String app(){
        authenticationRpcService.updatePermission("sso-server");
        return "loginout";
    }

}
