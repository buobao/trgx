package com.joint.base.service.impl;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Pager;
import com.fz.us.base.util.LogUtil;
import com.joint.base.activiti.JumpActivityCmd;
import com.joint.base.bean.FlowEnum;
import com.joint.base.dao.BaseEntityDao;
import com.joint.base.dao.BaseFlowDao;
import com.joint.base.entity.BusinessConfig;
import com.joint.base.entity.Users;
import com.joint.base.parent.BaseFlowEntity;
import com.joint.base.service.BaseFlowService;
import com.joint.base.service.BusinessConfigService;
import com.joint.base.service.ProcessConfigService;
import com.joint.base.service.UsersService;
import com.joint.base.service.activiti.WorkflowService;
import com.joint.base.service.activiti.WorkflowTraceService;
import com.joint.base.util.StringUtils;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;


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
public abstract class BaseFlowServiceImpl<T extends BaseFlowEntity,PK extends String> extends BaseEntityServiceImpl<T, PK> implements BaseFlowService<T,PK> {
    @Resource
    protected WorkflowTraceService workflowTraceService;
    @Resource
    protected WorkflowService workflowService;
    @Resource
    protected UsersService usersService;
    @Resource
    protected ProcessConfigService processConfigService;
    @Resource
    protected RuntimeService runtimeService;
    @Resource
    protected ManagementService managementService;
    @Resource
    protected BusinessConfigService businessConfigService;
    @Resource
    protected TaskService taskService;

    public abstract BaseFlowDao<T,PK> getBaseFlowDao();

    @Override
    public BaseEntityDao<T, PK> getBaseEntityDao() {
        return getBaseFlowDao();
    }

    @Override
    public PK save(T entity,String process,Map<String,Object> various) {
        entity.setProcessState(FlowEnum.ProcessState.Draft);

        BusinessConfig businessConfig = businessConfigService.getByBusinessKey(process);
        entity.setVersion(businessConfig.getVersion());
        PK pk =  save(entity);
        //发起流程
        ProcessInstance proIntance = workflowTraceService.createFlow(process, entity.getId(), various);
        return pk;
    }

    @Override
    public PK commit(T entity, String process, Map<String, Object> var1, Map<String, Object> var2,String curDutyId) {
        entity.setProcessState(FlowEnum.ProcessState.Running);
        PK pk =  save(entity);
        //发起流程
        workflowTraceService.createFlow(process,entity.getId(),var1);

        Task task = workflowService.getCurrentTask(entity.getId(), usersService.getLoginInfo());
        workflowTraceService.processAppprove(task,var2,curDutyId);

        if(workflowService.getCurrentTask(entity.getId(), usersService.getLoginInfo())==null){
            entity.setProcessState(FlowEnum.ProcessState.Finished);
            update(entity);
        }
        return pk;
    }

    /**
     * 废弃
     * @param entity
     * @param process
     * @param var1
     * @param var2
     * @param taskDefKey
     * @param varAssign
     * @param curDutyId
     * @return
     */
    @Override
    public PK commit(T entity, String process, Map<String, Object> var1, Map<String, Object> var2, String taskDefKey ,String varAssign, String curDutyId) {
        entity.setProcessState(FlowEnum.ProcessState.Running);
        PK pk =  save(entity);
        //发起流程
        workflowTraceService.createFlow(process,entity.getId(),var1);

        Task task = workflowService.getCurrentTask(entity.getId(),usersService.getLoginInfo());
        ProcessInstance processInstance = workflowService.getProIntanceByTask(task);
        approve(entity, null, var2,taskDefKey, varAssign, curDutyId);

        return pk;
    }

    @Override
    public void approve(T entity, FlowEnum.ProcessState processState,Map<String, Object> var,String curDutyId,String comment) {
        if(processState != null){
            entity.setProcessState(processState);
            update(entity);
        }
        Task task = workflowService.getCurrentTask(entity.getId(),usersService.getLoginInfo());
        if(comment != null && StringUtils.isNotEmpty(comment)) {
            taskService.addComment(task.getId(), task.getProcessInstanceId(), comment);
        }
        try {
            workflowTraceService.processAppprove(task,var,curDutyId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void approve(T entity, FlowEnum.ProcessState processState, Map<String, Object> var, String taskDefKey,String varAssign, String curDutyId) {
        if(processState != null){
            entity.setProcessState(processState);
            update(entity);
        }
        Task task = workflowService.getCurrentTask(entity.getId(),usersService.getLoginInfo());




        workflowTraceService.processAppprove(task,var,curDutyId);
    }



    @Override
    public void reject(T entity, String taskDefKey, int numStatus,String comment,String curDutyId) {
        entity.setProcessState(FlowEnum.ProcessState.Backed);
        super.update(entity);
        Users loginer = usersService.getLoginInfo();
        Task task = workflowService.getCurrentTask(entity.getId(), loginer);
        if (comment != null) {
            taskService.addComment(task.getId(), task.getProcessInstanceId(), comment);
        }
        taskService.claim(task.getId(), loginer.getId());
        taskService.setVariableLocal(task.getId(), "curDutyId",curDutyId);
        ProcessInstance proInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(entity.getId()).singleResult();
        Command<Object> command= new JumpActivityCmd(proInstance.getId(),taskDefKey,numStatus);
        managementService.executeCommand(command);


    }

    @Override
    public void destroy(T entity) {
        entity.setProcessState(FlowEnum.ProcessState.Destroy);
        entity.setState(BaseEnum.StateEnum.Disenable);

        if (runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(entity.getId()).active().list().size() > 0) {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(entity.getId()).active().singleResult();
            runtimeService.suspendProcessInstanceById(processInstance.getId());
        }

        super.update(entity);
    }

    @Override
    public void deny(T entity, String taskDefKey,String comment,String curDutyId) {
        entity.setProcessState(FlowEnum.ProcessState.Deny);
        super.update(entity);
        Users loginer = usersService.getLoginInfo();
        Task task = workflowService.getCurrentTask(entity.getId(), loginer);
        if(comment != null) {
            taskService.addComment(task.getId(), task.getProcessInstanceId(), comment);
        }
        taskService.claim(task.getId(), loginer.getId());
        taskService.setVariableLocal(task.getId(), "curDutyId", curDutyId);
        ProcessInstance proInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(entity.getId()).singleResult();
        Command<Object> command= new JumpActivityCmd(proInstance.getId(),taskDefKey,"否决", -1);
        managementService.executeCommand(command);


    }

    @Override
    public Pager findByPagerAndDraft(Users users, Pager pager, Map<String,Object> rmap) {
        return getBaseFlowDao().findByPagerAndDraft(users,pager,rmap);
    }

    @Override
    public Pager findByPagerAndLimit(boolean type,String procssKey,Pager pager,Map<String,Object> rmap) {
        return getBaseFlowDao().findByPagerAndLimit(type,procssKey,pager, rmap);
    }

    @Override
    public Pager findByPagerAndFinish(String procssKey,Pager pager, Map<String,Object> rmap) {
        BusinessConfig bcfg = businessConfigService.getByBusinessKey(procssKey);
        return getBaseFlowDao().findByPagerAndFinish(bcfg,pager, rmap);
    }

    @Override
    public Pager findByPagerAndBack(Pager pager, Map<String,Object> rmap) {
        return getBaseFlowDao().findByPagerAndBack(pager, rmap);
    }

    @Override
    public Pager findByPagerAndProcessState(Pager pager, Users users,String businessKey,FlowEnum.ProcessState processState,Map<String, Object> rmap){
        return getBaseFlowDao().findByPagerAndProcessState(pager,users,businessKey,processState,rmap);
    }



}