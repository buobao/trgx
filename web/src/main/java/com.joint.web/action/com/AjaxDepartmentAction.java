package com.joint.web.action.com;


import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Pager;
import com.fz.us.base.util.PinyinUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.joint.base.bean.EnumManage;
import com.joint.base.entity.*;
import com.joint.base.service.*;
import com.joint.core.service.AdminInfoService;
import com.joint.web.action.BaseAdminAction;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by hpj on 2014/9/16.
 */

@ParentPackage("com")
public class AjaxDepartmentAction extends BaseAdminAction {

    @Resource
    private UsersService usersService;
    @Resource
    private PostService postService;
    @Resource
    private DepartmentService departmentService;
    @Resource
    private DutyService dutyService;
    @Resource
    private PowerService powerService;
    @Resource
    private AdminInfoService adminInfoService;

    private Department department;
    private Pager pager;
    private Users user;
    private Post post;
    private Duty duty;
    private Department depart;
    private List<Post> postList;
    private ArrayList<Map<String,Object>> postArrayList;
    private ArrayList<Map<String,Object>> dutyArrayList;
    private List<Department> departList;
    private List<Duty> dutyList;
    private List<Users> usersList;
    private List<Department> childList;
    private ArrayList<Map<String, Object>> departmentArrayList;
    private ArrayList<Map> listMap;

    private String no;
    private String name;
    private String description;
    private String rowIds;
    private String rowId;
    private String departmentid;
    private String dutyId;
    private String pname;
    private String userId;
    private String departmentsJson;
    private String type;

    public String nestlist(){
        Users users = usersService.getLoginInfo();
        Company company = users.getCompany();
        departList = (List<Department>) departmentService.findByPagerAndCompany(new Pager(0), null, company, new BaseEnum.StateEnum[]{BaseEnum.StateEnum.Enable}).getList();
        Iterator<Department> dep_it = departList.iterator();
        while (dep_it.hasNext()){
            Department dep = dep_it.next();
            if (null != dep.getParent()){
                dep_it.remove();
            }
        }
        return "nestlist";
    }
    public String members(){
        Company company = usersService.getCompanyByUser();
        if (StringUtils.isEmpty(keyId)) {
            return "members";
        }
        depart = departmentService.get(keyId);
        usersList = (List<Users>) usersService.findByPagerAndCompany(new Pager(0),null,company,new BaseEnum.StateEnum[]{BaseEnum.StateEnum.Enable}).getList();
        listMap = Lists.newArrayList();
        Map<String,String> map ;
        List<Map> listOther = new ArrayList<Map>();
        //在本组中提前显示 ， 0留给负责人 id name department / dutyDefault principal (1,0 == 是、否)
        listMap.add(0, null);
        for(Users users:usersList){
            map = new HashMap<String,String>();
            map.put("id", users.getId());
            map.put("name", users.getName());
            duty=dutyService.getUsersDepartmentDuty(depart,users);
            if(duty!=null){
                map.put("dutyId",duty.getId());
            }
            //非本组成员
            List<Department> list = dutyService.getDepartments(users,company);
            if(!list.contains(depart)){
                map.put("dutyDefault","0");
                map.put("principal","0");

                List<Department> dList = dutyService.getDepartments(users,company);
                map.put("department",dList==null||dList.size()==0?"未分配":dList.get(0).getName());
                listOther.add(map);
            }else{
                //本组成员
                System.out.print("本组成员");
                map.put("dutyDefault","1");
                map.put("principal","0");
                if(dutyService.getPrincipal(depart)!=null){
                    if(dutyService.getPrincipal(depart).equals(users)){
                        map.put("principal","1");
                        listMap.add(0, map);
                    }else{
                        listMap.add(map);
                    }
                }else{
                    listMap.add(map);
                }
            }
        }
        listMap.addAll(listOther);
        return "members";
    }

