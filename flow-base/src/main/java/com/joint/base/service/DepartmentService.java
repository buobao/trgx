package com.joint.base.service;

import com.joint.base.entity.Department;

import java.util.List;
import java.util.Set;


/**
 * Service接口 - 
 * ============================================================================
 * 版权所有 2013 。
 * ----------------------------------------------------------------------------
 * 
 * @author 
 * 
 * @version 0.1 2013-01-06
 */

public interface DepartmentService extends BaseEntityService<Department, String> {

	/**
	 * 根据名称查找
	 * 
	 */
	public List<Department> getDepartments(String name);

    public Set<Department> getChildDepart(Department department, Set<Department> set);
}
