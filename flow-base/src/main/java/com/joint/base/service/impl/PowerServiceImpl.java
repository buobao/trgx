package com.joint.base.service.impl;

import com.google.common.collect.Sets;
import com.joint.base.dao.BaseEntityDao;
import com.joint.base.dao.PowerDao;
import com.joint.base.entity.Department;
import com.joint.base.entity.Duty;
import com.joint.base.entity.Post;
import com.joint.base.entity.Power;
import com.joint.base.service.DutyService;
import com.joint.base.service.PowerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;


/**
 * Service实现类 - 职权
 * ============================================================================
 * 版权所有 2013
 * ----------------------------------------------------------------------------
 * 
 * @author 
 * 
 * @version 0.1 2011-6-13
 */

@Service
public class PowerServiceImpl extends BaseEntityServiceImpl<Power, String> implements PowerService {
	
	@Resource
	PowerDao powerDao;
	@Resource
	public DutyService dutyService;


	@Override
	public BaseEntityDao<Power, String> getBaseEntityDao() {
		return powerDao;
	}

	@Override
	public List<Power> getByDepartId(String departId) {
		return powerDao.getByDepartId(departId);
	}

	@Override
	public Power getPowerByDepartAndPost(Department department, Post post) {
		return powerDao.getPowerByDepartAndPost(department, post);
	}

	@Override
	public Set<Duty> findParentByPower(Duty duty) {
		Power power = duty.getPower();
		Set<Duty> dutySet = Sets.newHashSet();
		if(power.getParent()==null){
			return dutySet;
		}
		List<Duty> dutyList = dutyService.findByPower(power.getParent());
		dutySet.addAll(dutyList);
		return dutySet;
	}

    @Override
    public List<Power> getByPostId(String postId) {
        return powerDao.getByPostId(postId);
    }

    @Override
    public int delConfigPower(String powerId) {
        return powerDao.delConfigPower(powerId);
    }

    @Override
    public int delRolePower(String powerId) {
        return powerDao.delRolePower(powerId);
    }
}