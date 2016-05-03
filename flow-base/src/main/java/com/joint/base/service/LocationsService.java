package com.joint.base.service;


import com.joint.base.entity.system.Locations;

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

public interface LocationsService extends BaseEntityService<Locations, String> {

	public void clearHistoryLocations();
	
	public Locations getLastLocation(String openId);

	/**
	 * 获取当前坐标的位置信息
	 * */
	public String getAddress(Locations locations);

}