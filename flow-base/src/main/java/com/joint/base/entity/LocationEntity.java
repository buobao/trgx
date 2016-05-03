package com.joint.base.entity;

import com.fz.us.base.entity.Entity;
import com.joint.base.bean.EnumManage;
import com.joint.base.entity.system.Locations;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created with us-parent -> com.fz.us.core.entity.system.
 * User: min_xu
 * Date: 2014/9/28
 * Time: 16:05
 * 说明：
 */
@javax.persistence.Entity
@Table(name="ss_locationentity")
public class LocationEntity extends Entity {

    private static final long serialVersionUID = -2991181662166642223L;

    public LocationEntity(){
        super();
    }
    public LocationEntity(Locations locations, String targetId, String targetClass){
        super();

        this.LocationsId = locations.getId();
        this.targetId = targetId;
        this.targetClass = targetClass;
        this.coordType = EnumManage.CoordTypeEnum.bd09ll;


    }
    public LocationEntity(String targetId, String targetClass){
        super();
    }
    /**
     * 目标对象，指向的对关联对象
     */
    private String targetId;
    private String targetClass;
    /**
     * 临时关联Locations
     * */
    private String LocationsId;

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
     * 完整路径
     * */
    private String fullAddress;
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
     * 坐标类别
     */
    private EnumManage.CoordTypeEnum coordType;
    /**
     * 用户Id
     * */
    private String usersId;
    /**
     * 百度地图转化的p2aJSON
     * */
    private String addressJson;

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
    public BigDecimal getRadius() {
        return radius;
    }
    public void setRadius(BigDecimal radius) {
        this.radius = radius;
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

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getLocationsId() {
        return LocationsId;
    }

    public void setLocationsId(String locationsId) {
        LocationsId = locationsId;
    }

    public String getUsersId() {
        return usersId;
    }

    public void setUsersId(String usersId) {
        this.usersId = usersId;
    }
    @Column(length = 65535)
    public String getAddressJson() {
        return addressJson;
    }

    public void setAddressJson(String addressJson) {
        this.addressJson = addressJson;
    }

}

