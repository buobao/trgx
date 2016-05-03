package com.joint.base.dao;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Pager;
import com.fz.us.base.dao.BaseDao;
import com.joint.base.entity.Company;
import com.joint.base.entity.Users;
import com.joint.base.parent.BaseEntity;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Dao接口 - Dao实体基类接口
 * ============================================================================
 * 版权所有 2014。
 * 
 * @author fallenpanda
 * @version 0.1 2014-07-26
 * ============================================================================
 */

public interface BaseEntityDao<T extends BaseEntity, PK extends Serializable> extends BaseDao<T, PK> {

    /**
     * 根据Pager对象进行查询(提供分页、查找、排序功能).
     *
     * @param pager
     *            Pager对象
     * @param states
     *            states数组
     * @return Pager对象
     */
    public Pager findByPagerAndStates(Pager pager, BaseEnum.StateEnum[] states);

    /**
     * 根据Pager对象进行查询(所有有效的对象).
     *
     * @param pager
     *            Pager对象
     * @return Pager对象
     */

    public Pager queryValidByPager(Pager pager);

    /**
     * 根据公司查找
     * @param pager
     * @param company
     * @param states
     * @returnfindByPagerAndLimit
     */
    public Pager findByPagerAndCompany(Pager pager, Users users,
                                       Company company, BaseEnum.StateEnum[] states);

    /**
     *根据权限查找
     * @param pager
     * @param usersSet
     * @param company
     * @return
     */
    public Pager findByPagerAndLimit(Pager pager, Set<Users> usersSet,
                                     Company company, Map<String, Object> rmap);
}