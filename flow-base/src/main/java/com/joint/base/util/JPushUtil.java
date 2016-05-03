package com.joint.base.util;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.ServiceHelper;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.PlatformNotification;
import com.joint.base.bean.EnumManage;
import com.joint.base.bean.EnumManage.PushTargetEnum;
import com.joint.base.bean.EnumManage.PushTypeEnum;
import com.joint.base.bean.EnumManage.PushWayEnum;
import com.joint.base.entity.PushEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 推送工具类 - 极光推送
 * ============================================================================
 * 版权所有 2015 。
 *
 * @author qiushihua
 *
 * @version 1.0 2015-01-21
 * ============================================================================
 */
public class JPushUtil {

	//正式
//	private static final String APPKEY_SMARTSALES ="4ff19304ccd2411f6481f4ac";
//	private static final String MASTERSECRET_SMARTSALES = "7b56a5ddaf3ca6b9900a8491";

    //测试Demo
    private static final String APPKEY_SMARTSALES ="0db551e22e7f3db489cab52d";
    private static final String MASTERSECRET_SMARTSALES = "844cf048b8a8124ff9b92aff";

	private static JPushClient jpush = null;

    /**
     * 保存离线的时长。秒为单位。最多支持10天（864000秒）。
     * 0 表示该消息不保存离线。即：用户在线马上发出，当前不在线用户将不会收到此消息。
     * 此参数不设置则表示默认，默认为保存1天的离线消息（86400秒）。
     */
	private static long timeToLive =  60 * 60 * 24 * 3;

