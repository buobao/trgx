package com.joint.base.service.impl;


import com.google.common.collect.Sets;
import com.joint.base.dao.BaseEntityDao;
import com.joint.base.dao.RoleDao;
import com.joint.base.entity.Role;
import com.joint.base.entity.Users;
import com.joint.base.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;


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
public class RoleServiceImpl extends BaseEntityServiceImpl<Role, String> implements RoleService {
	
	@Resource
    RoleDao roleDao;



	@Override
	public BaseEntityDao<Role, String> getBaseEntityDao() {
		return roleDao;
	}

	@Override
	public Set<String> findPnameByUser(Users users) {
		List<String> roleList = roleDao.findPnameByUser(users);
		Set<String> roleSet = Sets.newHashSet();
		for(String id: roleList){
			Role role = roleDao.get(id);
			roleSet.add(role.getPName());
		}

		return  roleSet;
	}
}