package com.joint.base.dao;


import com.joint.base.entity.Role;
import com.joint.base.entity.Users;

import java.util.List;

/**
 * Dao接口 - 角色
 * ============================================================================
 * 版权所有 2013 。
 * 
 * @author 
 * @version 0.1 2013-1-16
 * ============================================================================
 */

public interface RoleDao extends BaseEntityDao<Role, String> {

    /**
     * 根据用户查找角色
     * @param users
     * @return
     */
    public List<String> findPnameByUser(Users users);
}