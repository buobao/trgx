package com.joint.base.service.impl;


import com.fz.us.base.dao.BaseDao;
import com.fz.us.base.service.impl.BaseServiceImpl;
import com.joint.base.dao.PushEntityDao;
import com.joint.base.entity.PushEntity;
import com.joint.base.service.PushEntityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Service实现类 - 推送
 * ============================================================================
 * 版权所有 2015 。
 *
 * @author fallenpanda
 *
 * @version 0.1 2015-01-21
 * ============================================================================
 */

@Service
public class PushEntityServiceImpl extends BaseServiceImpl<PushEntity, String> implements PushEntityService {

	@Resource
	private PushEntityDao pushEntityDao;

    @Override
    public BaseDao<PushEntity, String> getBaseDao() {
        return pushEntityDao;
    }




}