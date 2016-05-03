package com.joint.base.entity.module;

import com.fz.us.base.entity.MongoEntity;
import com.joint.base.bean.module.EventBean;

/**
 * Created by hpj on 2015/5/5.
 */
public class Resume extends MongoEntity {

    /**
     *事件类型
     */
    private EventBean.EventEnum event;

    /**
     * 事件等级
     */
    private EventBean.EventLevel level;
    /**
     * 发生事件文字描述
     * */
    private String text;
    /**
     * 发生事件可能存在的对象封装(需要通过gson进行二次转化)
     * */
    private String jsonObject;
    /**
     * 发生事件可能存在的对象封装(需要通过gson进行二次转化)
     * */
    private String preJsonObject;
    /**
     * 发生事件可能存在的不可封装的对象转化成文字，比如修改记录比较结果
     * */
    private String textObject;
    /**
     * 公司对应的companyId
     */
    private String companyId;
    /**
     * targetId
     */
    private String targetId;
    /**
     * targetClass
     */
    private String targetClass;

    private EventBean.TargetTypeEnum targetType;
    private EventBean.ServiceEnum service;
    /**
     * 产生这个履历发生的Class和Method
     * */
    private String eventClass;
    private String eventMethod;

    private String eventDate;
    /**
     * 产生履历的Id
     */
    private String userId;

    private String bizerName;
    private String bizerHeadImageId;

    public Resume(){
        super();
    }
    public Resume(EventBean.EventEnum event,String text,String targetId,String targetClass){
        super();
        setEvent(event);
        setText(text);
        setTargetId(targetId);
        setTargetClass(targetClass);
    }

    public Resume(EventBean.EventEnum event,String text,String targetId,String targetClass,String bizerId,String companyId){
        super();
        setEvent(event);
        setText(text);
        setTargetId(targetId);
        setTargetClass(targetClass);
        setUserId(bizerId);
        setCompanyId(companyId);
    }


    public EventBean.EventEnum getEvent() {
        return event;
    }

    public void setEvent(EventBean.EventEnum event) {
        this.event = event;
    }

    public EventBean.EventLevel getLevel() {
        return level;
    }

    public void setLevel(EventBean.EventLevel level) {
        this.level = level;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(String jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getPreJsonObject() {
        return preJsonObject;
    }

    public void setPreJsonObject(String preJsonObject) {
        this.preJsonObject = preJsonObject;
    }

    public String getTextObject() {
        return textObject;
    }

    public void setTextObject(String textObject) {
        this.textObject = textObject;
    }

    public EventBean.TargetTypeEnum getTargetType() {
        return targetType;
    }

    public void setTargetType(EventBean.TargetTypeEnum targetType) {
        this.targetType = targetType;
    }

    public EventBean.ServiceEnum getService() {
        return service;
    }

    public void setService(EventBean.ServiceEnum service) {
        this.service = service;
    }

    public String getEventClass() {
        return eventClass;
    }

    public void setEventClass(String eventClass) {
        this.eventClass = eventClass;
    }

    public String getEventMethod() {
        return eventMethod;
    }

    public void setEventMethod(String eventMethod) {
        this.eventMethod = eventMethod;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBizerName() {
        return bizerName;
    }

    public void setBizerName(String bizerName) {
        this.bizerName = bizerName;
    }

    public String getBizerHeadImageId() {
        return bizerHeadImageId;
    }

    public void setBizerHeadImageId(String bizerHeadImageId) {
        this.bizerHeadImageId = bizerHeadImageId;
    }
}
