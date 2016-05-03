package com.joint.base.service;


import com.joint.base.entity.Permission;

import java.util.List;

/**
 * Service接口 - 职权
 * ============================================================================
 * 版权所有 2013
 * ----------------------------------------------------------------------------
 * 
 * @author 
 * 
 * @version 0.1 2013-05-09
 */

public interface PermissionService extends BaseEntityService<Permission, String> {

    /**
     * 根据 businessKey查找Permission
     * @param businessKey
     * @return
     */
    public Permission getByKey(String businessKey);

    /**
     * 根据类型获取permission
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