package com.joint.base.service.impl;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Pager;
import com.joint.base.bean.EnumManage;
import com.joint.base.dao.BaseEntityDao;
import com.joint.base.dao.PushLogDao;
import com.joint.base.entity.Comment;
import com.joint.base.entity.CustomMessage;
import com.joint.base.entity.PushLog;
import com.joint.base.entity.Users;
import com.joint.base.mp.WxEchoMpServiceImpl;
import com.joint.base.service.CustomMessageService;
import com.joint.base.service.PushLogService;
import com.joint.base.service.UsersService;
import com.joint.base.service.jms.AdvancedNotifyMessageProducer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Service实现类 - 推送记录
 * ============================================================================
 * 版权所有 2013 qshihua。
 * ----------------------------------------------------------------------------
 * 
 * @author qshihua
 * 
 * @version 0.1 2013-05-06
 */

@Service
public class PushLogServiceImpl extends BaseEntityServiceImpl<PushLog, String> implements PushLogService {

    /**
     * 批注动态
     */
    public final String PUSHTITLE="批注动态";

	@Resource
	private PushLogDao pushLogDao;
    @Override
    public BaseEntityDao<PushLog, String> getBaseEntityDao() {
        return pushLogDao;
    }


	@Resource
	private UsersService usersService;
    @Resource
    private AdvancedNotifyMessageProducer notifyMessageProducer;
    @Resource
    private CustomMessageService customMessageService;
    @Resource
    private WxEchoMpServiceImpl wxMpService;

    @Override
	public Pager queryByPager(Pager pager, Users users, EnumManage.PushWayEnum pushWay,
			EnumManage.PushTypeEnum pushType, int pushErrcode, BaseEnum.StateEnum...states) {
		return pushLogDao.getPager(pager, users, pushWay, pushType, pushErrcode, states);
	}

	@Override
	public Pager queryByPager(Pager pager, Users users, EnumManage.PushWayEnum pushWay,
			EnumManage.PushTypeEnum pushType, int pushErrcode, boolean isSend, BaseEnum.StateEnum...states) {
		return pushLogDao.getPager(pager, users, pushWay, pushType, pushErrcode, isSend, states);
	}

    @Override
    public Map<String, Object> getPushLogListMap(PushLog pushLog) {
        return null;
    }

    public Pager queryByPager(Pager pager, Users users, EnumManage.PushWayEnum pushWay,
			EnumManage.PushTypeEnum pushType, int pushErrcode, Date date,BaseEnum.StateEnum...states) {
		return pushLogDao.getPager(pager, users, pushWay, pushType, pushErrcode, date, states);
	}

    @Override
	public String saveAndEnable(PushLog pushLog) {
		pushLog.setState(BaseEnum.StateEnum.Enable);
		return pushLogDao.save(pushLog);
	}

    @Override
    public void sendCommentNotification(Comment comment, String target) {
        String content = getCommentContent(comment);

        //已发送对象
        List<Users> toUsersList = new ArrayList<Users>();
        //创建者默认为已发送对象
        if(comment.getCreater()!=null) toUsersList.add(comment.getCreater());

        //如果有发送目标对象
        if(comment.getToUsers()!=null){
            Users targetUsers = comment.getToUsers();
            if(!toUsersList.contains(targetUsers)){
                sendNotificationToUser(comment.getCreater(), targetUsers, PUSHTITLE, content, EnumManage.CustomMessageTypeEnum.FormRead, comment.getTargetClass().substring(comment.getTargetClass().lastIndexOf(".")+1), comment.getTargetId());
                toUsersList.add(targetUsers);
            }
        }
    }

