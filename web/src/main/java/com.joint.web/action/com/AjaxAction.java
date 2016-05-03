package com.joint.web.action.com;

import com.fz.us.base.bean.BaseEnum.StateEnum;
import com.fz.us.base.bean.Pager;
import com.fz.us.base.service.memcached.SpyMemcachedClient;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.joint.base.entity.Company;
import com.joint.base.entity.Duty;
import com.joint.base.entity.FileManage;
import com.joint.base.entity.Users;
import com.joint.base.entity.system.Admin;
import com.joint.base.service.CompanyService;
import com.joint.base.service.UsersService;
import com.joint.core.entity.ProInfo;
import com.joint.core.service.*;
import com.joint.web.action.BaseFlowAction;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@ParentPackage("com")
public class AjaxAction extends BaseFlowAction {
	
	private static final long serialVersionUID = 2332217693929363302L;

    @Resource
    private CompanyService companyService;
    @Resource
    private ClientService clientService;
    @Resource
    private LinkmanService linkmanService;
    @Resource
    private ProInfoService proInfoService;
    @Resource
    private ProBackService proBackService;
    @Resource
    private ProSignInService proSignInService;
    @Resource
    private ProLeaveService proLeaveService;
    @Resource
    private UsersService usersService;
    @Resource
    private SpyMemcachedClient spyMemcachedClient;

    private String flowName;
    private List<Duty> dutyList;

    private Admin admin;
    private Company org;
    private Users users;
    private int number;
    private Set<ProInfo> proInfoSet;
    private Set<Users> usersSet;
    private Map<String,Object> businessData;
    private String param;
    private String view_type;
    private String type;

    public String users(){
        users=usersService.getLoginInfo();
        return "users";
    }
    public String department(){
        return "department";
    }
    public String dict(){
        return "dict";
    }
    public String company(){
        org = usersService.getCompanyByUser();
        number= companyService.findUsersByCompany(org).size();
        admin = adminService.get(org.getAdminId());
        return "company";
    }
    public String client(){
        businessData = Maps.newHashMap();
        Pager pager = new Pager(0);
        Users user=usersService.getLoginInfo();
        Company com =usersService.getCompanyByUser();
        ProInfo proInfo=null;
        //项目
        Map<String,Object> rmap= Maps.newHashMap();
        rmap.put("state", StateEnum.Enable);
        //反馈
//        Map<String,Object> params = new HashMap<String,Object>();
//        params.put("state", BaseEnum.StateEnum.Enable);
//        pager=proInfoService.findByPagerAndCompany(pager,null,com,rmap);
//        List<ProInfo> proInfoList= (List<ProInfo>) pager.getList();
//        params.put("proInfo",proInfoList.toArray());
        int clientNum = clientService.findByPagerAndCompany(pager,null,com,new StateEnum[]{StateEnum.Enable}).getList().size();
        int linkNum = linkmanService.findByPagerAndCompany(pager,null,com,new StateEnum[]{StateEnum.Enable}).getList().size();
        int proNum=0;
        int backNum=0;
        Pager infopager=proInfoService.findByPagerAndLimit(new Pager(0),user,com,rmap);
        Pager backpager=proBackService.findByPagerAndLimit(new Pager(0),user,com,rmap);
        if(infopager!=null && infopager.getTotalCount() > 0){
            proNum=infopager.getList().size();
        }
        if(backpager!=null && backpager.getTotalCount() > 0){
            backNum=backpager.getList().size();
        }

        //int proNum=proInfoService.findByPagerAndCompany(pager,null,com,new StateEnum[]{StateEnum.Enable}).getList().size();
        //int backNum=proBackService.findByPagerAndCompany(pager,null,com,new StateEnum[]{StateEnum.Enable}).getList().size();
        businessData.put("client", clientNum);
        businessData.put("link",linkNum);
        businessData.put("proInfo",proNum);
        businessData.put("proBack",backNum);
        return "client";
    }
    public String baidumap(){
        return "baidumap";
    }
    public String project(){
        return "project";
    }
    public String knowledge() {return "knowledge";}
    public String systemSetting(){
        return ajaxHtmlCallback("400", "不提醒！","");
    }


    public List<Duty> getDutyList() {
        return dutyList;
    }

    public void setDutyList(List<Duty> dutyList) {
        this.dutyList = dutyList;
    }

    public  String prostudy(){
        return "prostudy";
    }
    public  String prolocate(){
        return "prolocate";
    }

    public String importFile(){
        return "importFile";
    }
    public String downloadFile(){
        return "import";
    }

    public String announce(){
        return "announce";
    }

