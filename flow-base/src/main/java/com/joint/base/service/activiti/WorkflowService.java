package com.joint.base.service.activiti;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.joint.base.entity.BusinessConfig;
import com.joint.base.entity.Users;
import com.joint.base.service.BusinessConfigService;
import com.joint.base.service.DutyService;
import com.joint.base.service.UsersService;
import com.joint.base.util.DataUtil;
import com.joint.base.util.StringUtils;
import com.joint.base.util.WorkflowUtils;
import org.activiti.engine.*;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.history.*;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.*;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


@Service
public class WorkflowService {


    @Resource
    protected RuntimeService runtimeService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected RepositoryService repositoryService;
    @Autowired
    protected IdentityService identityService;
    @Autowired
    protected HistoryService historyService;

    @Resource
    protected UsersService usersService;
    @Resource
    protected DutyService dutyService;
    @Resource
    protected BusinessConfigService businessConfigService;

    protected Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 获取本地var对应的值
     * @param taskId
     * @param var
     * @return
     */
    public String getVarInLocal(String taskId,String var ){
        List<HistoricVariableInstance> histance = historyService.createHistoricVariableInstanceQuery().taskId(taskId).variableName(var).list();
        if(histance.size()>0){
            HistoricVariableInstanceEntity hisVarEntity = (HistoricVariableInstanceEntity) histance.get(0);
            String value = hisVarEntity.getTextValue();
            return value;
        }
        return null;
    }

    /**
     * 获取审批文档中的numStatus
     * @param bussinessId --- unid
     * @return
     */
    public Integer getNumStatus(String bussinessId,Users users){
        Task task = getCurrentTask(bussinessId, users);
        if(task == null){
            return -1;
        }
        int numStatus = (Integer)runtimeService.getVariable(task.getExecutionId(),"numStatus");
        return numStatus;
    }
    /**
     * 获取指定num下的所有单子的id
     * @param num
     * @return
     */
    public List<String> findDocIdByNum(int num,Users users){
        List<ProcessInstance> proList = runtimeService.createProcessInstanceQuery().variableValueEquals("numStatus", num).involvedUser(users.getId()).active().list();
        List<String> docIdList = new ArrayList<String>();
        for(ProcessInstance processInstance : proList){
            docIdList.add(processInstance.getBusinessKey());
        }
        return docIdList;
    }
    /**
     * 获取流程定义发起人
     * @param bussinessKey--- 流程实例key(业务的id)
     * @return
     */
    public Users getStarter(String bussinessKey){
        List<HistoricProcessInstance> hpiList = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(bussinessKey).list();
        if(hpiList.size() == 0 ){
            return null;
        }
        HistoricProcessInstance hpi = hpiList.get(0);
        String userId = hpi.getStartUserId();
        Users users = usersService.get(userId);
        return users;
    }


    /**
     * 根据Key来查找最新的流程定义
     * @param bussinessKey
     * @return
     */
    public ProcessDefinition getProDefinitionByKey(String bussinessKey){
        List<ProcessDefinition>  defineList = repositoryService.createProcessDefinitionQuery().processDefinitionKey(bussinessKey).orderByProcessDefinitionVersion().desc().list();
        if(defineList.size() >0){
            return defineList.get(0);
        }else{
            return null;
        }
    }

    /**
     * 根据Key来查找所有的流程定义
     * @param bussinessKey
     * @return
     */
    public List<ProcessDefinition> getProDefinitionsByKey(String bussinessKey){
        List<ProcessDefinition>  defineList = repositoryService.createProcessDefinitionQuery().processDefinitionKey(bussinessKey).orderByProcessDefinitionVersion().desc().list();
        if(defineList!=null && defineList.size() >0){
            return defineList;
        }else{
            return null;
        }
    }
    /**
     * 根据流程定义ID查询流程定义对象{@link ProcessDefinition}
     *
     * @param processDefinitionId
     *          流程定义对象ID
     * @return 流程定义对象{@link ProcessDefinition}
     */
    public ProcessDefinition findProDefinitionById(String processDefinitionId) {
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery().processDefinitionId(
                        processDefinitionId).orderByProcessDefinitionVersion().desc().list().get(0);
        return processDefinition;
    }

