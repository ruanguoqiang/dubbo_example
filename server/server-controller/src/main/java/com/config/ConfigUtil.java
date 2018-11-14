package com.config;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

public class ConfigUtil extends PropertyPlaceholderConfigurer{

    private static Properties properties;
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props){
        super.processProperties(beanFactory,props);
        properties=props;
    }

    public static String getProperty(String name){
        return  properties.getProperty(name);
    }
}
