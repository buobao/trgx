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
@Table(name="sys_permission")
public class Permission extends BaseEntity {
	private static final long serialVersionUID = -6614052029623997372L;

    /**
     * 名称
     */
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * 排序
     */
    private Integer sortNumber;
    /**
     * 链接
     */
    private String url;
    /**
     * 图标
     */
    private String iconCls;
    /**
     * 图标颜色
     */
    private String iconcolor;
    /**
     * 描述
     */
    private String description;
    /**
     * 父节点
     */
    private Permission parent;
    /**
     * 子节点
     */
    private Set<Permission> children;
    /**
     * 权限
     */
    private Set<Role> roleSet;
    /**
     * 关键字
     */
    private String businessKey;
    /**
     * 等级
     */
    private int level;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconcolor() {
        return iconcolor;
    }

    public void setIconcolor(String iconcolor) {
        this.iconcolor = iconcolor;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Permission getParent() {
        return parent;
    }

    public void setParent(Permission parent) {
        this.parent = parent;
    }

    @OneToMany(mappedBy = "parent",fetch = FetchType.LAZY)
    @OrderBy("sortNumber asc")
    public Set<Permission> getChildren() {
        return children;
    }

    public void setChildren(Set<Permission> children) {
        this.children = children;
    }

    @Transient
    public Set<Role> getRoleSet() {
        return roleSet;
    }

    public void setRoleSet(Set<Role> roleSet) {
        this.roleSet = roleSet;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}