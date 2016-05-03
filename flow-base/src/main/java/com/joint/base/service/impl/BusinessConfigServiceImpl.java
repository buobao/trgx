package com.joint.base.service.impl;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.joint.base.dao.BaseEntityDao;
import com.joint.base.dao.BusinessConfigDao;
import com.joint.base.entity.BusinessConfig;
import com.joint.base.entity.CommonConfig;
import com.joint.base.entity.Duty;
import com.joint.base.entity.Users;
import com.joint.base.service.BusinessConfigService;
import com.joint.base.service.DutyService;
import com.joint.base.util.StringUtils;
import org.activiti.engine.RepositoryService;
import org.apache.commons.collections.CollectionUtils;
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
public class BusinessConfigServiceImpl extends BaseEntityServiceImpl<BusinessConfig, String> implements BusinessConfigService {
	@Resource
	private BusinessConfigDao businessConfigDao;
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private DutyService dutyService;


    @Override
    public BaseEntityDao<BusinessConfig, String> getBaseEntityDao() {
        return businessConfigDao;
    }

    @Override
    public BusinessConfig getBusinessConfigByDefintionId(String processDefintionId) {
        return businessConfigDao.getBusinessConfigByDefintionId(processDefintionId);
    }

    @Override
    public List<BusinessConfig> findBCfgByDefintionId(String processDefintionId) {
        return businessConfigDao.findBCfgByDefintionId(processDefintionId);
    }

    @Override
    public BusinessConfig getBusinessConfigByDefintionId(String processDefintionId, int version) {
        return businessConfigDao.getBusinessConfigByDefintionId(processDefintionId,version);
    }

    @Override
    public BusinessConfig getByBusinessKey(String businessKey) {
        return businessConfigDao.getByBusinessKey(businessKey);
    }

    @Override
    public List<BusinessConfig> getConfigsByBusinessKey(String businessKey) {
        return  businessConfigDao.getConfigsByBusinessKey(businessKey);
    }

    @Override
    public BusinessConfig getByBusinessKey(String businessKey, int version) {
        return businessConfigDao.getByBusinessKey(businessKey,version);
    }

    @Override
    public List<Duty> findDutyInDocRead(BusinessConfig bcfg) {
        List<Duty> dutyList = Lists.newArrayList();
        if(bcfg.getDocConfig().getStype()==-1){
            return dutyList;
        }
        String type = StringUtils.reverse(String.valueOf(bcfg.getDocConfig().getStype()));
        for(int i=0;i<type.length();i++){
            if(i==0 && type.charAt(i)=='1') dutyList.addAll(businessConfigDao.findDutyInDocReadAndRole(bcfg));
            if(i==1 && type.charAt(i)=='1') dutyList.addAll(businessConfigDao.findDutyInDocReadAndDepart(bcfg));
            if(i==2 && type.charAt(i)=='1') dutyList.addAll(businessConfigDao.findDutyInDocReadAndPower(bcfg));
            if(i==3 && type.charAt(i)=='1') dutyList.addAll(businessConfigDao.findDutyInDocReadAndPost(bcfg));
        }
        Set<Duty> dutySet = Sets.newHashSet();
        CollectionUtils.addAll(dutySet,dutyList.toArray());
        dutyList = Lists.newArrayList();
        dutyList.addAll(dutySet);
        return dutyList;
    }

    @Override
    public List<Duty> findDutyInEdit(BusinessConfig bcfg) {
        List<Duty> dutyList = Lists.newArrayList();
        if(bcfg.getEditConfig().getStype()==-1){
            return dutyList;
        }
        //todo 实现查看pcfg下面对应的归档后读者
        return null;
    }

    @Override
    public List<Duty> findDutyInArchiveRead(BusinessConfig bcfg) {
        List<Duty> dutyList = Lists.newArrayList();
        if(bcfg.getReadConfig().getStype()==-1){
            return dutyList;
        }
        //todo 实现查看pcfg下面对应的归档后读者

        return dutyList;
    }



    @Override
    public BusinessConfig getByUserAndBusinessInFEdit(Users user, String businessKey) {
        return businessConfigDao.getByUserAndBusinessInFEdit(user,businessKey);
    }

    @Override
    public List<BusinessConfig> getDocByUser(BusinessConfig bcfg,Users user) {

        return businessConfigDao.getDocByUser(bcfg,user);
    }

    @Override
    public List<BusinessConfig> getDocReadByUser(BusinessConfig bcfg,Users users) {
        return businessConfigDao.getDocReadByUser(bcfg,users);
    }

    @Override
    public List<BusinessConfig> getDocEditByUser(BusinessConfig bcfg,Users users) {
        return businessConfigDao.getDocEditByUser(bcfg,users);
    }

