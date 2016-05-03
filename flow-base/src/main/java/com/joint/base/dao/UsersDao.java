package com.joint.base.dao;


import com.joint.base.entity.Department;
import com.joint.base.entity.Users;
import com.joint.base.entity.system.Admin;

import java.util.List;


/**
 * Dao接口 - 用户信息
 * ============================================================================
 * 版权所有 2013 qshihua。
 * 
 * @author qshihua
 * @version 0.1 2013-05-06
 * ============================================================================
 */

public interface UsersDao extends BaseEntityDao<Users, String> {

	/**
	 * 通过手机号查找用户
	 * 
	 * @param mobile 手机号
	 * @return Users
	 */
	public Users getUsersByMobile(String mobile);

	/**
	 * 通过手机号查找已删除用户
	 *
	 * @param mobile 手机号
	 * @return Users
	 */
	public Users getUsersByMobileDel(String mobile);
	
	/**
	 * 通过邮箱查找用户
	 * 
	 * @param email 邮箱
	 * @return Users
	 */
	public Users getUsersByEmail(String email);


	/**
	 * 获取用户负责部门（包括下2级内部门）
	 * @param users 用户
	 * @return Department 集合
	 */
	public List<Department> getUsersPrincipal(Users users);

    /**
     * 获取帐号对应的用户
     * @param admin
     * @return
     */
    public Users getByAdmin(Admin admin);





}