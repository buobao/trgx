package com.joint.base.dao.impl;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.util.Collections3;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.joint.base.dao.BusinessConfigDao;
import com.joint.base.entity.*;
import com.joint.base.service.BusinessConfigService;
import com.joint.base.service.CommonConfigService;
import com.joint.base.service.DutyService;
import com.joint.base.service.ProcessConfigService;
import com.joint.base.service.activiti.WorkflowService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


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
public class BusinessConfigDaoImpl extends BaseEntityDaoImpl<BusinessConfig, String> implements BusinessConfigDao {

    @Resource
    private WorkflowService workflowService;
    @Resource
    private BusinessConfigService businessConfigService;
    @Resource
    private DutyService dutyService;
    @Resource
    private CommonConfigService commonConfigService;
    @Resource
    private ProcessConfigService processConfigService;

    @Override
    public BusinessConfig getBusinessConfigByDefintionId(String processDefintionId) {
        Assert.notNull(processDefintionId, "processDefintionId is required");
        String hql="from BusinessConfig as b where b.processDefinitionId=:p and b.state=:s order by b.createDate desc";
        List<BusinessConfig> businessConfigList = getSession().createQuery(hql).setParameter("p",processDefintionId).setParameter("s", BaseEnum.StateEnum.Enable).list();
        if(businessConfigList.size() > 0){
            return businessConfigList.get(0);
        }else {
            return null;
        }
    }

    @Override
    public BusinessConfig getBusinessConfigByDefintionId(String processDefintionId, int version) {
        Assert.notNull(processDefintionId, "processDefintionId is required");
        Assert.notNull(version, "version is required");
        String  hql="from BusinessConfig as b where b.processDefintionId=:d and b.ptype=1 and b.vesion=:v and b.state=:s";
        List<BusinessConfig> businessConfigList=getSession().createQuery(hql).setParameter("d",processDefintionId).setParameter("v",version).setParameter("s", BaseEnum.StateEnum.Enable).list();
        if(businessConfigList.size() > 0){
            return businessConfigList.get(0);
        }else {
            return null;
        }
    }

    @Override
    public List<BusinessConfig> findBCfgByDefintionId(String processDefintionId) {
        Assert.notNull(processDefintionId, "processDefintionId is required");
        String hql="from BusinessConfig as b where b.processDefinitionId=:p and b.state=:s order by b.version desc";
        return getSession().createQuery(hql).setParameter("p",processDefintionId).setParameter("s", BaseEnum.StateEnum.Enable).list();
    }

    @Override
    public BusinessConfig getByBusinessKey(String businessKey) {
        Assert.notNull(businessKey, "businessKey is required");
        String defId = workflowService.getProDefinitionByKey(businessKey).getId();
        return getBusinessConfigByDefintionId(defId);
    }

    @Override
    public List<BusinessConfig> getConfigsByBusinessKey(String businessKey) {
        Assert.notNull(businessKey, "businessKey is required");
        List<ProcessDefinition> processDefinitions= workflowService.getProDefinitionsByKey(businessKey);
        List<BusinessConfig> businessConfigList=Lists.newArrayList();
        if(processDefinitions!=null && processDefinitions.size()>0){
            for(ProcessDefinition processDefinition:processDefinitions){
                BusinessConfig businessConfig= getBusinessConfigByDefintionId(processDefinition.getId());
                if(businessConfig!=null){
                    businessConfigList.add(businessConfig);
                }
            }
            return businessConfigList;
        }
        return null;
    }

    @Override
    public BusinessConfig getByBusinessKey(String businessKey, int version) {
        Assert.notNull(businessKey, "businessKey is required");
        String defId = workflowService.getProDefinitionByKey(businessKey).getId();
        return getBusinessConfigByDefintionId(defId,version);
    }


