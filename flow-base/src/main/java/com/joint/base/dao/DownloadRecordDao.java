package com.joint.base.dao;


import com.fz.us.base.bean.Pager;
import com.joint.base.entity.Company;
import com.joint.base.entity.DownloadRecord;
import com.joint.base.entity.Users;


/**
 * Dao接口 - 用户账号Download日志
 * ============================================================================
 * 版权所有 2013 qshihua。
 * 
 * @author qshihua
 * @version 0.1 2013-05-06
 * ============================================================================
 */

public interface DownloadRecordDao extends BaseEntityDao<DownloadRecord, String> {

    public Pager getRootPager(Pager pager, Users users, Company company);
}