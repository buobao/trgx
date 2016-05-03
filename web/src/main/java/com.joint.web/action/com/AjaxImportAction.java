package com.joint.web.action.com;

import com.fz.us.base.bean.BaseEnum.OrderType;
import com.fz.us.base.bean.BaseEnum.StateEnum;
import com.fz.us.base.bean.Pager;
import com.fz.us.dict.entity.Dict;
import com.fz.us.dict.service.DictService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.joint.base.entity.Company;
import com.joint.base.entity.FileManage;
import com.joint.base.entity.Users;
import com.joint.base.service.AuthCodeService;
import com.joint.base.service.CompanyService;
import com.joint.base.service.FileManageService;
import com.joint.base.service.UsersService;
import com.joint.base.util.DataUtil;
import com.joint.base.util.excel.ExcelUtil;
import com.joint.base.util.excel.ExportExcel;
import com.joint.base.util.excel.ImportExcel;
import com.joint.core.entity.Client;
import com.joint.core.entity.Linkman;
import com.joint.core.entity.ProInfo;
import com.joint.core.service.ClientService;
import com.joint.core.service.LinkmanService;
import com.joint.core.service.ProInfoService;
import com.joint.web.action.BaseAdminAction;
import com.mongodb.gridfs.GridFSDBFile;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by amin on 2015/3/5.
 */
@ParentPackage("com")
public class AjaxImportAction extends BaseAdminAction {
    @Resource
    private ProInfoService proInfoService;
    @Resource
    private CompanyService companyService;
    @Resource
    private ClientService clientService;
    @Resource
    private LinkmanService linkmanService;
    @Resource
    private UsersService usersService;
    @Resource
    private DictService dictService;
    @Resource
    private FileManageService fileManageService;
    @Resource
    private AuthCodeService authCodeService;

    private File Filedata;
    private String Filename;
    private String FiledataFileName;
    private String FiledataContentType;
    private String csvname;
    private String idList;

    private static Logger log = LoggerFactory.getLogger(AjaxImportAction.class);

