package com.joint.base.service;


import com.joint.base.entity.Role;
import com.joint.base.entity.Users;

import java.util.Set;

/**
 * Service接口 - 角色
 * ============================================================================
 * 版权所有 2013
 * ----------------------------------------------------------------------------
 * 
 * @author 
 * 
 * @version 0.1 2013-05-09
 */

public interface RoleService extends BaseEntityService<Role, String> {
    /**
     * 根据users别名查找role的别名
     * @param users
     * @return
     */
    public Set<String> findPnameByUser(Users users);
}