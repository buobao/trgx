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
@Table(name="sys_role")
public class Role extends BaseEntity {
    private static final long serialVersionUID = -6614052029623997372L;
    /**
     * 名称
     */
    private String name;
    /**
     *别名
     */
    private String pName;
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
     * 是否为系统内置部门 1
     */
    private int beSystem;
    /**
     * 描述
     */
    private String description;
    /**
     * 用户
     */
    private Set<Users> usersSet;
    /**
     * 部门
     */
    private Set<Department> departSet;
    /**
     * 部门
     */
    private Set<Power> powertSet;
    /**
     * 权限
     */
    private Set<Permission> permissionSet;
    /**
     * 排序
     */
    private Integer orderList;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public int getBeSystem() {
        return beSystem;
    }

    public void setBeSystem(int beSystem) {
        this.beSystem = beSystem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @OrderBy("name asc")
    public Set<Users> getUsersSet() {
        return usersSet;
    }

    public void setUsersSet(Set<Users> usersSet) {
        this.usersSet = usersSet;
    }

    public Integer getOrderList() {
        return orderList;
    }

    public void setOrderList(Integer orderList) {
        this.orderList = orderList;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @OrderBy("name asc")
    public Set<Permission> getPermissionSet() {
        return permissionSet;
    }

    public void setPermissionSet(Set<Permission> permissionSet) {
        this.permissionSet = permissionSet;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="sys_role_depart",joinColumns = @JoinColumn(name="id"),inverseJoinColumns = @JoinColumn(name="depid"))
    @OrderBy("name asc")
    public Set<Department> getDepartSet() {
        return departSet;
    }

    public void setDepartSet(Set<Department> departSet) {
        this.departSet = departSet;
    }


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="sys_role_power",joinColumns = @JoinColumn(name="id"),inverseJoinColumns = @JoinColumn(name="powerid"))
    @OrderBy("name asc")
    public Set<Power> getPowertSet() {
        return powertSet;
    }

    public void setPowertSet(Set<Power> powertSet) {
        this.powertSet = powertSet;
    }

    public String getPName() {
        return pName;
    }

    public void setPName(String pName) {
        this.pName = pName;
    }
}