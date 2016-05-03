package com.joint.base.dao.impl;

import com.fz.us.base.bean.BaseEnum;
import com.google.common.collect.Lists;
import com.joint.base.dao.ProcessConfigDao;
import com.joint.base.entity.BusinessConfig;
import com.joint.base.entity.Duty;
import com.joint.base.entity.ProcessConfig;
import com.joint.base.entity.Users;
import com.joint.base.service.CommonConfigService;
import com.joint.base.service.DutyService;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;


/**
 * Dao实现类 - 日志设置
 * ============================================================================
 * 版权所有 2013 qshihua。
 * 
 * @author qshihua
 * @version 0.1 2013-1-16
 * ============================================================================
 */

@Repository
public class ProcessConfigDaoImpl extends BaseEntityDaoImpl<ProcessConfig, String> implements ProcessConfigDao {
	@Resource
	private DutyService dutyService;
    @Resource
    private CommonConfigService commonConfigService;

	@Override
	public ProcessConfig findConfigByActivityId(String processDefinitionId,
			String activityId) {
		String hql = "from ProcessConfig pc where pc.processDefinitionId = ? and pc.activityId = ? and pc.state=?";
		ProcessConfig proConfig = (ProcessConfig) getSession().createQuery(hql).setParameter(0, processDefinitionId).setParameter(1, activityId).setParameter(2, BaseEnum.StateEnum.Enable).uniqueResult();
        return proConfig;
	}

	@Override
	public List<ProcessConfig> findConfigByBcfg(BusinessConfig businessConfig) {
		String hql="from ProcessConfig pc where pc.businessConfig=:b and pc.state=:s";
		List<ProcessConfig> processConfigList= getSession().createQuery(hql).setParameter("b",businessConfig).setParameter("s", BaseEnum.StateEnum.Enable).list();
		if(processConfigList!=null && processConfigList.size()>0){
     		return processConfigList;
		}
		return null;
	}

	@Override
	public List<Duty> findDutyByConfig(ProcessConfig pConfig, Users users) {
		String sql = "";
		List<Object> objList = getSession().createSQLQuery(sql).setParameter("pid",pConfig.getId()).setParameter("uid",users.getId()).setParameter("s", BaseEnum.StateEnum.Enable.name()).list();
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
	public List<Duty> findDutyByConfig(ProcessConfig pConfig) {
        Assert.notNull(pConfig,"pConfig is required");
        return commonConfigService.findDutyByConfig(pConfig.getCommonConfig().getId());
	}

	@Override
	public List<Duty> findDutyByGroup(ProcessConfig processConfig) {
		Assert.notNull(processConfig,"processConfig is required");
		String sql="SELECT DISTINCT du.id FROM sys_processconfig as pc\n" +
				"  LEFT JOIN sys_processcfg_group as pg ON  pc.id = pg.id\n" +
				"  LEFT JOIN sys_group as sg ON sg.id =pg.gpid\n" +
				"  LEFT JOIN sys_group_depart as sgd ON sgd.id=sg.id\n" +
				"  LEFT JOIN sys_department as sd ON sd.id = sgd.dep_id\n" +
				"  LEFT JOIN sys_group_users as sgu ON sgu.id=sg.id\n" +
				"  LEFT JOIN sys_users as su ON su.id = sgu.user_id\n" +
				"  LEFT JOIN sys_group_power as sgp ON sgp.id=sg.id\n" +
				"  LEFT JOIN sys_power as sp ON sp.id = sgp.power_id\n" +
				"  LEFT JOIN sys_duty as du ON  (du.department_id=sd.id OR du.users_id=su.id OR du.power_id=sp.id) AND du.state='Enable'\n" +
				"  WHERE pc.id=:p";
		List<Object> objList = getSession().createSQLQuery(sql).setParameter("p", processConfig.getId()).list();
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
	public List<Duty> findDutyByDepart(ProcessConfig processConfig) {
		Assert.notNull(processConfig,"processConfig is required");
		String sql="SELECT DISTINCT du.id FROM sys_processconfig as pc\n" +
				"LEFT JOIN sys_processcfg_depart as spd ON pc.id = spd.id\n" +
				"LEFT JOIN sys_department as sd ON sd.id = spd.depid\n" +
				"LEFT JOIN sys_duty as du ON du.department_id=sd.id AND du.state='Enable'\n" +
				"WHERE pc.id=:p";
		List<Object> objList = getSession().createSQLQuery(sql).setParameter("p", processConfig.getId()).list();
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
	public List<Duty> findDutyByPost(ProcessConfig processConfig) {
		Assert.notNull(processConfig,"processConfig is required");
		String sql="SELECT DISTINCT du.id FROM sys_processconfig as pc\n" +
				"LEFT JOIN  sys_processcfg_post as spp ON pc.id = spp.id\n" +
				"LEFT JOIN  sys_post as sp ON sp.id = spp.postid\n" +
				"LEFT JOIN sys_duty as du ON du.post_id=sp.id AND du.state='Enable'\n" +
				"WHERE pc.id=:p";
		List<Object> objList = getSession().createSQLQuery(sql).setParameter("p", processConfig.getId()).list();
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
	public List<Duty> findDutyByPower(ProcessConfig processConfig) {
		Assert.notNull(processConfig,"processConfig is required");
		String sql="SELECT DISTINCT du.id FROM sys_processconfig as pc\n" +
				"LEFT JOIN sys_processcfg_power as spp ON pc.id = spp.id\n" +
				"LEFT JOIN sys_power as sp ON sp.id = spp.powerid\n" +
				"LEFT JOIN sys_duty as du ON du.power_id=sp.id AND du.state='Enable'\n" +
				"WHERE pc.id=:p";
		List<Object> objList = getSession().createSQLQuery(sql).setParameter("p", processConfig.getId()).list();
		List<Duty> dutyList = Lists.newArrayList();
		for(Object duId : objList){
			if(duId == null){
				continue;
			}
			dutyList.add(dutyService.get((String) duId));
		}
		return dutyList;
	}


}
