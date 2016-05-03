package com.joint.base.dao;


import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Pager;
import com.joint.base.bean.FlowEnum;
import com.joint.base.entity.BusinessConfig;
import com.joint.base.entity.Users;
import com.joint.base.parent.BaseFlowEntity;

import java.io.Serializable;
import java.util.Map;


/**
 * Dao接口 - 用户信息
 * ============================================================================
 * 版权所有 2013 qshihua。
 * 
 * @author qshihua
 * @version 0.1 2013-05-06
 * ============================================================================
 */

public interface BaseFlowDao<T extends BaseFlowEntity,PK extends Serializable> extends BaseEntityDao<T, PK> {
    /**
     * 根据当前user,计算草稿
     * @param users --
     * @param pager
     * @param rmap
     * @return
     */
    public Pager findByPagerAndDraft(Users users, Pager pager, Map<String,Object> rmap);
    /**
     * 根据权限计算显示数据
     * @return
     */
    public Pager findByPagerAndLimit(boolean type,String key, Pager pager,Map<String,Object> rmap);
    /**
     * 根据权限归档计算显示数据
     * @return
     */
    public Pager findByPagerAndFinish(BusinessConfig bdcg, Pager pager, Map<String,Object> rmap);

    /**
     * 显示退回中数据
     * @param pager
     * @param rmap
     * @return
     */
    public Pager findByPagerAndBack(Pager pager,Map<String,Object> rmap);


    /**
     * 根据状态查询单子
     * @param pager
     * @param users
     * @param rmap
     * @return
     */
    public Pager findByPagerAndProcessState(Pager pager,Users users,String businessKey,FlowEnum.ProcessState processState,Map<String,Object> rmap);

}