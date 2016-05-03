package com.joint.base.dao;


import com.joint.base.entity.Department;
import com.joint.base.entity.Post;
import com.joint.base.entity.Power;

import java.util.List;

/**
 * Dao接口 - 职权
 * ============================================================================
 * 版权所有 2013 。
 * 
 * @author 
 * @version 0.1 2013-1-16
 * ============================================================================
 */

public interface PowerDao extends BaseEntityDao<Power, String> {
    /**
     * 根据departId查找power
     * @param departId
     * @return
     */
    public List<Power> getByDepartId(String departId);

    /**
     * 根据department和post查找power
     * @param department
     * @param post
     * @return
     */
    public Power getPowerByDepartAndPost(Department department, Post post);

    /**
     * 根据postId查找power
     * @param postId
     * @return
     */
    public List<Power> getByPostId(String postId);

    /**
     * 根据职权id来删除流程配置中的职权
     * @param powerId
     * @return
     */
    public int delConfigPower(String powerId);

    /**
     * 根据职权id来删除角色中的职权
     * @param powerId
     * @return
     */
    public int delRolePower(String powerId);

}