package com.rpc.service.imp;

import com.config.ConfigUtil;
import com.config.Springutil;

import com.qiangge.interf.PermissionJmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

@Service
public class PermissionJmsServiceImpl implements PermissionJmsService {
    private static Logger logger= LoggerFactory.getLogger(PermissionJmsServiceImpl.class);

    @Override
    public void send(String appCode) {
        JmsTemplate jmsTemplate=getJmsTemplate();
        if (jmsTemplate!=null){
            sendJmsMessage(jmsTemplate,appCode);
        }
    }

    private JmsTemplate getJmsTemplate() {
        JmsTemplate jmsTemplate = null;
        try {
            jmsTemplate = Springutil.getbean(JmsTemplate.class);
        }
        catch (Exception e) {
            logger.warn("jmsTemplate注入失败",e);
        }
        return jmsTemplate;

    }

    private void sendJmsMessage(JmsTemplate jmsTemplate, final String appCode) {
      try {
          String destiname= ConfigUtil.getProperty("mq.permission.queue.prefix").concat(appCode);

        jmsTemplate.send(destiname, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(appCode);
            }
        });
        logger.info("消息服务通知appCode为：{}的应用更新权限", appCode);
      }catch (Exception e) {
          logger.error("消息服务通知appCode为：{}的应用更新权限异常", appCode, e);
      }
    }
}
