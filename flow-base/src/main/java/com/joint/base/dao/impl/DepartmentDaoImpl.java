package com.joint.base.dao.impl;

import com.fz.us.base.bean.BaseEnum;
import com.joint.base.dao.DepartmentDao;
import com.joint.base.entity.Department;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


/**
 * Dao实现类 - 
 * ============================================================================
 * 版权所有 2013 。
 * 
 * @author 
 * @version 0.1 2013-01-06
 * ============================================================================
 */

@Repository
public class DepartmentDaoImpl extends BaseEntityDaoImpl<Department, String> implements DepartmentDao {


    @Override
    public List<Department> getDepartments(String name) {
        Assert.notNull(name,"name is required");
        String hql="from Department as d where d.name=:n and d.state=:s";
        return getSession().createQuery(hql).setParameter("n",name).setParameter("s", BaseEnum.StateEnum.Enable).list();
    }
}
