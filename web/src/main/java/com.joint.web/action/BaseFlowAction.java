package com.joint.web.action;

import com.fz.us.base.util.Collections3;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.joint.base.entity.BusinessConfig;
import com.joint.base.entity.Duty;
import com.joint.base.entity.ProcessConfig;
import com.joint.base.entity.Users;
import com.joint.base.service.AdminService;
import com.joint.base.service.BusinessConfigService;
import com.joint.base.service.DutyService;
import com.joint.base.service.ProcessConfigService;
import com.joint.base.service.activiti.WorkflowService;
import com.joint.base.service.activiti.WorkflowTraceService;
import com.joint.base.util.excel.ExportExcel;
import com.joint.base.util.excel.annotation.ExcelField;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 后台Action类 - 流程中心基类
 * ============================================================================
 * 版权所有 2014 hpj
 * ----------------------------------------------------------------------------
 *
 * @author hpj
 *
 * @version 0.1 2013-3-1
 */
@Scope("prototype")
public class BaseFlowAction extends BaseAdminAction {
    @Resource
    protected WorkflowService workflowService;
    @Resource
    protected BusinessConfigService businessConfigService;
    @Resource
    protected HistoryService historyService;
    @Resource
    protected RuntimeService runtimeService;
    @Resource
    protected TaskService taskService;
    @Resource
    protected RepositoryService repositoryService;
    @Resource
    protected ProcessConfigService processConfigService;
    @Resource
    protected ManagementService managementService;
    @Resource
    protected WorkflowTraceService workflowTraceService;
    @Resource
    protected AdminService adminService;
    @Resource
    protected DutyService dutyService;
    @Resource
    protected SessionFactory sessionFactory;
    //流程使用
    protected String curDutyId;
    protected String proDefKey;
    protected int numStatus;

    //上传文件使用
    protected File filedata;
    protected String filename;
    protected String filedataFileName;
    protected String filedataContentType;
    public String comment;
    public String tendersurveyId;
    public String param;

    //新建人
    protected Users procreater;


    public String isCreate(String businessKey){
        Users loginer = usersService.getLoginInfo();
        if(StringUtils.isEmpty(businessKey)){
            return "style='display:none'";
        }
        ProcessDefinition processDefinition = workflowService.getProDefinitionByKey(businessKey);
        if(processDefinition == null){
            return "style='display:none'";
        }

        ProcessDefinitionEntity prodefEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(processDefinition.getId());
        List<ActivityImpl> activityList= prodefEntity.getActivities();
        ProcessConfig processConfig=new ProcessConfig();
        if(activityList.size()>0){
            ActivityImpl activity=activityList.get(0);
            processConfig = processConfigService.findConfigByActivityId(processDefinition.getId(),activity.getId());
            if(processConfig==null){
                return "style='display:none'";
            }
        }
        List<Duty> dutyList = processConfigService.findDutyByConfig(processConfig);
        dutyList = Collections3.intersection(dutyList, dutyService.getDutys(loginer));
        if(dutyList.size() > 0){
            return "";
        }
        return "style='display:none'";
    }
    /**
     * 审批按钮的控制
     * @param numStatus --- 传入的文档状态字段
     * @param id --- outcome 的Id
     * @return
     */
    public boolean isApprove(int numStatus,String id){
        int curNum = workflowService.getNumStatus(id, usersService.getLoginInfo());
        if(curNum != numStatus){
            return false;
        }
        List<Task> taskList =  workflowService.findTaskByKey(id);
        for(Task task : taskList){
            if(task.getAssignee().equals(usersService.getLoginInfo().getId())){
                return  true;
            }
        }
        return false;
    }
    /**
     * 判断编辑按钮是否具有点击权限
     * 1.判断是不是流转中
     * 2.流转中是否具有编辑权限
     * 3.判断是不是 已归档
     * 4.归档之后是否需要编辑权限
     * @param bussinessId
     * @return
     */
    public String isEdit(String bussinessId){
        HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(bussinessId).list().get(0);
        if(hpi.getEndTime() != null){
            return isFinishEdit(bussinessId);
        }else {
            return isRunningEdit(bussinessId);
        }

    }

    /**
     * 流转中文档编辑权限
     * @param bussinessId --- entityId
     * @return
     */
    public String isRunningEdit(String bussinessId){
        //检测是否 有权限编辑
        int num = workflowService.getNumStatus(bussinessId,usersService.getLoginInfo());
        Task task = workflowService.getCurrentTask(bussinessId, usersService.getLoginInfo());
        if(task == null){
            return "disabled";
        }
        if(num != -2 && num != 0){
            return  "disabled";
        }
        return "";
    }

