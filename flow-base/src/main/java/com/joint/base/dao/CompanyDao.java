package com.joint.base.dao;


import com.joint.base.entity.Company;
import com.joint.base.entity.Users;

import java.util.List;

/**
 * Dao接口 - 岗位
 * ============================================================================
 * 版权所有 2013 。
 * 
 * @author 
 * @version 0.1 2013-1-16
 * ============================================================================
 */

public interface CompanyDao extends BaseEntityDao<Company, String> {
    /**
     * 根据企业查找成员
     * @param company
     * @return
     */
    public List<Users> findUsersByCompany(Company company);
}