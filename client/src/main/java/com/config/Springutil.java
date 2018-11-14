package com.config;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public  class Springutil {

    public static <T extends Class<T>> T getbean(String beanName){
        WebApplicationContext webApplicationContext= ContextLoader.getCurrentWebApplicationContext();
        return  (T)webApplicationContext.getBean(beanName);
    }

    /**
     * 按类型获取spring bean 实例
     *
     * @param beanType
     * @return
     */
    public static <T> T getbean(Class<T> beanType){

        // 获取当前运行环境下Spring上下文
        WebApplicationContext webApp = ContextLoader.getCurrentWebApplicationContext();
        return webApp.getBean(beanType);
    }
}
