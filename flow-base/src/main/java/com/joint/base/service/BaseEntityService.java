package com.joint.base.service;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Pager;
import com.fz.us.base.bean.Result;
import com.fz.us.base.service.BaseService;
import com.joint.base.entity.Company;
import com.joint.base.entity.Users;
import com.joint.base.parent.BaseEntity;
import org.hibernate.criterion.DetachedCriteria;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Service接口 - Service实体基类接口
 * ============================================================================
 * 版权所有 2014。
 * 
 * @author fallenpanda
 * @version 0.1 2014-07-26
 * ============================================================================
 */

public interface BaseEntityService<T extends BaseEntity, PK extends Serializable> extends BaseService<T, PK> {
    /**
	 * 保存对象
	 * @param entity
	 * 				对象
	 * @param state
	 * 				文档状态
	 */
	public PK save(T entity, BaseEnum.StateEnum state);

    public PK save(T entity);

    public PK saveAndEnable(T entity);

    public T updateAndEnable(T entity);

    public T updateNameAndEnable(T entity);

    public void enable(T entity);

    public void disable(T entity);

    /**
	 * 根据Pager对象进行查询(提供分页、查找、排序功能).
	 * @param pager
	 *            	Pager对象
	 * @param creater
	 * 				创建人
	 * @param states
	 * 				文档状态
	 * 
	 * @return Pager对象
	 */
	public Pager findByPagerBy(Pager pager, Users creater, BaseEnum.StateEnum... states);

	/**
	 * 得到所有清单
	 * @param creater
	 * 				创建人
	 * @param states
	 * 				文档状态
	 * 
	 * @return
	 */
	public List<T> getListBy(Users creater, BaseEnum.StateEnum... states);

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
     * 根据Pager对象进行查询(所有启用的对象).
     *
     * @param pager
     *            Pager对象
     * @return Pager对象
     */
    public Pager queryEnableByPager(Pager pager);

    /**
     * 根据Pager对象进行查询(所有被删除的对象).
     *
     * @param pager
     *            Pager对象
     * @return Pager对象
     */
    public Pager queryDeletedByPager(Pager pager);

    /**
     * 根据Pager对象进行查询(所有有效的对象).
     *
     * @param pager
     *            Pager对象
     * @return Pager对象
     */
    public Pager queryValidByPager(Pager pager);

    /**
     * 验证对象读者权限
     *
     * @param entity
     *            entity对象
     * @return Result
     */
    public Result checkReadAccess(T entity, Users users);
    /**
     * 验证对象编辑权限
     *
     * @param entity
     *            entity对象
     * @return Result
     */
    public Result checkEditAccess(T entity, Users users);

    /**
     * 分页查询
     * @param pager
     * @param detachedCriteria
     * @return
     */
    public Pager findByPager(Pager pager, DetachedCriteria detachedCriteria);

    /**
     * 根据公司查找
     * @param pager
     * @param users
     * @param company
     * @param states
     * @return
     */
    public Pager findByPagerAndCompany(Pager pager, Users users,
                                       Company company, BaseEnum.StateEnum[] states);

    public Pager findByPagerAndCompany(Pager pager, Users users,
                                       Company company, Map<String, Object> para);

    /**
     * 根据时间范围查找数据
     * @param pager
     * @param searchDate
     * @param beginDate
     * @param endDate
     * @param users
     * @param company
     * @param para
     * @return
     */
    public Pager findByPagerAndCompany(Pager pager, String searchDate, Date beginDate, Date endDate, Users users, Company company, Map<String, Object> para);
}