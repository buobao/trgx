package com.joint.base.service;

import com.fz.us.base.bean.Result;
import com.joint.base.entity.*;

import java.util.List;


/**
 * Service接口 - 
 * ============================================================================
 * 版权所有 2013
 * ----------------------------------------------------------------------------
 * 
 * @author 
 * 
 * @version 0.1 2013-05-09
 */

public interface DutyService extends BaseEntityService<Duty, String> {
	public Duty getDuty(Users users, Department department, Post post);

    /**
	 * 用户默认职责Duty
	 * 
	 */
	public Duty getDefaultDuty(Users users);
	/**
	 * 用户所属所有部门
	 * 
	 */
	public List<Department> getDepartments(Users users, Company company);
	
	
	public List<Duty> getDutys(Users users);
	/**
	 * 用户负责的所属所有部门
	 * 
	 */
	public List<Department> getPrincipalDepartments(Users users);
	
	/**
	 * 用户所属所有岗位
	 * 
	 */
	public List<Post> getPosts(Users users);
	
	/**
	 * 用户某个部门所属所有岗位
	 * 
	 */
	public List<Post> getPosts(Users users, Department department);
	
	/**
	 * 用户某个岗位所属所有部门
	 * 
	 */
	public List<Department> getDepartments(Users users, Post post);
	
	/**
	 * 用户默认部门负责人
	 * 
	 */
	public Users getPrincipal(Users users);

	
	/**
	 * 用户上级部门负责人，如果没有，则以此类推再上级部门
	 *
	 */
	public Users getParentPrincipal(Users users);
	

	/**
	 * 获取部门下所有用户
	 * 
	 * @return 用户列表
	 */
	public List<Users>  getPersons(Department department);
	/**
	 * 获取部门、岗位下所有用户
	 * 
	 * @return 用户列表
	 */
	public List<Users>  getPersons(Department department, Post post);
	/**
	 * 获取部门负责人
	 * 
	 * @return 用户列表
	 */
	public Users getPrincipal(Department department);

	/**
	 * 用户部门副职
	 * 
	 */
	public Users getDeputy(Department department);

	/**
	 * 获取岗位下所有用户
	 * 
	 * @return 用户列表
	 */
	public List<Users>  getPersons(Post post);
	
	/**
	 * 获取部门下的职责清单
	 * */
	public List<Duty> getDutys(Department department);
	
	/**
	 * 获取部门下的用户职责
	 * */
	public Duty getUsersDepartmentDuty(Department department, Users users);

    /**
     * 根据岗位获取该岗位下的职责
     */
    public List<Duty> getDutyByPost(Post post);

	/**
	 * 根据人员和职权查找职责
	 * @param users
	 * @param power
	 * @return
	 */
	public Duty getDuty(Users users, Power power);

	/**
	 * 根据职权查找职责
	 * @param power
	 * @return
	 */
	public List<Duty> findByPower(Power power);
	/**
	 * 用户上级部门负责人
	 *
	 */
	public Duty getParentPrincipal(Department department);
	/**
	 * 获取部门领导人的职责
	 * @param department
	 * @return
	 */
	public Duty getPrincipalDuty(Department department);



	/**
	 * 更新principe
	 * @param duty1
	 * @return
	 */
	public Result updatePrinciple(Duty duty1);
}