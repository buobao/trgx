package com.joint.base.service.activiti;


import com.fz.us.base.bean.Result;
import com.joint.base.entity.BusinessConfig;
import com.joint.base.entity.Users;
import com.joint.base.service.BusinessConfigService;
import com.joint.base.service.PermissionService;
import com.joint.base.service.UsersService;
import com.joint.base.util.StringUtils;
import org.activiti.engine.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by hpj on 2014/8/5.
 */
@Service
public class WorkflowTraceService {

    @Autowired
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
    protected PermissionService permissionService;
    @Resource
    protected WorkflowService workflowService;
    @Resource
    protected BusinessConfigService businessConfigService;
    @Resource
    protected SessionFactory sessionFactory;

    protected Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 发起流程
     * @param process -- 流程名称
     * @param bussinessKey --- 流程实例key
     * @param various --- 流程变量
     * @return
     */
    public ProcessInstance createFlow(String process,String bussinessKey,Map<String, Object> various){
        //设置发起人信息
        Users loginer = usersService.getLoginInfo();
        identityService.setAuthenticatedUserId(loginer.getId());
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(process,bussinessKey,various);
        return processInstance;
    }

    /**
     * 审批
     * @param various--- 流程变量
     * @return
     */
    public String processAppprove(Task task,Map<String, Object> various,String curDutyId){
        Users loginer = usersService.getLoginInfo();
        System.out.println("loginer:"+loginer.getId());

        //签收任务
        try{

            taskService.setVariableLocal(task.getId(),"curDutyId",curDutyId);//设置task的本地变量
            taskService.claim(task.getId(), loginer.getId());
            taskService.complete(task.getId(),various);
            return "审批成功";
        }catch(ActivitiObjectNotFoundException e){
            return "找不到当前任务";
        }catch(ActivitiTaskAlreadyClaimedException e){
            return "当前任务已经被签收";
        }
    }

    /**
     * 添加审批意见
     * @param bussinessId --- 业务Id
     * @param comment
     */
    public void addComment(String bussinessId,String comment){
        Task task = workflowService.getCurrentTask(bussinessId);
        taskService.addComment(task.getId(),task.getProcessInstanceId(),comment);
    }



    /**
     *  删除草稿
     * @return
     */
    public Result draftDelete(String key,String keyId){
        Result result=new Result();
        result.setState(200);
        result.setMessage("删除成功");
        if(StringUtils.isNotEmpty(keyId)){
            BusinessConfig bConfig=businessConfigService.getByBusinessKey(key);
            if(bConfig==null){
                result.setState(400);
                result.setMessage("未找到配置文档!");
            }
            String table= bConfig.getTableKey();
            if(table==null){
                result.setState(400);
                result.setMessage("配置文档未配置表!");
            }
            String sql="update "+table+" SET state=:s where id=:d";
            Session session=sessionFactory.getCurrentSession();
            int num= session.createSQLQuery(sql).setParameter("s","Delete").setParameter("d",keyId).executeUpdate();
            if(num==0){
                result.setState(400);
                result.setMessage("删除失败");
            }
            ProcessInstance processInstance=runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(keyId).active().singleResult();
            //删除运行中的数据
            runtimeService.deleteProcessInstance(processInstance.getId(),"");
            //删除历史表的数据

            historyService.deleteHistoricProcessInstance(processInstance.getId());
        }
       return result;
    }




}
