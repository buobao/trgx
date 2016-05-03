package com.joint.base.service.impl;

import com.fz.us.base.dao.BaseDao;
import com.fz.us.base.service.impl.BaseServiceImpl;
import com.joint.base.dao.LocationEntityDao;
import com.joint.base.entity.LocationEntity;
import com.joint.base.service.LocationEntityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Service实现类 - 位置信息
 * ============================================================================
 * 版权所有 2013 qshihua。
 * ----------------------------------------------------------------------------
 * 
 * @author qshihua
 * 
 * @version 0.1 2013-05-06
 */

@Service
public class LocationEntityServiceImpl extends BaseServiceImpl<LocationEntity, String> implements LocationEntityService {

	@Resource
	private LocationEntityDao locationEntityDao;

    @Override
    public BaseDao<LocationEntity, String> getBaseDao() {
        return locationEntityDao;
    }

    public LocationEntity getLocationByTargetId(String targetId){
        return locationEntityDao.getLocationByTargetId(targetId);
    }


}