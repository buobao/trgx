package com.joint.base.dao.impl;


import com.fz.us.base.bean.BaseEnum;
import com.joint.base.dao.PowerDao;
import com.joint.base.entity.Department;
import com.joint.base.entity.Post;
import com.joint.base.entity.Power;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Dao实现类 - 职权
 * ============================================================================
 * 版权所有 2013 。
 * 
 * @author 
 * @version 0.1 2013-1-16
 * ============================================================================
 */

@Repository
public class PowerDaoImpl extends BaseEntityDaoImpl<Power, String> implements PowerDao {

    @Override
    public List<Power> getByDepartId(String departId) {
        String hql="from Power as p where p.department.id=? and p.state=?";
        return (List<Power>)getSession().createQuery(hql).setParameter(0,departId).setParameter(1, BaseEnum.StateEnum.Enable).list();
    }

    @Override
    public Power getPowerByDepartAndPost(Department department, Post post) {
        String hql="from Power as p where p.department.id=? and p.post.id=? and p.state=?";
        return (Power)getSession().createQuery(hql).setParameter(0,department.getId()).setParameter(1,post.getId()).setParameter(2, BaseEnum.StateEnum.Enable).list().get(0);
    }

    @Override
    public List<Power> getByPostId(String postId) {
        String hql = "from Power as p where p.post.id = ? and p.state=?";
        return (List<Power>)getSession().createQuery(hql).setParameter(0, postId).setParameter(1, BaseEnum.StateEnum.Enable).list();
    }

    @Override
    public int delConfigPower(String powerId) {
        Assert.notNull(powerId, "powerId is required");
        String sql = " delete from `sys_config_power` where `powerid` = ?";
        return getSession().createSQLQuery(sql).setParameter(0,powerId).executeUpdate();
    }

    @Override
    public int delRolePower(String powerId) {
        Assert.notNull(powerId,"powerId is required");
        String sql = " delete from `sys_role_power` where `powerid`= ?";
        return getSession().createSQLQuery(sql).setParameter(0,powerId).executeUpdate();
    }


}