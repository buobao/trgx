package com.joint.web.action.com;


import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Pager;
import com.fz.us.base.bean.Result;
import com.fz.us.dict.bean.DictBean;
import com.fz.us.dict.entity.Dict;
import com.fz.us.dict.service.DictService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.joint.base.entity.*;
import com.joint.base.service.*;
import com.joint.base.util.StringUtils;
import com.joint.web.action.BaseFlowAction;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricVariableInstanceEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by hpj on 2014/9/16.
 */
@ParentPackage("com")
public class AjaxDialogAction extends BaseFlowAction {

    @Resource
    private UsersService usersService;
    @Resource
    private DepartmentService departmentService;
    @Resource
    private PostService postService;
    @Resource
    private PowerService powerService;
    @Resource
    private DutyService dutyService;
    @Resource
    private GroupService groupService;
    @Resource
    private TaskService taskService;
    @Resource
    private RoleService roleService;
    @Resource
    private BusinessConfigService businessConfigService;
    @Resource
    private DictService dictService;


    private String flowName;

    private String clientId;

    private String param;
    private String key;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String client(){
        return "client";
    }

    /**
     * 选择字典
     * @return
     */
    public String dictDlg(){
        pager = new Pager(0);
        pager.setOrderBy("no");
        pager.setOrderType(BaseEnum.OrderType.asc);
        Company com = usersService.getCompanyByUser();
        List<Dict> dictList = dictService.listDefinedEnable(DictBean.DictEnum.valueOf(key), com.getId());

        Map<String,Object> rMap ;
        Map<String,Object> data = Maps.newHashMap();
        List<JSONObject> dataRows = Lists.newArrayList();

        for(Dict dict : dictList){
            rMap = Maps.newHashMap();
            rMap.put("name", dict.getName());
            rMap.put("id",dict.getId());
            if(StringUtils.isNotEmpty(keyIds)){
                if(StringUtils.contains(keyIds, dict.getId())){
                    rMap.put("checked","checked");
                }
            }
            dataRows.add(JSONObject.fromObject(rMap));
        }

        data.put("data", JSONArray.fromObject(dataRows));
        Result rs = new Result(200,"操作成功",data);
        return ajaxHtmlAppResult(rs);
    }


    /**
     * 选择某公司下的用户
     * @return
     */
    public String usersDlg(){
        pager = new Pager(0);
        pager.setOrderBy("no");
        pager.setOrderType(BaseEnum.OrderType.asc);
        Company com = usersService.getCompanyByUser();
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Users.class);
        detachedCriteria.add(Restrictions.eq("company", com))
                .add(Restrictions.like("adminType", "%1%"))
                .add(Restrictions.eq("state", BaseEnum.StateEnum.Enable));
        if (StringUtils.isNotEmpty(key)) {
            detachedCriteria.add(Restrictions.like("name","%"+key+"%"));
        }
        usersService.findByPager(pager, detachedCriteria);
        List<Users> usersList = (List<Users>) pager.getList();
        Map<String,Object> rMap ;
        Map<String,Object> data = Maps.newHashMap();
        List<JSONObject> dataRows = Lists.newArrayList();

        for(Users users : usersList){
            rMap = Maps.newHashMap();
            rMap.put("name", users.getName());
            rMap.put("id",users.getId());
            if(StringUtils.isNotEmpty(keyIds)){
                if(StringUtils.contains(keyIds, users.getId())){
                    rMap.put("checked","checked");
                }
            }
            dataRows.add(JSONObject.fromObject(rMap));
        }

