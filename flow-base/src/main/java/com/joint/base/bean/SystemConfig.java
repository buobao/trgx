package com.joint.base.bean;

/**
 * Created with us2 -> com.fz.us.web.bean.
 * User: min_xu
 * Date: 2014-12-02
 * Time: 15:39
 * 说明：
 */
public class SystemConfig {
    private String name;

    public String getWebroot() {
        return webroot;
    }

    public void setWebroot(String webroot) {
        this.webroot = webroot;
    }

    private String webroot;

    private String appname;
    private String smsname;
    private String audioPath;
    private String officeId;
    private String type;

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getSmsname() {
        return smsname;
    }

    public void setSmsname(String smsname) {
        this.smsname = smsname;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
