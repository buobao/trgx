package com.joint.base.dao.impl;


import com.joint.base.dao.CompanyDao;
import com.joint.base.entity.Company;
import com.joint.base.entity.Users;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Dao实现类 - 岗位
 * ============================================================================
 * 版权所有 2013 。
 * 
 * @author 
 * @version 0.1 2013-1-16
 * ============================================================================
 */

@Repository
public class CompanyDaoImpl extends BaseEntityDaoImpl<Company, String> implements CompanyDao {

    @Override
    public List<Users> findUsersByCompany(Company company) {
        Assert.notNull(company,"company is required");
        String sql="SELECT su.id from sys_users as su WHERE su.company_id=(SELECT sc.id from sys_company as sc WHERE sc.id=:i) AND su.state='enable'";
        return getSession().createSQLQuery(sql).setParameter("i",company.getId()).list();
    }
}