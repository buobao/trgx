package com.joint.base.service;

import com.joint.base.entity.Post;


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

public interface PostService extends BaseEntityService<Post, String> {
	/**
	 * 根据名称查找
	 * 
	 */
	public Post getPostByName(String name);
}