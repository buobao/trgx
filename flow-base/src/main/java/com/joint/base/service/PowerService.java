package com.joint.base.service;

import com.joint.base.entity.Department;
import com.joint.base.entity.Duty;
import com.joint.base.entity.Post;
import com.joint.base.entity.Power;

import java.util.List;
import java.util.Set;


/**
 * Service接口 - 
 * ============================================================================
 * 版权所有 2013
 * ----------------------------------------------------------------------------
 * 
 * @author 
 * 
 * @version 0.1 2013-05-09
 */

public interface PowerService extends BaseEntityService<Power, String> {
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
     * 获取当前职责的上一级职权的职责
     * @param duty
     * @return
     */
    public Set<Duty> findParentByPower(Duty duty);

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