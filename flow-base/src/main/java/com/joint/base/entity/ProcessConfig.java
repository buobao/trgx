package com.joint.base.entity;

import com.joint.base.parent.BaseEntity;
import org.activiti.engine.repository.Model;

import javax.persistence.*;
import java.util.Set;


/**
 * 实体类 - 流程配置表
 * ============================================================================
 * 版权所有 2013 qshihua。
 * 
 * @author qshihua
 * 
 * @version 0.1 2013-01-06
 * ============================================================================
 */
@Entity
@Table(name="sys_processconfig")
public class ProcessConfig extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4685567410425008815L;

    /**
     * 流程定义Id
     */
	private String processDefinitionId;
    /**
     * 节点Id
     */
	private String activityId;
    /**
     * 特殊设定
     * 上一步操作用户-职权上级 001
     * 上一步操作用户所属部门负责人 010
     * 上一步操作用户所属部门的上级部门负责人 100
     */
    private int type;
    /**
     * 操作设置
     * 审批 001
     * 退回 010
     * 通过 100
     */
    private int actionType;
    /**
     * 业务配置
     */
    private BusinessConfig businessConfig;

    /**
     * 通用配置
     */
    private CommonConfig commonConfig;

    /**
     * 自定义域
     * @return
     */
    private String variable;


	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}


    @ManyToOne(fetch = FetchType.LAZY)
    public BusinessConfig getBusinessConfig() {
        return businessConfig;
    }

    public void setBusinessConfig(BusinessConfig businessConfig) {
        this.businessConfig = businessConfig;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    @OneToOne(cascade=CascadeType.ALL)
    public CommonConfig getCommonConfig() {
        return commonConfig;
    }

    public void setCommonConfig(CommonConfig commonConfig) {
        this.commonConfig = commonConfig;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }
}
