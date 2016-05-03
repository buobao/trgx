package com.joint.base.parent;


import com.joint.base.bean.FlowEnum.ProcessState;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;


/**
 * 实体类 - 流程基类
 * ============================================================================
 * 版权所有 2014。
 * 
 * @author min_xu
 * 
 * @version 0.1 2014-01-20
 * ============================================================================
 */


@MappedSuperclass
public class BaseFlowEntity extends NameEntity implements Serializable {

	/**
	 * 文档状态
	 */
	private ProcessState processState;
	/**
	 * 流程版本号
	 */
	private int version;

	@Column(name="processState", length = 10)
	@Enumerated(EnumType.STRING)
	public ProcessState getProcessState() {
		return processState;
	}

	public void setProcessState(ProcessState processState) {
		this.processState = processState;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}