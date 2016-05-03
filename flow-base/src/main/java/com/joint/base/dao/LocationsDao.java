package com.joint.base.dao;


import com.joint.base.entity.system.Locations;

/**
 * Dao接口 - 位置信息
 * ============================================================================
 * 版权所有 2013 qshihua。
 * 
 * @author qshihua
 * @version 0.1 2013-05-06
 * ============================================================================
 */

public interface LocationsDao extends BaseEntityDao<Locations, String> {

	public Locations getLastLocation(String openId);
}