package com.joint.base.service.impl;

import com.fz.us.base.dao.jdbc.JdbcDao;
import com.fz.us.base.util.LogUtil;
import com.joint.base.dao.BaseEntityDao;
import com.joint.base.dao.LocationsDao;
import com.joint.base.entity.LocationEntity;
import com.joint.base.entity.system.Locations;
import com.joint.base.service.LocationEntityService;
import com.joint.base.service.LocationsService;
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
public class LocationsServiceImpl extends BaseEntityServiceImpl<Locations, String> implements LocationsService {

	@Resource
	private LocationsDao locationsDao;
	@Resource
	private LocationEntityService locationEntityService;


	@Resource
	private JdbcDao jdbcDao;

    @Override
    public BaseEntityDao<Locations, String> getBaseEntityDao() {
        return locationsDao;
    }


	public Locations getLastLocation(String openId){
		return locationsDao.getLastLocation(openId);
	}

	@Override
	public String getAddress(Locations locations) {
		if(null == locations){
			return "";
		}else{
			try {
				LocationEntity locationEntity = locationEntityService.getLocationByTargetId(locations.getId());
				if(null != locationEntity){
					return locationEntity.getFullAddress();
				}
			}catch (Exception e){
				LogUtil.info(e.getMessage(), e);
			}
		}
		return "";
	}


	@Override
	public void clearHistoryLocations(){
		//SELECT * FROM fm_users_fm_locations LEFT JOIN fm_locations ON fm_users_fm_locations.locationSet_id = fm_locations.id WHERE createDate < '2013-10-10 00:00:00'
		
		/*String sql = "select t.fm_users_id as id from (select d.id,d.fm_users_id,d.latitude,d.longitude, `myFun_CalcuDistance`(:lat,:lng,d.latitude,d.longitude,'m') as distince  from (select * from (SELECT  b.id as id,a.fm_users_id as fm_users_id,b.latitude as latitude ,b.longitude as longitude  from fm_courier as courier left join fm_users_fm_locations as a on courier.users_id = a.fm_users_id,fm_locations as b  where a.locationSet_id = b.id AND courier.activiti = 'Active' ORDER BY b.createDate DESC) as c  GROUP BY c.fm_users_id) as d) t where t.distince < 3000 order by t.distince LIMIT 100";
		
		Map<String,String> filter = new HashMap<String,String>();
        //filter.put("lat", order.getSendLocate().getLatitude().toString());
        //filter.put("lng", order.getSendLocate().getLongitude().toString());
		//filter.put("lat", "31.25311558");
		//filter.put("lng", "121.50331854");
        
        //System.out.println(sql + " " + order.getSendLocate().getLatitude().toString() + " " + order.getSendLocate().getLongitude().toString());
        try {
        		//System.out.println("jdbcDao.findForMap(sql,filter);");
                //@SuppressWarnings("unchecked")
                //Map map = jdbcDao.findForMap(sql,filter);
                List<Map<String, Object>> list = jdbcDao.findForListMap(sql,filter);
                System.out.println("getAssignUsers -> 数量："+list.size());
                if(null != list && list.size()>0) {
                	//可以分派。待指派，枚举
                	//Order tmp_order = orderService.get(orderId);
            		//tmp_order.setOrderStatus(OrderStateEnum.readytoappoint);
            		//orderService.update(tmp_order);
            		
	                	for(Map map:list){
	                		//createAssignMsg((String)map.get("id"),tmp_order);
	                		
	                		Set set = map.entrySet();
	                        // Get an iterator
	                        Iterator i = set.iterator();
	                        // Display elements
	                        while(i.hasNext()) {
		                        Map.Entry me = (Map.Entry)i.next();
		                        //System.out.print(me.getKey() + ": ");
		                        //System.out.println(me.getValue());
		                        
		                        createAssignMsg((String)me.getValue(),order.getId());
	                        }
	                	}
                
                        //Set<?> keys  = map.keySet();
                        //Object[] keyArr  = keys.toArray();
                        //return map.get(keyArr[0]).toString();
                        //return jdbcDao.findForMap(sql, filter);
                        // Get a set of the entries
                        
                        //return (Set<String>) keys;
                }else{
                	//没有满足条件的人，可能需要其他方法调度，
                	
                	//本次按照 3分钟后进入超时处理执行
                	System.out.println("没有满足 附近3公里内 条件的人员");
                	
                }
        } catch (Exception e) {
                e.printStackTrace();
        }*/
		
	}
}