    @Override
    public BusinessConfig getByUserAndBusinessInFEdit(Users user, String proDefId) {
        String sql = "SELECT DISTINCT bc.id FROM sys_businessconfig AS bc\n" +
                "LEFT JOIN sys_commonconfig as cc ON cc.id = bc.editConfig_id\n" +
                "LEFT JOIN sys_config_depart as cd ON cc.id = cd.id\n" +
                "LEFT JOIN sys_config_post as cp ON cc.id = cp.id\n" +
                "LEFT JOIN sys_config_power as cpw ON cc.id = cpw.id\n" +
                "LEFT JOIN sys_config_role as scr ON cc.id = scr.id\n" +
                "LEFT JOIN sys_role as sr ON sr.id = scr.roleid\n"+
                "LEFT JOIN sys_role_sys_users as srsu ON sr.id = srsu.roleSet_id\n"+
                "LEFT JOIN sys_users as su ON su.id = srsu.usersSet_id\n"+
                "LEFT JOIN sys_role_depart as srd ON sr.id = srd.id\n"+
                "LEFT JOIN sys_department as sd ON sd.id = srd.depid\n"+
                "LEFT JOIN sys_role_power as srp ON sr.id = srp.id\n"+
                "LEFT JOIN sys_power as sp ON sp.id = srp.powerid\n"+
                "LEFT JOIN sys_duty as duty ON (duty.post_id =cp.postid OR duty.department_id = cd.depid OR duty.power_id=cpw.powerid or duty.users_id=su.id OR duty.department_id=sd.id OR duty.power_id=sp.id)\n" +
                "WHERE duty.users_id=:u AND bc.processDefinitionId=:p AND NOT ISNULL(bc.editConfig_id)";

        List<String> objectList = getSession().createSQLQuery(sql).setParameter("p",proDefId).setParameter("u", user.getId()).list();
        if(objectList.size() > 0){
            return businessConfigService.get(objectList.get(0));
        }else {
            return null;
        }
    }


    @Override
    public List<BusinessConfig> getDocByUser(BusinessConfig bcfg,Users user) {
        //查询可阅览的业务
        Assert.notNull(user,"users is required");
        String sql ="SELECT DISTINCT bc.id FROM sys_businessconfig as bc\n" +
                "LEFT JOIN sys_commonconfig as cc ON cc.id = bc.docConfig_id\n" +
                "LEFT JOIN sys_config_depart as cd ON cc.id = cd.id\n" +
                "LEFT JOIN sys_config_post as cp ON cc.id = cp.id\n" +
                "LEFT JOIN sys_config_power as cpw ON cc.id = cpw.id\n" +
                "LEFT JOIN sys_duty as duty ON (duty.post_id =cp.postid OR duty.department_id = cd.depid OR duty.power_id=cpw.powerid)\n" +
                "WHERE duty.users_id=:u AND bc.processDefinitionId=:p AND NOT ISNULL(bc.docConfig_id)";
        List<String> objectList = getSession().createSQLQuery(sql).setParameter("u", user.getId()).setParameter("p",bcfg.getProcessDefinitionId()).list();
        List<BusinessConfig> businessConfigList = new ArrayList<BusinessConfig>();
        for(String id : objectList){
            businessConfigList.add(businessConfigService.get(id));
        }
        return businessConfigList;
    }

    @Override
    public List<BusinessConfig> getDocReadByUser(BusinessConfig bcfg,Users users) {
        Assert.notNull(users,"users is required");
        String sql="SELECT DISTINCT bc.id FROM sys_businessconfig as bc\n" +
                "LEFT JOIN sys_commonconfig as cc ON cc.id = bc.readConfig_id\n" +
                "LEFT JOIN sys_config_depart as cd ON cc.id = cd.id\n" +
                "LEFT JOIN sys_config_post as cp ON cc.id = cp.id\n" +
                "LEFT JOIN sys_config_power as cpw ON cc.id = cpw.id\n" +
                "LEFT JOIN sys_duty as duty ON (duty.post_id =cp.postid OR duty.department_id = cd.depid OR duty.power_id=cpw.powerid)\n" +
                "WHERE duty.users_id=:u AND bc.processDefinitionId=:p AND NOT ISNULL(bc.readConfig_id)";
        List<String> objectList = getSession().createSQLQuery(sql).setParameter("u", users.getId()).setParameter("p",bcfg.getProcessDefinitionId()).list();
        List<BusinessConfig> businessConfigList = new ArrayList<BusinessConfig>();
        for(String id : objectList){
            businessConfigList.add(businessConfigService.get(id));
        }
        return businessConfigList;
    }

