package com.joint.base.entity;

import com.fz.us.base.util.DataUtil;
import com.joint.base.bean.EnumManage;
import com.joint.base.parent.BaseEntity;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with us-parent -> com.fz.us.core.entity.common.
 * User: min_xu
 * Date: 2014/9/28
 * Time: 16:04
 * 说明：
 */
@Entity
@Table(name="ss_custommessage")
public class CustomMessage extends BaseEntity {

    private static final long serialVersionUID = 4266539342844717420L;
    /**
     * 消息级别
     */
    private EnumManage.CustomMessageLevelEnum msgLevel;
    /**
     * 消息类型
     */
    private EnumManage.CustomMessageTypeEnum msgType;
    /**
     * 消息关键字
     */
    private String msgKey;
    /**
     * 消息标题
     */
    private String msgTitle;
    /**
     * 消息内容
     */
    private String msgContent;
    /**
     * 消息时间
     */
    private Date msgDate;
    /**
     * 发送人
     */
    private Users sender;
    /**
     * 接收人
     */
    private Users receiver;
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
    public String getMsgTitle() {
        return msgTitle;
    }
    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }
    public String getMsgContent() {
        return msgContent;
    }
    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }
    public Date getMsgDate() {
        return msgDate;
    }
    public void setMsgDate(Date msgDate) {
        this.msgDate = msgDate;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    public Users getSender() {
        return sender;
    }
    public void setSender(Users sender) {
        this.sender = sender;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    public Users getReceiver() {
        return receiver;
    }
    public void setReceiver(Users receiver) {
        this.receiver = receiver;
    }

    public Map<String, Object> toMap(){
        Map<String,Object> row = new HashMap<String,Object>();
        row.put("msgType", msgType.key());
        if(StringUtils.isNotEmpty(msgKey)){
            row.put("msgKey", msgKey);
        }
        if(StringUtils.isNotEmpty(msgTitle)){
            row.put("msgTitle", msgTitle);
        }
        if(StringUtils.isNotEmpty(msgContent)){
            row.put("msgContent", msgContent);
        }
        if(msgDate!=null){
            row.put("msgDate", DataUtil.DateToString(msgDate, "yyyy-MM-dd HH:mm"));
        }
        if(sender!=null){
            row.put("sender", sender.getId());
        }
        if(receiver!=null){
            row.put("receiver", receiver.getId());
        }
        row.put("id", super.getId());
        return row;
    }

    public Map<String, Object> toEasyMap(){
        Map<String,Object> row = new HashMap<String,Object>();
        row.put("msgType", msgType.key());
        if(StringUtils.isNotEmpty(msgKey)){
            row.put("msgKey", msgKey);
        }
        if(StringUtils.isNotEmpty(msgContent)){
            row.put("msgContent", msgContent);
        }
        return row;
    }

}
