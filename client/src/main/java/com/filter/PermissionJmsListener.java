package com.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.JmxException;
import org.springframework.web.context.ContextLoader;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class PermissionJmsListener implements MessageListener {
    private static Logger logger= LoggerFactory.getLogger(PermissionJmsListener.class);

    private String ssoAppCode;
    @Override
    public void onMessage(Message message) {
        String appCode = null;
        try {
            appCode = ((TextMessage) message).getText();
        } catch (JMSException e) {
            logger.error("Jms illegal message!", e);
        }

        if (ssoAppCode.equals(appCode)) {
            PermissionFilter.invalidateSessionPermissions();
            SmartContainer smartContainer= ContextLoader.getCurrentWebApplicationContext().getBean(SmartContainer.class);
            ApplicationPermission.initApplicationPermissions(smartContainer.getAuthenticationRpcService(),ssoAppCode);
            logger.info("成功通知appCode为：{}的应用更新权限！", appCode);
        }
    }

    public void setSsoAppCode(String ssoAppCode) {
        this.ssoAppCode = ssoAppCode;
    }
}
