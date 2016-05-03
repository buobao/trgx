package com.joint.base.service;


import com.joint.base.entity.BusinessConfig;
import com.joint.base.entity.Duty;
import com.joint.base.entity.ProcessConfig;

import java.util.List;

/**
 * Service接口 - 管理员
 * ============================================================================
 * ----------------------------------------------------------------------------
 * 
 * @author yan_zhou
 * 
 * @version 0.1 2011-6-13
 */

public interface ProcessConfigService extends BaseEntityService<ProcessConfig, String> {
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
	 * 获取特殊设定
	 * @param processConfig
	 * @param hisDuty
	 * @return
	 */
	public List<String> findSpecailByUser(ProcessConfig processConfig, Duty hisDuty);

	/**
	 * 获取特殊设定中的人员职责
	 * @param processConfig
	 * @param hisDuty
	 * @return
	 */
	public List<Duty> findDutyInSpecail(ProcessConfig processConfig, Duty hisDuty);

	/**
	 * 获取当前节点常规设定人员
	 * @param pConfig
	 * @return
	 */
	public List<Duty> findDutyByConfig(ProcessConfig pConfig);
	/**
	 * 根据群组找职责
	 * @param processConfig
     * todo delete
	 * @return
	 */
	public List<Duty> findDutyByGroup(ProcessConfig processConfig);

	/**
	 * 根据部门找职责
	 * @param processConfig
     * todo delete
	 * @return
	 */
	public List<Duty> findDutyByDepart(ProcessConfig processConfig);

	/**
	 * 根据岗位找职责
	 * @param processConfig
     * todo delete
	 * @return
	 */
	public List<Duty> findDutyByPost(ProcessConfig processConfig);

	/**
	 * 根据职权找职责
	 * @param processConfig
     * todo delete
	 * @return
	 */
	public List<Duty> findDutyByPower(ProcessConfig processConfig);
}