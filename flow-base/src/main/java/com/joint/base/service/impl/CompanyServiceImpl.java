package com.joint.base.service.impl;

import com.joint.base.dao.BaseEntityDao;
import com.joint.base.dao.CompanyDao;
import com.joint.base.entity.Company;
import com.joint.base.entity.Users;
import com.joint.base.service.CompanyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


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
public class CompanyServiceImpl extends BaseEntityServiceImpl<Company, String> implements CompanyService {
	
	@Resource
    CompanyDao companyDao;


	@Override
	public BaseEntityDao<Company, String> getBaseEntityDao() {
		return companyDao;
	}

    @Override
    public List<Users> findUsersByCompany(Company company) {
        return companyDao.findUsersByCompany(company);
    }
}