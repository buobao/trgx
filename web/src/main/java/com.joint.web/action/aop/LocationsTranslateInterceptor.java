package com.joint.web.action.aop;

import com.fz.us.base.util.LogUtil;
import com.joint.base.bean.EnumManage;
import com.joint.base.entity.LocationEntity;
import com.joint.base.entity.system.Locations;
import com.joint.base.parent.BaseEntity;
import com.joint.base.service.LocationEntityService;
import com.joint.base.service.jms.AdvancedNotifyMessageProducer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * AOP 拦截器类 - 地理位置转化
 * ============================================================================
 * 版权所有 2014 min_xu，并保留所有权利。
 * ----------------------------------------------------------------------------
 * 
 * @author min_xu
 * 
 * @version 0.1 2014-07-16
 */

@Aspect
@Component
public class LocationsTranslateInterceptor {
	@Resource
	private AdvancedNotifyMessageProducer notifyMessageProducer;
    @Resource
    private LocationEntityService locationEntityService;
	//*******************************************Save After*********************************************
	@Pointcut("execution(* com.joint.base.service.impl.BaseEntityServiceImpl.save(..)) && target(com.joint.base.service.impl.LocationsServiceImpl)")
	public void locationsSave() {
		
	}
	@After("locationsSave()")
	public void afterLocationsSave(JoinPoint jp) {
		translatePoint(jp);
	}

	/**
	 * 转化坐标地址
	 * */
	public void translatePoint(JoinPoint jp){
		LogUtil.info("LocationsServiceImpl saveAndEnable..");
		if (jp.getArgs().length > 0) {
			if (jp.getArgs()[0] instanceof BaseEntity) {
				BaseEntity entity = (BaseEntity) jp.getArgs()[0];
				Locations locations = (Locations) entity;
                    if (EnumManage.CoordTypeEnum.wgs84.equals(locations.getCoordType())) {
                        LocationEntity locationEntity = new LocationEntity(locations, locations.getId(), Locations.class.getCanonicalName());
                        locationEntity.setLatitude(locations.getLatitude());
                        locationEntity.setLongitude(locations.getLongitude());
                        notifyMessageProducer.sendQueue(locationEntity, EnumManage.NotifyKeyEnum.locationEntity.name());
                    } else {
                        LocationEntity locationEntity = new LocationEntity(locations, locations.getId(), Locations.class.getCanonicalName());
                        locationEntity.setLatitude(locations.getLatitude());
                        locationEntity.setLongitude(locations.getLongitude());
                        locationEntity.setCoordType(locations.getCoordType());
                        locationEntity.setFullAddress(locations.getAddress());
                        locationEntityService.save(locationEntity);
                }
			}
		}
	}
}
