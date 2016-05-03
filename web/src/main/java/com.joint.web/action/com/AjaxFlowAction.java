package com.joint.web.action.com;


import com.fz.us.base.bean.Pager;
import com.fz.us.base.bean.Result;
import com.joint.base.entity.*;
import com.joint.base.service.*;
import com.joint.base.service.activiti.WorkflowService;
import com.joint.base.service.activiti.WorkflowTraceService;
import com.joint.base.util.DataUtil;
import com.joint.base.util.FileUtil;
import com.joint.base.util.WorkflowUtils;
import com.joint.web.action.BaseAdminAction;
import net.sf.json.JSONObject;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * Created by hpj on 2014/9/16.
 */
@ParentPackage("com")
public class AjaxFlowAction extends BaseAdminAction {

    @Resource
    private UsersService usersService;
    @Resource
    private PostService postService;
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private WorkflowService workflowService;
    @Resource
    private ProcessConfigService processConfigService;
    @Resource
    private FileManageService fileManageService;
    @Resource
    private BusinessConfigService businessConfigService;
    @Resource
    private TaskService taskService;
    @Resource
    private WorkflowTraceService workflowTraceService;

    private Pager pager;
    private Users user;
    private Post post;
    private ProcessDefinition processDefinition;
    private List<Post> postList;
    private  List<Deployment> deploymentList;
    private List<Map<String,Object>> dataRows;

    private String no;
    private String name;
    private String description;
    private File Filedata;
    private String Filename;
    private String FiledataFileName;
    private String FiledataContentType;
    private String keyId;
    private String filePath;
    private String key;
    private ArrayList<Map<String,Object>>  taskArrayList;

