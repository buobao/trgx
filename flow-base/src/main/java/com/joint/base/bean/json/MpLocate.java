package com.joint.base.bean.json;

import java.math.BigDecimal;

/**
 * Created by hpj on 2015/3/10.
 */
public class MpLocate {
    /**
     * 开发者微信号
     */
    private String toUserName;
    /**
     * 发送方帐号（一个OpenID）
     */
    private String fromUserName;
    /**
     * 消息创建时间 （整型）
     */
    private int createTime;
    /**
     * 消息类型，event
     */
    private String msgType;
    /**
     * 事件类型，LOCATION
     */
    private String event;
    /**
     * 地理位置纬度
     */
    private BigDecimal latitude;
    /**
     * 地理位置经度
     */
    private BigDecimal longitude;
    /**
     * 地理位置精度
     */
    private BigDecimal precision;

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public int getCreateTime() {
        return createTime;
    }

    public void setCreateTime(int createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getPrecision() {
        return precision;
    }

    public void setPrecision(BigDecimal precision) {
        this.precision = precision;
    }
}