    public void sendCommentNotification(Comment comment, Set<Users> usersSet){
        String content = getCommentContent(comment);
        //已发送对象
        List<Users> toUsersList = new ArrayList<Users>();
        //创建者默认为已发送对象
        if(comment.getCreater()!=null) toUsersList.add(comment.getCreater());
        //如果有发送目标对象
        if(comment.getToUsers()!=null){
                Users targetUsers = comment.getToUsers();
                if(!toUsersList.contains(targetUsers)){
                    sendNotificationToUser(comment.getCreater(), targetUsers, PUSHTITLE, content, EnumManage.CustomMessageTypeEnum.FormRead, comment.getTargetClass().substring(comment.getTargetClass().lastIndexOf(".")+1), comment.getTargetId());
                    toUsersList.add(targetUsers);
                }
        }
        //加入额外需要发送的人员
        for(Users users : usersSet){
            if(!toUsersList.contains(users)){
                sendNotificationToUser(comment.getCreater(), users, PUSHTITLE, content, EnumManage.CustomMessageTypeEnum.FormRead, comment.getTargetClass().substring(comment.getTargetClass().lastIndexOf(".")+1), comment.getTargetId());
                toUsersList.add(users);
            }
        }
    }
    /**
     * 向用户发送通知(mp)
     * @param targetUsers 必填 用户
     * @param content 必填 内容
     * @param msgType 必填 类型
     * @param msgKey 可选 关键字 （空则为只读信息）
     * @param msgContent 可选 关键字内容 （指向跳转位置）
     * @return
     * @throws Exception
     */
    private PushLog sendNotificationToUser(Users creater, Users targetUsers, String title, String content, EnumManage.CustomMessageTypeEnum msgType, String msgKey, String msgContent){
        PushLog pushLog = new PushLog();
        pushLog.setSender(creater);
        pushLog.setUsers(targetUsers);
        pushLog.setAppTarget(EnumManage.PushTargetEnum.SmartSales);
        pushLog.setPushWay(EnumManage.PushWayEnum.Notification);
        pushLog.setPushType(EnumManage.PushTypeEnum.Alias);
        pushLog.setPushAlias(targetUsers.getId());
        pushLog.setPushTitle(title);
        pushLog.setPushContent(content);
        CustomMessage customMessage = new CustomMessage();
        customMessage.setMsgLevel(EnumManage.CustomMessageLevelEnum.Normal);
        customMessage.setMsgType(msgType);
        if(StringUtils.isNotEmpty(msgKey))
            customMessage.setMsgKey(msgKey);
        if(StringUtils.isNotEmpty(msgContent))
            customMessage.setMsgContent(msgContent);
        customMessageService.saveAndEnable(customMessage);
        pushLog.setCustomMessage(customMessage);
        saveAndEnable(pushLog);

        notifyMessageProducer.sendQueue(pushLog, EnumManage.NotifyKeyEnum.pushLog.name());

        //mppush
        try {
            if(EnumManage.CustomMessageTypeEnum.FormRead.equals(msgType))
                wxMpService.sendFormReadMessage(targetUsers, content, msgKey, msgContent);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pushLog;
    }

    /**
     * App用
     * @param comment
     * @param usersSet
     */
    public void sendCommentNotificationApp(Comment comment, Set<Users> usersSet){
//        LogUtil.info("sendCommentNotificationApp start");
        String content = getCommentContent(comment);
        Users creater = comment.getCreater();
        String key = comment.getTargetClass().substring(comment.getTargetClass().lastIndexOf(".") + 1);
        String targetId = comment.getTargetId();
        //已发送对象
        List<Users> toUsersList = new ArrayList<Users>();
        //创建者默认为已发送对象
        if(creater!=null) toUsersList.add(creater);
        //如果有发送目标对象
        if(comment.getToUsers()!=null){
            Users targetUsers = comment.getToUsers();
            if(!toUsersList.contains(targetUsers)){
                sendNotificationToUserApp(creater, targetUsers, PUSHTITLE, content, EnumManage.CustomMessageTypeEnum.FormRead, key, targetId);
                toUsersList.add(targetUsers);
            }
        }

        //加入额外需要发送的人员
        if(usersSet.size()>0){
            for(Users users : usersSet){
                if(!toUsersList.contains(users)){
                    sendNotificationToUserApp(comment.getCreater(), users, PUSHTITLE, content, EnumManage.CustomMessageTypeEnum.FormRead, key, targetId);
                    toUsersList.add(users);
                }
            }
        }
//        LogUtil.info("sendCommentNotificationApp end");
    }

    /**
     * 向用户发送通知(app)
     * @param targetUsers 必填 用户
     * @param content 必填 内容
     * @param msgType 必填 类型
     * @param msgKey 可选 关键字 （空则为只读信息）
     * @param msgContent 可选 关键字内容 （指向跳转位置）
     * @return
     * @throws Exception
     */
    public PushLog sendNotificationToUserApp(Users creater, Users targetUsers, String title, String content, EnumManage.CustomMessageTypeEnum msgType, String msgKey, String msgContent){
//        LogUtil.info("sendNotificationToUserApp start");
        PushLog pushLog = new PushLog();
        pushLog.setSender(creater);
        pushLog.setUsers(targetUsers);
        pushLog.setAppTarget(EnumManage.PushTargetEnum.SmartSales);
        pushLog.setPushWay(EnumManage.PushWayEnum.Notification);
        pushLog.setPushType(EnumManage.PushTypeEnum.Alias);
        pushLog.setPushAlias(targetUsers.getId());
        pushLog.setPushTitle(title);
        pushLog.setPushContent(content);
        CustomMessage customMessage = new CustomMessage();
        customMessage.setMsgLevel(EnumManage.CustomMessageLevelEnum.Normal);
        customMessage.setMsgType(msgType);
        if(StringUtils.isNotEmpty(msgKey))
            customMessage.setMsgKey(msgKey);
        if(StringUtils.isNotEmpty(msgContent))
            customMessage.setMsgContent(msgContent);
        customMessageService.saveAndEnable(customMessage);
        pushLog.setCustomMessage(customMessage);
        saveAndEnable(pushLog);

        notifyMessageProducer.sendQueue(pushLog, EnumManage.NotifyKeyEnum.pushLog.name());

        //mppush
        try {
            if(EnumManage.CustomMessageTypeEnum.FormRead.equals(msgType))
                wxMpService.sendFormReadMessage(targetUsers, content, msgKey, msgContent);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return pushLog;
    }

    private String getCommentContent(Comment comment){
        String content = "";
        switch (comment.getModel()){
            case text:
                content = comment.getCreater().getName()+"："+comment.getText();
                break;
            case image:
                content = comment.getCreater().getName()+"：发送了一张图片";
                break;
            case voice:
                content = comment.getCreater().getName()+"：发送了一段语音";
                break;
            case video:
                content = comment.getCreater().getName()+"：发送了一段视频";
                break;
            case location:
                content = comment.getCreater().getName()+"：分享了一个位置";
                break;
            case link:
                content = comment.getCreater().getName()+"：分享了一个链接";
                break;
            default:
                content = comment.getCreater().getName()+"："+comment.getText();
        }
        return content;

    }


}