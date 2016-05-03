/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.joint.base.service.jms;

import com.fz.us.base.bean.Email;
import com.fz.us.base.util.DataUtil;
import com.fz.us.base.util.LogUtil;
import com.fz.us.base.util.mapper.JsonMapper;
import com.joint.base.bean.EnumManage;
import com.joint.base.entity.DownloadRecord;
import com.joint.base.entity.LocationEntity;
import com.joint.base.entity.PushEntity;
import com.joint.base.entity.PushLog;
import com.joint.base.entity.system.Sms;
import fz.me.chanjar.weixin.mp.bean.WxMpCustomMessage;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;
import java.util.Date;

/**
 * JMS用户变更消息生产者.
 * 
 * 使用jmsTemplate将用户变更消息分别发送到queue与topic.
 * 
 * @author calvin
 */

public class AdvancedNotifyMessageProducer {
    /**
     * 1、封装一个class
     * 2、封装一个bean的值json
     * 3、封装一个StringProperty
     * 4、封装一个
     * */
    private static JsonMapper binder = JsonMapper.nonEmptyMapper();

	private JmsTemplate jmsTemplate;
	private Destination notifyQueue;
	private Destination notifyTopic;


    public void sendQueue(final DownloadRecord downloadRecord,final String key){
        final String beanString = binder.toJson(downloadRecord);
        final String objectType = "downloadRecord";
        final String clazz = downloadRecord.getClass().getName();
        sendMesssage(key,beanString,clazz,objectType,notifyQueue);
    }

    public void sendQueue(final PushLog pushLog,final String key){
        PushEntity pushEntity = new PushEntity(pushLog);
        final String beanString = binder.toJson(pushEntity);
        final String objectType = "pushEntity";
        final String clazz = pushEntity.getClass().getName();
        sendMesssage(key,beanString,clazz,objectType,notifyQueue);
    }

    public void sendQueue(final Sms sms,final String key){
        final String beanString = binder.toJson(sms);
        final String objectType = "sms";
        final String clazz = sms.getClass().getName();
        sendMesssage(key,beanString,clazz,objectType,notifyQueue);
    }

    /**
     * 需要消息日志的时候再加
     * @param email
     * @param key
     */
//    public void sendQueue(final Log log,final String key){
//        final String beanString = binder.toJson(log);
//        final String objectType = "log";
//        final String clazz = log.getClass().getName();
//        sendMesssage(key,beanString,clazz,objectType,notifyQueue);
//    }

    public void sendQueue(final Email email,final String key){
        final String beanString = binder.toJson(email);
        final String objectType = "email";
        final String clazz = email.getClass().getName();
        sendMesssage(key,beanString,clazz,objectType,notifyQueue);
    }

    public void sendQueue(final WxMpCustomMessage wxMpCustomMessage,final String key){
        final String beanString = binder.toJson(wxMpCustomMessage);
        final String objectType = "wxMpCustomMessage";
        final String clazz = wxMpCustomMessage.getClass().getName();
        sendMesssage(key,beanString,clazz,objectType,notifyQueue);
    }

//    public void sendQueue(final ApiRequest apiRequest,final String key){
//        final String beanString = binder.toJson(apiRequest);
//        final String objectType = "apiRequest";
//        final String clazz = apiRequest.getClass().getName();
//        sendMesssage(key,beanString,clazz,objectType,notifyQueue);
//    }

    public void sendQueue(final LocationEntity locations,final String key){
        if(locations.getCoordType().equals(EnumManage.CoordTypeEnum.bd09ll)){
            final String beanString = binder.toJson(locations);
            final String objectType = "locationEntity";
            final String clazz = locations.getClass().getName();
            sendMesssage(key,beanString,clazz,objectType,notifyQueue);
        }
    }

    /**
     * 通用的调用方法
     * */
    private void sendMesssage(final String key,final String beanString, final String clazz, final String objectType, Destination destination){
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                //Only objectified primitive objects, String, Map and List types are allowed
                //如果是bean，转化成map或者json string

                //设置失效时间48个小时
                long expiration = 0L;
                long timeStamp = System.currentTimeMillis();
                long timeToLive = 48*60*60*1000;
                if (timeToLive > 0){
                    expiration = timeToLive + timeStamp;
                }

                MapMessage message = session.createMapMessage();
                message.setJMSExpiration(expiration);
                message.setString("key",key);
                message.setString("json",beanString);
                //JsonMapper jm = new Js
                message.setString("clazz",clazz);
                message.setStringProperty("objectType", objectType);
                //message.
                LogUtil.info("设置expiration过期日期：" + DataUtil.DateToString(new Date(message.getJMSExpiration())));
                return message;
            }
        });
    }

	/**
	 * 使用jmsTemplate的send/MessageCreator()发送Map类型的消息并在Message中附加属性用于消息过滤.
     public void sendQueue(final Admin user) {
     sendMessage(user, notifyQueue);
     }

     public void sendTopic(final Admin user) {
     sendMessage(user, notifyTopic);
     }

	private void sendMessage(final Admin user, Destination destination) {

		jmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
                //Only objectified primitive objects, String, Map and List types are allowed
                //如果是bean，转化成map或者json string
				MapMessage message = session.createMapMessage();
                String beanString = binder.toJson(user);
                message.setString("json", beanString);
                //JsonMapper jm = new Js
                message.setString("clazz",user.getClass().getName());
				message.setStringProperty("objectType", "user");
               //message.
				return message;
			}
		});
	}
     */
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void setNotifyQueue(Destination notifyQueue) {
		this.notifyQueue = notifyQueue;
	}

	public void setNotifyTopic(Destination nodifyTopic) {
		this.notifyTopic = nodifyTopic;
	}
}
