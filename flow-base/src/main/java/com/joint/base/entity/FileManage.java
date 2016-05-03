package com.joint.base.entity;


import com.fz.us.base.bean.BaseEnum;
import com.joint.base.parent.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 实体类 - 文件
 * ============================================================================
 * 版权所有 2014 。
 * 
 * @author min_xu
 * 
 * @version 0.1 2014-01-20
 * ============================================================================
 */
@Entity
@Table(name="ss_filemanage")
public class
        FileManage extends BaseEntity {

/**
	 * 
	 */
//	private static final long serialVersionUID = 5929110077551124922L;
//	private static final long serialVersionUID = -8739107962173432604L;
    /**
     * 目标对象
     */
    private String targetId;
    private String targetClass;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 路径
	 */
	private String url;
	/**
	 * gridFSId
	 */
	private String gridId;
    /**
     * keyId
     */
    private String keyId;
	/**
	 * 和业务相关的节点ID
	 */
	private String taskId;
	/**
	 * 流程实例Id
	 */
	private String proIntanceId;
	/**
	 * size
	 */
	private long size;
	/**
	 * 存放文件类型
	 */
	private String type;

	public FileManage(){
		super();
	}
	public FileManage(String filename, String gridId, String keyId, String proIntanceId, String taskId, String type){
		super();
		this.name = filename;
		this.gridId = gridId;
		this.keyId = keyId;
		this.taskId = taskId;
		this.proIntanceId=proIntanceId;
		this.type = type;
		this.url = "file.action?keyId="+gridId;
		this.state = BaseEnum.StateEnum.Enable;
	}
	public FileManage(String filename, String gridId, String keyId, String proIntanceId, String taskId){
		super();
		this.name = filename;
		this.gridId = gridId;
		this.keyId = keyId;
		this.taskId = taskId;
		this.proIntanceId=proIntanceId;
		this.url = "file.action?keyId="+gridId;
		this.state = BaseEnum.StateEnum.Enable;
	}
	public FileManage(String filename, String gridId, String keyId){
		super();
		this.name = filename;
		this.gridId = gridId;
		this.keyId = keyId;
		this.url = "file.action?keyId="+gridId;
		this.state = BaseEnum.StateEnum.Enable;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getGridId() {
		return gridId;
	}

	public void setGridId(String gridId) {
		this.gridId = gridId;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

	public String getProIntanceId() {
		return proIntanceId;
	}

	public void setProIntanceId(String proIntanceId) {
		this.proIntanceId = proIntanceId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }
}
