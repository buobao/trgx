package com.joint.base.entity;

import com.fz.us.base.entity.Entity;
import com.joint.base.bean.EnumManage;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created with us2 -> com.fz.us.core.entity.common.
 * User: qiushihua
 * Date: 2015-01-21
 * Time: 15:49
 * 说明：
 */
@javax.persistence.Entity
@Table(name="ss_pushentity")
public class PushEntity  extends Entity {

    private static final long serialVersionUID = 8441798852215078592L;

    public PushEntity() {
    }

    public PushEntity(PushLog pushLog) {
        this.pushLogId = pushLog.getId();

        if(pushLog.getSender()!=null){
            this.senderId = pushLog.getSender().getId();
            this.senderName = pushLog.getSender().getName();
        }
        if(pushLog.getUsers()!=null){
            this.receiverId = pushLog.getUsers().getId();
            this.receiverName = pushLog.getUsers().getName();
        }

        this.appTarget = pushLog.getAppTarget();
        this.pushWay = pushLog.getPushWay();
        this.pushType = pushLog.getPushType();

        this.pushIMEI = pushLog.getPushIMEI();
        this.pushAlias = pushLog.getPushAlias();
        this.pushTag = pushLog.getPushTag();
        this.pushRegistrationId = pushLog.getPushRegistrationId();

        this.pushTitle = pushLog.getPushTitle();
        this.pushContent = pushLog.getPushContent();

        if(pushLog.getCustomMessage()!=null){
            this.msgLevel = pushLog.getCustomMessage()!=null?pushLog.getCustomMessage().getMsgLevel(): EnumManage.CustomMessageLevelEnum.Normal;
            this.msgType = pushLog.getCustomMessage().getMsgType();
            this.msgKey = pushLog.getCustomMessage().getMsgKey();
            this.msgContent = pushLog.getCustomMessage().getMsgContent();
        }

        this.isSend = 0;
        this.isGet = 0;

    }

    private String pushLogId;

    private String no;//推送编号

    private String senderId;//发送者
    private String senderName;

    private String receiverId;//接收者
    private String receiverName;

    private EnumManage.PushTargetEnum appTarget;//推送目标（SmartSales）
    private EnumManage.PushWayEnum pushWay;//推送方式（Notification/CustomMessage）
    private EnumManage.PushTypeEnum pushType;//推送类型（ApplicationKey/IMEI/Alias/Tag/Tag_And/Registration_Id）

    private String pushIMEI;//值 - 推送IMEI
    private String pushAlias;//值 - 推送标识
    private String pushTag;//值 - 推送特征 （多参数 用 “,” 分隔）
    private String pushRegistrationId;//值 - 推送注册ID （多参数 用 “,” 分隔）

    private String pushTitle;//推送标题
    private String pushContent;//推送内容

    private EnumManage.CustomMessageLevelEnum msgLevel;//消息级别（Normal/Notice/Todo）
    private EnumManage.CustomMessageTypeEnum msgType;//消息类型（AdminMsg/UserMsg/Community/FormRead/WebView/NewCount）
    private String msgKey;//消息关键字
    private String msgContent;//消息关键值

    private long pushId;//消息ID
    private int pushErrcode;//发送结果
    private String pushErrmsg;

    private int isSend;//是否发送成功（0|1）
    private Date sendDate;//发送时间
    private int isGet;//是否接收成功（0|1）
    private Date getDate;//接收时间

    public String getPushLogId() {
        return pushLogId;
    }

    public void setPushLogId(String pushLogId) {
        this.pushLogId = pushLogId;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    @Enumerated(EnumType.STRING)
    public EnumManage.PushTargetEnum getAppTarget() {
        return appTarget;
    }

    public void setAppTarget(EnumManage.PushTargetEnum appTarget) {
        this.appTarget = appTarget;
    }

    @Enumerated(EnumType.STRING)
    public EnumManage.PushWayEnum getPushWay() {
        return pushWay;
    }

    public void setPushWay(EnumManage.PushWayEnum pushWay) {
        this.pushWay = pushWay;
    }

    @Enumerated(EnumType.STRING)
    public EnumManage.PushTypeEnum getPushType() {
        return pushType;
    }

    public void setPushType(EnumManage.PushTypeEnum pushType) {
        this.pushType = pushType;
    }

    public String getPushIMEI() {
        return pushIMEI;
    }

    public void setPushIMEI(String pushIMEI) {
        this.pushIMEI = pushIMEI;
    }

    public String getPushAlias() {
        return pushAlias;
    }

    public void setPushAlias(String pushAlias) {
        this.pushAlias = pushAlias;
    }

    public String getPushTag() {
        return pushTag;
    }

    public void setPushTag(String pushTag) {
        this.pushTag = pushTag;
    }

    public String getPushRegistrationId() {
        return pushRegistrationId;
    }

    public void setPushRegistrationId(String pushRegistrationId) {
        this.pushRegistrationId = pushRegistrationId;
    }

    public String getPushTitle() {
        return pushTitle;
    }

    public void setPushTitle(String pushTitle) {
        this.pushTitle = pushTitle;
    }

    public String getPushContent() {
        return pushContent;
    }

    public void setPushContent(String pushContent) {
        this.pushContent = pushContent;
    }

    @Enumerated(EnumType.STRING)
    public EnumManage.CustomMessageLevelEnum getMsgLevel() {
        return msgLevel;
    }

    public void setMsgLevel(EnumManage.CustomMessageLevelEnum msgLevel) {
        this.msgLevel = msgLevel;
    }

    @Enumerated(EnumType.STRING)
    public EnumManage.CustomMessageTypeEnum getMsgType() {
        return msgType;
    }

    public void setMsgType(EnumManage.CustomMessageTypeEnum msgType) {
        this.msgType = msgType;
    }

    public String getMsgKey() {
        return msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public long getPushId() {
        return pushId;
    }

    public void setPushId(long pushId) {
        this.pushId = pushId;
    }

    public int getPushErrcode() {
        return pushErrcode;
    }

    public void setPushErrcode(int pushErrcode) {
        this.pushErrcode = pushErrcode;
    }

    public String getPushErrmsg() {
        return pushErrmsg;
    }

    public void setPushErrmsg(String pushErrmsg) {
        this.pushErrmsg = pushErrmsg;
    }

    public int getIsSend() {
        return isSend;
    }

    public void setIsSend(int isSend) {
        this.isSend = isSend;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public int getIsGet() {
        return isGet;
    }

    public void setIsGet(int isGet) {
        this.isGet = isGet;
    }

    public Date getGetDate() {
        return getDate;
    }

    public void setGetDate(Date getDate) {
        this.getDate = getDate;
    }

}
