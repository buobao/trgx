/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.joint.base.service.jms;

import com.fz.us.base.bean.Email;
import com.fz.us.base.service.common.SimpleMailService;
import com.fz.us.base.util.DataUtil;
import com.fz.us.base.util.LogUtil;
import com.fz.us.base.util.mapper.JsonMapper;
import com.joint.base.bean.EnumManage;
import com.joint.base.entity.DownloadRecord;
import com.joint.base.entity.LocationEntity;
import com.joint.base.entity.PushEntity;
import com.joint.base.entity.system.Sms;
import com.joint.base.service.*;
import com.joint.base.util.BaiduMapUtil;
import com.joint.base.util.JPushUtil;
import com.joint.base.util.SMSUtil;
import fz.me.chanjar.weixin.mp.api.WxMpService;
import fz.me.chanjar.weixin.mp.bean.WxMpCustomMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 消息的异步被动接收者.
 * 
 * 使用Spring的MessageListenerContainer侦听消息并调用本Listener进行处理.
 * 
 * @author calvin
 */
public class AdvancedNotifyMessageListener implements MessageListener {

    @Resource
    private UsersService usersService;
    @Resource
    private SimpleMailService simpleMailService;
    @Resource
    private WxMpService wxMpService;
    @Resource
    private CompanyService companyService;
    @Resource
    private LocationEntityService locationEntityService;
    @Resource
    private DownloadRecordService downloadRecordService;
    @Resource
    private PushEntityService pushEntityService;

    public String getJpushAppKey() {
        return jpushAppKey;
    }

    public void setJpushAppKey(String jpushAppKey) {
        this.jpushAppKey = jpushAppKey;
    }

    public String getJpushMasterSecret() {
        return jpushMasterSecret;
    }

    public void setJpushMasterSecret(String jpushMasterSecret) {
        this.jpushMasterSecret = jpushMasterSecret;
    }

    private String jpushAppKey;
    private String jpushMasterSecret;

	private static Logger logger = LoggerFactory.getLogger(AdvancedNotifyMessageListener.class);
    /**
     * 1、封装一个class
     * 2、封装一个bean的值json
     * 3、封装一个StringProperty
     * 4、封装一个
     * */
    private static JsonMapper binder = JsonMapper.nonEmptyMapper();
	/**
	 * MessageListener回调函数.
	 */
	@Override
	public void onMessage(Message message) {
		try {
			MapMessage mapMessage = (MapMessage) message;
            // 打印消息详情
            //LogUtil.info("UserName:{}, Email:{}, ObjectType:{}", mapMessage.getString("userName"),mapMessage.getString("email"), mapMessage.getStringProperty("objectType"));

            //在ActiveMq重启的时候会把为消费的消息继续处理，在这里处理下消息的时效性，如果时间太长，则该消息不继续处理（message）
            LogUtil.info("获取expiration过期日期：" + DataUtil.DateToString(new Date(message.getJMSExpiration())));

			// 打印消息详情
			String key = mapMessage.getString("key");
            String json = mapMessage.getString("json");
            String clazz = mapMessage.getString("clazz");
            String property = mapMessage.getStringProperty("objectType");

            //Admin admin = binder.fromJson(json, Admin.class);
            //LogUtil.info("key:"+key+" clazz:"+clazz + " json:"+json);
            if(key.equals(EnumManage.NotifyKeyEnum.sms.name())){
                Sms sms = binder.fromJson(json,Sms.class);
                SMSUtil.httpSend(sms);
            }else if(key.equals(EnumManage.NotifyKeyEnum.email.name())){
                Email email = binder.fromJson(json, Email.class);
                simpleMailService.send(email);
            }else if(key.equals(EnumManage.NotifyKeyEnum.apiRequest.name())){
                //处理api通知接口
//                ApiRequest apiRequest = binder.fromJson(json, ApiRequest.class);
//                companyService.EntityActionNotice(apiRequest);
            }else if(key.equals(EnumManage.NotifyKeyEnum.pushLog.name())){
                //极光推送（App端）
                PushEntity pushEntity = binder.fromJson(json, PushEntity.class);
                pushEntity = JPushUtil.send(pushEntity, jpushMasterSecret, jpushAppKey);
                pushEntityService.save(pushEntity);
            }else if(key.equals(EnumManage.NotifyKeyEnum.locationEntity.name())){
                LocationEntity loca = binder.fromJson(json, LocationEntity.class);
                Map<String,String> map = BaiduMapUtil.getBd0911AddressMapFromWgs84(loca.getLongitude().toString(), loca.getLatitude().toString());
                loca.setLongitude(new BigDecimal(map.get("x")));
                loca.setLatitude(new BigDecimal(map.get("y")));
                loca.setFullAddress(map.get("address"));
                loca.setAddressJson(map.get("json"));
                locationEntityService.save(loca);
            }else if(key.equals(EnumManage.NotifyKeyEnum.wxMpCustomMessage.name())){
                //微信推送
                WxMpCustomMessage wxMpCustomMessage = binder.fromJson(json, WxMpCustomMessage.class);
                wxMpService.customMessageSend(wxMpCustomMessage);
            }else if(key.equals(EnumManage.NotifyKeyEnum.downloadRecord.name())){
                //文件下载申请
                DownloadRecord downloadRecord = binder.fromJson(json, DownloadRecord.class);
//                downloadRecordService.createDownloadDataFiles(downloadRecord);
            }else{

            }


        } catch (Exception e) {
			logger.error("处理消息时发生异常.", e);
		}
	}
}