        data.put("data", JSONArray.fromObject(dataRows));
        Result rs = new Result(200,"操作成功",data);
        return ajaxHtmlAppResult(rs);
    }

    /**
     * 选择用户
     * @return
     */
    public String userDlg(){

        List<Users> usersList = (List<Users>) usersService.findByPagerAndStates(new Pager(0), new BaseEnum.StateEnum[]{BaseEnum.StateEnum.Enable}).getList();
        Map<String,Object> rMap ;
        Map<String,Object> data = Maps.newHashMap();
        List<JSONObject> dataRows = Lists.newArrayList();

        for(Users users : usersList){
            rMap = Maps.newHashMap();
            rMap.put("name", users.getName());
            rMap.put("id",users.getId());
            if(StringUtils.isNotEmpty(keyIds)){
                if(StringUtils.contains(keyIds, users.getId())){
                    rMap.put("checked","checked");
                }
            }
            dataRows.add(JSONObject.fromObject(rMap));
        }

        data.put("data", JSONArray.fromObject(dataRows));
        Result rs = new Result(200,"操作成功",data);
        return ajaxHtmlAppResult(rs);
    }

    /**
     * 依据用户列出用户所有部门
     * @return
     */
    public String departmentForUserDlg(){
        Users users;
        List<Department> departments = new ArrayList<>();
        if (StringUtils.isNotEmpty(keyId)) {
            users = usersService.get(keyId);
            Set<Duty> duties = users.getDutySet();
            for (Duty duty:duties) {
                if (!departments.contains(duty.getDepartment())) {
                    departments.add(duty.getDepartment());
                }
            }
        }

        Map<String,Object> rMap ;
        Map<String,Object> data = Maps.newHashMap();
        List<JSONObject> dataRows = Lists.newArrayList();
        for (Department department:departments) {
            rMap = Maps.newHashMap();
            rMap.put("name", department.getName());
            rMap.put("id",department.getId());
            if(StringUtils.isNotEmpty(keyIds)){
                if(StringUtils.contains(keyIds, department.getId())){
                    rMap.put("checked","checked");
                }
            }
            dataRows.add(JSONObject.fromObject(rMap));
        }

        data.put("data", JSONArray.fromObject(dataRows));
        Result rs = new Result(200,"操作成功",data);
        return ajaxHtmlAppResult(rs);
    }

    /**
     * 选择角色
     * @return
     */
    public String roleDlg(){

        List<Role> roleList = (List<Role>) roleService.findByPagerAndStates(new Pager(0), new BaseEnum.StateEnum[]{BaseEnum.StateEnum.Enable}).getList();
        Map<String,Object> rMap ;
        Map<String,Object> data = Maps.newHashMap();
        List<JSONObject> dataRows = Lists.newArrayList();

        for(Role role : roleList){
            rMap = Maps.newHashMap();
            rMap.put("name", role.getName());
            rMap.put("id",role.getId());
            if(StringUtils.isNotEmpty(keyIds)){
                if(StringUtils.contains(keyIds, role.getId())){
                    rMap.put("checked","checked");
                }
            }
            dataRows.add(JSONObject.fromObject(rMap));
        }

        data.put("data", JSONArray.fromObject(dataRows));
        Result rs = new Result(200,"操作成功",data);
        return ajaxHtmlAppResult(rs);
    }

    /**
     * 选择部门
     * @return
     */
    public String departDlg(){
        pager = new Pager();
        pager.setOrderBy("createDate");
        pager.setOrderType(BaseEnum.OrderType.asc);
        List<Department> departmentList = (List<Department>) departmentService.findByPagerAndStates(pager, new BaseEnum.StateEnum[]{BaseEnum.StateEnum.Enable}).getList();
        Map<String,Object> rMap ;
        Map<String,Object> data = Maps.newHashMap();
        List<JSONObject> dataRows = Lists.newArrayList();
        for(Department department : departmentList){
            rMap = Maps.newHashMap();
            rMap.put("name", department.getName());
            rMap.put("id",department.getId());
            if(StringUtils.isNotEmpty(keyIds)){
                if(StringUtils.contains(keyIds, department.getId())){
                    rMap.put("checked","checked");
                }
            }
            dataRows.add(JSONObject.fromObject(rMap));
        }

        data.put("data", JSONArray.fromObject(dataRows));
        Result rs = new Result(200,"操作成功",data);
        return ajaxHtmlAppResult(rs);
    }

    /**
     * 选择岗位
     * @return
     */
    public String postDlg(){
        List<Post> postList = (List<Post>) postService.findByPagerAndStates(new Pager(0), new BaseEnum.StateEnum[]{BaseEnum.StateEnum.Enable}).getList();
        Map<String,Object> rMap ;
        Map<String,Object> data = Maps.newHashMap();
        List<JSONObject> dataRows = Lists.newArrayList();
        for(Post post : postList){
            rMap = Maps.newHashMap();
            rMap.put("name", post.getName());
            rMap.put("id",post.getId());
            if(StringUtils.isNotEmpty(keyIds)){
                if(StringUtils.contains(keyIds, post.getId())){
                    rMap.put("checked","checked");
                }
            }
            dataRows.add(JSONObject.fromObject(rMap));
        }

        data.put("data", JSONArray.fromObject(dataRows));
        Result rs = new Result(200,"操作成功",data);
        return ajaxHtmlAppResult(rs);
    }

    /**
     * 选择群组
     * @return
     */
    public String groupDlg(){
        List<Group> groupList = (List<Group>) groupService.findByPagerAndStates(new Pager(0), new BaseEnum.StateEnum[]{BaseEnum.StateEnum.Enable}).getList();
        Map<String,Object> rMap ;
        Map<String,Object> data = Maps.newHashMap();
        List<JSONObject> dataRows = Lists.newArrayList();
        for(Group group : groupList){
            rMap = Maps.newHashMap();
            rMap.put("name", group.getName());
            rMap.put("id",group.getId());
            if(StringUtils.isNotEmpty(keyIds)){
                if(StringUtils.contains(keyIds, group.getId())){
                    rMap.put("checked","checked");
                }

            }
            dataRows.add(JSONObject.fromObject(rMap));
        }

        data.put("data", JSONArray.fromObject(dataRows));
        Result rs = new Result(200,"操作成功",data);
        return ajaxHtmlAppResult(rs);
    }

    public String powerDlg(){
        List<Power> powerList = (List<Power>) powerService.findByPagerAndStates(new Pager(0), new BaseEnum.StateEnum[]{BaseEnum.StateEnum.Enable}).getList();
        Map<String,Object> rMap ;
        Map<String,Object> data = Maps.newHashMap();
        List<JSONObject> dataRows = Lists.newArrayList();
        for(Power power : powerList){
            rMap = Maps.newHashMap();
            rMap.put("name","【"+power.getDepartment().getName()+"-"+power.getPost().getName()+"】");
            rMap.put("id",power.getId());
            if(StringUtils.isNotEmpty(keyIds)){
                if(StringUtils.contains(keyIds, power.getId())){
                    rMap.put("checked","checked");
                }
            }
            dataRows.add(JSONObject.fromObject(rMap));
        }

        data.put("data", JSONArray.fromObject(dataRows));
        Result rs = new Result(200,"操作成功",data);
        return ajaxHtmlAppResult(rs);
    }

    public String dutyDlg(){
        Users loginer = usersService.getLoginInfo();
        BusinessConfig bg = businessConfigService.getByBusinessKey(flowName);
        List<Duty> dutyList = Lists.newArrayList();
        Map<String, Object> data = new HashMap<String, Object>();
        if(StringUtils.isEmpty(keyId)){
            //dutyList = businessConfigService.findDutyInConfig(bg, loginer);
        }else {
            Task task = taskService.createTaskQuery().taskCandidateOrAssigned(loginer.getId()).processInstanceBusinessKey(keyId).orderByTaskCreateTime().desc().list().get(0);
            ProcessInstance processInstance = workflowService.getProIntanceByTask(task);
            ProcessConfig processConfig = processConfigService.findConfigByActivityId(task.getProcessDefinitionId(), task.getTaskDefinitionKey());
            //获取上一任务节点
            HistoricTaskInstanceEntity hisTask = (HistoricTaskInstanceEntity) workflowService.earlyTask(task.getProcessInstanceId());
            dutyList.addAll(processConfigService.findDutyByConfig(processConfig));
            if(hisTask != null){
                // todo 获取历史流程变量
                List<HistoricVariableInstance> histance = historyService.createHistoricVariableInstanceQuery().taskId(hisTask.getId()).variableName("curDutyId").list();
                if(histance.size()>0){
                    HistoricVariableInstanceEntity hisVarEntity = (HistoricVariableInstanceEntity) histance.get(0);
                    String dutyId = hisVarEntity.getTextValue();
                    Duty duty = dutyService.get(dutyId);
                    dutyList.addAll(processConfigService.findDutyInSpecail(processConfig,duty));
                }
            }else {
                String dutyId = (String) runtimeService.getVariable(task.getExecutionId(), "initDuty");
                Duty duty = dutyService.get(dutyId);
                dutyList.add(duty);
            }

        }
        List<JSONObject> datarows = Lists.newArrayList();
        //个人职责
        List<Duty> dList = dutyService.getDutys(loginer);
        Map<String,Object> rMap = null;
        for(Duty duty : dutyList){
            if(!dList.contains(duty))continue;
            rMap = Maps.newHashMap();
            rMap.put("name",duty.getPower().getDepartment().getName()+"-"+duty.getPower().getPost().getName());
            rMap.put("id",duty.getId());
            rMap.put("checked","");
            datarows.add(JSONObject.fromObject(rMap));
        }
        data.put("data", JSONArray.fromObject(datarows));
        data.put("size",datarows.size());
        Result rs = new Result(200,"操作成功",data);
        return ajaxHtmlAppResult(rs);
    }
    public String commonDlg(){
        return "commonDlg";
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }
}
