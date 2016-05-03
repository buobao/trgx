package com.joint.base.service.impl;

import com.fz.us.base.bean.Pager;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.joint.base.entity.Company;
import com.joint.base.entity.Department;
import com.joint.base.entity.Users;
import com.joint.base.parent.BaseEntity;
import com.joint.base.service.BaseLimitService;
import com.joint.base.service.DepartmentService;
import com.joint.base.service.DutyService;
import com.joint.base.util.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by amin on 2015/5/10.
 */
@Service
public abstract class BaseLimitServiceImpl<T extends BaseEntity, PK extends Serializable> extends BaseEntityServiceImpl<T,PK> implements BaseLimitService<T,PK> {
    @Resource
    private DepartmentService departmentService;
    @Resource
    private DutyService dutyService;
    public Pager findByPagerAndLimit(Pager pager,Users users, Company company,Map<String,Object> rmap) {
        Set<Users> usersSet= Sets.newHashSet();
        List<Users> usersList= Lists.newArrayList();
        List<Users> childList;

        //当前登陆人所在部门
        List<Department> departmentList=dutyService.getDepartments(users,company);
        //若当前登录人未分配组织结构，什么都看不到
        if(departmentList==null && departmentList.size()==0){
            return  new Pager();
        }else{
            //若开启了部门间共享，则组织结构之间可互看,无视权限
            if(company.getDepartmentShare()==1){
                return getBaseEntityDao().findByPagerAndLimit(pager,null,company,rmap);
            }

            //若开启了部门内部共享
            else if(company.getShare()==1){
                childList=Lists.newArrayList();
                Set<Department> departmentSet=Sets.newHashSet();
                for(Department department:departmentList){
                    departmentSet=departmentService.getChildDepart(department,departmentSet);
                    for(Department childDepart:departmentSet){
                        childList=dutyService.getPersons(childDepart);
                        if(childList!=null && childList.size()>0){
                            //部门内部互看以及看到下级
                            usersSet.addAll(childList);
                        }
                    }
                }
                return getBaseEntityDao().findByPagerAndLimit(pager,usersSet,company,rmap);
            }
            else{
                for(Department department:departmentList){
                    Set<Department> departmentSet=Sets.newHashSet();
                    Set<Department> childDepartment=Sets.newHashSet();
                    departmentSet=departmentService.getChildDepart(department,departmentSet);
                    childDepartment.addAll(departmentSet);
                    Users user=dutyService.getPrincipal(department);
                    if(user!=null && StringUtils.equals(user.getId(), users.getId())){
                        //部门负责人可见所有以及下级部门
                        for(Department part:departmentSet){
                            List<Users> userses=dutyService.getPersons(part);
                            usersList.addAll(userses);
                        }
                    }else{
                        childList=Lists.newArrayList();
                        if(childDepartment!=null && childDepartment.size()>0){
                            childDepartment.remove(department);
                            for(Department child:childDepartment){
                                childList=dutyService.getPersons(child);
                            }
                        }
                        //组员只能看自己的以及下级部门
                        childList.add(users);
                        usersList.addAll(childList);
                    }

                }
            }
        }

        if(usersList==null || usersList.size()==0){
            return  new Pager();
        }else{
            usersSet.addAll(usersList);
        }
        return getBaseEntityDao().findByPagerAndLimit(pager,usersSet,company,rmap);
    }
}
