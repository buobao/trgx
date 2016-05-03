package com.joint.base.dao.impl;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Pager;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.joint.base.bean.FlowEnum;
import com.joint.base.dao.BaseFlowDao;
import com.joint.base.entity.BusinessConfig;
import com.joint.base.entity.Users;
import com.joint.base.parent.BaseFlowEntity;
import com.joint.base.service.BusinessConfigService;
import com.joint.base.service.UsersService;
import com.joint.base.service.activiti.WorkflowService;
import com.joint.base.util.StringUtils;
import org.activiti.engine.HistoryService;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by hpj on 2014/12/22.
 */
@Repository
public class BaseFlowDaoImpl<T extends BaseFlowEntity,PK extends Serializable> extends BaseEntityDaoImpl<T,PK> implements BaseFlowDao<T, PK> {
    @Resource
    protected WorkflowService workflowService;
    @Resource
    protected HistoryService historyService;
    @Resource
    protected UsersService usersService;
    @Resource
    protected BusinessConfigService businessConfigService;

    @Override
    public Pager findByPagerAndDraft( Users users, Pager pager, Map<String,Object> rmap) {
        //草稿箱中所有当前人的单子
        List<String> idList = workflowService.findDocIdByNum(0,users);
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(entityClass);
        detachedCriteria.add(Restrictions.eq("processState", FlowEnum.ProcessState.Draft));
        if(idList.size()>0){
            detachedCriteria.add(Restrictions.in("id", idList));
        }else{
            return pager;
        }
        return findByPager(pager,detachedCriteria,null,null,null,null,rmap);
    }

    @Override
    public Pager findByPagerAndLimit(boolean type,String key,Pager pager,Map<String,Object> rmap) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(entityClass);
        Users users = usersService.getLoginInfo();
        List<BusinessConfig> businessConfigList=businessConfigService.getConfigsByBusinessKey(key);
        BusinessConfig bConfig=businessConfigService.getByBusinessKey(key);
        List<String> bussinessList=null;
        List<BusinessConfig> bcList=null;
        Set<BusinessConfig> businessConfigSet=Sets.newHashSet();

        if(businessConfigList.size()==0){
            return new Pager();
        }

        //查询所有单子
        if(type==true){
            bussinessList = workflowService.getAllProcessByUsers(businessConfigList,bConfig, users);
        }else{
            bussinessList = workflowService.getAllInProcessByUsers(businessConfigList,bConfig, users);
        }
        if(bussinessList.size() >0){
            detachedCriteria.add(Restrictions.in("id", bussinessList));
            if(bConfig!=null && bConfig.getPtype()==1){
                for(BusinessConfig business:businessConfigList){
                    bcList= businessConfigService.findByBcfgAndUserInDoc(business,users);
                    businessConfigSet.addAll(bcList);
                }
                List<Integer> vList = Lists.newArrayList();
                for(BusinessConfig bcf:businessConfigSet){
                    vList.add(bcf.getVersion());
                }
                //判断业务的version是否在cfg对应的version
                detachedCriteria.add(Restrictions.in("version", vList));
            }
        }else {
            return new Pager();
        }

