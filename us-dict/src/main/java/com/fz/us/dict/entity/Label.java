package com.fz.us.dict.entity;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.entity.Entity;
import com.fz.us.dict.bean.SystemLabel;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Created with us-parent -> com.fz.us.core.entity.common.
 * User: min_xu
 * Date: 2014/9/10
 * Time: 22:52
 * 说明：字段配置包每个企业都会有一套，生成的时候从系统包拷贝复制一份，采用树形模型 企业版本-地址key -> form.field
 */
@javax.persistence.Entity
@Table(name="ss_label")
public class Label extends Entity {
    private static final long serialVersionUID = 5929110077551124922L;
    public Label(){
        super();
    }

    public Label(String manageId,SystemLabel.LabelEnum labelEnum,String address,String value){
        super();
        this.setManageId(manageId);
        this.setAddress(address);
        this.setValue(value);
        this.setLabelEnum(labelEnum);
        this.setState(BaseEnum.StateEnum.Enable);
    }

    /**
     * 是否生成对比信息
     * */
    private int resumeDiffed;
    /**
     * 通过专用配置文件xml进行配置
     * 地址key：需要将整个显示view通过地址整理 form.field、page.section.id
     * 系统有一套标准配置，当有新增时需要同步新增其他包，不做更新处理，后台显示原始名称，可以重置
     */
    private String address;
    /**
     * 字段显示名称
     */
    private String value;
    /**
     * 状态
     */
    private BaseEnum.StateEnum state;
    /**
     * 字段包Id
     * */
    private String manageId;
    /**
     * 字段的类型，表单、视图、其他等
     * */
    private SystemLabel.LabelEnum labelEnum;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getManageId() {
        return manageId;
    }
    public void setManageId(String manageId) {
        this.manageId = manageId;
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public BaseEnum.StateEnum getState() {
        return state;
    }
    public void setState(BaseEnum.StateEnum state) {
        this.state = state;
    }

    public SystemLabel.LabelEnum getLabelEnum() {
        return labelEnum;
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public void setLabelEnum(SystemLabel.LabelEnum labelEnum) {
        this.labelEnum = labelEnum;
    }


    public int getResumeDiffed() {
        return resumeDiffed;
    }

    public void setResumeDiffed(int resumeDiffed) {
        this.resumeDiffed = resumeDiffed;
    }
}