    /**
     * 根据bussinessKey查找当前流程节点
     * @param id
     * @return
     */
    public Task getCurrentTask(String id){
        List<Task> taskList = taskService.createTaskQuery().processInstanceBusinessKey(id).list();

        if (taskList.size() >0) {
            Task task = taskList.get(0);
            return task;
        } else {
            return null;
        }
    }
    /**
     * 根据bussinessKey,users查找当前流程节点
     * @param id -- unid
     * @param users
     * @return
     */
    public Task getCurrentTask(String id,Users users){
        List<Task> taskList = taskService.createTaskQuery().processInstanceBusinessKey(id).taskCandidateOrAssigned(users.getId()).list();

        if (taskList.size() >0) {
            Task task = taskList.get(0);
            return task;
        } else {
            return null;
        }
    }
    /**
     * 根据bussinessKey查找多任务当前流程节点
     * @param key
     * @return
     */
    public List<Task> findTaskByKey(String key){
        List<Task> taskList = taskService.createTaskQuery().processInstanceBusinessKey(key).list();

        if (taskList.size() >0) {
            return taskList;
        } else {
            return null;
        }
    }
    /**
     * 实现获取上一个被执行的任务
     * @param processInstanceId
     * @return
     */
    public HistoricTaskInstance earlyTask(String processInstanceId) {
        List<HistoricTaskInstance> histask = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).finished().orderByTaskCreateTime().desc().list();
        if(histask.size()!=0){
            return histask.get(0);
        }
        return null;
    }
    /**
     * 根据task查找流程实例
     * @param task --- 任务
     * @return
     */
    public ProcessInstance getProIntanceByTask(Task task){
        String proId = task.getProcessInstanceId();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(proId).active().singleResult();
        return processInstance;
    }
    /**
     * 根据task查找流程定义
     * @param task--- 任务
     * @return
     */
    public ProcessDefinition getProDefinitionByTask(Task task) {
        String proId = task.getProcessDefinitionId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(proId).active().singleResult();
        return processDefinition;
    }
    /**
     * 获取指定流程实例的当前任务节点
     * @param processInstanceId
     * @return
     */
    public Map<String, Object> getCurrentTaskInfo(String processInstanceId) {
        if(StringUtils.isEmpty(processInstanceId)){
            return null;
        }
        Users loginer = usersService.getLoginInfo();

        TaskQuery taskQuery = taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee(loginer.getId()).active();
        if(taskQuery.list().size() ==0){
            return null;
        }
        Task task = taskQuery.list().get(0);

        ProcessDefinition processDefinition = repositoryService.getProcessDefinition(task.getProcessDefinitionId());
        return packageTaskInfo(task,processDefinition);
    }
    /**
     * 获取历史流程的处理人员
     * @return
     */
    public String getApprover(String taskId){
        HistoricTaskInstance hisTask = historyService.createHistoricTaskInstanceQuery().taskId(taskId).finished().list().get(0);
        return hisTask.getAssignee();
    }

    /**
     * 查找流程中所有activity
     * @param processDefinitionId
     * @return
     */
    public List<ActivityImpl> findAllActivities(String processDefinitionId){
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getProcessDefinition(processDefinitionId);
        List<ActivityImpl> activities= processDefinition.getActivities();
        return activities;
    }

    /**
     * 查找流程中所有activity
     * @param businessKey
     * @return
     */
    public List<ActivityImpl> findAllActivitiesByKey(String businessKey){
        ProcessDefinition processDefinition=getProDefinitionByKey(businessKey);
        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getProcessDefinition(processDefinition.getId());
        List<ActivityImpl> activities= processDefinitionEntity.getActivities();
        return activities;
    }
    /**
     * 根据流程定义id和taskId查找
     * @param processDefinitionId
     * @param activtyId
     * @return
     */
    public ActivityImpl getActivityById(String processDefinitionId,String activtyId){
        List<ActivityImpl> activities = findAllActivities(processDefinitionId);
        for(ActivityImpl activity : activities){
            if(activity.getId().equals(activtyId)){
                return activity;
            }
        }
        return null;
    }
    /**
     * 判断activiti是否为taskActiviti
     * @param activity
     */
    public boolean isTask(ActivityImpl activity){
        ActivityBehavior activitiBehavior = activity.getActivityBehavior();
        if(activitiBehavior instanceof UserTaskActivityBehavior){
            return true;
        }

        return false;
    }

    /**
     * 获取审批信息 -- 包含附件
     * @param processInstanceId --- 流程实例ID
     * @return
     */
    public List<Map<String,Object>> getCommentList(String processInstanceId){
        List<Map<String,Object>> dataRows = new ArrayList<Map<String,Object>>();
        List<HistoricTaskInstance> hisTaskList =  historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByTaskCreateTime().finished().desc().list();
        HistoricProcessInstance hisProInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        Map<String, Object> rMap;
        for (HistoricTaskInstance hti : hisTaskList) {
            List<Comment> comments = taskService.getTaskComments(hti.getId());
            if(comments.size() == 0){
                continue;
            }


            for(Comment comment : comments){
                CommentEntity commentEntity = (CommentEntity)comment;
                String msg  = commentEntity.getFullMessage().toString();
                String assignee=usersService.get(hti.getAssignee()).getName();

                List<HistoricVariableInstance> histance = historyService.createHistoricVariableInstanceQuery().taskId(hti.getId()).variableName("curDutyId").list();

                // todo 获取本地历史变量调用字service
                String dutyId = "";
                if(histance.size()>0){
                    HistoricVariableInstanceEntity hisVarEntity = (HistoricVariableInstanceEntity) histance.get(0);
                    dutyId = hisVarEntity.getTextValue();
                }


                rMap = new HashMap<String, Object>();
                rMap.put("curDuty","");
                rMap.put("commentMsg", msg);
                rMap.put("startTime", DataUtil.DateTimeToString(commentEntity.getTime()));
                rMap.put("assignee",assignee);
                rMap.put("pId",processInstanceId);
                rMap.put("tId",hti.getId());
                rMap.put("mergecomment",assignee+"  "+msg);
                rMap.put("name",hti.getName());
                if(StringUtils.isNotEmpty(dutyId)){
                    rMap.put("curDuty",dutyService.get(dutyId));
                }
                dataRows.add(rMap);
            }
        }
        return dataRows;
    }
    /**
     *获取节点和流程定义信息
     * @param task
     * @param processDefinition
     * @return
     */
    public Map<String, Object> packageTaskInfo(Task task, ProcessDefinition processDefinition) {
        Map<String, Object> singleTask = new HashMap<String, Object>();
        singleTask.put("id", task.getId());
        singleTask.put("name", task.getName());
        singleTask.put("pdname", processDefinition.getName());
        singleTask.put("pdversion", processDefinition.getVersion());
        singleTask.put("pid", task.getProcessInstanceId());
        return singleTask;
    }

    /**
     * 获取当前登录人下的所有的流程中单子，返回bussinessId
     * @return
     */
    public List<String> getAllInProcessByUsers(){
        Users users = usersService.getLoginInfo();
        List<Task> taskList = taskService.createTaskQuery().taskCandidateOrAssigned(users.getId()).list();
        List<String> keyList = Lists.newArrayList();
        for(Task task : taskList){
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            int numStatus = (Integer)runtimeService.getVariable(task.getExecutionId(),"numStatus");
            if(numStatus != 0 ){
                keyList.add(processInstance.getBusinessKey());
            }

        }
        return keyList;
    }


    /**
     * 获取当前登录人下的所有的流程中单子，返回bussinessId
     * @return
     */
    public List<String> getAllInBackByUsers(){
        List<ProcessInstance> proIntanceList = runtimeService.createProcessInstanceQuery().involvedUser(usersService.getLoginInfo().getId()).active().list();
        List<String> idList = new ArrayList<String>();
        for(ProcessInstance proIntance : proIntanceList){
            idList.add(proIntance.getBusinessKey());
        }
        return idList;
    }

    /**
     * 获取当前登录人所有单子
     * @param businessConfigList
     * @param bcfg
     * @param users
     * @return
     */
    public List<String> getAllProcessByUsers(List<BusinessConfig> businessConfigList,BusinessConfig bcfg,Users users){
        Set<HistoricProcessInstance> bcList=new HashSet<HistoricProcessInstance>();

        List<BusinessConfig> readList=Lists.newArrayList();
        List<BusinessConfig> editList=Lists.newArrayList();
        List<BusinessConfig> docReadList=Lists.newArrayList();
        Set<BusinessConfig> businessConfigSet=Sets.newHashSet();
        if(bcfg!=null && bcfg.getPtype()==0){
            //最新的配置没有打钩，但是如果配置了他，老版本的流程对应的单子也可见
            readList.addAll(businessConfigService.findByBcfgAndUserInArchRead(bcfg, users));
            editList.addAll(businessConfigService.findByBcfgAndUserInArchEdit(bcfg, users));
            docReadList.addAll(businessConfigService.findByBcfgAndUserInDoc(bcfg, users));
            businessConfigSet.addAll(readList);
            businessConfigSet.addAll(editList);
            businessConfigSet.addAll(docReadList);
            if(businessConfigSet.size()>0){
                for(BusinessConfig businessConfig:businessConfigList){
                    List<HistoricProcessInstance> proList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(businessConfig.getProcessDefinitionId()).orderByProcessInstanceStartTime().desc().list();
                    bcList.addAll(proList);
                }
            }

        }else{
            for(BusinessConfig businessConfig:businessConfigList){
                readList.addAll(businessConfigService.findByBcfgAndUserInArchRead(businessConfig, users));
                editList.addAll(businessConfigService.findByBcfgAndUserInArchEdit(businessConfig, users));
                docReadList.addAll(businessConfigService.findByBcfgAndUserInDoc(businessConfig, users));
                businessConfigSet.addAll(readList);
                businessConfigSet.addAll(editList);
                businessConfigSet.addAll(docReadList);
            }
            for(BusinessConfig cfg:businessConfigSet){
                List<HistoricProcessInstance>  readproList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(cfg.getProcessDefinitionId()).orderByProcessInstanceStartTime().desc().list();
                bcList.addAll(readproList);
            }
        }

        if(bcfg!=null && bcfg.getOtype()==1){
            List<HistoricProcessInstance> hisProList = historyService.createHistoricProcessInstanceQuery().involvedUser(users.getId()).orderByProcessInstanceEndTime().desc().list();
            bcList.addAll(hisProList);
        }

        List<String> idList =Lists.newArrayList();
        for(HistoricProcessInstance hisProIntance : bcList){
            idList.add(hisProIntance.getBusinessKey());
        }
        return idList;
    }


    /**
     * 获取当前登陆人所有已归档的单子
     * @return
     */
    public List<String> getAllInFinishByUsers(List<BusinessConfig> businessConfigList,BusinessConfig bcfg,Users users){
        Set<HistoricProcessInstance> bcList=new HashSet<HistoricProcessInstance>();

        List<BusinessConfig> readList=Lists.newArrayList();
        List<BusinessConfig> editList=Lists.newArrayList();
        List<BusinessConfig> docReadList=Lists.newArrayList();
        Set<BusinessConfig> businessConfigSet=Sets.newHashSet();
        if(bcfg!=null && bcfg.getPtype()==0){
            //最新的配置没有打钩，但是如果配置了他，老版本的流程对应的单子也可见
            readList.addAll(businessConfigService.findByBcfgAndUserInArchRead(bcfg, users));
            editList.addAll(businessConfigService.findByBcfgAndUserInArchEdit(bcfg, users));
            docReadList.addAll(businessConfigService.findByBcfgAndUserInDoc(bcfg, users));
            businessConfigSet.addAll(readList);
            businessConfigSet.addAll(editList);
            businessConfigSet.addAll(docReadList);
            if(businessConfigSet.size()>0){
                for(BusinessConfig businessConfig:businessConfigList){
                    List<HistoricProcessInstance> proList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(businessConfig.getProcessDefinitionId()).finished().orderByProcessInstanceStartTime().desc().list();
                    bcList.addAll(proList);
                }
            }

        }else{
            for(BusinessConfig businessConfig:businessConfigList){
                readList.addAll(businessConfigService.findByBcfgAndUserInArchRead(businessConfig, users));
                editList.addAll(businessConfigService.findByBcfgAndUserInArchEdit(businessConfig, users));
                docReadList.addAll(businessConfigService.findByBcfgAndUserInDoc(businessConfig, users));
                businessConfigSet.addAll(readList);
                businessConfigSet.addAll(editList);
                businessConfigSet.addAll(docReadList);
            }
            for(BusinessConfig cfg:businessConfigSet){
                List<HistoricProcessInstance>  readproList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(cfg.getProcessDefinitionId()).finished().orderByProcessInstanceStartTime().desc().list();
                bcList.addAll(readproList);
            }
        }

        if(bcfg!=null && bcfg.getOtype()==1){
            List<HistoricProcessInstance> hisProList = historyService.createHistoricProcessInstanceQuery().involvedUser(users.getId()).finished().orderByProcessInstanceEndTime().desc().list();
            bcList.addAll(hisProList);
        }

        List<String> idList =Lists.newArrayList();
        for(HistoricProcessInstance hisProIntance : bcList){
            idList.add(hisProIntance.getBusinessKey());
        }
        return idList;
    }
    /**
     * 获取当前users下的所有的流程中单子，返回bussinessId
     * @param users
     * @return
     */
    public List<String> getAllInProcessByUsers(List<BusinessConfig> businessConfigs,BusinessConfig bcfg,Users users){
        // 1 计算流转中的
        List<HistoricProcessInstance> hisProList = historyService.createHistoricProcessInstanceQuery().involvedUser(users.getId()).unfinished().list();
        List<BusinessConfig> readList=Lists.newArrayList();
        List<BusinessConfig> editList=Lists.newArrayList();
        List<BusinessConfig> docReadList=Lists.newArrayList();
        Set<BusinessConfig> businessConfigSet= Sets.newHashSet();
        // 2  users 是否可以看到bcfg(文档阅览者)
        //List<BusinessConfig> bcList= businessConfigService.findByBcfgAndUserInDoc(bcfg,users);
        if(bcfg!=null && bcfg.getPtype()==0){
            //最新的配置没有打钩，但是如果配置了他，老版本的流程对应的单子也可见
            readList.addAll(businessConfigService.findByBcfgAndUserInArchRead(bcfg, users));
            editList.addAll(businessConfigService.findByBcfgAndUserInArchEdit(bcfg, users));
            docReadList.addAll(businessConfigService.findByBcfgAndUserInDoc(bcfg, users));
            businessConfigSet.addAll(readList);
            businessConfigSet.addAll(editList);
            businessConfigSet.addAll(docReadList);
            if(businessConfigSet.size()>0){
                for(BusinessConfig businessConfig:businessConfigs){
                    List<HistoricProcessInstance> proList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(businessConfig.getProcessDefinitionId()).unfinished().orderByProcessInstanceStartTime().desc().list();
                    hisProList.addAll(proList);
                }
            }

        }else{
            //3 开启版本控制开关
            for(BusinessConfig businessConfig:businessConfigs){
                businessConfigSet.addAll(businessConfigService.findByBcfgAndUserInDoc(businessConfig, users));
            }
            for(BusinessConfig cfg:businessConfigSet){
                List<HistoricProcessInstance>  proList = historyService.createHistoricProcessInstanceQuery().processDefinitionId(cfg.getProcessDefinitionId()).unfinished().orderByProcessInstanceStartTime().desc().list();
                hisProList.addAll(proList);
            }
        }

        List<String> idList = new ArrayList<String>();
        for(HistoricProcessInstance proIntance : hisProList){
            idList.add(proIntance.getBusinessKey());
        }
        return idList;
    }

    /**
     * 流程图信息
     * @param processInstanceId		流程定义ID
     * @return	封装了所有节点信息
     */
    public List<Map<String, Object>> activitis(String processInstanceId) throws Exception {
        Execution execution = runtimeService.createExecutionQuery().executionId(processInstanceId).singleResult();//执行实例
        Object property = PropertyUtils.getProperty(execution, "activityId");
        String activityId = "";
        if (property != null) {
            activityId = property.toString();
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
                .singleResult();
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
        List<ActivityImpl> activitiList = processDefinition.getActivities();//获得当前任务的所有节点

        List<Map<String, Object>> activityInfos = new ArrayList<Map<String, Object>>();
        for (ActivityImpl activity : activitiList) {

            boolean currentActiviti = false;
            String id = activity.getId();

            // 当前节点
            if (id.equals(activityId)) {
                currentActiviti = true;
            }

            Map<String, Object> activityImageInfo = packageSingleActivitiInfo(activity, processInstance, currentActiviti);

            activityInfos.add(activityImageInfo);
        }

        return activityInfos;
    }


    /**
     * 流程跟踪图
     * @param processInstanceId		流程实例ID
     * @return	封装了各种节点信息
     */
    public List<Map<String, Object>> traceProcess(String processInstanceId) throws Exception {
        Execution execution = runtimeService.createExecutionQuery().executionId(processInstanceId).singleResult();//执行实例
        Object property = PropertyUtils.getProperty(execution, "activityId");
        String activityId = "";
        if (property != null) {
            activityId = property.toString();
        }
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
                .singleResult();
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
        List<ActivityImpl> activitiList = processDefinition.getActivities();//获得当前流程定义的所有节点
        List<Map<String, Object>> activityInfos = new ArrayList<Map<String, Object>>();
        for (ActivityImpl activity : activitiList) {
            boolean currentActiviti = false;
            String id = activity.getId();
            // 当前节点
            if (id.equals(activityId)) {
                currentActiviti = true;
            }
            Map<String, Object> activityImageInfo = packageSingleActivitiInfo(activity, processInstance, currentActiviti);
            activityInfos.add(activityImageInfo);
        }
        return activityInfos;
    }

    /**
     * 流程跟踪图
     * @param processInstanceId		流程实例ID
     * @return	封装了各种节点信息
     */
    public List<Map<String, Object>> traceHistoryProcess(String processInstanceId) throws Exception {
        HistoricProcessInstanceQuery historicprocessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
        HistoricProcessInstance execution = historicprocessInstanceQuery.processInstanceId(processInstanceId).singleResult();

        List<HistoricActivityInstance> historyActivityList = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();

        List<Map<String, Object>> activityInfos = new ArrayList<Map<String, Object>>();

        for(HistoricActivityInstance historyActivity : historyActivityList){
            ActivityImpl activity =getActivityById(execution.getProcessDefinitionId(), historyActivity.getActivityId());
            boolean currentActiviti = false;
            if(historyActivity == historyActivityList.get(historyActivityList.size()-1)){
                currentActiviti = true;
            }
            Map<String, Object> activityImageInfo = packageSingleActivitiInfo(activity, execution, currentActiviti);
            activityInfos.add(activityImageInfo);
        }

        return activityInfos;
    }

    /**
     * 流程跟踪图
     * @param processDefinitonId		流程定义ID
     * @return	封装了各种节点信息
     */
    public List<Map<String, Object>> allActivities(String processDefinitonId) throws Exception {

        ProcessDefinitionEntity prodefEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(processDefinitonId);


        List<ActivityImpl> activityList =  prodefEntity.getActivities();
        List<Map<String, Object>> activityInfos = new ArrayList<Map<String, Object>>();

        for(ActivityImpl actDef : activityList){
            Object property = PropertyUtils.getProperty(actDef.getProperties(), "type");
            String type = "";
            if(property != null){
                type = property.toString();
            }

            Map<String, Object> activityInfo = new HashMap<String, Object>();

            setPosition(actDef, activityInfo);
            setWidthAndHeight(actDef, activityInfo);
            setLeft(prodefEntity,activityInfo);
            setTop(prodefEntity,activityInfo);

            activityInfo.put("type",type);
            activityInfo.put("id",actDef.getId());
            activityInfo.put("proDefId",processDefinitonId);
            activityInfo.put("parseType", WorkflowUtils.parseToZhType(type));
            activityInfos.add(activityInfo);
        }

        return activityInfos;
    }
    /**
     * 封装输出信息，包括：当前节点的X、Y坐标、变量信息、任务类型、任务描述
     * @param activity
     * @param processInstance
     * @param currentActiviti
     * @return
     */
    private Map<String, Object> packageSingleActivitiInfo(ActivityImpl activity, ProcessInstance processInstance,
                                                          boolean currentActiviti) throws Exception {
        Map<String, Object> vars = new HashMap<String, Object>();
        Map<String, Object> activityInfo = new HashMap<String, Object>();
        activityInfo.put("currentActiviti", currentActiviti);
        setPosition(activity, activityInfo);
        setWidthAndHeight(activity, activityInfo);

        Map<String, Object> properties = activity.getProperties();
        vars.put("任务类型", WorkflowUtils.parseToZhType(properties.get("type").toString()));

        ActivityBehavior activityBehavior = activity.getActivityBehavior();
        if (activityBehavior instanceof UserTaskActivityBehavior) {

            Task currentTask = null;

			/*
			 * 当前节点的task
			 */
            if (currentActiviti) {
                currentTask = getCurrentTaskInfo(processInstance);
            }

			/*
			 * 当前任务的分配角色
			 */
            UserTaskActivityBehavior userTaskActivityBehavior = (UserTaskActivityBehavior) activityBehavior;
            TaskDefinition taskDefinition = userTaskActivityBehavior.getTaskDefinition();
            Set<Expression> candidateGroupIdExpressions = taskDefinition.getCandidateGroupIdExpressions();
            if (!candidateGroupIdExpressions.isEmpty()) {

                // 任务的处理角色
                setTaskGroup(vars, candidateGroupIdExpressions);

                // 当前处理人
                if (currentTask != null) {
                    setCurrentTaskAssignee(vars, currentTask);
                }
            }
        }

        vars.put("节点说明", properties.get("documentation"));

        String description = activity.getProcessDefinition().getDescription();
        vars.put("描述", description);

        activityInfo.put("vars", vars);
        return activityInfo;
    }

    private Map<String, Object> packageSingleActivitiInfo(ActivityImpl activity, HistoricProcessInstance processInstance,
                                                          boolean currentActiviti) throws Exception {
        Map<String, Object> vars = new HashMap<String, Object>();
        Map<String, Object> activityInfo = new HashMap<String, Object>();
        activityInfo.put("currentActiviti", currentActiviti);
        setPosition(activity, activityInfo);
        setWidthAndHeight(activity, activityInfo);
        setLeft(processInstance,activityInfo);

        Map<String, Object> properties = activity.getProperties();
        vars.put("任务类型", WorkflowUtils.parseToZhType(properties.get("type").toString()));
        vars.put("type",properties.get("type").toString());

        ActivityBehavior activityBehavior = activity.getActivityBehavior();
        if (activityBehavior instanceof UserTaskActivityBehavior) {

            Task currentTask = null;

			/*
			 * 当前节点的task
			 */
            if (currentActiviti) {
                currentTask = null;
            }

			/*
			 * 当前任务的分配角色
			 */
            UserTaskActivityBehavior userTaskActivityBehavior = (UserTaskActivityBehavior) activityBehavior;
            TaskDefinition taskDefinition = userTaskActivityBehavior.getTaskDefinition();
            Set<Expression> candidateGroupIdExpressions = taskDefinition.getCandidateGroupIdExpressions();
            if (!candidateGroupIdExpressions.isEmpty()) {

                // 任务的处理角色
                setTaskGroup(vars, candidateGroupIdExpressions);

                // 当前处理人
                if (currentTask != null) {
                    setCurrentTaskAssignee(vars, currentTask);
                }
            }
        }

        vars.put("节点说明", properties.get("documentation"));

        String description = activity.getProcessDefinition().getDescription();
        vars.put("描述", description);

        activityInfo.put("vars", vars);
        return activityInfo;
    }

    /**
     * 设置任务候选组
     * @param vars
     * @param candidateGroupIdExpressions
     */
    private void setTaskGroup(Map<String, Object> vars, Set<Expression> candidateGroupIdExpressions) {
        String roles = "";
        for (Expression expression : candidateGroupIdExpressions) {
            String expressionText = expression.getExpressionText();
            String roleName = identityService.createGroupQuery().groupId(expressionText).singleResult().getName();
            roles += roleName;
        }
        vars.put("任务所属角色", roles);
    }

    /**
     * 设置当前处理人信息
     * @param vars
     * @param currentTask
     */
    private void setCurrentTaskAssignee(Map<String, Object> vars, Task currentTask) {
        String assignee = currentTask.getAssignee();
        if (assignee != null) {
            User assigneeUser = identityService.createUserQuery().userId(assignee).singleResult();
            String userInfo = assigneeUser.getFirstName() + " " + assigneeUser.getLastName();
            vars.put("当前处理人", userInfo);
        }
    }

    /**
     * 获取当前节点信息
     * @param processInstance
     * @return
     */
    private Task getCurrentTaskInfo(ProcessInstance processInstance) {
        Task currentTask = null;
        try {
            String activitiId = (String) PropertyUtils.getProperty(processInstance, "activityId");

            currentTask = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskDefinitionKey(activitiId)
                    .singleResult();

        } catch (Exception e) {
        }
        return currentTask;
    }


    /**
     * 设置宽度、高度属性
     * @param activity
     * @param activityInfo
     */
    private void setWidthAndHeight(ActivityImpl activity, Map<String, Object> activityInfo) {
        activityInfo.put("width", activity.getWidth());
        activityInfo.put("height", activity.getHeight());
    }

    /**
     * 设置坐标位置
     * @param activity
     * @param activityInfo
     */
    private void setPosition(ActivityImpl activity, Map<String, Object> activityInfo) {
        activityInfo.put("x", activity.getX());
        activityInfo.put("y", activity.getY());
    }

    /**
     * 查找x最小值
     * @param processInstance
     * @param activityInfo
     * @throws Exception
     */
    public void setLeft(HistoricProcessInstance processInstance ,Map<String, Object> activityInfo) throws Exception {

        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                .getDeployedProcessDefinition(processInstance.getProcessDefinitionId());
        List<ActivityImpl> activitiList = processDefinition.getActivities();//获得当前任务的所有节点
        int x = 0;
        for (ActivityImpl activity : activitiList) {
            if(activity.getX() <x || x == 0){
                x = activity.getX();
            }
        }
        activityInfo.put("left", x);
    }
    /**
     * 查找x最小值
     * @param processDefinition
     * @param activityInfo
     * @throws Exception
     */
    public void setLeft(ProcessDefinition processDefinition ,Map<String, Object> activityInfo) throws Exception {
        ProcessDefinitionEntity proDefEntity = (ProcessDefinitionEntity) processDefinition;
        List<ActivityImpl> activitiList = proDefEntity.getActivities();//获得当前任务的所有节点
        int x = 0;
        for (ActivityImpl activity : activitiList) {
            if(activity.getX() <x || x == 0){
                x = activity.getX();
            }
        }
        activityInfo.put("left", x);
    }

    /**
     * 查找x最小值
     * @param processDefinition
     * @param activityInfo
     * @throws Exception
     */
    public void setTop(ProcessDefinition processDefinition ,Map<String, Object> activityInfo) throws Exception {
        ProcessDefinitionEntity proDefEntity = (ProcessDefinitionEntity) processDefinition;
        List<ActivityImpl> activitiList = proDefEntity.getActivities();//获得当前任务的所有节点
        int y = 0;
        for (ActivityImpl activity : activitiList) {
            if(activity.getY()<y||y==0){
                y = activity.getY();
            }
        }
        activityInfo.put("top", y);
    }
    /**
     * 跳转至指定活动节点
     *
     * @param targetTaskDefinitionKey
     * @throws Exception
     */
    public void jump(String targetTaskDefinitionKey,String processId) throws Exception
    {
        TaskEntity currentTask = (TaskEntity)taskService.createTaskQuery()
                .processInstanceId(processId).singleResult();
        jump(currentTask, targetTaskDefinitionKey);
    }

    /**
     *
     * @param currentTaskEntity
     *            当前任务节点
     * @param targetTaskDefinitionKey
     *            目标任务节点（在模型定义里面的节点名称）
     * @throws Exception
     */
    private void jump(final TaskEntity currentTaskEntity, String targetTaskDefinitionKey) throws Exception
    {

        final ActivityImpl activity=((ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(currentTaskEntity.getProcessDefinitionId())).findActivity(targetTaskDefinitionKey);

        final ExecutionEntity execution = (ExecutionEntity)runtimeService.createExecutionQuery()
                .executionId(currentTaskEntity.getExecutionId()).singleResult();

        //包装一个Command对象
        ((RuntimeServiceImpl) runtimeService).getCommandExecutor().execute(
                new Command<Void>() {
                    @Override
                    public Void execute(CommandContext commandContext) {
                        //创建新任务
                        execution.setActivity(activity);
                        execution.executeActivity(activity);

                        //删除当前的任务
                        //不能删除当前正在执行的任务，所以要先清除掉关联
                        currentTaskEntity.setExecutionId(null);
                        taskService.saveTask(currentTaskEntity);
                        taskService.deleteTask(currentTaskEntity.getId(), true);

                        return null;
                    }
                });
    }
}
