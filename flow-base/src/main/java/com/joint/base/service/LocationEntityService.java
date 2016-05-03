package com.joint.base.service;

import com.fz.us.base.service.BaseService;
import com.joint.base.entity.LocationEntity;

/**
 * Service接口 - 位置信息
 * ============================================================================
 * 版权所有 2013 qshihua。
 * ----------------------------------------------------------------------------
 * 
 * @author qshihua
 * 
 * @version 0.1 2013-05-06
 */

public interface LocationEntityService extends BaseService<LocationEntity, String> {

    public LocationEntity getLocationByTargetId(String targetId);
}