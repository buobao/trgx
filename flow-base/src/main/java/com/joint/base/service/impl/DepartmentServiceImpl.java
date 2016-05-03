package com.joint.base.service.impl;

import com.joint.base.dao.BaseEntityDao;
import com.joint.base.dao.DepartmentDao;
import com.joint.base.entity.Department;
import com.joint.base.service.DepartmentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;


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
public class DepartmentServiceImpl extends BaseEntityServiceImpl<Department, String> implements DepartmentService {
	
	@Resource
    DepartmentDao departmentDao;

	@Override
	public BaseEntityDao<Department, String> getBaseEntityDao() {
		return departmentDao;
	}



	public List<Department> getDepartments(String name) {
		return departmentDao.getDepartments(name);
	}

    @Override
    public Set<Department> getChildDepart(Department department,Set<Department> set) {
        Set<Department> departmentChildren=null;
        if (department != null) {
           departmentChildren = department.getChildren();
           set.add(department);
        }else{
            return set;
        }

        if(departmentChildren!=null){
            for(Department depart:departmentChildren){
                getChildDepart(depart,set);
            }
        }else{
            return set;
        }
        return set;
    }

}