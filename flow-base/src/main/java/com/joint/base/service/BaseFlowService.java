package com.joint.base.service;


import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Pager;
import com.joint.base.bean.FlowEnum;
import com.joint.base.entity.Users;
import com.joint.base.parent.BaseFlowEntity;

import java.util.Map;

/**
 * Service接口 - 管理员
 * ============================================================================
 * 版权所有 2014 上海玖达信息职业技术有限公司，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 
 * @author hpj
 * 
 * @version 0.1 2014-11-18
 */

public interface BaseFlowService<T extends BaseFlowEntity,PK extends String> extends BaseEntityService<T, PK> {
    /**
     * 保存流程
     * @param entity -- 实体类
     * @param process -- 流程定义名称
     * @param various --- 需要传入的流程变量 （必须包含numStatus）
     * @return
     */
    public PK save(T entity, String process, Map<String, Object> various);

    /**
     * 提交业务（包含保存操作）
     * @param entity
     * @param process
     * @param var1 --- 保存时候所需要的变量
     * @param var2 --- 提交时候所需要的变量
     * @return
     */
    public PK commit(T entity, String process, Map<String, Object> var1, Map<String, Object> var2, String curDutyId);

    /**
     * 提交业务（包含保存操作）
     * @param entity
     * @param process
     * @param var1
     * @param var2
     * @param varAssign
     * @param curDutyId
     * @return
     */
    public PK commit(T entity, String process, Map<String, Object> var1, Map<String, Object> var2, String taskDefKey, String varAssign, String curDutyId);
    /**
     * 审批更新（文档 状态需要改变的时候）
     * @param entity
     * @param processState 文档状态
     * @param var1
     * @parm curDutyId--dutyId
     */
    public void approve(T entity, FlowEnum.ProcessState processState, Map<String, Object> var1, String curDutyId,String comment);

    /**
     * 审批更新（文档 状态需要改变的时候,无需改变的时候null）
     * @param entity
     * @param processState
     * @param var
     * @param var
     * @param curDutyId
     */
    public void approve(T entity, FlowEnum.ProcessState processState, Map<String, Object> var, String taskDefKey, String varAssign, String curDutyId);


    /**
     * 退回操作
     * @param entity
     * @param taskDefKey --- task的Id
     * @param numStatus --- numStatus
     */
    public void reject(T entity, String taskDefKey, int numStatus,String comment,String curDutyId);

    /**
     * 废止操作
     * @param entity
     */
    public void destroy(T entity);

    /**
     * 否决操作
     * @param entity
     * @param taskDefKey --- task的Id
     */
    public void deny(T entity, String taskDefKey,String comment,String curDutyId);

    /**
     * 根据当前user,计算草稿
     * @param users --
     * @param pager
     * @param rmap
     * @return
     */
    public Pager findByPagerAndDraft(Users users, Pager pager,Map<String,Object> rmap);
    /**
     * 根据权限计算显示数据
     * @return
     */
    public Pager findByPagerAndLimit(boolean type,String processKey, Pager pager,Map<String,Object> rmap);
    /**
     * 根据权限归档计算显示数据
     * @return
     */
    public Pager findByPagerAndFinish(String processKey, Pager pager, Map<String,Object> rmap);

    /**
     * 显示退回中数据
     * @param pager
     * @param rmap
     * @return
     */
    public Pager findByPagerAndBack(Pager pager, Map<String,Object> rmap);

    /**
     * 根据文档状态查找
     * @param pager
     * @param users
     * @param rmap
     * @return
     */
    public Pager findByPagerAndProcessState(Pager pager, Users users,String businessKey,FlowEnum.ProcessState processState,Map<String, Object> rmap);

}