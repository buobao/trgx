package com.joint.base.service;

import com.joint.base.entity.*;

import java.util.List;


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

public interface CommonConfigService extends BaseEntityService<CommonConfig, String> {

	/**
	 * 通过CommonConfig对象的id，得到Roles
	 * @param id
	 * @return
	 */
	public  List<Role> findRoleById(String id);

	/**
	 * 通过CommonConfig对象的id，得到Departments
	 * @param id
	 * @return
	 */
	List<Department> findDepartmentById(String id);

	/**
	 * 通过CommonConfig对象的id，得到Posts
	 * @param id
	 * @return
	 */
	List<Post> findPostById(String id);

	/**
	 * 通过CommonConfig对象的id，得到Powers
	 * @param id
	 * @return
	 */
	List<Power> findPowerById(String id);

    /**
     * 通过CommonConfig对象的departmentSet找到对应的duty列表
     * @param id
     * @return
     */
    List<Duty> findDutyByCfgInDepart(String id);

    /**
     * 通过CommonConfig对象的postSet找到对应的duty列表
     * @param id
     * @return
     */
    List<Duty> findDutyByCfgInPost(String id);

    /**
     * 通过CommonConfig对象的powerSet找到对应的duty列表
     * @param id
     * @return
     */
    List<Duty> findDutyByCfgInPower(String id);
    /**
     * 通过CommonConfig对象的roleSet找到对应的duty列表
     * @param id
     * @return
     */
    List<Duty> findDutyByCfgInRole(String id);
    /**
     * 通过部门，岗位，职权，角色共同查询职责
     * @param id
     * @return
     */
    public List<Duty> findDutyByConfig(String id);
}
