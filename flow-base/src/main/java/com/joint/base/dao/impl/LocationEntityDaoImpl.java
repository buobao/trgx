package com.joint.base.dao.impl;

import com.fz.us.base.dao.impl.BaseDaoImpl;
import com.joint.base.dao.LocationEntityDao;
import com.joint.base.entity.LocationEntity;
import org.springframework.stereotype.Repository;

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
public class LocationEntityDaoImpl extends BaseDaoImpl<LocationEntity, String> implements LocationEntityDao {

    public LocationEntity getLocationByTargetId(String targetId){
        String hql = "from LocationEntity where lower(targetId) = lower(:targetId) ";
        //Admin admin = (Admin) getSession().createQuery(hql).setParameter(0, username).uniqueResult();
        return (LocationEntity) getSession().createQuery(hql).setParameter("targetId",targetId).uniqueResult();
    }
}