    /**
     * 下载视图
     */
    public String datalist(){
        pager = new Pager();
        pager.setPageSize(rows);
        pager.setPageNumber(page);
        pager.setOrderBy("createDate");
        pager.setOrderType(OrderType.desc);
        Company company=usersService.getCompanyByUser();
        Map<String,Object> map= Maps.newHashMap();
        map.put("name",".xls");
        pager=fileManageService.findByPagerAndCompany(pager, null, company,map);
        List<FileManage> fileManageList= (List<FileManage>) pager.getList();
        List<JSONObject> dataRows=new ArrayList<JSONObject>();
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String,Object> rMap;
        for(FileManage fileManage:fileManageList){
            rMap = new HashMap<String, Object>();
           if(StringUtils.isNotEmpty(fileManage.getTargetClass())){
               rMap.put("id",fileManage.getGridId());
               rMap.put("name",fileManage.getName());
               rMap.put("createDate", DataUtil.DateToString(fileManage.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
           }
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
     * 下载文件
     */
    public String download() throws IOException{
        GridFSDBFile file = null;
        String fileName=null;
        if(StringUtils.isNotEmpty(keyId)){
            if(fileManageService.get(keyId) != null){
                file = gridFSGet(fileManageService.get(keyId).getGridId());
            }else {
                file = gridFSGet(keyId);
            }
            OutputStream out = null;
            HttpServletResponse response = getTheResponse();
            response.setContentType(file.getContentType());
            fileName=new String(file.getFilename().getBytes("iso-8859-1"),"UTF-8");
            try {
                response.setHeader("Content-Disposition","attachment;filename="+fileName);
                response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(file.getFilename().getBytes("UTF-8"), "iso-8859-1") + "\"");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            out = response.getOutputStream();
            file.writeTo(out);
        }
        return null;
    }
    /**
     * 下载模板
     * @return
     */
    public String downTemplate(){
        try {
            if(StringUtils.isNotEmpty(csvname)){
               if("project".equals(csvname)){
                   String fileName = "项目信息数据导入模板.xlsx";
                   List<String> str= Lists.newArrayList();
                   str.add("项目名称");
                   new ExportExcel("项目信息数据", ProInfo.class,2,str).write(getResponse(), fileName).dispose();
                   return null;
               }
                if ("linkman".equals(csvname)){
                    String fileName = "联系人数据导入模板.xlsx";
                    List<String> str = Lists.newArrayList();
                    str.add("姓名");
                    new ExportExcel("联系人信息数据", Linkman.class,2,str).write(getResponse(), fileName).dispose();
                    return null;
                }
                if("client".equals(csvname)){
                    String fileName = "客户数据导入模板.xlsx";
                    List<String> str = Lists.newArrayList();
                    str.add("客户名称");
                    new ExportExcel("客户信息数据", Client.class,2,str).write(getResponse(), fileName).dispose();
                    return null;
                }
            }

        } catch (Exception ex) {
            log.info("项目信息数据导入模板下载 error: " + ex.toString());
        }
        return null;
    }

    /**
     * 导出项目信息数据
     * @return
     */
    public String exportData() {
        try {
            //创建一个webbook，对应一个Excel文件
            HSSFWorkbook wb = new HSSFWorkbook();
            //在webbook中添加一个sheet
            HSSFSheet sheet = wb.createSheet("数据表信息");
            //在sheet中添加表头第0行
            HSSFRow row = sheet.createRow((int) 0);
            //创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle style = wb.createCellStyle();
            // 创建一个居中格式
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            HSSFCell cell;
            ExcelUtil excelUtil=new ExcelUtil();
            Map<Integer,Object> cmap= Maps.newHashMap();
            List<String> title= Lists.newArrayList();
            Class clazz=null;
            String fileName=null;
            ProInfo proInfo=null;
            Linkman linkman=null;
            Client client=null;
            Company company = usersService.getCompanyByUser();
            Map<String,Object> rmap=null;
            List<ProInfo> proInfoList=null;
            if(StringUtils.isNotEmpty(keyId)){
                if(StringUtils.equals("proInfo", keyId)){
                    proInfoList=new ArrayList<ProInfo>();
                    clazz = ProInfo.class;
                    fileName="项目信息数据";
                    title.add(0,"项目名称");
                    title.add(1,"项目类别");
                    title.add(2,"客户名称");
                    title.add(3,"客户联系人");
                    title.add(4,"项目地址");
                    title.add(5,"项目总监");
                    rmap= Maps.newHashMap();

                    String[] ary=idList.split(",");
                    for(String item:ary){
                        if(StringUtils.isNotEmpty(item)){
                            rmap.put("id",item);
                            rmap.put("state", StateEnum.Enable);
                            List<ProInfo> plist= (List<ProInfo>) proInfoService.findByPagerAndCompany(new Pager(),null,company,rmap).getList();
                            if(plist.size() > 0) proInfoList.add(plist.get(0));
                        }
                    }
                    excelUtil.setTitle(row,title);
                    for (int i = 0; i < proInfoList.size(); i++) {
                        proInfo = (ProInfo) proInfoList.get(i);
                        if(proInfo == null) continue;
                        cmap.put(0, proInfo.getName());
                        cmap.put(1,"");
                        cmap.put(2,"");
                        cmap.put(3,"");
                        if(proInfo.getCategory() != null) cmap.put(1,proInfo.getCategory().getName());
                        if(proInfo.getClient() != null) cmap.put(2,proInfo.getClient().getName());
                        if(proInfo.getLinkman() != null) cmap.put(3,proInfo.getLinkman().getName());

                        cmap.put(4,proInfo.getAddress());
                        cmap.put(5,"");
                        if(proInfo.getLinkman() != null) cmap.put(5,proInfo.getChief().getName());

                        row=sheet.createRow((int)i+1);
                        excelUtil.fullCell(row,cmap);
                    }
                }
                if(StringUtils.equals("linkman", keyId)){
                    clazz = Linkman.class;
                    fileName="联系人信息数据";
                    title.add(0, "姓名");
                    title.add(1,"客户");
                    title.add(2,"别名");
                    title.add(3,"职责");
                    title.add(4,"喜好");
                    title.add(5,"办公电话");
                    title.add(6,"手机");
                    title.add(7,"备用手机");
                    title.add(8,"邮箱");
                    title.add(9,"备注");
                    List<Linkman> linkmanList = Lists.newArrayList();
                    rmap= Maps.newHashMap();

                    String[] ary=idList.split(",");
                    for(String item:ary){
                        if(StringUtils.isNotEmpty(item)){
                            rmap.put("id",item);
                            rmap.put("state", StateEnum.Enable);
                            linkman= (Linkman) linkmanService.findByPagerAndCompany(new Pager(),null,company,rmap).getList().get(0);
                            linkmanList.add(linkman);
                        }
                    }
                    excelUtil.setTitle(row,title);
                    for (int i = 0; i < linkmanList.size(); i++) {
                        linkman = (Linkman) linkmanList.get(i);
                        cmap.put(0,linkman.getName());
                        cmap.put(1,linkman.getClient().getName());
                        cmap.put(2,linkman.getSubName());
                        cmap.put(3,linkman.getDuty());
                        cmap.put(4,linkman.getPreferences());
                        cmap.put(5,linkman.getTel());
                        cmap.put(6,linkman.getMobile());
                        cmap.put(7,linkman.getMobile_back());
                        cmap.put(8,linkman.getEmail());
                        cmap.put(9,linkman.getRemark());
                        row=sheet.createRow((int)i+1);
                        excelUtil.fullCell(row,cmap);
                    }
                }
                if(StringUtils.equals("client", keyId)){
                    clazz = Client.class;
                    fileName="客户信息数据";
                    title.add(0,"客户名称");
                    title.add(1,"简称");
                    title.add(2,"客户性质");
                    title.add(3,"发票抬头");
                    title.add(4,"省(直辖市)");
                    title.add(5,"市(区)");
                    title.add(6,"县");
                    title.add(7,"备注");
                    title.add(8,"情况说明");
                    List<Client> clientList = Lists.newArrayList();
                    rmap= Maps.newHashMap();
                    String[] ary=idList.split(",");
                    for(String item:ary){
                        if(StringUtils.isNotEmpty(item)){
                            rmap.put("id",item);
                            rmap.put("state", StateEnum.Enable);
                            client= (Client) clientService.findByPagerAndCompany(new Pager(),null,company,rmap).getList().get(0);
                            clientList.add(client);
                        }
                    }

                    excelUtil.setTitle(row,title);
                    for (int i = 0; i < clientList.size(); i++) {
                        client = (Client) clientList.get(i);
                        cmap.put(0,client.getName());
                        cmap.put(1,client.getSubName());
                        cmap.put(2,"");
                        if(client.getType() != null){
                            cmap.put(2,client.getType().getName());
                        }

                        cmap.put(3,client.getInvoiceTitle());
                        cmap.put(4,client.getProvince());
                        cmap.put(5,client.getCity());
                        cmap.put(6,client.getCounty());
                        cmap.put(7,client.getRemark());
                        cmap.put(8,client.getSitutation());
                        row=sheet.createRow((int)i+1);
                        excelUtil.fullCell(row,cmap);
                    }
                }
            }
            ResourceLoader resourceLoader = new DefaultResourceLoader();
            HttpServletResponse response = getTheResponse();
            String path = "/resource/excel/" + fileName + ".xls";
            String realPath = ServletActionContext.getServletContext().getRealPath(path);
            FileOutputStream fops = new FileOutputStream(realPath);
            wb.write(fops);
            fops.close();

            File file = new File(realPath);

            FileManage fileManage = gridFSSave(new FileInputStream(file),fileName+".xls");
            fileManage.setTargetClass(clazz.toString());
            fileManageService.updateAndEnable(fileManage);
            GridFSDBFile gfile = null;
            String gridId = fileManage.getGridId();
            gfile = gridFSGet(fileManageService.get("gridId", gridId).getGridId());
            OutputStream out = null;
            response.setContentType(gfile.getContentType());
            response.setHeader("content-disposition", "attachment;filename="+ new String((fileName+".xls").getBytes(), "ISO8859-1"));
            out = response.getOutputStream();
            gfile.writeTo(out);
          // file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
       // return ajaxHtmlCallback("200", "请进入下载中心下载！", "操作状态");
    }
    public String uploadFile() throws IOException, InvalidFormatException {
        ImportExcel ei =new ImportExcel(Filename,Filedata, 1, 0);
        List<Map<String,Object>> mapList;
        Map<String,Integer> cmap= Maps.newHashMap();
        Map<String,Object> map=null;
        Company company=usersService.getCompanyByUser();
        Linkman linkman=null;
        Client client=null;
        Dict dict=null;
        ProInfo proInfo=null;
        Users users=null;
        if(StringUtils.isNotEmpty(csvname)){
            if("proInfo".equals(csvname)){//项目导入
                cmap.put("name",0);
                cmap.put("type",1);
                cmap.put("chief",2);
                cmap.put("client",3);
                cmap.put("link",4);
                cmap.put("address",5);
                mapList=ei.getValue(ei,cmap);
                for(Map<String,Object> rmap:mapList){
                    Map<String,Object> pmap= Maps.newHashMap();
                    pmap.put("state", StateEnum.Enable);
                    pmap.put("name", rmap.get("name").toString());
                    pager= proInfoService.findByPagerAndCompany(new Pager(),null,company,pmap);
                    if(pager.getList().size()>0){
                        continue;
                    }else{
                        proInfo=new ProInfo();
                    }
                    proInfo.setState(StateEnum.Enable);
                    proInfo.setName(rmap.get("name").toString());
                    proInfo.setAddress(rmap.get("address").toString());

                    map=new HashMap<String,Object>();
                    map.put("name",rmap.get("type").toString());
                    pager=dictService.findByPager(new Pager(), map);
                    if(pager!=null && pager.getList().size()>0){
                        dict= (Dict)pager.getList().get(0);
                    }

                    map.put("name", rmap.get("client").toString());
                    pager=clientService.findByPager(new Pager(),map);
                    if(pager!=null && pager.getList().size()>0){
                        client= (Client)pager.getList().get(0);
                    }

                    map.put("name", rmap.get("link").toString());
                    pager=linkmanService.findByPager(new Pager(),map);
                    if(pager!=null && pager.getList().size()>0){
                        linkman= (Linkman)pager.getList().get(0);
                    }

                    map.put("name", rmap.get("chief").toString());
                    pager=usersService.findByPager(new Pager(),map);
                    if(pager!=null && pager.getList().size()>0){
                        users= (Users)pager.getList().get(0);
                    }

                    if(dict!=null){
                        proInfo.setCategory(dict);
                    }else{
                        dict = new Dict();
                        dict.setState(StateEnum.Enable);
                        dict.setName(rmap.get("type").toString());
                        dict.setCompanyId(company.getId());
                        dictService.save(dict);
                        proInfo.setCategory(dict);
                    }


                    if(client!=null){
                        proInfo.setClient(client);
                    }else{
                        client= new Client();
                        client.setState(StateEnum.Enable);
                        client.setName(rmap.get("client").toString());
                        client.setType(null);
                        client.setCompany(company);
                        clientService.save(client);
                        proInfo.setClient(client);
                    }

                    if(linkman!=null){
                        proInfo.setLinkman(linkman);
                    }else{
                        linkman=new Linkman();
                        linkman.setState(StateEnum.Enable);
                        linkman.setName(rmap.get("link").toString());
                        linkman.setClient(proInfo.getClient());
                        linkman.setCompany(company);
                        linkmanService.save(linkman);
                        proInfo.setLinkman(linkman);
                    }

                    if(users!=null){
                        proInfo.setChief(users);
                    }else{
                        users=new Users();
                        users.setState(StateEnum.Enable);
                        users.setCompany(company);
                        users.setName(rmap.get("chief").toString());
                        usersService.save(users);
                        proInfo.setChief(users);
                    }
                    proInfo.setCreater(usersService.getLoginInfo());
                    proInfo.setCompany(company);
                    proInfoService.save(proInfo);
                }
            }
            if("linkman".equals(csvname)){//联系人导入
                System.out.print("联系人导入");
                cmap.put("name",0);
                cmap.put("client",1);
                cmap.put("subName",2);
                cmap.put("duty",3);
                cmap.put("preferences",4);
                cmap.put("tel",5);
                cmap.put("mobile",6);
                cmap.put("mobile_back",7);
                cmap.put("email",8);
                cmap.put("remark",9);
                mapList=ei.getValue(ei,cmap);
                for(Map<String,Object> rmap:mapList){
                    map= Maps.newHashMap();
                    map.put("name",rmap.get("name").toString());
                    pager=linkmanService.findByPagerAndCompany(pager,null,company,map);
                    if(pager!=null && pager.getList().size()>0){
                        linkman= (Linkman) pager.getList().get(0);
                    }else{
                        linkman=new Linkman();
                    }
                    map.put("name", rmap.get("client").toString());
                    pager=clientService.findByPagerAndCompany(pager,null,company,map);
                    if(pager!=null && pager.getList().size()>0){
                        client= (Client) pager.getList().get(0);
                    }else{
                        client=new Client();
                        client.setState(StateEnum.Enable);
                        client.setCompany(company);
                        client.setCreater(usersService.getLoginInfo());
                        client.setName(rmap.get("client").toString());
                        clientService.save(client);
                    }
                    linkman.setState(StateEnum.Enable);
                    linkman.setCompany(company);
                    linkman.setCreater(usersService.getLoginInfo());
                    linkman.setName(rmap.get("name").toString());
                    linkman.setClient(client);
                    linkman.setSubName(rmap.get("subName").toString());
                    linkman.setDuty(rmap.get("duty").toString());
                    linkman.setPreferences(rmap.get("preferences").toString());
                    linkman.setTel(rmap.get("tel").toString());
                    linkman.setMobile(rmap.get("mobile").toString());
                    linkman.setMobile_back(rmap.get("mobile_back").toString());
                    linkman.setEmail(rmap.get("email").toString());
                    linkman.setRemark(rmap.get("remark").toString());
                    linkmanService.save(linkman);
                }
            }
            if("client".equals(csvname)) {//客户导入
                cmap.put("name", 0);
                cmap.put("subName", 1);
                cmap.put("type", 2);
                cmap.put("invoiceTitle", 3);
                cmap.put("province", 4);
                cmap.put("city", 5);
                cmap.put("county", 6);
                cmap.put("remark", 7);
                cmap.put("situtation", 8);
                mapList = ei.getValue(ei, cmap);
                for (Map<String, Object> rmap : mapList) {
                    map = Maps.newHashMap();
                    map.put("state", StateEnum.Enable);
                    map.put("name", rmap.get("name").toString());
                    if (StringUtils.equals(rmap.get("name").toString(), "") || clientService.findByPager(new Pager(), map).getList().size() > 0) {
                        continue;
                    } else {
                        client = new Client();
                    }
                    client.setState(StateEnum.Enable);
                    client.setName(rmap.get("name").toString());
                    client.setCompany(company);
                    client.setSubName(rmap.get("subName").toString());
                    client.setCity(rmap.get("city").toString());
                    client.setProvince(rmap.get("province").toString());
                    client.setCounty(rmap.get("county").toString());
                    client.setInvoiceTitle(rmap.get("invoiceTitle").toString());
                    client.setRemark(rmap.get("remark").toString());
                    client.setSitutation(rmap.get("situtation").toString());
                    client.setNo(authCodeService.createAuthCode("client"));

                    map.put("name", rmap.get("type").toString());
                    pager = dictService.findByPager(new Pager(), map);
                    if (pager != null && pager.getList().size() > 0) {
                        dict = (Dict) pager.getList().get(0);
                    }
                    if (dict == null) {
                        dict = new Dict();
                        dict.setState(StateEnum.Enable);
                        dict.setName(rmap.get("type").toString());
                        dict.setCompanyId(company.getId());
                        dictService.save(dict);
                    }
                    client.setType(dict);
                    //clientService.save(client);
                    if (StringUtils.isNotEmpty(client.getId())) {
                        clientService.update(client);
                    } else {
                        clientService.save(client);
                    }
                    clientService.save(client);
                }
            }

            return  ajaxHtmlCallback("200","上传成功","操作状态");
        }
        return null;
    }

    public String list(){
        return "list";
    }

    public String dialog(){
        return "dialog";
    }

    public String getCsvname() {
        return csvname;
    }

    public void setCsvname(String csvname) {
        this.csvname = csvname;
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

    public String getIdList() {
        return idList;
    }

    public void setIdList(String idList) {
        this.idList = idList;
    }
}
