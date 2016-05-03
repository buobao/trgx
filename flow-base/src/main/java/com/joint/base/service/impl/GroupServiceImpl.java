package com.joint.base.service.impl;

import com.joint.base.dao.BaseEntityDao;
import com.joint.base.dao.GroupDao;
import com.joint.base.entity.Group;
import com.joint.base.service.GroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * Service实现类 - 
 * ============================================================================
 * 版权所有 2013
 * ----------------------------------------------------------------------------
 * 
 * @author 
 * 
 * @version 0.1 2011-6-13
 */

@Service
public class GroupServiceImpl extends BaseEntityServiceImpl<Group, String> implements GroupService {
	
	@Resource
	GroupDao groupDao;


	@Override
	public BaseEntityDao<Group, String> getBaseEntityDao() {
		return groupDao;
	}
}