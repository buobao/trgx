package com.joint.base.service;

import com.fz.us.base.bean.Pager;
import com.fz.us.base.bean.Result;
import com.joint.base.bean.EnumManage;
import com.joint.base.entity.Company;
import com.joint.base.entity.Department;
import com.joint.base.entity.Users;
import com.joint.base.entity.system.Admin;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Service接口 - 用户信息
 * ============================================================================
 * 版权所有 2013 qshihua。
 * ----------------------------------------------------------------------------
 * 
 * @author qshihua
 * 
 * @version 0.1 2013-05-06
 */

public interface UsersService extends BaseEntityService<Users, String> {
    /**
     * App中转化为列表Map集合
     * @param
     * @return
     */
    public Map<String, Object> getEasyMap(Users users);

    /**
     * App中转化为列表Map集合
     * @param
     * @return
     */
    public Map<String, Object> getListItemMap(Users users);
    /**
     * App中得到登录用户的信息
     * @param users
     * @return
     */
    public Map<String, Object> getLoginItemMap(Users users);

    public String salt(Users users);

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
     * 获取登录人信息
     * @return
     */
    public Users getLoginInfo();

	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 * @param name
	 * @return
     * todo delete
	 */
	public String encodedPassword(String name, String password, String salt);

	/**
	 * 验证密码
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
     * todo delete
	 */
	public boolean validatePassword(String plainPassword, String password);

    /**
     * 注册用户和帐号
     * @param name
     * @param password
     * @param company
     * @return
     */
    public Result registerUserAndAdmin(String name, String password, Company company);

    /**
     * 注册用户和帐号
     * @param users
     * @param company
     * @return
     */
    public Result registerUserAndAdmin(Users users, Company company);


    /**
     * 注册企业和用户
     * @param name
     * @param password
     * @return
     */
    public Result registerCompanyAndUser(String name, String password);

    /**
     * 体验帐号注册
     * @param username
     * @param users
     * @return
     */
    public Result registerExperienceUser(String username, Users users);


    /**
     * 根据当前登录人查找公司
     * @return
     */
    public Company getCompanyByUser();
    public Company getCompanyByUser(Users creater);

    public String getMpAccount(Users users, String openId);

    /**
     * @param name
     * @return
     */
    public String openId(String name);

    /**
     * @param users
     * @return
     */
    public String openId(Users users);

    /**
     * 根据admin获取users
     * @param admin
     * @return
     */
    public Users getByAdmin(Admin admin);

    /**
     * 关联账户（微信）验证用户名和密码，通过Shiro来实现的Login
     * @param httpServletResponse
     * @param admin
     * @param openId
     * @return
     */
    public Result linkUser(HttpServletResponse httpServletResponse, Admin admin, String openId);

    /**
     * 发送短消息验证
     * @param username
     * @param passwordForget
     * @return
     */
    public Result sendAuthCode(String username, EnumManage.AuthCodeEnum passwordForget, int experience, Users users);

    /**
     * 判断是否微信登录
     * @param openId
     * @return
     */
    public boolean isWxUsers(String openId);

    /**
     * 重置密码
     * @param users
     * @param password
     * @throws Exception
     */
    public void ResetPass(Users users, String password);

    /**
     * 根据部门查找该部门成员
     * @param department
     * @return
     */
    public Pager findNotInDep(Pager pager, Department department, Map rmap);

    public Pager findInDep(Pager pager, Department department, Map rmap);

    /**
     * 根据openId来查找用户信息
     * @param openId
     * @return
     */
    public Users getUsersByOpenId(String openId);

    /**
     * 重置密码
     * */
    public Result resetPassword(String name, String password);
}