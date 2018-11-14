package com.controller;

import com.anonation.ValidateParam;
import com.config.ConfigUtil;
import com.model.Validator;
import com.rpc.service.imp.PermissionServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Controller
public class PersonInfoController {
    @Resource
    PermissionServiceImpl permissionService;

    @RequestMapping(value = "/index2",method = RequestMethod.GET)
    public String index(){
        System.out.println("开始");
        System.out.println(ConfigUtil.getProperty("system.name"));
        return "index";
    }
    @RequestMapping(value = "/person",method = RequestMethod.GET)
    public String getPersonInfo(@ValidateParam({ Validator.MOBILE}) String mobile){
        System.out.println(mobile +"必须是数字型号");
        permissionService.findListById("sso-server",1);
        return "index";
    }
}