        return findByPager(pager,detachedCriteria,null,null,null,null,rmap);
    }

    @Override
    public Pager findByPagerAndFinish(BusinessConfig bcfg,Pager pager, Map<String,Object> rmap) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(entityClass);
        Users users = usersService.getLoginInfo();
        // 所有已归档的单子
        List<String> bussinessList = workflowService.getAllInFinishByUsers(null,bcfg,users);

        List<BusinessConfig> readList= businessConfigService.findByBcfgAndUserInArchRead(bcfg, users);
        List<BusinessConfig> editList= businessConfigService.findByBcfgAndUserInArchEdit(bcfg, users);
        List<BusinessConfig> docList= businessConfigService.findByBcfgAndUserInDoc(bcfg, users);

        Set<Integer> vList =new HashSet<Integer>();
        for(BusinessConfig bcf:readList){
            vList.add(bcf.getVersion());
        }
        for(BusinessConfig bcf:editList){
            vList.add(bcf.getVersion());
        }
        for(BusinessConfig bcf:docList){
            vList.add(bcf.getVersion());
        }

        if(vList.size()==0){
            return pager;
        }
        if(bussinessList.size() >0){
            detachedCriteria.add(Restrictions.in("id", bussinessList));
            if(bcfg.getPtype()==1){
                //判断业务的version是否在cfg对应的version
                detachedCriteria.add(Restrictions.in("version", vList));
            }
        }else {
            return pager;
        }
        detachedCriteria.addOrder(Order.desc("createDate"));
        return findByPager(pager,detachedCriteria,null,null,null,null,rmap);
    }

    @Override
    public Pager findByPagerAndBack(Pager pager, Map<String,Object> rmap) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(entityClass);
        // 所有退回的单子
        List<String> bussinessList = workflowService.getAllInBackByUsers();
        if(bussinessList.size() >0){
            detachedCriteria.add(Restrictions.in("id", bussinessList));
        }else {
            return pager;
        }
        detachedCriteria.add(Restrictions.eq("processState", FlowEnum.ProcessState.Backed));
        return findByPager(pager,detachedCriteria,null,null,null,null,rmap);
    }

    @Override
    public Pager findByPagerAndProcessState(Pager pager, Users users,String businessKey,FlowEnum.ProcessState processState,Map<String, Object> rmap) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(entityClass);
        List<String> idList =Lists.newArrayList();
        Set<Integer> vList =new HashSet<Integer>();
        Set<BusinessConfig> bcList= Sets.newHashSet();

        List<BusinessConfig> businessConfigList=businessConfigService.getConfigsByBusinessKey(businessKey);

        BusinessConfig bConfig=businessConfigService.getByBusinessKey(businessKey);
        if(businessConfigList==null){
            return new Pager();
        }

        if(processState.equals(FlowEnum.ProcessState.Draft)){
            // 所有草稿
            idList= workflowService.findDocIdByNum(0, users);
        }else if(processState.equals(FlowEnum.ProcessState.Backed)){
            // 所有退回的单子
            idList= workflowService.getAllInBackByUsers();
        }else if(processState.equals(FlowEnum.ProcessState.Running)){
            //计算当前人员可见的版本号
            for(BusinessConfig bcfg:businessConfigList){
                List<BusinessConfig> businessConfigs= businessConfigService.findByBcfgAndUserInDoc(bcfg, users);
                bcList.addAll(businessConfigs);
            }

            idList = workflowService.getAllInProcessByUsers(businessConfigList,bConfig, users);

            if(idList.size()>0){
                for(BusinessConfig bcf:bcList){
                    vList.add(bcf.getVersion());
                }
                if(bConfig!=null &&  bConfig.getPtype()==1){
                    //判断业务的version是否在cfg对应的version
                    detachedCriteria.add(Restrictions.in("version", vList));
                }
            }

        } else{
            Set<BusinessConfig> readList=Sets.newHashSet();
            Set<BusinessConfig> editList=Sets.newHashSet();
            Set<BusinessConfig> docList=Sets.newHashSet();
            Set<BusinessConfig> allList=Sets.newHashSet();

            //所有归档的单子
            idList.addAll(workflowService.getAllInFinishByUsers(businessConfigList,bConfig, users));

            if(idList.size() >0){
                for(BusinessConfig businessConfig:businessConfigList){
                    readList.addAll(businessConfigService.findByBcfgAndUserInArchRead(businessConfig, users));
                    editList.addAll(businessConfigService.findByBcfgAndUserInArchEdit(businessConfig, users));
                    docList.addAll(businessConfigService.findByBcfgAndUserInDoc(businessConfig, users));
                    allList.addAll(readList);
                    allList.addAll(editList);
                    allList.addAll(docList);
                }
            }

            if(bConfig!=null && bConfig.getPtype()==1){
                for(BusinessConfig bcf:allList){
                    vList.add(bcf.getVersion());
                }
                if(bConfig!=null &&  bConfig.getPtype()==1){
                    //判断业务的version是否在cfg对应的version
                    detachedCriteria.add(Restrictions.in("version", vList));
                }
            }
        }
        if(idList.size() >0){
            detachedCriteria.add(Restrictions.in("id", idList));
        }else {
            return new Pager();
        }

        detachedCriteria.add(Restrictions.eq("processState", processState));
        return findByPager(pager,detachedCriteria,null,null,null,null,rmap);
    }
}
