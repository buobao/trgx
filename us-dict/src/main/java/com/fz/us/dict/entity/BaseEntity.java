package com.fz.us.dict.entity;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.entity.Entity;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

/**
 * Created with us-parent -> com.fz.us.core.entity.
 * User: min_xu
 * Date: 2014/8/12
 * Time: 15:38
 * 说明：US基类，所有US对象继承此类
 */
@MappedSuperclass
public class BaseEntity extends Entity {
    private static final long serialVersionUID = 5929110077551124922L;

    /**
     * 所属公司
     */
    private String companyId;
    /**
     * 文档状态
     */
    private BaseEnum.StateEnum state;
    /**
     * 名称
    */
    private String name;
    /**
     * 拼音首字母
     */
    private String pinYinHead;
    /**
     * 拼音
     */
    private String pinYin;


    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinYinHead() {
        return pinYinHead;
    }

    public void setPinYinHead(String pinYinHead) {
        this.pinYinHead = pinYinHead;
    }

    public String getPinYin() {
        return pinYin;
    }

    public void setPinYin(String pinYin) {
        this.pinYin = pinYin;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public BaseEnum.StateEnum getState() {
        return state;
    }
    public void setState(BaseEnum.StateEnum state) {
        this.state = state;
    }
}
