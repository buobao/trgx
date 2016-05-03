package com.joint.web.action.com;


import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.BaseEnum.StateEnum;
import com.fz.us.base.bean.Pager;
import com.fz.us.base.bean.Result;
import com.fz.us.base.util.PinyinUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.joint.base.entity.*;
import com.joint.base.entity.system.Admin;
import com.joint.base.service.*;
import com.joint.base.util.DataUtil;
import com.joint.core.service.AdminInfoService;
import com.joint.web.action.BaseAdminAction;
import net.sf.json.JSONObject;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by hpj on 2014/9/16.
 */
@ParentPackage("com")
public class AjaxUsersAction extends BaseAdminAction {

    @Resource
    private AdminService adminService;
    @Resource
    private UsersService usersService;
    @Resource
    private DepartmentService departmentService;
    @Resource
    private PostService postService;
    @Resource
    private DutyService dutyService;
    @Resource
    private TaskService taskService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private HistoryService historyService;
    @Resource
    private FileManageService fileManageService;
    @Resource
    private PowerService powerService;
    @Resource
    private ProcessConfigService processConfigService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private AdminInfoService adminInfoService;

    protected Pager pager;
    protected Users user;
    protected List<Users> usersList;
    private ArrayList<Map<String,Object>> departArrayList;
    private List<Map<String,Object>> postArrayList;
    private Map<String,Object> dutyMap;
    private Set<Post> postSet;
    private List<Duty> dutyList;
    private List<Department> departmentList;
    private ArrayList<Map<String,Object>> dutyArrayList;
    private ArrayList<Map<String,Object>> taskArrayList;
    private List<Map<String,Object>> usersmapList;
    private List<Map<String,Object>> commentList;

    private String keyId;
    private String email;
    private String mobile;
    private String name;
    private String password;
    private String no;
    private String rowId;
    private String rowIds;
    private String idCard;
    private String address;
    private String province;
    private String city;
    private String county;
    private String phone;
    private String terminal;
    private String personalSign;
    private String departmentname;
    private String postname;
    private File filedata;
    private FileManage fileManage;
    private String fileId;
    private String filename;
    private String filedataFileName;
    private String filedataContentType;
    private String imgUrl;
    private String todoTask;
    private String finishTask;
    private String businessKeyList;
    private String repassword;
    private String validPeriod;
    private List<String> adminType;
    private int todoNum;
    private int finishNum;
    private int totalTask;
    private int x;
    private int y;
    private int w;
    private int h;

    private BigDecimal latitude;
    private BigDecimal longitude;

    private String authcode;
    private String newId;
    private Users users;

    /**
     *返回人员管理视图
     * @return
     */
    public String list(){

        pager = new Pager();
        pager.setPageSize(rows);
        pager.setPageNumber(page);
        pager.setOrderBy("no");
        pager.setOrderType(BaseEnum.OrderType.asc);
        if(StringUtils.isNotEmpty(sidx)&& BaseEnum.OrderType.valueOf(sord)!=null){
            pager.setOrderBy(sidx);
            pager.setOrderType(BaseEnum.OrderType.valueOf(sord));
        }
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("state", new StateEnum[]{StateEnum.Enable});
        params = getSearchFilterParams(_search,params,filters);

        Company company = usersService.getCompanyByUser();
        pager = usersService.findByPagerAndCompany(pager,null,company,params);
        usersList = (List<Users>) pager.getList();
        List<JSONObject> dataRows=new ArrayList<JSONObject>();
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String,Object> rMap;

        for(Users users:usersList){
            rMap = new HashMap<String, Object>();
            rMap.put("id", users.getId());
            rMap.put("no",users.getNo());
            rMap.put("iscompany", StringUtils.equals(company.getAdminId(), users.getAdminId())?"1":0);

            if(StateEnum.Enable.value().equals(users.getState().value())){
                rMap.put("state","state");
            }else{
                rMap.put("state","");
            }
            String imgUrl = "";
            List<FileManage> fileManageList = fileManageService.getFileByKeyId(users.getId());
            if(fileManageList.size() > 0){
                FileManage fileManage = fileManageList.get(0);
                imgUrl = fileManage.getUrl();
            }
            if (usersService.getLoginInfo().getId().equals(users.getId())) {
                rMap.put("isCurrentUser","1");
            } else {
                rMap.put("isCurrentUser","0");
            }
            Admin admin = adminService.get(users.getAdminId());
            String openId =  admin.getOpenId();
            String deviceId =  admin.getDeviceId();

            if (usersService.getLoginInfo().getAdminType().indexOf("3")>=0 && StringUtils.isNotEmpty(openId)){
                rMap.put("isCurrentUser",rMap.get("isCurrentUser")+"1");
            }else{
                rMap.put("isCurrentUser",rMap.get("isCurrentUser")+"0");
            }
            rMap.put("userImg",imgUrl);
            rMap.put("name",users.getName());
            rMap.put("mobile",users.getMobile());
            rMap.put("createDate", DataUtil.DateTimeToString(users.getCreateDate()));
            rMap.put("adminType", users.getAdminType()!=null?users.getAdminType():"");
            rMap.put("isBind", StringUtils.isNotEmpty(openId) || StringUtils.isNotEmpty(deviceId));
            if(StringUtils.isNotEmpty(users.getAdminId())){
                rMap.put("role",adminService.get(users.getAdminId()).getAccountType());
            }

            rMap.put("validPeriod",adminInfoService.getDaysByUsers(users));
            rMap.put("codeStatus",adminInfoService.getStatusByUsers(users));

            dataRows.add(JSONObject.fromObject(rMap));
        }
        long total =pager.getTotalCount();
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
        data.put("records",pager.getTotalCount());
        return  ajaxJson(JSONObject.fromObject(data).toString());
    }