    protected Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 返回流程管理视图
     * @return
     */
    public String list(){
        pager = new Pager();
        pager.setPageSize(rows);
        pager.setPageNumber(page);
        List<JSONObject> dataRows=new ArrayList<JSONObject>();
        Map<String,Object> data=new HashMap<String, Object>();
        Map<String, Object> rMap = null;
        int first = (page-1)*rows;
        int end = page*rows;
        int rowSize= repositoryService.createProcessDefinitionQuery().active().list().size();

        List<ProcessDefinition> proDefinList = repositoryService.createProcessDefinitionQuery().active().orderByProcessDefinitionId().desc().listPage(first,end);
        for (ProcessDefinition processDefinition : proDefinList) {
            String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).list().get(0);
            rMap = new HashMap<String, Object>();
            rMap.put("pid", processDefinition.getId());
            rMap.put("id", processDefinition.getId());
            rMap.put("did", deployment.getId());
            rMap.put("name", StringUtils.isEmpty(processDefinition.getName())?deployment.getName():processDefinition.getName());
            rMap.put("key", processDefinition.getKey());
            rMap.put("version", processDefinition.getVersion());
            rMap.put("xml", processDefinition.getResourceName());
            rMap.put("pic", processDefinition.getResourceName());
            rMap.put("time", DateFormatUtils.format(deployment.getDeploymentTime(), "yyyy-MM-dd kk:mm:ss"));
            if(processDefinition.isSuspended()){
                rMap.put("active", "挂起");
            }else{
                rMap.put("active", "启用");
            }
            dataRows.add(JSONObject.fromObject(rMap));
        }
        long total =rowSize;
        if(total%rows==0){
            total = total/rows;
        }else{
            if(total<rows){
                total = 1;
            }else{
                total = total/rows + 1;
            }
        }
        data.put("dataRows",dataRows);
        data.put("page",page);
        data.put("rows",rows);
        data.put("total",total);
        data.put("records",rowSize);
        return  ajaxJson(JSONObject.fromObject(data).toString());
    }

    /**
     * 待办视图
     */
    public String toDoList(){
        System.out.println("进入代办");
        Users users = usersService.getLoginInfo();
        List<Task> taskList = taskService.createTaskQuery().taskCandidateOrAssigned(users.getId()).active().orderByTaskCreateTime().desc().list();
        taskArrayList=new ArrayList();
        Map<String,Object> rMap;
        for(Task task : taskList){
            ProcessDefinition processDefinition = workflowService.getProDefinitionByTask(task);
            if(processDefinition==null){
                continue;
            }

            ProcessInstance processInstance = workflowService.getProIntanceByTask(task);

            if(workflowService.getNumStatus(processInstance.getBusinessKey(),users) == 0){
                continue;
            }

            rMap = new HashMap<String,Object>();
            rMap.put("pname",StringUtils.isEmpty(processDefinition.getName())?repositoryService.createDeploymentQuery().deploymentId(processDefinition.getDeploymentId()).list().get(0).getName():processDefinition.getName());
            rMap.put("time",task.getCreateTime());
            String toUser;
            String msg = "";
            HistoricTaskInstance lastTask = workflowService.earlyTask(task.getProcessInstanceId()) ;
            if(lastTask != null){
                toUser = usersService.get(lastTask.getAssignee()).getName();
                List<org.activiti.engine.task.Comment> comments = taskService.getTaskComments(lastTask.getId());
                for(org.activiti.engine.task.Comment comment : comments){
                    msg = msg+comment.getFullMessage()+"["+ DataUtil.DateTimeToString(comment.getTime())+"]";
                }
            }else{
                toUser = "";
            }

            rMap.put("toUser",toUser);
            rMap.put("msg",msg);
            rMap.put("docId",processInstance.getBusinessKey());
//            Permission permission = permissionService.getByKey(processDefinition.getKey());
            rMap.put("url","ajax-"+processDefinition.getKey()+"!read.action?keyId="+processInstance.getBusinessKey());


            if(StringUtils.isEmpty("msg")){
                rMap.put("msg","暂时没有审批意见");
            }
            taskArrayList.add(rMap);
        }
        return  "toUsersList";
    }
    public String draftList(){
        List<Task> taskList = taskService.createTaskQuery().processVariableValueEquals("numStatus", 0).taskAssignee(usersService.getLoginInfo().getId()).active().orderByTaskCreateTime().desc().list();
        System.out.println(taskList.size());
        taskArrayList=new ArrayList();
        Map<String,Object> rMap;
        for(Task task : taskList){
            ProcessInstance processInstance = workflowService.getProIntanceByTask(task);
            ProcessDefinition processDefinition = workflowService.getProDefinitionByTask(task);
            rMap = new HashMap<String, Object>();
            rMap.put("time",task.getCreateTime());
            rMap.put("creater", workflowService.getStarter(processInstance.getBusinessKey()));
            rMap.put("pName", processDefinition.getName());
            rMap.put("docId",processInstance.getBusinessKey());
            rMap.put("key",processDefinition.getKey());
            rMap.put("url","ajax-"+processDefinition.getKey()+"!read.action?keyId="+processInstance.getBusinessKey());
            taskArrayList.add(rMap);
        }
        return "draftList";
    }

    /**
     * 流程挂起
     * @return
     */
    public String suspend() {
        if (StringUtils.isEmpty(keyId)) {
            return ajaxHtmlCallback("200", "挂起失败，请选择流程！","操作状态");
        }
        repositoryService.suspendProcessDefinitionById(keyId);
        return ajaxHtmlCallback("200", "流程挂起成功！", "操作状态");

    }

    /**
     * 点击流程部署弹出dialog
     * @return
     */
    public String dialog(){
        return "dialog";
    }

    public String createDlg(){
        return "createDlg";
    }

    public void readXml() throws IOException {
        ProcessDefinition processDef = workflowService.findProDefinitionById(keyId);
        String resourceName = processDef.getResourceName();
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDef.getDeploymentId(), resourceName);
        HttpServletResponse response = ServletActionContext.getResponse();
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }

    /**
     * 点击流程部署弹出dialog中的确定按钮事件
     * @return
     * @throws IOException
     */
    public String deploy() throws IOException {
        String path = "/WEB-INF/deploy/diagram";
        File folder = new File(ServletActionContext.getServletContext().getRealPath(path));
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String filePath = path + "/" +uuid+".zip";
        if(!folder.exists()){
            folder.mkdirs();
        }
        File outFile = new File(ServletActionContext.getServletContext().getRealPath(filePath));
        FileOutputStream outStream = new FileOutputStream(outFile);
        FileInputStream inStream = new FileInputStream(Filedata);
        byte[] buffer = new byte[1024];
        int i =0;
        while((i = inStream.read(buffer))>0){
            outStream.write(buffer,0,i);
        }
        outStream.close();
        inStream.close();
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        String classpathResourceUrl = "classpath:deploy/diagram/" + uuid + ".zip";
        //logger.debug("read workflow from: {}", classpathResourceUrl);
        org.springframework.core.io.Resource rs = new FileSystemResource(ServletActionContext.getServletContext().getRealPath(filePath));
        //org.springframework.core.io.Resource resource = (org.springframework.core.io.Resource) resourceLoader.getResource(uuid+".zip");
        InputStream inputStream = ((InputStreamSource) rs).getInputStream();
        Deployment deployment = null;
        if (FiledataFileName.indexOf(".zip")!=-1) {
            ZipInputStream zip = new ZipInputStream(inputStream);
            deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
        } else {
            deployment = repositoryService.createDeployment().addInputStream(FiledataFileName, inputStream).deploy();
        }
        inputStream.close();
        return ajaxHtmlCallback("200", "部署成功，请查看！","操作状态");
    }

    /**
     * 点击流程管理视图中的查看流程图
     * @return
     * @throws IOException
     */
    public String export() throws IOException {
        if(StringUtils.isEmpty(keyId)){
            return ajaxHtmlCallback("200", "查看失败，请选择流程！","操作状态");
        }
        List<FileManage> fileManageList = fileManageService.getFileByKeyId(keyId);
        if(fileManageList.size() > 0){
            FileManage fileManage = fileManageList.get(0);
            filePath = fileManage.getUrl();
            return "export";
        }
        String path = "/resource/bpm/images";
        //获取绝对路径
        String exportDir = ServletActionContext.getServletContext().getRealPath(path);
        processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(keyId).singleResult();
        if(processDefinition == null){
            return ajaxHtmlCallback("200", "查找不到当前流程请联系管理员！","操作状态");
        }
        String diagramResourceName = processDefinition.getDiagramResourceName();
        String key = processDefinition.getKey();
        int version = processDefinition.getVersion();
        WorkflowUtils.exportDiagramToFile(repositoryService, processDefinition, exportDir);
        String diagramDir = exportDir + "/" + key + "/" + version;
        String  diagramPath = diagramDir + "/" + diagramResourceName;
        File file = new File(diagramPath);
        FileManage fileManage = gridFSSave(new FileInputStream(file), diagramResourceName, processDefinition.getId());
        filePath = fileManage.getUrl();
        //删除本地文件
        //FileUtils.deleteQuietly(file);
        FileUtil.del(exportDir);
        return "export";
    }

    /**
     * 流程配置
     * @return
     */
    public String config(){
        if(StringUtils.isEmpty(keyId)){
            return ajaxHtmlCallback("400", "请选择流程定义！","操作状态");
        }
        List<ActivityImpl> activities = workflowService.findAllActivities(keyId);
        Map<String, Object> rMap;
        dataRows = new ArrayList<Map<String, Object>>();

        processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(keyId).singleResult();
        for(ActivityImpl activity : activities){
            if(!workflowService.isTask(activity)){
                continue;
            }
            rMap = new HashMap<String, Object>();
            Map<String, Object> properties = activity.getProperties();
            ProcessConfig proConfig= processConfigService.findConfigByActivityId(processDefinition.getId(), activity.getId());
            if(proConfig != null){
                rMap.put("depart", proConfig.getCommonConfig().getDepartmentSet());
                rMap.put("post", proConfig.getCommonConfig().getPostSet());
            }

            rMap.put("type", properties.get("type"));
            rMap.put("documentation", properties.get("documentation"));
            rMap.put("name", properties.get("name"));
            rMap.put("id", activity.getId());
            rMap.put("pid", keyId);
            dataRows.add(rMap);
        }
        return "config";
    }

    /**
     * 删除草稿
     * @return
     */
    public String draftDelete(){
        Result result= workflowTraceService.draftDelete(key, keyId);
        return ajaxHtmlAppResult(result);
    }

    public String showhistory(){
        return "history";
    }

    public String listHistory(){
        if (StringUtils.isNotEmpty(keyId)){
            pager = new Pager();
            pager.setPageSize(rows);
            pager.setPageNumber(page);
            List<JSONObject> dataRows=new ArrayList<JSONObject>();
            Map<String,Object> data=new HashMap<String, Object>();
            Map<String, Object> rMap = null;
            int first = (page-1)*rows;
            int end = page*rows;
            processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(keyId).orderByProcessDefinitionVersion().desc().list().get(0);
            List<BusinessConfig> businessConfigsList = businessConfigService.findBCfgByDefintionId(keyId);
            if (businessConfigsList.size() > end-1){
                businessConfigsList = businessConfigsList.subList(first, end);
            }
            else{
                businessConfigsList = businessConfigsList.subList(first, businessConfigsList.size());
                end = businessConfigsList.size();
            }

            for (BusinessConfig businessConfig : businessConfigsList) {
                String deploymentId = processDefinition.getDeploymentId();
                Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).list().get(0);
                rMap = new HashMap<String, Object>();
                rMap.put("pid", processDefinition.getId());
                rMap.put("id", processDefinition.getId()+"|"+businessConfig.getVersion());
                rMap.put("name", StringUtils.isEmpty(processDefinition.getName())?deployment.getName():processDefinition.getName());
                rMap.put("time", DateFormatUtils.format(businessConfig.getCreateDate(), "yyyy:MM:dd kk:mm:ss"));
                rMap.put("version",businessConfig.getVersion());
                dataRows.add(JSONObject.fromObject(rMap));
            }
            long total = end - first + 1;
            if(total%rows==0){
                total = total/rows;
            }else{
                if(total<rows){
                    total = 1;
                }else{
                    total = total/rows + 1;
                }
            }
            data.put("dataRows",dataRows);
            data.put("page",page);
            data.put("rows",rows);
            data.put("total",total);
            data.put("records",dataRows.size());
            return  ajaxJson(JSONObject.fromObject(data).toString());
        }
        return "";
    }


    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public List<Deployment> getDeploymentList() {
        return deploymentList;
    }

    public void setDeploymentList(List<Deployment> deploymentList) {
        this.deploymentList = deploymentList;
    }

    public File getFiledata() {
        return Filedata;
    }

    public void setFiledata(File filedata) {
        Filedata = filedata;
    }

    public String getFilename() {
        return Filename;
    }

    public void setFilename(String filename) {
        Filename = filename;
    }

    public String getFiledataFileName() {
        return FiledataFileName;
    }

    public void setFiledataFileName(String filedataFileName) {
        FiledataFileName = filedataFileName;
    }

    public String getFiledataContentType() {
        return FiledataContentType;
    }

    public void setFiledataContentType(String filedataContentType) {
        FiledataContentType = filedataContentType;
    }

    public List<Map<String, Object>> getDataRows() {
        return dataRows;
    }

    public void setDataRows(List<Map<String, Object>> dataRows) {
        this.dataRows = dataRows;
    }

    public ProcessDefinition getProcessDefinition() {
        return processDefinition;
    }

    public void setProcessDefinition(ProcessDefinition processDefinition) {
        this.processDefinition = processDefinition;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<Map<String, Object>> getTaskArrayList() {
        return taskArrayList;
    }

    public void setTaskArrayList(ArrayList<Map<String, Object>> taskArrayList) {
        this.taskArrayList = taskArrayList;
    }
}
