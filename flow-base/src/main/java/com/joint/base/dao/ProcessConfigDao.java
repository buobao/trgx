package com.joint.base.dao;

import com.joint.base.entity.BusinessConfig;
import com.joint.base.entity.Duty;
import com.joint.base.entity.ProcessConfig;
import com.joint.base.entity.Users;

import java.util.List;

/**
 * Dao接口 - 项目信息表
 * ============================================================================
 * 版权所有 2013 qshihua。
 * 
 * @author qshihua
 * @version 0.1 2013-1-16
 * ============================================================================
 */

public interface ProcessConfigDao extends BaseEntityDao<ProcessConfig, String> {
	/**
	 * 根据流程定义Id和activityId查找对应的配置
	 * @param processDefinitionId
	 * @param activtyId
	 * @return
	 */
	public ProcessConfig findConfigByActivityId(String processDefinitionId, String activtyId);

	/**
	 * 根据业务配置表找到对应所有流程配置
	 * @param businessConfig
	 * @return
	 */
	public List<ProcessConfig> findConfigByBcfg(BusinessConfig businessConfig);

	/**
	 * 根据config找到里面的职位（包括user的默认职位）
	 * @param pConfig
	 * @return
	 */
	public List<Duty> findDutyByConfig(ProcessConfig pConfig, Users users);
    /**
     * 根据config找到里面的职位
     * @param pConfig
     * @return
     */
    public List<Duty> findDutyByConfig(ProcessConfig pConfig);


	/**
	 * 根据群组找职责
	 * @param processConfig
	 * @return
	 */
	public List<Duty> findDutyByGroup(ProcessConfig processConfig);

	/**
	 * 根据部门找职责
	 * @param processConfig
	 * @return
	 */
	public List<Duty> findDutyByDepart(ProcessConfig processConfig);

	/**
	 * 根据岗位找职责
	 * @param processConfig
	 * @return
	 */
	public List<Duty> findDutyByPost(ProcessConfig processConfig);

	/**
	 * 根据职权找职责
	 * @param processConfig
	 * @return
	 */
	public List<Duty> findDutyByPower(ProcessConfig processConfig);



}