    @Override
    public List<Duty> findDutyInCreateByConfig(BusinessConfig bcfg, Users users) {

        List<Duty> dutyList = Lists.newArrayList();
        CommonConfig commonConfig = bcfg.getCreateConfig();
        if(commonConfig == null){
            return dutyList;
        }
        if(commonConfig.getStype() == -1){
//            Duty duty = dutyService.getDefaultDuty(users);  //申请人是所有人
//            dutyList.add(duty);
            return dutyList;
        }
        dutyList.addAll(businessConfigDao.findDutyInCreateByConfig(bcfg, users));
        Set<Duty> dutySet = Sets.newHashSet();
        CollectionUtils.addAll(dutySet,dutyList.toArray());
        dutyList = Lists.newArrayList();
        dutyList.addAll(dutySet);

        return dutyList;
    }

    @Override
    public List<Duty> findDutyInDocReadAndGroup(BusinessConfig bcfg) {
        return businessConfigDao.findDutyInDocReadAndRole(bcfg);
    }

    @Override
    public List<Duty> findDutyInDocReadAndDepart(BusinessConfig bcfg) {
        return businessConfigDao.findDutyInDocReadAndDepart(bcfg);
    }

    @Override
    public List<Duty> findDutyInDocReadAndPost(BusinessConfig bcfg) {
        return businessConfigDao.findDutyInDocReadAndPost(bcfg);
    }

    @Override
    public List<Duty> findDutyInDocReadAndPower(BusinessConfig bcfg) {
        return businessConfigDao.findDutyInDocReadAndPower(bcfg);
    }

    @Override
    public List<Duty> findDutyInEditAndGroup(BusinessConfig bcfg) {
        return businessConfigDao.findDutyInEditAndRole(bcfg);
    }

    @Override
    public List<Duty> findDutyInEditAndDepart(BusinessConfig bcfg) {
        return businessConfigDao.findDutyInEditAndDepart(bcfg);
    }

    @Override
    public List<Duty> findDutyInEditAndPost(BusinessConfig bcfg) {
        return businessConfigDao.findDutyInEditAndPost(bcfg);
    }

    @Override
    public List<Duty> findDutyInEditAndPower(BusinessConfig bcfg) {
        return businessConfigDao.findDutyInEditAndPower(bcfg);
    }

    @Override
    public List<Duty> findDutyInReadAndGroup(BusinessConfig bcfg) {
        return businessConfigDao.findDutyInReadAndRole(bcfg);
    }

    @Override
    public List<Duty> findDutyInReadAndDepart(BusinessConfig bcfg) {
        return businessConfigDao.findDutyInReadAndDepart(bcfg);
    }

    @Override
    public List<Duty> findDutyInReadAndPost(BusinessConfig bcfg) {
        return businessConfigDao.findDutyInReadAndPost(bcfg);
    }

    @Override
    public List<Duty> findDutyInReadAndPower(BusinessConfig bcfg) {
        return businessConfigDao.findDutyInReadAndPower(bcfg);
    }

    @Override
    public List<BusinessConfig> findByBcfgAndUserInDoc(BusinessConfig bcfg, Users users) {
        //根据businessConfig，users查找在阅览者不同版本bcf的
        return businessConfigDao.findByBcfgAndUserInDoc(bcfg,users);
    }

    @Override
    public List<Duty> findDutyByBcfgAndUserInCreate(BusinessConfig bcfg, Users users) {
        // 根据当前bcfg查找申请人的职责
        return businessConfigDao.findDutyByBcfgAndUserInCreate(bcfg,users);
    }

    @Override
    public List<BusinessConfig> findByBcfgAndUserInArchEdit(BusinessConfig bcfg, Users users) {
        return businessConfigDao.findByBcfgAndUserInArchEdit(bcfg,users);
    }

    @Override
    public List<BusinessConfig> findByBcfgAndUserInArchRead(BusinessConfig bcfg, Users users) {
        return businessConfigDao.findByBcfgAndUserInArchRead(bcfg,users);
    }

    @Override
    public List<BusinessConfig> findByCfg(BusinessConfig bcfg) {
        return businessConfigDao.findByCfg(bcfg);
    }

    @Override
    public List<Duty> findDutyInGroup(BusinessConfig bcfg, Users users) {
        return businessConfigDao.findDutyInRole(bcfg, users);
    }

    @Override
    public List<Duty> findDutyInDepart(BusinessConfig bcfg, Users users) {
        return businessConfigDao.findDutyInDepart(bcfg,users);
    }

    @Override
    public List<Duty> findDutyInPower(BusinessConfig bcfg, Users users) {
        return businessConfigDao.findDutyInPower(bcfg,users);
    }

    @Override
    public List<Duty> findDutyInPost(BusinessConfig bcfg, Users users) {
        return businessConfigDao.findDutyInPost(bcfg,users);
    }


}