    public String memberList(){
        if(StringUtils.isEmpty(keyId)) ajaxHtmlCallback("400", "请选择部门！","操作状态");

        pager = new Pager();
        pager.setPageSize(rows);
        pager.setPageNumber(page);
        pager.setOrderBy("createDate");
        pager.setOrderType(BaseEnum.OrderType.desc);
        if(StringUtils.isNotEmpty(sidx)&& BaseEnum.OrderType.valueOf(sord)!=null){
            pager.setOrderBy(sidx);
            pager.setOrderType(BaseEnum.OrderType.valueOf(sord));
        }

        department = departmentService.get(keyId);


        Map<String,Object> pmap = Maps.newHashMap();
        pmap.put("company", usersService.getCompanyByUser());
        pmap.put("state", BaseEnum.StateEnum.Enable);
        pmap.put("name", name);



        pmap = getSearchFilterParams(_search, pmap, filters);

        if(StringUtils.equals(type, "outer")){
            pager = usersService.findNotInDep(pager, department, pmap);
        }else if (StringUtils.equals(type, "own")){
            pager = usersService.findInDep(pager, department, pmap);
        }else {
            pager = usersService.findByPager(pager, pmap);
        }

        List<Users> usersList = (List<Users>) (pager.getTotalCount()>0?pager.getList(): Lists.newArrayList());

        Map<String,Object> data = Maps.newHashMap();
        List<JSONObject> datarows = Lists.newArrayList();
        Map<String,Object> rMap = Maps.newHashMap();

        for(Users users : usersList){
            List<FileManage> fileManageList = fileManageService.getFileByKeyId(users.getId());
            String imgUrl = fileManageList.size()>0 ? fileManageList.get(0).getUrl() : "";
            rMap.put("userImg",imgUrl);
            rMap.put("name",users.getName());
            rMap.put("mobile", users.getMobile());
            Duty duty = dutyService.getUsersDepartmentDuty(department,users);
            rMap.put("state",duty!=null ? duty.getDutyState() !=null ? duty.getDutyState().getName():"" : "");
            rMap.put("id",users.getId());
            rMap.put("dutyId",duty!=null ? duty.getId() : "");
            rMap.put("validPeriod",adminInfoService.getDaysByUsers(users));
            datarows.add(JSONObject.fromObject(rMap));

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
        data.put("dataRows",datarows);
        data.put("page",page);
        data.put("rows",rows);
        data.put("total",total);
        data.put("records",pager.getTotalCount());

        return  ajaxJson(JSONObject.fromObject(data).toString());
    }
    /**
     * 返回部门编辑页面
     * @return
     */
    public String read(){
        Map<String,Object> map=null;
        if(StringUtils.isEmpty(keyId)){
            departList =departmentService.getAll();
            departmentArrayList = new ArrayList<Map<String, Object>>();
            for (Department department : departList) {
                map = new HashMap<String, Object>();
                map.put("id", department.getId());
                map.put("name", department.getName());
                departmentArrayList.add(map);
            }
            return "read";
        }
        depart = departmentService.get(keyId);
        pager = new Pager(0);
        pager = departmentService.findByPagerAndStates(pager, new BaseEnum.StateEnum[]{BaseEnum.StateEnum.Enable});
        departList = (List<Department>) pager.getList();
        departmentArrayList = new ArrayList<Map<String, Object>>();
        for (Department department : departList) {
            map = new HashMap<String, Object>();
            map.put("id", department.getId());
            map.put("selected", "");
            if (depart != null && department.equals(depart.getParent())) {
                map.put("selected", "selected");
            }
            map.put("name", department.getName());
            departmentArrayList.add(map);
        }

        pager = new Pager();
        pager = postService.findByPagerAndStates(pager, new BaseEnum.StateEnum[]{BaseEnum.StateEnum.Enable});
        postList = (List<Post>) pager.getList();
        return "read";
    }

    /**
     * 部门信息的添加和更新
     * @return
     */
    public String save(){
        Users users = usersService.getLoginInfo();
        Company company = users.getCompany();

        Department depart;
        if(StringUtils.isNotEmpty(keyId)){
            depart = departmentService.get(keyId);
        }else {
            depart = new Department();
        }
        depart.setName(name);
        depart.setPinYinHead(PinyinUtil.getPinYinHeadChar(name));
        depart.setPinYin(PinyinUtil.getPingYin(name));
        depart.setBeSystem(0);
        if(StringUtils.isNotEmpty(keyId)){
            departmentService.update(depart);
        }else {
            depart.setCompany(company);
            depart.setCreater(users);
            departmentService.save(depart);
        }
        return ajaxHtmlCallback("200", "保存成功！","操作状态");
    }

    /**
     * 删除部门信息
     * @return
     */
    public String delete() {
        if (StringUtils.isEmpty(keyId)) {
            return ajaxHtmlCallback("400", "请选择部门！","操作状态");
        }
        depart = departmentService.get(keyId);
        List<Duty> dList=dutyService.getDutys(depart);
        if(dList.size()>0){
            return ajaxHtmlCallback("400", "请先删除该部门对应的职责！", "提示信息");
        }
        Set<Department> departments = depart.getChildren();
        Iterator<Department> it = departments.iterator();
        while (it.hasNext()) {
            Department dep = it.next();
            if (dep.getState() == BaseEnum.StateEnum.Enable){
                return ajaxHtmlCallback("400", "请先删除子部门！", "操作状态");
            }
        }

        depart.setState(BaseEnum.StateEnum.Delete);
        departmentService.update(depart);
        return ajaxHtmlCallback("200", "删除成功！", "操作状态");

    }

    /**
     * 删除部门成员
     * @return
     */
    public String deleteUser(){
        if(StringUtils.isNotEmpty(keyId)){
            Duty duty=dutyService.get(keyId);
            dutyService.delete(duty);
        }
        return ajaxHtmlCallback("200", "删除成功！", "操作状态");
    }
    /**
     * 部门唯一性校验
     * @return
     */
    public void isUnique(){
        PrintWriter out= null;
        try {
            out=getResponse().getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(departmentService.isUnique(validatePro,keyId,newValue)){
            out.print("true");
        }else{
            out.print("false");
        }

    }
    /**
     *点击部门信息视图的分配岗位弹出的dialog
     * @return
     */
    public String postQuery(){
        List<Power> powerList=null;
        if(StringUtils.isEmpty(keyId)){
            return ajaxHtmlCallback("400", "请选择部门！","操作状态");
        }
        if(powerService.getByDepartId(keyId)!=null){
            powerList=powerService.getByDepartId(keyId);
        }

        List<Post> postLit=null;
        if(powerList.size()>0){
            for(Power power:powerList){
                postLit=new ArrayList<Post>();
                postLit.add(power.getPost());
            }
        }
        Map<String,Object> map;
        postArrayList=new ArrayList<Map<String, Object>>();
        if(postLit!=null && postLit.size()>0){
            for(Post post :postLit){
                map = new HashMap<String, Object>();
                map.put("id",keyId+"-"+post.getId());
                map.put("name",post.getName());
                map.put("checked","");
                Power power=powerService.getPowerByDepartAndPost(departmentService.get(keyId), postService.get(post.getId()));
                if(power.getPowerDefault()==1){
                    map.put("checked","checked");
                }
                postArrayList.add(map);
            }
        }
        return "postQuery";
    }

    /**
     *部门zTree
     */
    public String zTree(){
        Pager pager = new Pager(0);
        pager.setPageSize(rows);
        pager.setPageNumber(page);
        pager.setOrderBy("no");
        pager.setOrderType(BaseEnum.OrderType.asc);
        List<Department> departmentList= (List<Department>) departmentService.findByPagerAndStates(pager,new BaseEnum.StateEnum[]{BaseEnum.StateEnum.Enable}).getList();
        List<JSONObject> treeList = new ArrayList<JSONObject>();
        for(Department per : departmentList){
            Map<String,Object> rMap = new HashMap<String, Object>();
            rMap.put("id",per.getId());
            rMap.put("pId",per.getParent()==null?"":per.getParent().getId());
            rMap.put("pName",per.getName());
            rMap.put("sName",per.getName());
            rMap.put("name",per.getName());
            rMap.put("open", false);
            treeList.add(JSONObject.fromObject(rMap));
        }
        Map<String, Object> data = new HashMap<String, Object>();
        data = new HashMap<String, Object>();
        data.put("data", JSONArray.fromObject(treeList));

        return ajaxHtmlAppResult(1, "", JSONObject.fromObject(data));
    }


    /**
     * 点击设定负责人弹出视图中的确定按钮事件
     * @return
     */
    public String lead(){
        if(StringUtils.isEmpty(keyId)){
            return ajaxHtmlCallback("400", "请选择部门！","操作状态");
        }
        Set<Post> postSet = new HashSet();
        Set<Duty> dutySet =new HashSet<Duty>();

        Department department =departmentService.get(keyId);
        Duty duty1=dutyService.get(rowIds);
        //将之前的部门负责人变成组员
        Duty duty = dutyService.getPrincipalDuty(department);
        if(duty1.getDutyState()== EnumManage.DutyState.Principal){
            duty.setDutyState(EnumManage.DutyState.Default);
            dutyService.update(duty);
        }else{
            //将组员变成部门负责人
            if(duty!=null){
                duty.setDutyState(EnumManage.DutyState.Default);
                dutyService.update(duty);
            }
            duty1.setDutyState(EnumManage.DutyState.Principal);
            dutyService.update(duty1);
        }
        return ajaxHtmlCallback("200", "保存成功！","操作状态");
    }

    /**
     * 设置部门负责人
     * @return
     */
    public String membersMethod(){
        Duty duty1=null;
        if(StringUtils.isEmpty(departmentid))return ajaxHtmlCallback("400", "请选择部门！","操作状态");
        if(StringUtils.isEmpty(keyId)) return ajaxHtmlCallback("400", "请选择人员！","操作状态");

        user=usersService.get(keyId);
        department=departmentService.get(departmentid);

        if(StringUtils.equals(type, "add")){
            Duty duty = new Duty();
            duty.setUsers(user);
            duty.setDepartment(department);
            duty.setCompany(usersService.getCompanyByUser(user));
            duty.setDutyState(EnumManage.DutyState.Default);
            dutyService.updateAndEnable(duty);
        }
        if(StringUtils.isNotEmpty(dutyId)){
            duty1=dutyService.get(dutyId);
        }
        if(StringUtils.equals(type, "delete")){
            duty1.setDutyState(EnumManage.DutyState.Default);
            duty1.setState(BaseEnum.StateEnum.Delete);
            dutyService.update(duty1);
        }
        if(StringUtils.equals(type, "principal")){
            dutyService.updatePrinciple(duty1);
        }
        return ajaxHtmlCallback("200", "保存成功！","操作状态");
    }
    /**
     * 点击分配岗位弹出的dialog中的确定按钮事件
     * @return
     */
    public String config(){
        if(StringUtils.isEmpty(keyId)){
            return ajaxHtmlCallback("400", "请选择部门！","操作状态");
        }
        JSONArray dataRows = JSONArray.fromObject(rowIds);
        Set<Post> postSet = new HashSet<Post>();
        Department department =departmentService.get(keyId);
        for(int i=0;i<dataRows.size();i++){
            JSONObject obj = dataRows.getJSONObject(i);
            String postId = obj.getString("id");
            Post post = postService.get(postId);
            postSet.add(post);
        }
        department.setPostSet(postSet);
        departmentService.update(department);
        return ajaxHtmlCallback("200", "保存成功！","操作状态");
    }

    /**
     * 点击设定负责人弹出dialog
     * @return
     */
    public String configView(){
        dutyArrayList = new ArrayList<Map<String, Object>>();
        if(StringUtils.isEmpty(keyId)){
            return ajaxHtmlCallback("400", "请选择负责人！","操作状态");
        }
        depart = departmentService.get(keyId);
        Set<Post> postSet = depart.getPostSet();

        Map<String,Object> map;
        for (Post post:postSet) {
            map=new HashMap<String, Object>();
            map.put("postId",post.getId());
            map.put("postName",post.getName());
            map.put("checked","");
            dutyArrayList.add(map);
        }
        return "configView";
    }

    /**
     * 点击设定负责人弹出dialog中的部门checkBox，查出该部门下对应的岗位和人员
     * @return
     */
    public String configPost(){
        if(StringUtils.isEmpty(rowId)){
            return ajaxHtmlCallback("400", "请选择岗位！","操作状态");
        }
        List<JSONObject> dataRows = new ArrayList<JSONObject>();
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, Object> rMap = null;
        depart=departmentService.get(keyId);
        post=postService.get(rowId);
        List<Users> usersList = dutyService.getPersons(depart,post);

        for(Users users :usersList){
            rMap = new HashMap<String, Object>();
            rMap.put("id",post.getId()+"-"+users.getId());
            rMap.put("name",post.getName()+"-"+users.getName());
            rMap.put("checked","");
            dataRows.add(JSONObject.fromObject(rMap));
        }
        data.put("state","200");
        data.put("rows", JSONArray.fromObject(dataRows));
        data.put("title","操作状态");
        return ajaxJson(JSONObject.fromObject(data).toString());
    }

    /**
     * 点击查看成员弹出的dialog
     * @return
     */
    public String leftView(){
        if(StringUtils.isEmpty(keyId)){
            return ajaxHtmlCallback("400", "请选择部门！","操作状态");
        }
        Department depart = departmentService.get(keyId);
        dutyList = dutyService.getDutys(depart);
        Map<String,Object> rmap;
        dutyArrayList=new ArrayList<Map<String, Object>>();
        for(Duty duty:dutyList){
            rmap=new HashMap<String, Object>();
            rmap.put("id",duty.getId());
            rmap.put("checked","");
            rmap.put("user", duty.getUsers().getName());
            rmap.put("post", duty.getPower().getPost().getName());
            if(duty.getDutyState()== EnumManage.DutyState.Principal){
                rmap.put("checked","checked");
            }
            dutyArrayList.add(rmap);
        }
        return "leftView";
    }

    /**
     * 暂未发现
     * @return
     */
    public String userList(){
        if(StringUtils.isEmpty(keyId)){
            return ajaxHtmlCallback("400", "请选择部门！","操作状态");
        }
        List<JSONObject> dataRows = new ArrayList<JSONObject>();
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, Object> rMap = null;
        Department depart = departmentService.get(keyId);
        List<Duty> dutyList = dutyService.getDutys(depart);
        for(Duty duty:dutyList){
            rMap = new HashMap<String, Object>();
            rMap.put("name",duty.getUsers().getName());
            rMap.put("post", duty.getPower().getPost().getName());
            rMap.put("depart",depart.getName());
            dataRows.add(JSONObject.fromObject(rMap));
        }
        data.put("state","200");
        data.put("rows", JSONArray.fromObject(dataRows));
        data.put("title","操作状态");
        return ajaxJson(JSONObject.fromObject(data).toString());
    }

    /**
     * 获取当前部门负责人
     * @param departId
     * @return
     */
    public String getPrincipal(String departId){
        Department department = departmentService.get(departId);
        Users users = dutyService.getPrincipal(department);
        if(users != null){
            return users.getName();
        }else{
            return "";
        }
    }

    /**
     * 获取所有部门的组织结构信息
     * @return
     */
    public String organization(){
        Department dep = null;
        for(Department department : departmentService.getAll()){
            if(department.getParent() != null){
                continue;
            }
            dep=department;
        }
        Map<String, Object> data = Maps.newHashMap();
        data.put("state","200");
        data.put("rows", JSONObject.fromObject(depInfo(dep)));

        return ajaxJson(JSONObject.fromObject(data).toString());
    }

    /**
     * 只重新设置部门间上下级结构，给下级部门update department.setParent即可
     * */
    public String resave(){
        if (StringUtils.isEmpty(departmentsJson)){
            return ajaxHtmlCallback("400", "操作错误！","");
        }else{
            System.out.println(departmentsJson);

            JSONArray firstArray = JSONArray.fromObject(departmentsJson);
            setNextLevelDepartmentParent(firstArray,null);

            return ajaxHtmlCallback("200", "操作成功！","");
        }
    }

    //递归更新下级业务组的上级
    private void setNextLevelDepartmentParent(JSONArray secondArray,Department parent){
        if(secondArray!=null&&secondArray.size()>0){
            for(int i=0;i<secondArray.size();i++){
                JSONObject temp = secondArray.getJSONObject(i);
                String id = temp.getString("id");

                Department _parent = departmentService.get(id);
                _parent.setParent(parent);
                departmentService.update(_parent);

                System.out.println(id + " " + name);
                if(temp.containsKey("children")){
                    JSONArray _secondArray =  temp.getJSONArray("children");
                    setNextLevelDepartmentParent(_secondArray,_parent);
                }
            }
        }
    }

    private Map<String,Object> depInfo(Department dep){
        Map<String,Object> rMap = Maps.newHashMap();
        rMap.put("name",dep.getName());
        rMap.put("id",dep.getId());
        List<JSONObject> dataRows = new ArrayList<JSONObject>();
        for (Department depart : dep.getChildren()){
            dataRows.add(JSONObject.fromObject(depInfo(depart)));
        }
        if(dataRows.size() > 0){
            rMap.put("child", JSONArray.fromObject(dataRows));
        }

        return rMap;
    }

    public String membersAddOrRemove(){
        if(StringUtils.isEmpty(keyId) || StringUtils.isEmpty(userId)){
            return ajaxHtmlCallback("400","请选择组或人员!","操作状态");
        }
        if(StringUtils.isEmpty(dutyId)){
            duty =new Duty();
            duty.setUsers(usersService.get(userId));
            duty.setDepartment(departmentService.get(keyId));
            duty.setDutyDefault(1);
            dutyService.save(duty);
        }else{
            duty=dutyService.get(dutyId);
            duty.setDutyDefault(0);
            duty.setState(BaseEnum.StateEnum.Disenable);
            dutyService.update(duty);
        }
        return ajaxHtmlCallback("200","提交成功!","操作状态");
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

    public Department getDepart() {
        return depart;
    }

    public void setDepart(Department depart) {
        this.depart = depart;
    }

    public List<Department> getDepartList() {
        return departList;
    }

    public void setDepartList(List<Department> departList) {
        this.departList = departList;
    }

    public String getRowIds() {
        return rowIds;
    }

    public void setRowIds(String rowIds) {
        this.rowIds = rowIds;
    }

    public ArrayList<Map<String, Object>> getPostArrayList() {
        return postArrayList;
    }

    public void setPostArrayList(ArrayList<Map<String, Object>> postArrayList) {
        this.postArrayList = postArrayList;
    }

    public List<Duty> getDutyList() {
        return dutyList;
    }

    public void setDutyList(List<Duty> dutyList) {
        this.dutyList = dutyList;
    }

    public String getDepartmentid() {
        return departmentid;
    }

    public void setDepartmentid(String departmentid) {
        this.departmentid = departmentid;
    }

    public ArrayList<Map<String, Object>> getDutyArrayList() {
        return dutyArrayList;
    }

    public void setDutyArrayList(ArrayList<Map<String, Object>> dutyArrayList) {
        this.dutyArrayList = dutyArrayList;
    }

    public List<Users> getUsersList() {
        return usersList;
    }

    public void setUsersList(List<Users> usersList) {
        this.usersList = usersList;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public List<Department> getChildList() {
        return childList;
    }

    public void setChildList(List<Department> childList) {
        this.childList = childList;
    }

    public Duty getDuty() {
        return duty;
    }

    public void setDuty(Duty duty) {
        this.duty = duty;
    }

    public ArrayList<Map<String, Object>> getDepartmentArrayList() {
        return departmentArrayList;
    }

    public void setDepartmentArrayList(ArrayList<Map<String, Object>> departmentArrayList) {
        this.departmentArrayList = departmentArrayList;
    }

    public String getDutyId() {
        return dutyId;
    }

    public void setDutyId(String dutyId) {
        this.dutyId = dutyId;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public ArrayList<Map> getListMap() {
        return listMap;
    }

    public void setListMap(ArrayList<Map> listMap) {
        this.listMap = listMap;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getDepartmentsJson() {
        return departmentsJson;
    }

    public void setDepartmentsJson(String departmentsJson) {
        this.departmentsJson = departmentsJson;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
