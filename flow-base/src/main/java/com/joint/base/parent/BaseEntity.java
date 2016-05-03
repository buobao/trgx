package com.joint.base.parent;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.entity.Entity;
import com.joint.base.entity.Company;
import com.joint.base.entity.Users;

import javax.persistence.*;
import java.util.Set;

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
     * 创建人
     */
    protected Users creater;
    /**
     * 所属公司
     */
    private Company company;

    /**
     * 文档状态
     */
    protected BaseEnum.StateEnum state;

    @ManyToOne(fetch = FetchType.LAZY)
    public Users getCreater() {
        return creater;
    }
    public void setCreater(Users creater) {
        this.creater = creater;
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public BaseEnum.StateEnum getState() {
        return state;
    }
    public void setState(BaseEnum.StateEnum state) {
        this.state = state;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Company getCompany() {
        return company;
    }
    public void setCompany(Company company) {
        this.company = company;
    }

}
