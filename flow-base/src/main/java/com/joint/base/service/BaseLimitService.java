package com.joint.base.service;

import com.fz.us.base.bean.Pager;
import com.joint.base.entity.Company;
import com.joint.base.entity.Users;
import com.joint.base.parent.BaseEntity;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by amin on 2015/5/10.
 */
public interface BaseLimitService<T extends BaseEntity, PK extends Serializable> extends BaseEntityService<T, PK> {
    /**
     * 根据权限查找
     * @param pager
     * @return
     */
    public Pager findByPagerAndLimit(Pager pager, Users users, Company company, Map<String, Object> rmap);
}
