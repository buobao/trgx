package com.joint.base.dao.impl;

import com.fz.us.base.dao.jdbc.JdbcDao;
import com.joint.base.dao.LocationsDao;
import com.joint.base.entity.system.Locations;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Dao实现类 - 位置信息
 * ============================================================================
 * 版权所有 2013 qshihua。
 * 
 * @author qshihua
 * @version 0.1 2013-05-06
 * ============================================================================
 */

@Repository
public class LocationsDaoImpl extends BaseEntityDaoImpl<Locations, String> implements LocationsDao {
	
	@Resource
	private JdbcDao jdbcDao;
	
	public Locations getLastLocation(String openId){
		if(StringUtils.isEmpty(openId) == true){
			return null;
		}
		String hql = "from Locations locations where locations.openId = :openId order by createDate desc";
		Locations locations = (Locations)getSession().createQuery(hql).setParameter("openId", openId).setMaxResults(1).uniqueResult();

		/*if(locations == null){
			return  null;
		}

		String location = locations.getLatitude().toString() + "," + locations.getLongitude().toString();

		//formatted_address
		Map<String, Object> a;
		try {
			a = JSONObject.fromObject(BaiduMapUtil.p2a(location));
			if (a.containsKey("status")) {
				if (((Integer) a.get("status")).intValue() == 0) {
					Map<String, Object> result = JSONObject.fromObject(a.get("result"));

					String formatted_address = result.get("formatted_address").toString();
					Map<String, Object> addressComponent = JSONObject.fromObject(result.get("addressComponent"));

					String rdistrict = addressComponent.get("district").toString();    //区县
					String rprovince = addressComponent.get("province").toString();    //省
					String street = addressComponent.get("street").toString();    //省

					locations.setAddress(formatted_address);
					update(locations);
					return locations;
				}
			} else {

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block

		}*/
		return locations;
	}
}
