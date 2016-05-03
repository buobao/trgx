package com.joint.base.entity.system;

import com.fz.us.base.entity.Entity;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Created with us-parent -> com.fz.us.core.entity.common.
 * User: min_xu
 * Date: 2014/9/28
 * Time: 16:00
 * 说明：
 */
@javax.persistence.Entity
@Table(name="ss_sms")
public class Sms extends Entity {
    private static final long serialVersionUID = 7790183356145922978L;

    public Sms(){
        super();
    }
    /**
     * 构造函数
     * @param
     * @param
     * @param （DocumentStateEnum）
     */
    public Sms(String msg, String phone, SendStateEnum sendState) {
        super();
        this.msg = msg;
        this.phone = phone;
        this.setSendState(sendState);
    }

    /**
     * 用户
     */
    private String msg;
    /**
     * 余额
     */
    private String phone;
    /**
     * 备注
     */
    private String remark;
    /**
     * 发送状态
     */
    private SendStateEnum sendState;

    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Enumerated(EnumType.STRING)
    public SendStateEnum getSendState() {
        return sendState;
    }
    public void setSendState(SendStateEnum sendState) {
        this.sendState = sendState;
    }

    public static enum SendStateEnum{
        /**
         * 创建
         */
        FRESH("创建", 1),
        /**
         * 成功
         */
        SUCCESS("成功", 1),
        /**
         * 失败
         */
        FAIL("失败", 2);

        private final String value;
        private final int key;

        private SendStateEnum(final String value, final int key) {
            this.value = value;
            this.key = key;
        }
        public String value() {
            return this.value;
        }
        public int key() {
            return this.key;
        }

    }
}

