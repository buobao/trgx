package com.joint.base.dao;

import com.fz.us.base.dao.BaseDao;
import com.joint.base.entity.LocationEntity;

/**
 * Dao接口 - 位置信息
 * ============================================================================
 * 版权所有 2013 qshihua。
 * 
 * @author qshihua
 * @version 0.1 2013-05-06
 * ============================================================================
 */

public interface LocationEntityDao extends BaseDao<LocationEntity, String> {

    public LocationEntity getLocationByTargetId(String targetId);
}