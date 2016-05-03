package com.joint.base.service.impl;

import com.fz.us.base.bean.Result;
import com.joint.base.bean.EnumManage;
import com.joint.base.dao.BaseEntityDao;
import com.joint.base.dao.DutyDao;
import com.joint.base.entity.*;
import com.joint.base.service.DutyService;
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
public class DutyServiceImpl extends BaseEntityServiceImpl<Duty, String> implements DutyService {
	
	@Resource
	DutyDao dutyDao;
	@Override
	public BaseEntityDao<Duty, String> getBaseEntityDao() {
		return dutyDao;
	}

    @Override
    public Duty getDuty(Users users, Department department, Post post) {
        return dutyDao.getDuty(users, department, post);
    }

    @Override
    public Duty getDefaultDuty(Users users) {
		return dutyDao.getDefaultDuty(users);
	}

    @Override
	public List<Department> getDepartments(Users users,Company company) {
		return dutyDao.getDepartments(users,company);
	}
    @Override
	public List<Duty> getDutys(Users users){
		return dutyDao.getDutys(users);
	}

    @Override
	public List<Department> getPrincipalDepartments(Users users) {
		return dutyDao.getPrincipalDepartments(users);
	}

    @Override
	public List<Department> getDepartments(Users users, Post post) {
		return dutyDao.getDepartments(users, post);
	}


    @Override
	public Users getDeputy(Department department) {
		return dutyDao.getDeputy(department);
	}

    @Override
	public Users getParentPrincipal(Users users) {
		return null;
	}

    @Override
	public Duty getParentPrincipal(Department department) {
		return dutyDao.getParentPrincipal(department);
	}

    @Override
	public List<Users> getPersons(Department department) {
		return dutyDao.getPersons(department);
	}

    @Override
	public List<Users> getPersons(Department department, Post post) {
		return dutyDao.getPersons(department, post);
	}

    @Override
	public List<Users> getPersons(Post post) {
		return dutyDao.getPersons(post);
	}

    @Override
	public List<Post> getPosts(Users users) {
		return dutyDao.getPosts(users);
	}

    @Override
	public List<Post> getPosts(Users users, Department department) {
		return dutyDao.getPosts(users, department);
	}

    @Override
	public Users getPrincipal(Users users) {
		return dutyDao.getPrincipal(users);
	}

    @Override
	public Users getPrincipal(Department department) {
		return dutyDao.getPrincipal(department);
	}
    @Override
    public Duty getPrincipalDuty(Department department) {
        return dutyDao.getPrincipalDuty(department);
    }

	@Override
	public Result updatePrinciple(Duty duty1) {
		Result r = new Result(200,"更新成功");
		Department department = duty1.getDepartment();
		Duty pduty = getPrincipalDuty(department);
		if(pduty != null){
			pduty.setDutyState(EnumManage.DutyState.Default);
			updateAndEnable(pduty);
		}
		duty1.setDutyState(EnumManage.DutyState.Principal);
		updateAndEnable(duty1);
		return r;
	}

	@Override
	public List<Duty> getDutys(Department department){
		return dutyDao.getDutys(department);
	}

    @Override
	public Duty getUsersDepartmentDuty(Department department,Users users){
		return dutyDao.getUsersDepartmentDuty(department, users);
	}
    @Override
    public List<Duty> getDutyByPost(Post post){
         return dutyDao.getDutyByPost(post);
    }

	@Override
	public Duty getDuty(Users users, Power power) {
		return dutyDao.getDuty(users,power);
	}

	@Override
	public List<Duty> findByPower(Power power) {
		return dutyDao.findByPower(power);
	}

}