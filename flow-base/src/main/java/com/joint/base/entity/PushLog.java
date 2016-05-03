package com.joint.base.entity;


import com.joint.base.bean.EnumManage;
import com.joint.base.parent.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with us-parent -> com.fz.us.core.entity.common.
 * User: min_xu
 * Date: 2014/9/28
 * Time: 16:07
 * 说明：
 */
@Entity
@Table(name="ss_pushlog")
public class PushLog extends BaseEntity {

    private static final long serialVersionUID = 6364213965751742538L;

    public PushLog(){
        super();
    }

    public PushLog(Users users,Users sender){
        super();
        this.setUsers(users);
        this.setSender(sender);
    }

    /**
     * url名
     */
    private String uriName;
    @Transient
    public String getUriName() {
        return uriName;
    }
    public void setUriName(String uriName) {
        this.uriName = uriName;
    }

    /**
     * 接收用户
     */
    private Users users;
    /**
     * 发送用户
     */
    private Users sender;
    /**
     * 推送目标
     */
    private EnumManage.PushTargetEnum appTarget;
    /**
     * 编号
     */
    private String no;
    /**
     * 消息ID
     */
    private long msg_id;
    /**
     * 推送方式（通知/自定义消息）
     */
    private EnumManage.PushWayEnum pushWay;
    /**
     * 推送类型（By ALL|IMEI|Alias|Tag）
     */
    private EnumManage.PushTypeEnum pushType;
    /**
     * 推送IMEI
     */
    private String pushIMEI;
    /**
     * 推送标识
     */
    private String pushAlias;
    /**
     * 推送特征
     * （多参数 用 “,” 分隔）
     */
    private String pushTag;
    /**
     * 推送注册ID
     * （多参数 用 “,” 分隔）
     */
    private String pushRegistrationId;
    /**
     * 推送标题
     */
    private String pushTitle;
    /**
     * 推送内容
     */
    private String pushContent;
    /**
     * 自定义推送内容
     */
    private CustomMessage customMessage;
    /**
     * 发送结果
     */
    private int pushErrcode;
    /**
     * 结果信息
     */
    private String pushErrmsg;
    /**
     * 是否发送成功（0|1）
     */
    private int isSend;
    /**
     * 发送时间
     */
    private Date sendDate;
    /**
     * 是否接收成功（0|1）
     */
    private int isGet;
    /**
     * 接收时间
     */
    private Date getDate;
    /**
     * 是否已读（0|1）
     */
    private int isRead;
    /**
     * 已读时间
     */
    private Date readDate;

    @ManyToOne(fetch = FetchType.LAZY)
    public Users getUsers() {
        return users;
    }
    public void setUsers(Users users) {
        this.users = users;
    }
    public String getNo() {
        return no;
    }
    public void setNo(String no) {
        this.no = no;
    }
    public long getMsg_id() {
        return msg_id;
    }
    public void setMsg_id(long msg_id) {
        this.msg_id = msg_id;
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
    @Column(length = 65535)
    public String getPushContent() {
        return pushContent;
    }
    public void setPushContent(String pushContent) {
        this.pushContent = pushContent;
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
    public int getIsGet() {
        return isGet;
    }
    public void setIsGet(int isGet) {
        this.isGet = isGet;
    }
    @OneToOne(fetch = FetchType.EAGER)
    public CustomMessage getCustomMessage() {
        return customMessage;
    }
    public void setCustomMessage(CustomMessage customMessage) {
        this.customMessage = customMessage;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    public Users getSender() {
        return sender;
    }
    public void setSender(Users sender) {
        this.sender = sender;
    }
    public Date getSendDate() {
        return sendDate;
    }
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
    public Date getGetDate() {
        return getDate;
    }
    public void setGetDate(Date getDate) {
        this.getDate = getDate;
    }
    public int getIsRead() {
        return isRead;
    }
    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }
    public Date getReadDate() {
        return readDate;
    }
    public void setReadDate(Date readDate) {
        this.readDate = readDate;
    }

}
