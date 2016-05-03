package com.joint.base.entity;

import com.joint.base.parent.BaseEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by gcw on 2015/2/4.
 */
@Entity
@Table(name="sys_commonconfig")
public class CommonConfig extends BaseEntity {

    /**
     * checkBox type
     * 0000-所有人(请选择)，0001-角色，0010-部门 ，0100-职权，1000-岗位
     */
    private int stype;
    /**
     * 群组配置
     */
    private Set<Role> roleSet;

    /**
     * 部门配置
     */
    private Set<Department> departmentSet;
    /**
     * 岗位配置
     */
    private Set<Post> postSet;
    /**
     * 职权配置
     */
    private Set<Power> powerSet;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="sys_config_power",joinColumns = @JoinColumn(name="id"),inverseJoinColumns = @JoinColumn(name="powerid"))
    @OrderBy("name asc")
    public Set<Power> getPowerSet() {
        return powerSet;
    }

    public void setPowerSet(Set<Power> powerSet) {
        this.powerSet = powerSet;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="sys_config_post",joinColumns = @JoinColumn(name="id"),inverseJoinColumns = @JoinColumn(name="postid"))
    @OrderBy("name asc")
    public Set<Post> getPostSet() {
        return postSet;
    }

    public void setPostSet(Set<Post> postSet) {
        this.postSet = postSet;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="sys_config_depart",joinColumns = @JoinColumn(name="id"),inverseJoinColumns = @JoinColumn(name="depid"))
    @OrderBy("name asc")
    public Set<Department> getDepartmentSet() {
        return departmentSet;
    }

    public void setDepartmentSet(Set<Department> departmentSet) {
        this.departmentSet = departmentSet;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="sys_config_role",joinColumns = @JoinColumn(name="id"),inverseJoinColumns = @JoinColumn(name="roleid"))
    @OrderBy("name asc")
    public Set<Role> getRoleSet() {
        return roleSet;
    }

    public void setRoleSet(Set<Role> roleSet) {
        this.roleSet = roleSet;
    }

    public int getStype() {
        return stype;
    }

    public void setStype(int stype) {
        this.stype = stype;
    }
}
