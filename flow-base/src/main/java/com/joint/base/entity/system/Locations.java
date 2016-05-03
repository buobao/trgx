package com.joint.base.entity.system;


import com.joint.base.bean.EnumManage;
import com.joint.base.parent.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with us-parent -> com.fz.us.core.entity.system.
 * User: min_xu
 * Date: 2014/9/28
 * Time: 16:05
 * 说明：
 */
@Entity
@Table(name="sys_locations")
public class Locations extends BaseEntity {

    private static final long serialVersionUID = -2991181662166642223L;

    /**
     * 定位方式
     */
    private int locType;
    /**
     * 经度
     */
    private BigDecimal longitude;
    /**
     * 纬度
     */
    private BigDecimal latitude;
    /**
     * 精度半径
     */
    private BigDecimal radius;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String county;
    /**
     * 地址
     */
    private String address;
    /**
     * 时间
     */
    private Date logDate;
    /**
     * 坐标类别
     */
    private EnumManage.CoordTypeEnum coordType;
    /**
     * 微信用户Id
     */
    private String openId;
    /**
     * 目标对象
     */
    private String targetId;
    private String targetClass;

    @Column(precision=16,scale=8)
    public BigDecimal getLongitude() {
        return longitude;
    }
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
    @Column(precision=16,scale=8)
    public BigDecimal getLatitude() {
        return latitude;
    }
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
    public Date getLogDate() {
        return logDate;
    }
    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }
    @Enumerated(EnumType.STRING)
    public EnumManage.CoordTypeEnum getCoordType() {
        return coordType;
    }
    public void setCoordType(EnumManage.CoordTypeEnum coordType) {
        this.coordType = coordType;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCounty() {
        return county;
    }
    public void setCounty(String county) {
        this.county = county;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public int getLocType() {
        return locType;
    }
    public void setLocType(int locType) {
        this.locType = locType;
    }
    public BigDecimal getRadius() {
        return radius;
    }
    public void setRadius(BigDecimal radius) {
        this.radius = radius;
    }
    public String getOpenId() {
        return openId;
    }
    public void setOpenId(String openId) {
        this.openId = openId;
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
}

