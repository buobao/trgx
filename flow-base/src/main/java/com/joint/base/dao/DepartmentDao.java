package com.joint.base.dao;


import com.joint.base.entity.Department;

import java.util.List;

/**
 * Dao接口 - 
 * ============================================================================
 * 版权所有 2013 。
 * 
 * @author 
 * @version 0.1 2013-01-06
 * ============================================================================
 */

public interface DepartmentDao extends BaseEntityDao<Department, String> {

    public List<Department> getDepartments(String name);

}