    @Override
    public List<BusinessConfig> getDocEditByUser(BusinessConfig bcfg,Users users) {
        String sql="SELECT DISTINCT bc.id FROM sys_businessconfig as bc\n" +
                "LEFT JOIN sys_commonconfig as cc ON cc.id = bc.editConfig_id\n" +
                "LEFT JOIN sys_config_depart as cd ON cc.id = cd.id\n" +
                "LEFT JOIN sys_config_post as cp ON cc.id = cp.id\n" +
                "LEFT JOIN sys_config_power as cpw ON cc.id = cpw.id\n" +
                "LEFT JOIN sys_duty as duty ON (duty.post_id =cp.postid OR duty.department_id = cd.depid OR duty.power_id=cpw.powerid)\n" +
                "WHERE duty.users_id=:u AND bc.processDefinitionId=:p AND NOT ISNULL(bc.editConfig_id)";
        List<String> objectList = getSession().createSQLQuery(sql).setParameter("u", users.getId()).setParameter("p",bcfg.getProcessDefinitionId()).list();
        List<BusinessConfig> businessConfigList = new ArrayList<BusinessConfig>();
        for(String id : objectList){
            businessConfigList.add(businessConfigService.get(id));
        }
        return businessConfigList;
    }

    @Override
    public List<Duty> findDutyInRole(BusinessConfig bcfg,Users users) {
        // 查找users在group中的职位
       String sql="SELECT DISTINCT du.id FROM sys_businessconfig as bc\n" +
               "  LEFT JOIN sys_create_group as cg ON cg.bc_id=bc.id\n" +
               "  LEFT JOIN sys_group        as g ON g.id=cg.gid\n" +
               "  LEFT JOIN sys_group_users  as gu ON gu.id = g.id\n" +
               "  LEFT JOIN sys_users as u on gu.user_id = u.id\n" +
               "  LEFT JOIN sys_group_depart AS gd on gd.id= g.id\n" +
               "  LEFT JOIN sys_department as d on d.id=gd.dep_id\n" +
               "  LEFT JOIN sys_group_power AS gp on gp.id = g.id\n" +
               "  LEFT JOIN sys_power as p ON p.id = gp.power_id\n" +
               "  LEFT JOIN sys_duty as du ON (du.department_id = d.id OR du.power_id=p.id OR du.users_id = u.id) AND du.users_id=:u AND du.state='Enable'\n" +
               "  WHERE  bc.id=:b";
        List<Object> objList = getSession().createSQLQuery(sql).setParameter("u", users.getId()).setParameter("b", bcfg.getId()).list();
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
    public List<Duty> findDutyInDepart(BusinessConfig bcfg,Users users) {
        String sql="SELECT DISTINCT du.id FROM sys_businessconfig as bc\n" +
                "LEFT JOIN sys_create_depart as cg ON cg.business_id=bc.id\n" +
                "LEFT JOIN sys_department as d on d.id = cg.createrDeapartSet_id\n" +
                "LEFT JOIN sys_duty as du ON du.department_id = d.id AND du.users_id=:u AND du.state='Enable' \n" +
                "WHERE  bc.id=:b \n";
        List<Object> objList = getSession().createSQLQuery(sql).setParameter("u", users.getId()).setParameter("b", bcfg.getId()).list();
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
    public List<Duty> findDutyInPower(BusinessConfig bcfg,Users users) {
        // 查找users在power中的职位
        String sql="SELECT du.id FROM sys_businessconfig as bc\n" +
                "  LEFT JOIN sys_create_power as cg ON cg.bc_id=bc.id\n" +
                "  LEFT JOIN sys_power as p ON p.id=cg.pid\n" +
                "  LEFT JOIN sys_duty as du ON (du.power_id=p.id) AND du.users_id=:u AND du.state='Enable' \n" +
                "  WHERE bc.id=:b";
        List<Object> objList = getSession().createSQLQuery(sql).setParameter("u", users.getId()).setParameter("b", bcfg.getId()).list();
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
    public List<Duty> findDutyInPost(BusinessConfig bcfg,Users users) {
        //  查找users在post中的职位
        String sql="SELECT du.id FROM sys_businessconfig as bc\n" +
                "LEFT JOIN sys_create_post as cg ON cg.business_id=bc.id\n" +
                "LEFT JOIN sys_post as d on d.id =cg.createrPostSet_id\n" +
                "LEFT JOIN sys_duty as du ON du.post_id = d.id AND du.users_id=:u AND du.state='Enable' \n" +
                "WHERE bc.id=:b\n";
        List<Object> objList = getSession().createSQLQuery(sql).setParameter("u", users.getId()).setParameter("b", bcfg.getId()).list();
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
    public List<Duty> findDutyInDocReadAndRole(BusinessConfig pcfg){
        //查找业务配置表中对应的文档阅览者中群组
        Assert.notNull(pcfg,"pcfg is required");
        String sql="SELECT DISTINCT du.id FROM sys_businessconfig as bc\n" +
                "LEFT JOIN sys_doc_group AS sdg ON bc.id = sdg.bc_id\n" +
                "LEFT JOIN sys_group as sg ON sg.id = sdg.gid\n" +
                "LEFT JOIN sys_group_users as sgu ON sg.id = sgu.id\n" +
                "LEFT JOIN sys_users as su ON su.id = sgu.user_id\n" +
                "LEFT JOIN sys_group_depart as sgd ON sg.id = sgd.id\n" +
                "LEFT JOIN sys_department as sd ON sd.id = sgd.dep_id\n" +
                "LEFT JOIN sys_group_power as sgp ON sg.id = sgp.id\n" +
                "LEFT JOIN sys_power as sp ON sp.id = sgp.power_id\n" +
                "LEFT JOIN sys_duty as du ON (du.users_id=su.id OR du.department_id=sd.id OR du.power_id=sp.id) AND du.state='Enable'\n" +
                "where bc.id=:b";
        List<Object> objList = getSession().createSQLQuery(sql).setParameter("b", pcfg.getId()).list();
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
    public List<Duty> findDutyInDocReadAndDepart(BusinessConfig pcfg){
        //查找业务配置表中对应的文档阅览者中部门
        Assert.notNull(pcfg,"pcfg is required");
        String sql="SELECT DISTINCT du.id FROM sys_businessconfig as bc\n" +
                "LEFT JOIN sys_doc_depart as sdd ON sdd.business_id=bc.id\n" +
                "LEFT JOIN sys_department as sd ON sd.id = sdd.docDepartSet_id\n" +
                "LEFT JOIN sys_duty as du ON du.department_id=sd.id AND du.state='Enable'\n" +
                "where bc.id=:b";
        List<Object> objList = getSession().createSQLQuery(sql).setParameter("b",pcfg.getId()).list();
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
    public List<Duty> findDutyInDocReadAndPost(BusinessConfig pcfg){
        //查找业务配置表中对应的文档阅览者中岗位
        Assert.notNull(pcfg,"pcfg is required");
        String sql="SELECT DISTINCT du.id FROM sys_businessconfig as bc\n" +
                "LEFT JOIN sys_doc_post as sdp ON bc.id = sdp.business_id\n" +
                "LEFT JOIN sys_post as sp ON sp.id = sdp.docPostSet_id\n" +
                "LEFT JOIN sys_duty as du ON du.post_id=sp.id AND du.state='Enable'\n" +
                "where bc.id=:b";
        List<Object> objList = getSession().createSQLQuery(sql).setParameter("b",pcfg.getId()).list();
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
    public List<Duty> findDutyInDocReadAndPower(BusinessConfig pcfg){
        //查找业务配置表中对应的文档阅览者中职权
        Assert.notNull(pcfg,"pcfg is required");
        String sql="SELECT DISTINCT du.id FROM sys_businessconfig as bc\n" +
                "LEFT JOIN sys_doc_power as sdp ON bc.id = sdp.bc_id\n" +
                "LEFT JOIN sys_power as sp ON sp.id = sdp.pid\n" +
                "LEFT JOIN sys_duty as du ON du.power_id=sp.id AND du.state='Enable'\n" +
                "where bc.id=:b";
        List<Object> objList = getSession().createSQLQuery(sql).setParameter("b",pcfg.getId()).list();
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
    public List<Duty> findDutyInEditAndRole(BusinessConfig bcfg) {
        //查找业务配置表中对应的归档后编辑者中群组
        Assert.notNull(bcfg,"bcfg is required");
        String sql="SELECT DISTINCT du.id FROM sys_businessconfig as bc\n" +
                "LEFT JOIN sys_archive_edit_group AS sdg ON bc.id = sdg.bc_id\n" +
                "LEFT JOIN sys_group as sg ON sg.id = sdg.gid\n" +
                "LEFT JOIN sys_group_users as sgu ON sg.id = sgu.id\n" +
                "LEFT JOIN sys_users as su ON su.id = sgu.user_id\n" +
                "LEFT JOIN sys_group_depart as sgd ON sg.id = sgd.id\n" +
                "LEFT JOIN sys_department as sd ON sd.id = sgd.dep_id\n" +
                "LEFT JOIN sys_group_power as sgp ON sg.id = sgp.id\n" +
                "LEFT JOIN sys_power as sp ON sp.id = sgp.power_id\n" +
                "LEFT JOIN sys_duty as du ON (du.users_id=su.id OR du.department_id=sd.id OR du.power_id=sp.id) AND du.state='Enable'\n" +
                "where bc.id=:b";
        List<Object> objList = getSession().createSQLQuery(sql).setParameter("b",bcfg.getId()).list();
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
    public List<Duty> findDutyInEditAndDepart(BusinessConfig bcfg) {
        //查找业务配置表中对应的归档后编辑者中部门
        Assert.notNull(bcfg,"bcfg is required");
        String sql="SELECT DISTINCT du.id FROM sys_businessconfig as bc\n" +
                "LEFT JOIN sys_archive_edit_depart as sdd ON sdd.business_id=bc.id\n" +
                "LEFT JOIN sys_department as sd ON sd.id = sdd.archiveEditDeapartSet_id\n" +
                "LEFT JOIN sys_duty as du ON du.department_id=sd.id AND du.state='Enable'\n" +
                "where bc.id=:b";
        List<Object> objList = getSession().createSQLQuery(sql).setParameter("b",bcfg.getId()).list();
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
    public List<Duty> findDutyInEditAndPost(BusinessConfig bcfg) {
        //查找业务配置表中对应的归档后编辑者中岗位
        Assert.notNull(bcfg,"bcfg is required");
        String sql="SELECT DISTINCT du.id FROM sys_businessconfig as bc\n" +
                "LEFT JOIN sys_archive_edit_post as sdp ON bc.id = sdp.business_id\n" +
                "LEFT JOIN sys_post as sp ON sp.id = sdp.archiveEditPostSet_id\n" +
                "LEFT JOIN sys_duty as du ON du.post_id=sp.id AND du.state='Enable'\n" +
                "where bc.id=:b";
        List<Object> objList = getSession().createSQLQuery(sql).setParameter("b",bcfg.getId()).list();
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
    public List<Duty> findDutyInEditAndPower(BusinessConfig bcfg) {
        //查找业务配置表中对应的归档后编辑者中职权
        Assert.notNull(bcfg,"bcfg is required");
        String sql="SELECT DISTINCT du.id FROM sys_businessconfig as bc\n" +
                "LEFT JOIN sys_archive_edit_power as sdp ON bc.id = sdp.bc_id\n" +
                "LEFT JOIN sys_power as sp ON sp.id = sdp.pid\n" +
                "LEFT JOIN sys_duty as du ON du.power_id=sp.id AND du.state='Enable'\n" +
                "where bc.id=:b";
        List<Object> objList = getSession().createSQLQuery(sql).setParameter("b",bcfg.getId()).list();
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
    public List<Duty> findDutyInReadAndRole(BusinessConfig bcfg) {
        //查找业务配置表中对应的归档后读者群组
        Assert.notNull(bcfg,"bcfg is required");
        String sql="SELECT DISTINCT du.id FROM sys_businessconfig as bc\n" +
                "LEFT JOIN sys_archive_group AS sdg ON bc.id = sdg.bc_id\n" +
                "LEFT JOIN sys_group as sg ON sg.id = sdg.gid\n" +
                "LEFT JOIN sys_group_users as sgu ON sg.id = sgu.id\n" +
                "LEFT JOIN sys_users as su ON su.id = sgu.user_id\n" +
                "LEFT JOIN sys_group_depart as sgd ON sg.id = sgd.id\n" +
                "LEFT JOIN sys_department as sd ON sd.id = sgd.dep_id\n" +
                "LEFT JOIN sys_group_power as sgp ON sg.id = sgp.id\n" +
                "LEFT JOIN sys_power as sp ON sp.id = sgp.power_id\n" +
                "LEFT JOIN sys_duty as du ON (du.users_id=su.id OR du.department_id=sd.id OR du.power_id=sp.id) AND du.state='Enable'\n" +
                "where bc.id=:b";
        List<Object> objList = getSession().createSQLQuery(sql).setParameter("b",bcfg.getId()).list();
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
    public List<Duty> findDutyInReadAndDepart(BusinessConfig bcfg) {
        //查找业务配置表中对应的归档后读者部门
        Assert.notNull(bcfg,"bcfg is required");
        String sql="SELECT DISTINCT du.id FROM sys_businessconfig as bc\n" +
                "LEFT JOIN sys_archive_read_depart as sdd ON sdd.business_id=bc.id\n" +
                "LEFT JOIN sys_department as sd ON sd.id = sdd.archiveReadDeapartSet_id\n" +
                "LEFT JOIN sys_duty as du ON du.department_id=sd.id AND du.state='Enable'\n" +
                "where bc.id=:b";
        List<Object> objList = getSession().createSQLQuery(sql).setParameter("b",bcfg.getId()).list();
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
    public List<Duty> findDutyInReadAndPost(BusinessConfig bcfg) {
        //查找业务配置表中对应的归档后读者岗位
        Assert.notNull(bcfg,"bcfg is required");
        String sql="SELECT DISTINCT du.id FROM sys_businessconfig as bc\n" +
                "LEFT JOIN sys_archive_read_post as sdp ON bc.id = sdp.business_id\n" +
                "LEFT JOIN sys_post as sp ON sp.id = sdp.archiveReadPostSet_id\n" +
                "LEFT JOIN sys_duty as du ON du.post_id=sp.id AND du.state='Enable'\n" +
                "where bc.id=:b";
        List<Object> objList = getSession().createSQLQuery(sql).setParameter("b",bcfg.getId()).list();
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
    public List<Duty> findDutyInReadAndPower(BusinessConfig bcfg) {
        //查找业务配置表中对应的归档读者中职权
        Assert.notNull(bcfg,"bcfg is required");
        String sql="SELECT DISTINCT du.id FROM sys_businessconfig as bc\n" +
                "LEFT JOIN sys_archive_power as sdp ON bc.id = sdp.bc_id\n" +
                "LEFT JOIN sys_power as sp ON sp.id = sdp.pid\n" +
                "LEFT JOIN sys_duty as du ON du.power_id=sp.id AND du.state='Enable'\n" +
                "where bc.id=:b";
        List<Object> objList = getSession().createSQLQuery(sql).setParameter("b",bcfg.getId()).list();
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
    public List<BusinessConfig> findByCfg(BusinessConfig bcfg) {
        Assert.notNull(bcfg,"bcfg is required");
        String hql="from BusinessConfig as b where b.processDefinitionId=:p and b.ptype=0 and b.state=:s order by b.version desc";
        List<BusinessConfig> businessConfigList = getSession().createQuery(hql).setParameter("p",bcfg.getProcessDefinitionId()).setParameter("s", BaseEnum.StateEnum.Enable).list();
        if(businessConfigList.size() > 0){
            return businessConfigList;
        }else {
            return Lists.newArrayList();
        }
    }

    @Override
    public List<BusinessConfig> findByBcfgAndUserInArchEdit(BusinessConfig bcfg, Users users) {
        Assert.notNull(bcfg,"bcfg is required");
        Assert.notNull(users,"users is required");
        List<Duty> dutyList =Lists.newArrayList();
        Set<BusinessConfig> businessConfigSet= Sets.newHashSet();
        List<BusinessConfig> bcfgList = Lists.newArrayList();
        List<BusinessConfig> businessConfigList= businessConfigService.findBCfgByDefintionId(bcfg.getProcessDefinitionId());
        for(BusinessConfig businessConfig:businessConfigList) {
            List<ProcessConfig> processConfigList=processConfigService.findConfigByBcfg(businessConfig);
            if(processConfigList!=null && processConfigList.size()>0){
                for(ProcessConfig processConfig:processConfigList){
                    List<Duty> duties=commonConfigService.findDutyByConfig(processConfig.getCommonConfig().getId());
                    dutyList = Collections3.intersection(duties, dutyService.getDutys(users));
                    if(dutyList.size()>0){
                        businessConfigSet.add(businessConfig);
                    }
                }
            }
            if (businessConfig.getEditConfig() != null) {
                List<Duty> configs= commonConfigService.findDutyByConfig(businessConfig.getEditConfig().getId());
                dutyList = Collections3.intersection(configs, dutyService.getDutys(users));
                if (dutyList.size() > 0) {
                    businessConfigSet.add(businessConfig);
                }
            }
        }
        bcfgList.addAll(businessConfigSet);
        return bcfgList;
    }

    @Override
    public List<BusinessConfig> findByBcfgAndUserInArchRead(BusinessConfig bcfg, Users users) {
        Assert.notNull(bcfg,"bcfg is required");
        Assert.notNull(users,"users is required");
        List<Duty> dutyList =Lists.newArrayList();
        Set<BusinessConfig> businessConfigSet= Sets.newHashSet();
        List<BusinessConfig> bcfgList = Lists.newArrayList();
        List<BusinessConfig> businessConfigList= businessConfigService.findBCfgByDefintionId(bcfg.getProcessDefinitionId());
        for(BusinessConfig businessConfig:businessConfigList) {
            List<ProcessConfig> processConfigList=processConfigService.findConfigByBcfg(businessConfig);
            if(processConfigList!=null && processConfigList.size()>0){
                for(ProcessConfig processConfig:processConfigList){
                    List<Duty> duties=commonConfigService.findDutyByConfig(processConfig.getCommonConfig().getId());
                    dutyList = Collections3.intersection(duties, dutyService.getDutys(users));
                    if(dutyList.size()>0){
                        businessConfigSet.add(businessConfig);
                    }
                }
            }
            if (businessConfig.getReadConfig() != null) {
                List<Duty> configs = commonConfigService.findDutyByConfig(businessConfig.getReadConfig().getId());
                dutyList = Collections3.intersection(configs, dutyService.getDutys(users));
                if (dutyList.size() > 0) {
                    businessConfigSet.add(businessConfig);
                }
            }
        }
        bcfgList.addAll(businessConfigSet);
        return bcfgList;
    }

    @Override
    public List<BusinessConfig> findByBcfgAndUserInDoc(BusinessConfig bcfg, Users users) {
        Assert.notNull(bcfg,"bcfg is required");
        Assert.notNull(users,"users is required");
        List<Duty> dutyList =Lists.newArrayList();
        Set<BusinessConfig> businessConfigSet= Sets.newHashSet();
        List<BusinessConfig> bcfgList = Lists.newArrayList();
        List<BusinessConfig> businessConfigList= businessConfigService.findBCfgByDefintionId(bcfg.getProcessDefinitionId());
        for(BusinessConfig businessConfig:businessConfigList){
            List<ProcessConfig> processConfigList=processConfigService.findConfigByBcfg(businessConfig);
            if(processConfigList!=null && processConfigList.size()>0){
                for(ProcessConfig processConfig:processConfigList){
                    List<Duty> duties=commonConfigService.findDutyByConfig(processConfig.getCommonConfig().getId());
                    dutyList = Collections3.intersection(duties, dutyService.getDutys(users));
                    if(dutyList.size()>0){
                        businessConfigSet.add(businessConfig);
                    }
                }
            }
            if(businessConfig.getDocConfig()!=null){
                List<Duty> configs = commonConfigService.findDutyByConfig(businessConfig.getDocConfig().getId());
                dutyList = Collections3.intersection(configs,dutyService.getDutys(users));
                if(dutyList.size()>0){
                    businessConfigSet.add(businessConfig);
                }
            }
        }
        bcfgList.addAll(businessConfigSet);
        return bcfgList;
    }

    @Override
    public List<Duty> findDutyByBcfgAndUserInCreate(BusinessConfig bcfg, Users users) {
        Assert.notNull(bcfg,"bcfg is required");
        Assert.notNull(users,"users is required");
        List<Duty> dutyList = Lists.newArrayList();
        CommonConfig commonConfig = bcfg.getCreateConfig();
        if(commonConfig!=null){
            dutyList= commonConfigService.findDutyByConfig(commonConfig.getId());
            dutyList = Collections3.intersection(dutyList,dutyService.getDutys(users));
        }
        if(dutyList.size()>0){
            return dutyList;
        }
        return null;
    }

    @Override
    public List<Duty> findDutyInCreateByConfig(BusinessConfig bcfg, Users users) {
        Assert.notNull(bcfg,"bcfg is required");
        Assert.notNull(users,"users is required");
        List<Duty> dutyList = Lists.newArrayList();

        dutyList=commonConfigService.findDutyByConfig(bcfg.getCreateConfig().getId());
        dutyList = Collections3.intersection(dutyList,dutyService.getDutys(users));

        return dutyList;
    }
}
