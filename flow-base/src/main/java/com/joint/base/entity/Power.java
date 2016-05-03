package com.joint.base.entity;

import com.joint.base.parent.BaseEntity;
import com.joint.base.util.excel.annotation.ExcelField;

import javax.persistence.*;
import java.util.Set;

/**
 * 实体类 -职权（部门-岗位）
 * ============================================================================
 * 版权所有 2014 。
 * 
 * @author min_xu
 * 
 * @version 0.1 2014-01-20
 * ============================================================================
 */
@Entity
@Table(name="sys_power")
public class Power extends BaseEntity {
	private static final long serialVersionUID = -6614052029623997372L;
	/**
	 * 名称
	 */
	private String name; 
	/**
	 * 拼音首字母
	 */
	private String pinYinHead;
	/**
	 * 拼音
	 */
	private String pinYin;
	/**
	 * 编号
	 */
	private String no;
	/**
	 * 描述
	 */
	private String description;

	/**
	 * 岗位
	 */
	private Post post;
	/**
	 * 部门
	 */
	private Department department;
	/**
	 * 是否默认岗位，同一个人的职责类，只存在一个默认1,否则0
	 * */
	private int powerDefault;
	/**
	 * 排序
	 */
	private Integer orderList;
	/**
	 * 父节点
	 */
	private Power parent;
	/**
	 * 子节点
	 */
	private Set<Power> children;


	@Column(nullable = false)
	@ExcelField(title="职权名称", align=1, sort=1)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(length = 5000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}


	
	public Integer getOrderList() {
		return orderList;
	}

	public void setOrderList(Integer orderList) {
		this.orderList = orderList;
	}

	public String getPinYinHead() {
		return pinYinHead;
	}

	public void setPinYinHead(String pinYinHead) {
		this.pinYinHead = pinYinHead;
	}

	public String getPinYin() {
		return pinYin;
	}

	public void setPinYin(String pinYin) {
		this.pinYin = pinYin;
	}


	@ManyToOne(fetch = FetchType.LAZY)
	public Post getPost() {
		return post;
	}

	@ExcelField(title="岗位", align=1, sort=2, value="post.name" )
	public void setPost(Post post) {
		this.post = post;
	}

	@ExcelField(title="部门", align=1, sort=3, value ="department.name" )
	@ManyToOne(fetch = FetchType.LAZY)
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@ExcelField(title="上级职权", align=1, sort=4, value="parent.name" )
	@ManyToOne(fetch = FetchType.LAZY)
	public Power getParent() {
		return parent;
	}

	public void setParent(Power parent) {
		this.parent = parent;
	}

	@OneToMany(mappedBy = "parent",fetch = FetchType.LAZY)
	@OrderBy("name asc")
	public Set<Power> getChildren() {
		return children;
	}

	public void setChildren(Set<Power> children) {
		this.children = children;
	}

	public int getPowerDefault() {
		return powerDefault;
	}

	public void setPowerDefault(int powerDefault) {
		this.powerDefault = powerDefault;
	}
}