package com.joint.base.service.impl;

import com.joint.base.dao.BaseEntityDao;
import com.joint.base.dao.CustomMessageDao;
import com.joint.base.entity.CustomMessage;
import com.joint.base.service.CustomMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * Service实现类 - 自定义消息
 * ============================================================================
 * 版权所有 2013 qshihua。
 * ----------------------------------------------------------------------------
 * 
 * @author qshihua
 * 
 * @version 0.1 2013-05-06
 */

@Service
public class CustomMessageServiceImpl extends BaseEntityServiceImpl<CustomMessage, String> implements CustomMessageService {

	@Resource
	private CustomMessageDao customMessageDao;

    @Override
    public BaseEntityDao<CustomMessage, String> getBaseEntityDao() {
        return customMessageDao;
    }

}