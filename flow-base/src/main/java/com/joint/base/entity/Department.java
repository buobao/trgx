package com.joint.base.entity;


import com.joint.base.parent.BaseEntity;
import com.joint.base.util.excel.annotation.ExcelField;

import javax.persistence.*;
import java.util.Set;

/**
 * 实体类 -部门
 * ============================================================================
 * 版权所有 2014 。
 * 
 * @author min_xu
 * 
 * @version 0.1 2014-01-20
 * ============================================================================
 */
@Entity
@Table(name="sys_department")
public class Department extends BaseEntity {
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
	 * 是否为系统内置部门 1
	 */
	private int beSystem;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 岗位
	 */
	private Set<Post> postSet;
	/**
	 * 排序
	 */
	private Integer orderList;
	/**
	 * 父节点
	 */
	private Department parent;
	/**
	 * 标识
	 */
	private String pname;
	/**
	 * 子节点
	 */
	private Set<Department> children;
	/**
	 *状态 停用
	 */
	private boolean enabled;
	/**
	 *令牌
	 */
	private String value;
	/**
	 * 职责
	 */
	private Set<Power> powerSet;
	
	@Column(nullable = false)
	@ExcelField(title="部门名称", align=2, sort=10)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(length = 5000)
	@ExcelField(title="部门描述", align=2, sort=20)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	/*
	@JoinTable(name="control",
		joinColumns=@JoinColumn(name="department_id", referencedColumnName="id"),
		inverseJoinColumns=@JoinColumn(name="post_id", referencedColumnName="id")
		)
	*/
	@OrderBy("name asc")
	public Set<Post> getPostSet() {
		return postSet;
	}

	public void setPostSet(Set<Post> postSet) {
		this.postSet = postSet;
	}
	
	public Integer getOrderList() {
		return orderList;
	}

	public void setOrderList(Integer orderList) {
		this.orderList = orderList;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@ExcelField(title="上级部门", align=2, value="parent.name", sort=30)
	public Department getParent() {
		return parent;
	}

	public void setParent(Department parent) {
		this.parent = parent;
	}
	
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy("orderList asc")
	public Set<Department> getChildren() {
		return children;
	}

	public void setChildren(Set<Department> children) {
		this.children = children;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	/*
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="control",
		joinColumns=@JoinColumn(name="department_id", referencedColumnName="id"),
		inverseJoinColumns=@JoinColumn(name="person_id", referencedColumnName="id")
		)
	@OrderBy("name asc")
	public Set<Person> getPersonSet() {
		return personSet;
	}

	public void setPersonSet(Set<Person> personSet) {
		this.personSet = personSet;
	}
	*/
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	

	@OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
	@OrderBy("id asc")
	public Set<Power> getPowerSet() {
		return powerSet;
	}

	public void setPowerSet(Set<Power> powerSet) {
		this.powerSet = powerSet;
	}

	public int getBeSystem() {
		return beSystem;
	}

	public void setBeSystem(int beSystem) {
		this.beSystem = beSystem;
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

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}
}