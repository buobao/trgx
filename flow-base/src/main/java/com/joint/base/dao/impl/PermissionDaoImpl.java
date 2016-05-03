package com.joint.base.dao.impl;

import com.fz.us.base.bean.BaseEnum;
import com.joint.base.dao.PermissionDao;
import com.joint.base.entity.Permission;
import com.joint.base.entity.Users;
import com.joint.base.service.PermissionService;
import com.joint.base.service.UsersService;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Dao实现类 - 岗位
 * ============================================================================
 * 版权所有 2013 。
 * 
 * @author 
 * @version 0.1 2013-1-16
 * ============================================================================
 */

@Repository
public class PermissionDaoImpl extends BaseEntityDaoImpl<Permission, String> implements PermissionDao {
    @Resource
    private UsersService usersService;
    @Resource
    private PermissionService permissionService;
    @Override
    public Permission getByKey(String businessKey) {
        String hql = "from Permission p where p.businessKey = :b and p.state = :s";
        List<Permission> permissionList = getSession().createQuery(hql).setParameter("b",businessKey).setParameter("s", BaseEnum.StateEnum.Enable).list();
        if(permissionList.size() > 0){
            return permissionList.get(0);
        }
        return null;
    }

    @Override
    public List<Permission> findAllByType(String type) {
        Assert.notNull(type,"type is required");
        Users users = usersService.getLoginInfo();
        String sql = "SELECT DISTINCT p.id FROM sys_permission as p \n" +
                "LEFT JOIN  sys_role_sys_permission as srsp ON p.id = srsp.permissionSet_id\n" +
                "LEFT JOIN sys_role as sr ON sr.id = srsp.sys_role_id\n" +
                "LEFT JOIN sys_role_sys_users as srsu ON sr.id = srsu.roleSet_id\n" +
                "LEFT JOIN sys_users as su ON su.id = srsu.usersSet_id \n" +
                "WHERE p.state='Enable' AND p.type=:t AND su.id=:u order by p.sortNumber asc";
        List<String> objectList = getSession().createSQLQuery(sql).setParameter("u",users.getId()).setParameter("t", type).list();
        List<Permission> permissionList = new ArrayList<Permission>();
        for(String id : objectList){
            if(id==null){
                continue;
            }
            permissionList.add(permissionService.get(id));
        }
        return permissionList;
    }

    @Override
    public List<Permission> getAllByUser(String userId) {
        String sql = "SELECT p.id from sys_permission AS p \n"+
                        "INNER JOIN sys_role_sys_permission as rp on rp.permissionSet_id = p.id \n" +
                        "INNER JOIN sys_role AS r on r.id=rp.sys_role_id \n"+
                        "INNER JOIN sys_role_sys_users as ru on ru.roleSet_id = r.id \n"+
                        "INNER JOIN sys_users as u on u.id = ru.usersSet_id \n"+
                        "WHERE u.id= :u order by p.sortNumber asc";
        List<String> objectList = getSession().createSQLQuery(sql).setParameter("u",userId).list();
        List<Permission> permissionList = new ArrayList<Permission>();
        for(String id : objectList){
            permissionList.add(permissionService.get(id));
        }
        return permissionList;
    }

    @Override
    public void updateState(String key,String keyId) {
        Assert.notNull(key,"key is required");
        Assert.notNull(keyId,"keyId is required");
        //TODO 具体业务实体对应数据库表的前缀(tr_)
        String sql="update ec_"+key+" SET state=:s where id=:d";
        getSession().createSQLQuery(sql).setParameter("s","Delete").setParameter("d",keyId).executeUpdate();
    }
}