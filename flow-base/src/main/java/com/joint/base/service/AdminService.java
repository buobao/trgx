package com.joint.base.service;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Result;
import com.joint.base.bean.EnumManage;
import com.joint.base.entity.system.Admin;

import java.util.List;


/**
 * Service接口 - 组织
 * ============================================================================
 * 版权所有 2013
 * ----------------------------------------------------------------------------
 * 
 * @author 
 * 
 * @version 0.1 2013-05-09
 */

public interface AdminService extends BaseEntityService<Admin, String> {

    /**
     * App 判断deviceId是否被重复绑定
     * @param admin
     * @param deviceId
     * @return
     */
    public boolean isDeviceIdRepeat(Admin admin, String deviceId);

    /**
     * 校验账号
     * 注册账号，在注册用户时使用（调用方法：手动注册，批量导入，体验注册）
     * @param name
     * @param password
     * @return
     */
    public Result registe(String name, String password, EnumManage.AccountType accountType);

    /**
     * 根据手机号码查找帐号
     * @param username
     * @return
     */
    public Admin getByMobile(String username);

    /**
     * 根据openId获取管理员对象，若管理员不存在，则返回null（区分大小写）
     * @param openId
     *
     * @return
     */
    public Admin getAdminByOpenId(String openId);

    /**
     * 更新openID判断是否重复
     * @param admin
     * @param openId
     * @return
     */
    public Result updateOpenId(Admin admin, String openId);

    /**
     * 加密
     * @param name
     * @param password
     * @param salt
     * @return
     */
    public String encodedPassword(String name, String password, String salt);

    public Admin getAdminByAccount(String name);

    /**
     * 获得所有Admin的集合
     * @return
     */
    public List<Admin> getList(BaseEnum.StateEnum[] states);

    public boolean checkIsExistAccount(String name, BaseEnum.AccountEnum accountEnum);
    /**
     * 修改密码
     * */
    public Result updatePassword(Admin admin, String password);
}