    public String calendar(){
        pager=new Pager(0);
        Map<String,Object> params= Maps.newHashMap();
        params.put("state", StateEnum.Enable);
        Users loginUser=usersService.getLoginInfo();
        Company com=usersService.getCompanyByUser();
        pager=proInfoService.findByPagerAndLimit(pager,loginUser,com,params);
        List<ProInfo> proInfos= (List<ProInfo>) (pager.getTotalCount()>0?pager.getList(): Lists.newArrayList());
        proInfoSet= Sets.newHashSet();
        usersSet= Sets.newHashSet();
        proInfoSet.addAll(proInfos);
        params.put("company",com);
        usersSet.addAll(usersService.getList(params));
        return "calendar";
    }
    public String comment(){
        return "comment";
    }

    public String assignments(){
        return "assignments";
    }
    public String tendersurvey(){
        return "tendersurvey";
    }

    public String eclient(){
        return "eclient";
    }

    public String tender(){
        getRequest().setAttribute("view_type",view_type);
        return "tender";
    }

    public String linkman() {
        return "linkman";
    }

    /**
     * 合同评审
     *
     * @return
     */
    public String contract() {
        if(com.joint.base.util.StringUtils.isEmpty(type)){
            return "contract";
        }
        if(type.equals("1")){
            return "contract";
        }else if(type.equals("2")){
            return "contractDo";
        }
        return "contract";
    }


    public String assignmentsgroup(){
        return "assignmentsgroup";
    }

    /**
     * 上传文件
     * @return
     * @throws IOException
     */
    public String uploadFile() throws IOException {
        Map<String, Object> data = new HashMap<String, Object>();
        FileManage fileManage;
        if(StringUtils.isNotEmpty(keyId)){
            fileManage = gridFSSave(new FileInputStream(filedata), filename, keyId,"APPLICATION/OCTET-STREAM");
        }else {
            fileManage = gridFSSave(new FileInputStream(filedata), filename);
        }
        data.put("fileId",fileManage.getId());
        data.put("state",200);
        data.put("name",fileManage.getName());
        data.put("url",fileManage.getUrl());
        data.put("msg","上传成功");
        return ajaxJson(JSONObject.fromObject(data).toString());
    }
    /**
     * 删除附件
     * @return
     */
    public String delFile(){
        if(StringUtils.isNotEmpty(keyId)){
            FileManage fileManage = fileManageService.get(keyId);
            fileManageService.delete(fileManage);
            return ajaxHtmlCallback("200", "删除成功！","操作状态");
        }else {
            return ajaxHtmlCallback("400", "请选择删除文件！","操作状态");
        }
    }
    public String fileList(){
        Map<String, Object> data = new HashMap<String, Object>();
        ArrayList<Map<String,Object>> dataRows= Lists.newArrayList();
        Map<String,Object> rMap ;
        if(StringUtils.isNotEmpty(keyId)){
            for(String fileId : StringUtils.split(keyId, ",")){
                FileManage fileManage = fileManageService.get(fileId);
                if(fileManage == null){
                    continue;
                }
                rMap =  Maps.newHashMap();
                rMap.put("fileId",fileManage.getId());
                rMap.put("name",fileManage.getName());
                rMap.put("url",fileManage.getUrl());
                dataRows.add(JSONObject.fromObject(rMap));
            }
        }
        data.put("dataRows",dataRows);
        data.put("num",dataRows.size());
        data.put("state",200);
        return ajaxJson(JSONObject.fromObject(data).toString());
    }

    /**
     * 主页图表
     * @return
     */
    public String echart(){
        pager = new Pager(0);
        Company com = usersService.getCompanyByUser();
        Users user = usersService.getLoginInfo();
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("state",new StateEnum[]{StateEnum.Enable});
        Pager pagerProInfo = proInfoService.findByPagerAndLimit(pager,user,com,params);
        List<ProInfo>  proInfoList= (List<ProInfo>) pagerProInfo.getList();
        proInfoSet= Sets.newHashSet();
        proInfoSet.addAll(proInfoList);
        return "echart" ;
    }

    /**
     * 柱状图年份
     * @return
     */
    public String echartLineByYear(){
        return "echartLineByYear" ;
    }
    /**
     * 柱状图月份
     * @return
     */
    public String echartLineByMonth(){
        return "echartLineByMonth" ;
    }
    /**
     * 柱状图周
     * @return
     */
    public String echartLineByWeek(){
        return "echartLineByWeek" ;
    }
    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Company getOrg() {
        return org;
    }

    public void setOrg(Company org) {
        this.org = org;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Map<String, Object> getBusinessData() {
        return businessData;
    }

    public void setBusinessData(Map<String, Object> businessData) {
        this.businessData = businessData;
    }

    public Set<Users> getUsersSet() {
        return usersSet;
    }

    public void setUsersSet(Set<Users> usersSet) {
        this.usersSet = usersSet;
    }

    public Set<ProInfo> getProInfoSet() {
        return proInfoSet;
    }

    public void setProInfoSet(Set<ProInfo> proInfoSet) {
        this.proInfoSet = proInfoSet;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
