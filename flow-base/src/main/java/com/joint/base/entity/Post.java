package com.joint.base.entity;

import com.joint.base.parent.BaseEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * 实体类 -岗位
 * ============================================================================
 * 版权所有 2014 。
 * 版权所有 2014 。
 *
 * @author min_xu
 * 
 * @version 0.1 2014-01-20
 * ============================================================================
 */
@Entity
@Table(name="sys_post")
public class Post extends BaseEntity {
	private static final long serialVersionUID = -6614052029623997372L;
	/**
	 * 名称
	 */
	private String name; 
	/**
	 * 编号
	 */
	private String no; 
	/**
	 * 令牌
	 */
	private String value; 
	/**
	 * 是否为系统内置部门 1
	 */
	private int beSystem;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 人员
	 
	private Set<Users> userSet; */
	/**
	 * 标识
	 */
	private String pname;
	/**
	 * 部门
	 */
	private Set<Department> departmentSet;
	/**
	 * 职责
	 */
	private Set<Power> powerSet;
	
	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(length = 5000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	/*
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="control",
		joinColumns=@JoinColumn(name="post_id", referencedColumnName="id"),
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
	@ManyToMany(cascade = {CascadeType.ALL},fetch = FetchType.LAZY, mappedBy = "postSet")
	@OrderBy("name asc")
	public Set<Department> getDepartmentSet() {
		return departmentSet;
	}

	public void setDepartmentSet(Set<Department> departmentSet) {
		this.departmentSet = departmentSet;
	}
	
	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}
	
	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
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

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}
}