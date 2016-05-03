package com.joint.base.service.impl;

import com.joint.base.dao.BaseEntityDao;
import com.joint.base.dao.CommonConfigDao;
import com.joint.base.entity.*;
import com.joint.base.service.CommonConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * Service实现类 - 
 * ============================================================================
  * 版权所有 2013 。
 * ----------------------------------------------------------------------------
 * 
 * @author 
 * 
 * @version 0.1 2013-01-06
 */
@Service
public class CommonConfigServiceImpl extends BaseEntityServiceImpl<CommonConfig, String> implements CommonConfigService {
	
	@Resource
    CommonConfigDao commonConfigDao;

	@Override
	public BaseEntityDao<CommonConfig, String> getBaseEntityDao() {
		return commonConfigDao;
	}

	@Override
	public List<Role> findRoleById(String id) {
		return commonConfigDao.findRoleById(id);
	}

	@Override
	public List<Department> findDepartmentById(String id) {
		return commonConfigDao.findDepartmentById(id);
	}

	@Override
	public List<Post> findPostById(String id) {
		return commonConfigDao.findPostById(id);
	}

	@Override
	public List<Power> findPowerById(String id) {
		return commonConfigDao.findPowerById(id);
	}

    @Override
    public List<Duty> findDutyByCfgInDepart(String id) {
        return commonConfigDao.findDutyByCfgInDepart(id);
    }

    @Override
    public List<Duty> findDutyByCfgInPost(String id) {
        return commonConfigDao.findDutyByCfgInPost(id);
    }

    @Override
    public List<Duty> findDutyByCfgInPower(String id) {
        return commonConfigDao.findDutyByCfgInPower(id);
    }

    @Override
    public List<Duty> findDutyByCfgInRole(String id) {
        return commonConfigDao.findDutyByCfgInRole(id);
    }

    @Override
    public List<Duty> findDutyByConfig(String id) {
        return commonConfigDao.findDutyByConfig(id);
    }
}