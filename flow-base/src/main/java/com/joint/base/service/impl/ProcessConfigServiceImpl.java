package com.joint.base.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.joint.base.dao.BaseEntityDao;
import com.joint.base.dao.ProcessConfigDao;
import com.joint.base.entity.BusinessConfig;
import com.joint.base.entity.Duty;
import com.joint.base.entity.ProcessConfig;
import com.joint.base.service.DutyService;
import com.joint.base.service.PowerService;
import com.joint.base.service.ProcessConfigService;
import com.joint.base.service.UsersService;
import com.joint.base.util.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;


/**
 * Service实现类 - 
 * ============================================================================
  * 版权所有 2013 。
 * ----------------------------------------------------------------------------
 * 
 * @author 
 * 
 * @version 0.1 2013-01-06
 */
@Service
public class ProcessConfigServiceImpl extends BaseEntityServiceImpl<ProcessConfig, String> implements ProcessConfigService {
	@Resource
	private ProcessConfigDao processConfigDao;
	@Resource
	private UsersService usersService;
	@Resource
	private PowerService powerService;
	@Resource
	private DutyService dutyService;

	@Override
	public BaseEntityDao<ProcessConfig, String> getBaseEntityDao() {
		return processConfigDao;
	}


	@Override
	public ProcessConfig findConfigByActivityId(String processDefinitionId,
			String activityId) {
		return processConfigDao.findConfigByActivityId(processDefinitionId, activityId);
	}

	@Override
	public List<ProcessConfig> findConfigByBcfg(BusinessConfig businessConfig) {
		return processConfigDao.findConfigByBcfg(businessConfig);
	}

	@Override
	public List<String> findSpecailByUser(ProcessConfig processConfig, Duty hisDuty) {
		String type = StringUtils.reverse(String.valueOf(processConfig.getType()));
		List<String> idList = Lists.newArrayList();
		for(int i=0;i<type.length();i++){
			if(i==0 && type.charAt(i)=='1'){
				Set<Duty> dutySet = powerService.findParentByPower(hisDuty);
				for(Duty duty : dutySet){
					idList.add(duty.getUsers().getId());
				}
			}
			if(i==2 && type.charAt(i)=='1'){
				Duty duty = dutyService.getPrincipalDuty(hisDuty.getDepartment());
                if(duty != null) idList.add(duty.getUsers().getId());

			}
			if(i==3 && type.charAt(i)=='1') {
				Duty duty = dutyService.getParentPrincipal(hisDuty.getDepartment());
                if(duty != null) idList.add(duty.getUsers().getId());
			}
		}
		Set<String> idSet = Sets.newHashSet();
		idSet.addAll(idList);
		idList = Lists.newArrayList();
		idList.addAll(idSet);
		return idList;
	}

	@Override
	public List<Duty> findDutyInSpecail(ProcessConfig processConfig, Duty hisDuty) {
		String type = StringUtils.reverse(String.valueOf(processConfig.getType()));
		List<Duty> dutyList = Lists.newArrayList();
		if(processConfig.getType() == -1){
			return dutyList;
		}
		for(int i=0;i<type.length();i++){
			if(i==0 && type.charAt(i)=='1') dutyList.addAll(powerService.findParentByPower(hisDuty));
			if(i==2 && type.charAt(i)=='1') dutyList.add(dutyService.getPrincipalDuty(hisDuty.getDepartment()));
			if(i==3 && type.charAt(i)=='1') dutyList.add(dutyService.getPrincipalDuty(hisDuty.getDepartment()));
		}
		return dutyList;
	}

	@Override
	public List<Duty> findDutyByConfig(ProcessConfig pConfig) {

        return processConfigDao.findDutyByConfig(pConfig);
	}

	@Override
	public List<Duty> findDutyByGroup(ProcessConfig processConfig) {
		return processConfigDao.findDutyByGroup(processConfig);
	}

	@Override
	public List<Duty> findDutyByDepart(ProcessConfig processConfig) {
		return processConfigDao.findDutyByDepart(processConfig);
	}

	@Override
	public List<Duty> findDutyByPost(ProcessConfig processConfig) {
		return processConfigDao.findDutyByPost(processConfig);
	}

	@Override
	public List<Duty> findDutyByPower(ProcessConfig processConfig) {
		return processConfigDao.findDutyByPower(processConfig);
	}
}