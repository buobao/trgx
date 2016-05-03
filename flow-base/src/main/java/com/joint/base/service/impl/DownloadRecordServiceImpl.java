package com.joint.base.service.impl;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Pager;
import com.joint.base.dao.BaseEntityDao;
import com.joint.base.dao.DownloadRecordDao;
import com.joint.base.entity.Company;
import com.joint.base.entity.DownloadRecord;
import com.joint.base.entity.Users;
import com.joint.base.service.DownloadRecordService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service实现类 - 用户账号登陆日志
 * ============================================================================
 * 版权所有 2013 qshihua。
 * ----------------------------------------------------------------------------
 * 
 * @author qshihua
 * 
 * @version 0.1 2013-05-06
 */

@Service
public class DownloadRecordServiceImpl extends BaseEntityServiceImpl<DownloadRecord, String> implements DownloadRecordService {

    private static long maxCount = 10000l;

    @Resource
    private DownloadRecordDao downloadRecordDao;


    @Override
    public BaseEntityDao<DownloadRecord, String> getBaseEntityDao() {
        return downloadRecordDao;
    }

    public Pager getRootPager(Pager pager, Users users, Company company) {
        return downloadRecordDao.getRootPager(pager, users, company);
    }

    public List<DownloadRecord> getChildList(Company company, String parentId) {
        Assert.notNull(parentId, "parentId is null");
        Map<String, Object> params = new HashMap<String, Object>();
        BaseEnum.StateEnum state = BaseEnum.StateEnum.Enable;
        if (company != null) {
            params.put("company", company);
        }
        params.put("parentId", parentId);

        params.put("state", state);
        return getList(params);
    }

}