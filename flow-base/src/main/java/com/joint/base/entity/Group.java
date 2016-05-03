package com.joint.base.entity;

import com.joint.base.parent.BaseEntity;

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
@Table(name="sys_group")
public class Group extends BaseEntity {
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
	 * 群组类型  用户群组-0001,部门群组-0010,职权群组-0100
	 */
	private int type;
	/**
	 * 岗位群组
	 */
	private Set<Power> powerSet;
	/**
	 * 部门群组
	 */
	private Set<Department> departmentSet;
	/**
	 * 人员群组
	 */
	private Set<Users> usersSet;
	/**
	 * 排序
	 */
	private Integer orderList;

	
	@Column(nullable = false)
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



	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="sys_group_power",
			joinColumns ={@JoinColumn(name="id")},
			inverseJoinColumns = {@JoinColumn(name="power_id")})
	@OrderBy("name asc")
	public Set<Power> getPowerSet() {
		return powerSet;
	}

	public void setPowerSet(Set<Power> powerSet) {
		this.powerSet = powerSet;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="sys_group_depart",
			joinColumns ={@JoinColumn(name="id")},
	inverseJoinColumns = {@JoinColumn(name="dep_id")})
	@OrderBy("name asc")
	public Set<Department> getDepartmentSet() {
		return departmentSet;
	}

	public void setDepartmentSet(Set<Department> departmentSet) {
		this.departmentSet = departmentSet;
	}
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="sys_group_users",
			joinColumns ={@JoinColumn(name="id")},
			inverseJoinColumns = {@JoinColumn(name="user_id")})
	@OrderBy("name asc")
	public Set<Users> getUsersSet() {
		return usersSet;
	}

	public void setUsersSet(Set<Users> usersSet) {
		this.usersSet = usersSet;
	}
}