	public static PushEntity send(PushEntity pushEntity, String mastersecret, String appkey){

        if(StringUtils.isNotEmpty(mastersecret)&&StringUtils.isNotEmpty(appkey)){
            jpush = new JPushClient(mastersecret, appkey);
        }else{
            if(PushTargetEnum.SmartSales.equals(pushEntity.getAppTarget())){
                jpush = new JPushClient(MASTERSECRET_SMARTSALES, APPKEY_SMARTSALES);
            }else{
                jpush = new JPushClient(MASTERSECRET_SMARTSALES, APPKEY_SMARTSALES);
            }
        }

		//参数
		Options options = Options.newBuilder()
				.setSendno(ServiceHelper.generateSendno())
				.setOverrideMsgId(0)
				.setApnsProduction(true)//true 表示推送生产环境，false 表示要推送开发环境
				.setTimeToLive(timeToLive)
				.build();
		
		String msgTitle = pushEntity.getPushTitle();//标题
		String msgContent = pushEntity.getPushContent();//内容
		msgContent = msgContent.length() >20?(msgContent.substring(0,20)+"..."):msgContent;
		
		String imei = pushEntity.getPushIMEI();
		String alias = pushEntity.getPushAlias();
		String tagValue = pushEntity.getPushTag();
		String registrationId = pushEntity.getPushRegistrationId();
		
		//自定义参数
        int cmsgLevel = EnumManage.CustomMessageLevelEnum.Normal.key();
		int cmsgType = EnumManage.CustomMessageTypeEnum.UserMsg.key();
		String cmsgKey = pushEntity.getMsgKey();
		String cmsgContent = pushEntity.getMsgContent();

		PushResult pushResult = null;
		PushPayload payload =  null;
		
		//根据类型/方式推送
		if(pushEntity.getPushWay().equals(PushWayEnum.Notification)){
			//推送通知
			//Android通知 自定义参数
			PlatformNotification androidNotification = AndroidNotification.newBuilder()
					.setAlert(msgContent)
					.setTitle(msgTitle)
                    .addExtra("msgLevel", cmsgLevel)
					.addExtra("msgType", cmsgType)
					.addExtra("msgKey", cmsgKey)
					.addExtra("msgContent", cmsgContent)
					.build();
			PlatformNotification iosNotification = IosNotification.newBuilder()
					.setAlert(msgContent)
                    .setBadge(1)
                    .addExtra("msgLevel", cmsgLevel)
					.addExtra("msgType", cmsgType)
					.addExtra("msgKey", cmsgKey)
					.addExtra("msgContent", cmsgContent)
					.build();
			if(pushEntity.getPushType().equals(PushTypeEnum.ApplicationKey)){
				//推送所有
				payload = PushPayload.newBuilder()
						.setOptions(options)
						.setPlatform(Platform.android_ios())
		                .setAudience(Audience.all())
		                .setNotification(Notification.newBuilder()
		                        .addPlatformNotification(androidNotification)
		                        .addPlatformNotification(iosNotification)
		                        .build())
		                .build();
			}else if(pushEntity.getPushType().equals(PushTypeEnum.IMEI)){
				//已过时
				if(StringUtils.isEmpty(imei)){
					return null;
				}
				return null;
			}else if(pushEntity.getPushType().equals(PushTypeEnum.Alias)){
				//按标识
				if(StringUtils.isEmpty(alias)){
					return null;
				}
				payload = PushPayload.newBuilder()
						.setOptions(options)
						.setPlatform(Platform.android_ios())
						.setAudience(Audience.alias(alias))
						.setNotification(Notification.newBuilder()
								.addPlatformNotification(androidNotification)
								.addPlatformNotification(iosNotification)
		                        .build())
		                .build();
			}else if(pushEntity.getPushType().equals(PushTypeEnum.Tag)){
				//按特征or
				if(StringUtils.isEmpty(tagValue)){
					return null;
				}
				payload = PushPayload.newBuilder()
						.setOptions(options)
						.setPlatform(Platform.android_ios())
						.setAudience(Audience.tag(tagValue))
						.setNotification(Notification.newBuilder()
								.addPlatformNotification(androidNotification)
								.addPlatformNotification(iosNotification)
		                        .build())
						.build();
			}else if(pushEntity.getPushType().equals(PushTypeEnum.Tag_And)){
				//特征and
				if(StringUtils.isEmpty(tagValue)){
					return null;
				}
				payload = PushPayload.newBuilder()
						.setOptions(options)
						.setPlatform(Platform.android_ios())
						.setAudience(Audience.tag_and(tagValue))
						.setNotification(Notification.newBuilder()
								.addPlatformNotification(androidNotification)
								.addPlatformNotification(iosNotification)
		                        .build())
						.build();
			}else if(pushEntity.getPushType().equals(PushTypeEnum.Registration_Id)){
				//按注册ID
				if(StringUtils.isEmpty(registrationId)){
					return null;
				}
				payload = PushPayload.newBuilder()
						.setOptions(options)
						.setPlatform(Platform.android_ios())
						.setAudience(Audience.registrationId(registrationId))
						.setNotification(Notification.newBuilder()
								.addPlatformNotification(androidNotification)
								.addPlatformNotification(iosNotification)
		                        .build())
						.build();
			}else{
				return null;
			}
		}else if(pushEntity.getPushWay().equals(PushWayEnum.CustomMessage)){
			//推送自定义消息
			//Android自定义消息 自定义参数
			Message androidMessage = Message.newBuilder()
                    .setMsgContent(msgContent)
                    .addExtra("msgLevel", cmsgLevel)
                    .addExtra("msgType", cmsgType)
                    .addExtra("msgKey", cmsgKey)
                    .addExtra("msgContent", cmsgContent)
                    .build();
			if(pushEntity.getPushType().equals(PushTypeEnum.ApplicationKey)){
				//推送所有
				payload = PushPayload.newBuilder()
						.setOptions(options)
						.setPlatform(Platform.android())
						.setAudience(Audience.all())
						.setMessage(androidMessage)
						.setOptions(Options.sendno())
						.build();
			}else if(pushEntity.getPushType().equals(PushTypeEnum.IMEI)){
				//已过时
				if(StringUtils.isEmpty(imei)){
					return null;
				}
				return null;
			}else if(pushEntity.getPushType().equals(PushTypeEnum.Alias)){
				//按标识
				if(StringUtils.isEmpty(alias)){
					return null;
				}
				payload = PushPayload.newBuilder()
						.setOptions(options)
						.setPlatform(Platform.android())
						.setAudience(Audience.alias(alias))
						.setMessage(androidMessage)
		                .build();
			}else if(pushEntity.getPushType().equals(PushTypeEnum.Tag)){
				//按特征or
				if(StringUtils.isEmpty(tagValue)){
					return null;
				}
				payload = PushPayload.newBuilder()
						.setOptions(options)
						.setPlatform(Platform.android())
						.setAudience(Audience.tag(tagValue))
						.setMessage(androidMessage)
						.build();
			}else if(pushEntity.getPushType().equals(PushTypeEnum.Tag_And)){
				//特征and
				if(StringUtils.isEmpty(tagValue)){
					return null;
				}
				payload = PushPayload.newBuilder()
						.setOptions(options)
						.setPlatform(Platform.android())
						.setAudience(Audience.tag_and(tagValue))
						.setMessage(androidMessage)
						.build();
			}else if(pushEntity.getPushType().equals(PushTypeEnum.Registration_Id)){
				//按注册ID
				if(StringUtils.isEmpty(registrationId)){
					return null;
				}
				payload = PushPayload.newBuilder()
						.setOptions(options)
						.setPlatform(Platform.android())
						.setAudience(Audience.registrationId(registrationId))
						.setMessage(androidMessage)
						.build();
			}else{
				return null;
			}
		}else{
			return null;
		}
		
		if(payload!=null){
            pushEntity.setNo(String.valueOf(options.getSendno()));
            try {
                pushResult = jpush.sendPush(payload);
                if (null != pushResult) {
                    pushEntity.setPushId(pushResult.msg_id);
                    System.out.println("JPush-Result：["+pushResult.toString()+"]");
                    if (pushResult.isResultOK()) {
                        System.out.println("Success，sendno："+pushResult.sendno);
                        pushEntity.setIsSend(1);
                        pushEntity.setSendDate(new Date());
                    } else {
                        System.out.println("Fail");
                        System.out.println(pushResult.getOriginalContent());
                        pushEntity.setIsSend(0);
                    }
                } else {
                    System.out.println("No result");
                    pushEntity.setIsSend(0);
                }
            } catch (APIConnectionException e) {
                // Connection error, should retry later
                // e.printStackTrace();
                System.out.println("Connection error, should retry later");
                pushEntity.setIsSend(0);
            } catch (APIRequestException e) {
                // Should review the error, and fix the request
                // e.printStackTrace();
                System.out.println("Should review the error, and fix the request");
                System.out.println("HTTP Status: " + e.getStatus());
                pushEntity.setIsSend(0);
                pushEntity.setPushErrcode(e.getErrorCode());
                pushEntity.setPushErrmsg(e.getErrorMessage());
            }
		}else{
			System.out.println("Error Params");
			return null;
		}
		return pushEntity;
	}

}