    /**
     * 返回人员编辑视图
     * @return
     */
    public String read(){
        if(StringUtils.isNotEmpty(keyId)){
            user = usersService.get(keyId);
            dutyList=dutyService.getDutys(user);
        }
        return "read";
    }
    /**
     * 冻结、激活
     */
    public String setUserState(){
        if(StringUtils.isNotEmpty(keyId)){
            Users users=usersService.get(keyId);

            if(users.getState().equals(StateEnum.Enable)){
                users.setState(StateEnum.Disenable);
            }else {
                users.setState(StateEnum.Enable);
            }
            usersService.update(users);
        }
        return ajaxHtmlCallback("200", "设置成功!", "操作状态");
    }

    /**
     * 绑定authcode
     * @return
     */
    public String bindCode(){
        Result r = new Result(400,"请输入充值码",400,null);
        if(StringUtils.isEmpty(authcode)) return ajaxHtmlAppResult(r);
        r.setMessage("请选择人员");
        if(StringUtils.isEmpty(keyId)) return ajaxHtmlAppResult(r);
        String adminId = usersService.get(keyId).getAdminId();
        Company company = usersService.getCompanyByUser();

        r = adminInfoService.bindAuthCode(authcode, adminId,company);
        return ajaxHtmlAppResult(r);
    }

    public String bindMultiCode(){
        Result r = new Result(400,"请选择人员",400,null);
        if(StringUtils.isEmpty(keyId)) return ajaxHtmlAppResult(r);

        Company company = usersService.getCompanyByUser();

        Set<String> adminIds = Sets.newHashSet();
        for(String id : keyId.split(",")){
            if(StringUtils.isNotEmpty(id)){
                adminIds.add(usersService.get(id).getAdminId());
            }
        }
        if(adminIds.contains(company.getAdminId())) {
            r.setMessage("充值的帐号中包含企业帐号");
            return ajaxHtmlAppResult(r);
        }
        r = adminInfoService.bindMultiCode(adminIds,company);
        return ajaxHtmlAppResult(r);
    }

    /**
     * 帐号转移页面
     * @return
     */
    public String move(){
        users = usersService.get(keyId);
        Company company = usersService.getCompanyByUser();
        String adminId = company.getAdminId();
        Users cuser = usersService.get("adminId", adminId);
        pager = new Pager(0);
        Map<String,Object> rmap = Maps.newHashMap();
        rmap.put("company",usersService.getCompanyByUser());
        usersList = (List<Users>) usersService.findByPager(pager,rmap).getList();
        usersList.remove(cuser);
        validPeriod = String.valueOf(adminInfoService.getDaysByUsers(users));
        return "move";
    }

    /**
     * 帐号转移
     * @return
     */
    public String moveCode(){

        Users users = usersService.get(keyId);
        Users nusers = usersService.get(newId);
        Result r = adminInfoService.moveCode(users, nusers);
        return ajaxHtmlAppResult(r);
    }

    /**
     * 帐号暂停
     * @return
     */
    public String pauseCode(){
        Result r = new Result(400,"请选择人员",400,null);
        if(StringUtils.isEmpty(keyId)) return ajaxHtmlAppResult(r);
        Users users = usersService.get(keyId);


        adminInfoService.pauseCode(users.getAdminId());
        r = new Result(200,"该帐号已被暂停");

        return ajaxHtmlAppResult(r);
    }

