package com.joint.base.dao;


import com.joint.base.entity.Post;

/**
 * Dao接口 - 岗位
 * ============================================================================
 * 版权所有 2013 。
 * 
 * @author 
 * @version 0.1 2013-1-16
 * ============================================================================
 */

public interface PostDao extends BaseEntityDao<Post, String> {
    /**
     * 根据岗位名称查找岗位
     * @param name -- 岗位名称
     * @return
     */
    public Post getPostByName(String name);
}