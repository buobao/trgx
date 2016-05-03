package com.joint.base.dao.impl;

import com.fz.us.base.bean.BaseEnum;
import com.google.common.collect.Lists;
import com.joint.base.bean.EnumManage;
import com.joint.base.dao.DutyDao;
import com.joint.base.entity.*;
import com.joint.base.service.UsersService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Dao实现类 - 
 * ============================================================================
 * 版权所有 2013 。
 * 
 * @author 
 * @version 0.1 2013-1-16
 * ============================================================================
 */

@Repository
public class DutyDaoImpl extends BaseEntityDaoImpl<Duty, String> implements DutyDao {
	@Resource
	private UsersService usersService;

	@Override
	public Duty getDefaultDuty(Users users) {
		Assert.notNull(users, "users is required");
		String hql = "from Duty duty where duty.users.id = ? and duty.dutyDefault = 1 and duty.state = ?";
		List<Duty> list = getSession().createQuery(hql).setParameter(0, users.getId()).setParameter(1, BaseEnum.StateEnum.Enable).list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<Department> getDepartments(Users users,Company company) {
		Assert.notNull(users, "users is required");
		String hql = "from Duty duty where duty.users.id = ? and duty.state = ?  and duty.company.id=?";
		List<Duty> list = getSession().createQuery(hql).setParameter(0, users.getId()).setParameter(1, BaseEnum.StateEnum.Enable).setParameter(2,company.getId()).list();
		List<Department> dList = new ArrayList<Department>();
		if(CollectionUtils.isEmpty(list) || list.size()==0){
			return  dList;
		}
		for(Duty duty:list){
			dList.add(duty.getDepartment());
		}
		return dList;
	}

	@Override
	public List<Duty> getDutys(Users users) {
		Assert.notNull(users, "users is required");
		String hql = "from Duty duty where duty.users.id = ? and state = ? ";
		List<Duty> list = getSession().createQuery(hql).setParameter(0, users.getId()).setParameter(1, BaseEnum.StateEnum.Enable).list();
		
		return list;
	}

	@Override
	public List<Department> getPrincipalDepartments(Users users) {
		Assert.notNull(users, "users is required");
		String hql = "from Duty duty where duty.users.id = ? and state = ? and duty.dutyState = ? ";
		List<Duty> list = getSession().createQuery(hql).setParameter(0, users.getId()).setParameter(1, BaseEnum.StateEnum.Enable).setParameter(2, EnumManage.DutyState.Principal).list();
		List<Department> dList = new ArrayList<Department>();
		if(CollectionUtils.isEmpty(list) || list.size()>0){
			return  dList;
		}
		for(Duty duty:list){
			dList.add(duty.getPower().getDepartment());
		}
		return dList;
	}

	@Override
	public List<Department> getDepartments(Users users, Post post) {
		Assert.notNull(users, "users is required");
		Assert.notNull(post, "post is required");
		String hql = "from Duty duty where duty.users.id = ? and duty.power.post.id = ? and state = ? ";
		List<Duty> list = getSession().createQuery(hql).setParameter(0, users.getId()).setParameter(1, post.getId()).setParameter(2, BaseEnum.StateEnum.Enable).list();
		List<Department> dList = new ArrayList<Department>();
		if(CollectionUtils.isEmpty(list) || list.size()>0){
			return  dList;
		}
		for(Duty duty:list){
			dList.add(duty.getPower().getDepartment());
		}
		return dList;
	}

	@Override
	public Users getDeputy(Department department) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Duty getParentPrincipal(Department department) {
		//用户上级部门负责人
		Assert.notNull(department, "department is required");
		String hql="from Duty as du where du.department=(select dp.parent from Department as dp where dp=:m) and du.dutyState=:d and du.state=:s";
		List<Duty> dutyList = getSession().createQuery(hql).setParameter("m", department).setParameter("d", EnumManage.DutyState.Principal).setParameter("s", BaseEnum.StateEnum.Enable).list();
		if(dutyList.size() == 0){
			return null;
		}
		return dutyList.get(0);
	}

	@Override
	public List<Users> getPersons(Department department) {
		Assert.notNull(department, "department is required");
		String hql = "from Duty duty where duty.department.id = ? and duty.state = ?";
		List<Duty> list = getSession().createQuery(hql).setParameter(0, department.getId()).setParameter(1, BaseEnum.StateEnum.Enable).list();
		List<Users> usersList = new ArrayList<Users>();
		if(CollectionUtils.isEmpty(list) || list.size()==0){
			return  usersList;
		}
		for(Duty duty:list){
			if(duty.getUsers()!=null&&!usersList.contains(duty.getUsers())){
				usersList.add(duty.getUsers());
			}
		}
		return usersList;
	}

	@Override
	public List<Users> getPersons(Department department, Post post) {
		Assert.notNull(department, "department is required");
		Assert.notNull(post, "post is required");
		String hql = "from Duty duty where duty.power.department.id = ? and duty.power.post.id = ? and state = ? ";
		List<Duty> list = getSession().createQuery(hql).setParameter(0, department.getId()).setParameter(1, post.getId()).setParameter(2, BaseEnum.StateEnum.Enable).list();
		List<Users> usersList = new ArrayList<Users>();
		if(list!=null&&list.size()>0){
			for(Duty duty:list){
				if(duty.getUsers()!=null&&!usersList.contains(duty.getUsers())){
					usersList.add(duty.getUsers());
				}
			}
		}
		return usersList;
	}

	@Override
	public List<Users> getPersons(Post post) {
		Assert.notNull(post, "post is required");
		String hql = "from Duty duty where duty.power.post.id = ? and state = ? ";
		List<Duty> list = getSession().createQuery(hql).setParameter(0, post.getId()).setParameter(1, BaseEnum.StateEnum.Enable).list();
		List<Users> usersList = new ArrayList<Users>();
		if(list!=null&&list.size()>0){
			for(Duty duty:list){
				if(duty.getUsers()!=null&&!usersList.contains(duty.getUsers())){
					usersList.add(duty.getUsers());
				}
			}
		}
		return usersList;
	}

	@Override
	public List<Post> getPosts(Users users) {
		Assert.notNull(users, "users is required");
		String hql = "from Duty duty where duty.users.id = ? and state = ? ";
		List<Duty> dutyList = getSession().createQuery(hql).setParameter(0, users.getId()).setParameter(1, BaseEnum.StateEnum.Enable).list();
		List<Post> postList = Lists.newArrayList();
		for(Duty duty : dutyList){
			// TODO Auto-generated method stub
		}
		return null;

	}

	@Override
	public List<Post> getPosts(Users users, Department department) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Users getPrincipal(Users users) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Users getPrincipal(Department department) {
		Assert.notNull(department, "department is required");
		String hql = "from Duty duty where duty.department.id = ? and duty.dutyState = ? and duty.state = ? ";
		List<Duty> list = getSession().createQuery(hql).setParameter(0, department.getId()).setParameter(1, EnumManage.DutyState.Principal).setParameter(2, BaseEnum.StateEnum.Enable).list();
		if(list!=null&&list.size()>0){
			Duty duty = list.get(0);
			if(duty.getUsers()!=null){
				return duty.getUsers();
			}
		}
		return null;
	}
    @Override
    public Duty getPrincipalDuty(Department department) {
        Assert.notNull(department, "department is required");
        String hql = "from Duty duty where duty.department.id = ? and duty.dutyState = ? and duty.state = ? ";
        List<Duty> list = getSession().createQuery(hql).setParameter(0, department.getId()).setParameter(1, EnumManage.DutyState.Principal).setParameter(2, BaseEnum.StateEnum.Enable).list();
        if(list!=null&&list.size()>0){
            Duty duty = list.get(0);
            if(duty.getUsers()!=null){
                return duty;
            }
        }
        return null;
    }


	@Override
	public List<Duty> getDutys(Department department){
		Assert.notNull(department, "department is required");
		String hql = "from Duty duty where duty.department.id = ? and state = ? ";
		return getSession().createQuery(hql).setParameter(0, department.getId()).setParameter(1, BaseEnum.StateEnum.Enable).list();
	}

	@Override
	public Duty getUsersDepartmentDuty(Department department,Users users){
		Assert.notNull(department, "department is required");
		Assert.notNull(users, "users is required");
		String hql = "from Duty duty where duty.users.id = ? and duty.department.id = ? and state = ? ";
		List<Duty> list = getSession().createQuery(hql).setParameter(0, users.getId()).setParameter(1, department.getId()).setParameter(2, BaseEnum.StateEnum.Enable).list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

    @Override
    public Duty getDuty(Users users, Department department, Post post) {
        String hql = "from Duty duty where duty.users.id = ? and duty.power.department.id = ? and duty.power.post.id = ? and state = ?";
        List<Duty> list = getSession().createQuery(hql).setParameter(0, users.getId()).setParameter(1, department.getId()).setParameter(2,post.getId()).setParameter(3, BaseEnum.StateEnum.Enable).list();
        if(list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }

	@Override
	public Duty getDuty(Users users, Power power) {
		String hql = "from Duty duty where duty.users.id = ? and duty.power.id=? and duty.state = ?";
		List<Duty> list = getSession().createQuery(hql).setParameter(0, users.getId()).setParameter(1, power.getId()).setParameter(2, BaseEnum.StateEnum.Enable).list();
		if(list!=null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

	/**
     * 根据岗位id获取该岗位下的职责
     */
	@Override
    public List<Duty> getDutyByPost(Post post){
        String hql="from Duty duty where duty.power.post.id=? and state=?";
        return  getSession().createQuery(hql).setParameter(0,post.getId()).setParameter(1, BaseEnum.StateEnum.Enable).list();
    }

	@Override
	public List<Duty> findByPower(Power power) {
		//根据power获取该power下的职责
		String hql="from Duty as d where d.power.id=:p and d.state=:s";
		return getSession().createQuery(hql).setParameter("p", power.getId()).setParameter("s", BaseEnum.StateEnum.Enable).list();
	}


}