    /**
     * 帐号恢复
     * @return
     */
    public String resumeCode(){
        Result r = new Result(400,"请选择人员",400,null);
        if(StringUtils.isEmpty(keyId)) return ajaxHtmlAppResult(r);
        Users users = usersService.get(keyId);


        adminInfoService.resumeCode(users.getAdminId());
        r = new Result(200,"该帐号已恢复");

        return ajaxHtmlAppResult(r);
    }



    /**
     * 重置密码
     * @return
     */
    public String reset(){
        if(StringUtils.isEmpty(keyId)){
            return ajaxHtmlCallback("400", "请选择员工！","操作状态");
        }
        Users user = usersService.get(keyId);
        usersService.ResetPass(user,password);
        return ajaxHtmlCallback("200", "重置成功！","操作状态");
    }





    /**
     * 人员添加页面
     */
    public String input(){
        if(StringUtils.isNotEmpty(keyId)){
            user = usersService.get(keyId);
        }
        return  "input";
    }

    /**
     * 用户唯一性校验
     * @return
     */
    public void isUnique() {
        PrintWriter out = null;
        Users user= usersService.getUsersByEmail(email);
        try {
            out = getResponse().getWriter();
            if (user== null){
                out.print("true");
            } else{
                out.print("false");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 用户唯一性校验(通过登陆手机号)
     * @return
     */
    public void isUniqueByMobile() {
        PrintWriter out= null;
        try {
            out=getResponse().getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Users users= usersService.getUsersByMobile(mobile);
        if(users == null){
            out.print("true");
        }else{
            out.print("false");
        }
    }

    /**
     * 添加人员
     * @return
     */
    public String save(){
        if(!StringUtils.equals(password, repassword)){
            return ajaxHtmlCallback("400", "两次输入密码不相同！","操作状态");
        }

        if(StringUtils.isNotEmpty(keyId)){
            user = usersService.get(keyId);
        }else{
            user = usersService.getUsersByMobileDel(mobile);
            if (null == user) {
                user = new Users();
            }
        }
        String aType = "";
        for(String type : adminType){
            aType = aType +  type;
        }

        user.setAdminType(aType);
        user.setName(name);
        user.setMobile(mobile);
        user.setPassword(password);
        user.setState(StateEnum.Enable);
        user.setPinYin(PinyinUtil.getPingYin(name));
        user.setPinYinHead(PinyinUtil.getPinYinHeadChar(name));


        if (StringUtils.isNotEmpty(keyId)) {
            usersService.update(user);
        }else {
            usersService.registerUserAndAdmin(user, usersService.getCompanyByUser());
        }

        return ajaxHtmlCallback("200", "保存成功！","操作状态");
    }

    /**
     * 人员删除
     * @return
     */
    public String delete(){
        if(StringUtils.isEmpty(keyId)){
            return ajaxHtmlCallback("400", "请选择成员！","操作状态");
        }
        Users user = usersService.get(keyId);
        List<Task> taskList=taskService.createTaskQuery().taskAssignee(user.getId()).active().orderByTaskCreateTime().desc().list();
        List<Duty> dutyList1=dutyService.getDutys(user);
        if(dutyList1.size()>0){
            return ajaxHtmlCallback("400","请先删除职责","操作状态");
        }
        if(taskList.size()>0){
            return ajaxHtmlCallback("400","请先处理待办","操作状态");
        }

        Admin admin=null;
        if(StringUtils.isNotEmpty(user.getAdminId())){
            admin = adminService.get(user.getAdminId());
            admin.setState(StateEnum.Delete);
            admin.setOpenId(null);
            adminService.update(admin);
        }
        user.setState(StateEnum.Delete);
        usersService.update(user);
        return ajaxHtmlCallback("200", "保存成功！", "操作状态");
    }

    public String bindcancel(){
        if(StringUtils.isEmpty(keyId)){
            return ajaxHtmlCallback("400", "请选择成员！","操作状态");
        }
        Users user = usersService.get(keyId);
        Admin adminInfo = adminService.get(user.getAdminId());
        adminInfo.setOpenId(null);
        adminInfo.setDeviceId(null);
        adminService.update(adminInfo);
        return ajaxHtmlCallback("200", "解绑成功！", "操作状态");
    }

    public String profile(){
        if(StringUtils.isNotEmpty(keyId)){
            user=usersService.get(keyId);
        }
        return "profile";
    }

    /**
     * 上传图片
     * @return
     */
    public String uploadImg() throws IOException {
        fileManage = gridFSSave(new FileInputStream(filedata), filename,keyId,"APPLICATION/OCTET-STREAM",Users.class);
        fileId=fileManage.getId();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("gridId",fileManage.getId());
        data.put("fileUrl",fileManage.getUrl());
        data.put("status","200");
        data.put("title","操作状态");
        data.put("message","上传头像成功！");

        return ajaxJson(JSONObject.fromObject(data).toString());
    }

    public String location(){
		/*String webUsername = (String) getSession("webUsername");
		Admin admin = adminService.getAdminByUsername(webUsername);
		admin.setLat(lat);
		admin.setLng(lng);
		adminService.update(admin);*/
        Users users = usersService.getLoginInfo();
        users.setLatitude(latitude);
        users.setLongitude(longitude);
        usersService.updateAndEnable(users);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("state", "200");
        data.put("title", "设置状态");
        data.put("message", "默认地图显示地址设置成功");
        return ajaxJson(JSONObject.fromObject(data).toString());
    }

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Map<String, Object>> getDepartArrayList() {
        return departArrayList;
    }

    public void setDepartArrayList(ArrayList<Map<String, Object>> departArrayList) {
        this.departArrayList = departArrayList;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getRowIds() {
        return rowIds;
    }

    public void setRowIds(String rowIds) {
        this.rowIds = rowIds;
    }

    public List<Department> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<Department> departmentList) {
        this.departmentList = departmentList;
    }

    public Set<Post> getPostSet() {
        return postSet;
    }

    public void setPostSet(Set<Post> postSet) {
        this.postSet = postSet;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getPersonalSign() {
        return personalSign;
    }

    public void setPersonalSign(String personalSign) {
        this.personalSign = personalSign;
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public String getPostname() {
        return postname;
    }

    public void setPostname(String postname) {
        this.postname = postname;
    }

    public Map<String, Object> getDutyMap() {
        return dutyMap;
    }

    public void setDutyMap(Map<String, Object> dutyMap) {
        this.dutyMap = dutyMap;
    }

    public List<Duty> getDutyList() {
        return dutyList;
    }

    public void setDutyList(List<Duty> dutyList) {
        this.dutyList = dutyList;
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

    public List<Map<String, Object>> getUsersmapList() {
        return usersmapList;
    }

    public void setUsersmapList(List<Map<String, Object>> usersmapList) {
        this.usersmapList = usersmapList;
    }

    public List<Map<String, Object>> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Map<String, Object>> commentList) {
        this.commentList = commentList;
    }

    public ArrayList<Map<String, Object>> getDutyArrayList() {
        return dutyArrayList;
    }

    public void setDutyArrayList(ArrayList<Map<String, Object>> dutyArrayList) {
        this.dutyArrayList = dutyArrayList;
    }

    public ArrayList<Map<String, Object>> getTaskArrayList() {
        return taskArrayList;
    }

    public void setTaskArrayList(ArrayList<Map<String, Object>> taskArrayList) {
        this.taskArrayList = taskArrayList;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public String getTodoTask() {
        return todoTask;
    }

    public void setTodoTask(String todoTask) {
        this.todoTask = todoTask;
    }

    public String getFinishTask() {
        return finishTask;
    }

    public void setFinishTask(String finishTask) {
        this.finishTask = finishTask;
    }

    public int getTodoNum() {
        return todoNum;
    }

    public void setTodoNum(int todoNum) {
        this.todoNum = todoNum;
    }

    public int getFinishNum() {
        return finishNum;
    }

    public void setFinishNum(int finishNum) {
        this.finishNum = finishNum;
    }

    public int getTotalTask() {
        return totalTask;
    }

    public void setTotalTask(int totalTask) {
        this.totalTask = totalTask;
    }

    public List<Map<String, Object>> getPostArrayList() {
        return postArrayList;
    }

    public void setPostArrayList(List<Map<String, Object>> postArrayList) {
        this.postArrayList = postArrayList;
    }

    public String getBusinessKeyList() {
        return businessKeyList;
    }

    public void setBusinessKeyList(String businessKeyList) {
        this.businessKeyList = businessKeyList;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    public FileManage getFileManage() {
        return fileManage;
    }

    public void setFileManage(FileManage fileManage) {
        this.fileManage = fileManage;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public List<String> getAdminType() {
        return adminType;
    }

    public void setAdminType(List<String> adminType) {
        this.adminType = adminType;
    }

    public String getAuthcode() {
        return authcode;
    }

    public void setAuthcode(String authcode) {
        this.authcode = authcode;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public String getNewId() {
        return newId;
    }

    public void setNewId(String newId) {
        this.newId = newId;
    }

    public String getValidPeriod() {
        return validPeriod;
    }

    public void setValidPeriod(String validPeriod) {
        this.validPeriod = validPeriod;
    }
}
