package com.joint.base.service;

import com.fz.us.base.bean.Pager;
import com.joint.base.entity.Company;
import com.joint.base.entity.DownloadRecord;
import com.joint.base.entity.Users;

import java.util.List;

/**
 * Service接口 - 用户账号Download日志
 * ============================================================================
 * 版权所有 2013 qshihua。
 * ----------------------------------------------------------------------------
 * 
 * @author qshihua
 * 
 * @version 0.1 2013-05-06
 */

public interface DownloadRecordService extends BaseEntityService<DownloadRecord, String> {

	public Pager getRootPager(Pager pager, Users users, Company company);
	public List<DownloadRecord> getChildList(Company company, String parentId);


}