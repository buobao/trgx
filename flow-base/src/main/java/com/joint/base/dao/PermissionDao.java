package com.joint.base.dao;

import com.joint.base.entity.Permission;

import java.util.List;

/**
 * Dao接口 - 岗位
 * ============================================================================
 * 版权所有 2013 。
 * 
 * @author hpj
 * @version 0.1 2013-1-16
 * ============================================================================
 */

public interface PermissionDao extends BaseEntityDao<Permission, String> {
    /**
     * 根据key来查找Permission
     * @param businessKey
     * @return
     */
    public Permission getByKey(String businessKey);

    /**
     * 根据资源类型查找
     * @param type
     * @return
     */
    public List<Permission> findAllByType(String type);

    /**
     * 根据Id来获取到当前人访问的资源
     * @param userId
     * @return
     */
    public List<Permission> getAllByUser(String userId);

    /**
     * 更新实体的文档状态
     */
    public void updateState(String key, String keyId);
}