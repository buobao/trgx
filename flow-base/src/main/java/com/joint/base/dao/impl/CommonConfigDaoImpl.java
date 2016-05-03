package com.joint.base.dao.impl;

import com.fz.us.base.bean.BaseEnum;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.joint.base.dao.CommonConfigDao;
import com.joint.base.entity.*;
import com.joint.base.service.DutyService;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;


/**
 * Dao实现类 - 
 * ============================================================================
 * 版权所有 2013 。
 * 
 * @author 
 * @version 0.1 2013-01-06
 * ============================================================================
 */

@Repository
public class CommonConfigDaoImpl extends BaseEntityDaoImpl<CommonConfig, String> implements CommonConfigDao {
    @Resource
    private DutyService dutyService;

    @Override
    public List<Role> findRoleById(String id) {
        Assert.notNull(id,"id is required");
        String hql = "select role from CommonConfig as cfg left join cfg.roleSet as role where cfg.id=:n and role.state=:s";
        List<Role> roleList = getSession().createQuery(hql).setParameter("n",id).setParameter("s", BaseEnum.StateEnum.Enable).list();
        if(roleList.size()>0){
            return roleList;
        }else{
            return Lists.newArrayList();
        }
    }

    @Override
    public List<Department> findDepartmentById(String id) {
        Assert.notNull(id,"id is required");
        String hql="select dept from CommonConfig as cfg left join cfg.departmentSet as dept on cfg.id=:n and dept.state=:s";
        List<Department> deptList = getSession().createQuery(hql).setParameter("n",id).setParameter("s", BaseEnum.StateEnum.Enable).list();
        if(deptList.size()>0){
            return deptList;
        }else{
            return Lists.newArrayList();
        }
    }

    @Override
    public List<Post> findPostById(String id) {
        Assert.notNull(id,"id is required");
        String hql="select post from CommonConfig as cfg left join cfg.postSet as post on cfg.id=:n and post.state=:s";
        List<Post> postList = getSession().createQuery(hql).setParameter("n",id).setParameter("s", BaseEnum.StateEnum.Enable).list();
        if(postList.size()>0){
            return postList;
        }else{
            return Lists.newArrayList();
        }
    }

    @Override
    public List<Power> findPowerById(String id) {
        Assert.notNull(id,"id is required");
        String hql="select power from CommonConfig as cfg left join cfg.powerSet as power on cfg.id=:n and power.state=:s";
        List<Power> powerList = getSession().createQuery(hql).setParameter("n",id).setParameter("s", BaseEnum.StateEnum.Enable).list();
        if(powerList.size()>0){
            return powerList;
        }else{
            return Lists.newArrayList();
        }
    }

    @Override
    public List<Duty> findDutyByCfgInDepart(String id) {
        Assert.notNull(id,"id is required");
        String sql="SELECT DISTINCT duty.id FROM sys_commonconfig as cc\n" +
                "  LEFT JOIN sys_config_depart as cd ON cc.id = cd.id\n" +
                "  LEFT JOIN sys_department as sd ON sd.id = cd.depid\n" +
                "  LEFT JOIN sys_duty as duty ON duty.department_id = sd.id\n" +
                "  WHERE  cc.id=:n AND duty.state='Enable'";
        List<Object> objList = getSession().createSQLQuery(sql).setParameter("n", id).list();
        List<Duty> dutyList = Lists.newArrayList();
        for(Object duId : objList){
            if(duId == null){
                continue;
            }
            dutyList.add(dutyService.get((String) duId));
        }
        return dutyList;
    }

    @Override
    public List<Duty> findDutyByCfgInPost(String id) {
        Assert.notNull(id,"id is required");
        String sql="SELECT DISTINCT duty.id FROM sys_commonconfig as cc\n" +
                "  LEFT JOIN sys_config_post as cp ON cc.id = cp.id\n" +
                "  LEFT JOIN sys_post as sp ON sp.id = cp.postid\n" +
                "  LEFT JOIN sys_duty as duty ON duty.post_id = sp.id\n" +
                "  WHERE  cc.id=:n AND duty.state='Enable'";
        List<Object> objList = getSession().createSQLQuery(sql).setParameter("n", id).list();
        List<Duty> dutyList = Lists.newArrayList();
        for(Object duId : objList){
            if(duId == null){
                continue;
            }
            dutyList.add(dutyService.get((String) duId));
        }
        return dutyList;
    }

    @Override
    public List<Duty> findDutyByCfgInPower(String id) {
        Assert.notNull(id,"id is required");
        String sql="select DISTINCT duty.id from sys_commonconfig as cc\n" +
                "  LEFT JOIN sys_config_power as cp on cp.id = cc.id\n" +
                "  LEFT JOIN sys_power as power on power.id = cp.powerid\n" +
                "  LEFT JOIN sys_duty as duty on duty.power_id = power.id\n" +
                "WHERE cc.id = :n and duty.state = :s";
        List<Object> objList = getSession().createSQLQuery(sql).setParameter("n", id).setParameter("s", "Enable").list();
        List<Duty> dutyList = Lists.newArrayList();
        for(Object duId : objList){
            if(duId == null){
                continue;
            }
            dutyList.add(dutyService.get((String) duId));
        }
        return dutyList;
    }

    @Override
    public List<Duty> findDutyByCfgInRole(String id) {
        Assert.notNull(id,"id is required");
        String sql="SELECT DISTINCT duty.id FROM sys_commonconfig as sc\n" +
                "LEFT JOIN sys_config_role as scr ON sc.id = scr.id\n" +
                "LEFT JOIN sys_role as sr ON sr.id = scr.roleid\n" +
                "LEFT JOIN sys_role_sys_users as srsu ON sr.id = srsu.roleSet_id\n" +
                "LEFT JOIN sys_users as su ON su.id = srsu.usersSet_id\n" +
                "LEFT JOIN sys_role_depart as srd ON sr.id = srd.id\n" +
                "LEFT JOIN sys_department as sd ON sd.id = srd.depid\n" +
                "LEFT JOIN sys_role_power as srp ON sr.id = srp.id\n" +
                "LEFT JOIN sys_power as sp ON sp.id = srp.powerid\n" +
                "LEFT JOIN sys_duty as duty ON (duty.users_id=su.id OR duty.department_id=sd.id OR duty.power_id=sp.id)\n" +
                "WHERE sc.id=:n AND duty.state='Enable'";

        List<Object> objList = getSession().createSQLQuery(sql).setParameter("n", id).list();
        List<Duty> dutyList = Lists.newArrayList();
        for(Object duId : objList){
            if(duId == null){
                continue;
            }
            dutyList.add(dutyService.get((String) duId));
        }
        return dutyList;
    }

    @Override
    public List<Duty> findDutyByConfig(String id) {
        List<Duty> dutyListRole=findDutyByCfgInRole(id);
        List<Duty> dutyListDepart=findDutyByCfgInDepart(id);
        List<Duty> dutyListPost=findDutyByCfgInPost(id);
        List<Duty> dutyListPower=findDutyByCfgInPower(id);

        List<Duty> dutyList=Lists.newArrayList();
        Set<Duty> dutySet= Sets.newHashSet();

        dutySet.addAll(dutyListRole);
        dutySet.addAll(dutyListDepart);
        dutySet.addAll(dutyListPost);
        dutySet.addAll(dutyListPower);
        dutyList.addAll(dutySet);

        return dutyList;
    }
}
