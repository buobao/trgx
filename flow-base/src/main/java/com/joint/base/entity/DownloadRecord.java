package com.joint.base.entity;


import com.joint.base.parent.BaseEntity;

import javax.persistence.*;

/**
 * 实体类 -  下载中心记录
 * ============================================================================
 * 版权所有 2014 。
 * 
 * @author min_xu
 * 
 * @version 0.1 2014-01-20
 * ============================================================================
 */

@Entity
@Table(name="ss_downloadrecord")
public class DownloadRecord extends BaseEntity {

	private static final long serialVersionUID = -7894524049778924812L;
	private String name;
	private String parentId;
	/**
	 * 文件
	 */
	private FileManage file;
	private String tmp_CompanyId;

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	@OneToOne(fetch = FetchType.LAZY)
	public FileManage getFile() {
		return file;
	}
	public void setFile(FileManage file) {
		this.file = file;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Transient
	public String getTmp_CompanyId() {
		return tmp_CompanyId;
	}
	public void setTmp_CompanyId(String tmp_CompanyId) {
		this.tmp_CompanyId = tmp_CompanyId;
	}
	
}
