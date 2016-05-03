package com.joint.base.dao;


import com.fz.us.base.bean.BaseEnum;
import com.joint.base.entity.system.Admin;

import java.util.List;

/**
 * Dao接口 - 岗位
 * ============================================================================
 * 版权所有 2013 。
 * 
 * @author 
 * @version 0.1 2013-1-16
 * ============================================================================
 */

public interface AdminDao extends BaseEntityDao<Admin, String> {
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

    public Admin getAdminByUsermobile(String usermobile);

    public List<Admin> getList(BaseEnum.StateEnum[] states);

    /**
     * 根据deviceId获取管理员对象，若管理员不存在，则返回null（区分大小写）
     * @param deviceId
     * @return
     */
    public Admin getAdminByDeviceId(String deviceId);
}