    /**
     * 归档后的编辑权限
     * @param bussinessId --- entityId
     * @return
     */
    public String isFinishEdit(String bussinessId){
        HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(bussinessId).finished().list().get(0);
        Users loginer = usersService.getLoginInfo();
        //检测是否 有权限编辑
        BusinessConfig bg = businessConfigService.getByUserAndBusinessInFEdit(loginer, hpi.getProcessDefinitionId());
        if(bg == null){
            return  "disabled";
        }else {
            return "";
        }

    }

    /**
     * 获取下步操作人
     * @param id
     * @return
     */
    public String nextApprover(String id){
        List<Task> taskList = workflowService.findTaskByKey(id);
        if (taskList==null){
            return null;
        }
        String name = "";
        for(Task task : taskList){
            String usersId = task.getAssignee();
            Users users = usersService.get(usersId);
            name = name + users.getName()+",";
        }
        name = name.substring(0,name.length()-1);
        return name;
    }

    /**
     * 查找节点配置的候选人
     * @param key
     * @param taskdefKey
     * @return
     */
    public Set<String> findTaskCandition(String key,String taskdefKey){
        Set<String> idList= Sets.newHashSet();
        ProcessDefinition processDefinition=workflowService.getProDefinitionByKey(key);

        ProcessConfig processConfig = processConfigService.findConfigByActivityId(processDefinition.getId(), taskdefKey);
        List<Duty> dutyList = processConfigService.findDutyByConfig(processConfig);
        for(Duty duty :dutyList){
            idList.add(duty.getUsers().getId());
        }
        return  idList;
    }


    public Map<String,Object> initMappings() {
           Map<String,Object> mappings = Maps.newHashMap();
//            idMappings = new HashMap<String, String>();
            Map metaMap = sessionFactory.getAllClassMetadata();
            for (String key : (Set<String>) metaMap.keySet()) {
                AbstractEntityPersister classMetadata = (AbstractEntityPersister) metaMap
                        .get(key);
                String tableName = classMetadata.getTableName().toLowerCase();
                int index = tableName.indexOf(".");
                if (index >= 0) {
                    tableName = tableName.substring(index + 1);
                }
                String className = classMetadata.getEntityMetamodel().getName();
                String idName = classMetadata.getIdentifierColumnNames()[0];
                //根据表名返回实体名
                mappings.put(tableName, className);
                //根据实体返回表名
//                idMappings.put(className, idName);
        }
        return mappings;
    }


    /**
     * 根据业务配置提取实体属性
     * @param businessConfig
     * @return
     */
    public List<Map<String,Object>> getEntityFields(BusinessConfig businessConfig) throws ClassNotFoundException {
         List<Map<String,Object>> keyList= Lists.newArrayList();
         Map<String,Object> rMap= initMappings();
         Map<String,Object> rmap;
         if(StringUtils.isNotEmpty(businessConfig.getTableKey())){
             String entityKey= (String) rMap.get(businessConfig.getTableKey());
             Class clazz=Class.forName(entityKey);
             ExportExcel exportExcel= new ExportExcel("", clazz,2,null);
             for(Object[] os:exportExcel.getAnnotation()){
                 rmap=Maps.newHashMap();
                 //字段名
                 String fieldname = ((ExcelField)os[0]).value();
                 //标题
                 String title=((ExcelField)os[0]).title();
                 rmap.put("filedname",fieldname);
                 rmap.put("title",title);
                 keyList.add(rmap);
             }
         }
        return keyList;
    }





    public File getFiledata() {
        return filedata;
    }

    public void setFiledata(File filedata) {
        this.filedata = filedata;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFiledataFileName() {
        return filedataFileName;
    }

    public void setFiledataFileName(String filedataFileName) {
        this.filedataFileName = filedataFileName;
    }

    public String getFiledataContentType() {
        return filedataContentType;
    }

    public void setFiledataContentType(String filedataContentType) {
        this.filedataContentType = filedataContentType;
    }

    public String getCurDutyId() {
        return curDutyId;
    }

    public void setCurDutyId(String curDutyId) {
        this.curDutyId = curDutyId;
    }


    public Users getProcreater() {
        return procreater;
    }

    public void setProcreater(Users procreater) {
        this.procreater = procreater;
    }

    public String getProDefKey() {
        return proDefKey;
    }

    public void setProDefKey(String proDefKey) {
        this.proDefKey = proDefKey;
    }

    public int getNumStatus() {
        return numStatus;
    }

    public void setNumStatus(int numStatus) {
        this.numStatus = numStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTendersurveyId() {
        return tendersurveyId;
    }

    public void setTendersurveyId(String tendersurveyId) {
        this.tendersurveyId = tendersurveyId;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
