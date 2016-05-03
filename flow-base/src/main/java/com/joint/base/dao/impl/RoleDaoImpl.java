package com.joint.base.dao.impl;

import com.google.common.collect.Lists;
import com.joint.base.dao.RoleDao;
import com.joint.base.entity.Role;
import com.joint.base.entity.Users;
import org.springframework.stereotype.Repository;

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
public class RoleDaoImpl extends BaseEntityDaoImpl<Role, String> implements RoleDao {


    @Override
    public List<String> findPnameByUser(Users users) {
        String sql="SELECT s.id FROM sys_role as s\n" +
                "LEFT JOIN sys_role_depart as srd ON s.id=srd.id\n" +
                "LEFT JOIN sys_department as sd ON sd.id=srd.depid\n" +
                "LEFT JOIN sys_role_power as srp ON srp.id=s.id\n" +
                "LEFT JOIN sys_power as spo ON spo.id=srp.powerid\n" +
                "LEFT JOIN sys_role_sys_users as srsu ON srsu.roleSet_id=s.id\n" +
                "LEFT JOIN sys_users as su ON su.id=srsu.usersSet_id\n" +
                "LEFT JOIN sys_duty as duty ON (duty.users_id=su.id OR duty.department_id=sd.id OR duty.power_id=spo.id)\n"+
                "WHERE duty.users_id=:u";
        List<String> roleList = getSession().createSQLQuery(sql).setParameter("u", users.getId()).list();
        if(roleList == null){
            return Lists.newArrayList();
        }
        return roleList;
    }
}