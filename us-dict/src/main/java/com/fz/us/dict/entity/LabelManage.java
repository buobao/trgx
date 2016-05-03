package com.fz.us.dict.entity;


import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.entity.Entity;
import com.fz.us.dict.bean.SystemLabel;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Created with us-parent -> com.fz.us.dict.entity.
 * User: min_xu
 * Date: 2014/9/10
 * Time: 23:08
 * 说明：
 */
@javax.persistence.Entity
@Table(name="ss_labelmanage")
public class LabelManage extends Entity {
    private static final long serialVersionUID = 5929110077551124922L;

    public LabelManage(){
        super();
    }
    public LabelManage(int version,SystemLabel.ManageEnum manage,String companyId){
        super();
        this.setVersion(version);
        this.setManage(manage);
        this.setCompanyId(companyId);
        this.setState(BaseEnum.StateEnum.Enable);
    }
    /**
     * 字段包版本，每次有新的包时发布，有更新时可以缓存整个包，每次更新版本加1
     */
    private int version;
    /**
     * 字段包的行业，每家公司都有一个所属行业，可自定义包
     * 如果companyId为空，则是系统自带语言包
     * */
    private SystemLabel.ManageEnum manage;
    /**
     * 状态
     */
    private BaseEnum.StateEnum state;
    /**
     * 所属公司
     */
    private String companyId;


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public SystemLabel.ManageEnum getManage() {
        return manage;
    }

    public void setManage(SystemLabel.ManageEnum manage) {
        this.manage = manage;
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
