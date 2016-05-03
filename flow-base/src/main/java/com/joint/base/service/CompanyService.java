package com.joint.base.service;

import com.joint.base.entity.Company;
import com.joint.base.entity.Users;

import java.util.List;


/**
 * Service接口 - 组织
 * ============================================================================
 * 版权所有 2013
 * ----------------------------------------------------------------------------
 * 
 * @author 
 * 
 * @version 0.1 2013-05-09
 */

public interface CompanyService extends BaseEntityService<Company, String> {
    public List<Users> findUsersByCompany(Company company);
}