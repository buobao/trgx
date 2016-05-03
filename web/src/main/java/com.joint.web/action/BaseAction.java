package com.joint.web.action;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Result;
import com.fz.us.base.service.common.ResultService;
import com.fz.us.base.util.LogUtil;
import com.fz.us.base.util.mapper.JsonMapper;
import com.fz.us.dict.service.DictService;
import com.joint.base.entity.Company;
import com.joint.base.entity.FileManage;
import com.joint.base.entity.Users;
import com.joint.base.entity.system.Admin;
import com.joint.web.exception.CurrentUserNotFoundException;
import com.joint.web.shiro.Constants;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSFile;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 后台Action类 - Action基类
 * ============================================================================
 * 版权所有 2014 。
 *
 * @author min_xu
 *
 * @version 0.1 2014-01-20
 * @version 0.2 2014-07-25 update by fallenpanda
 * ============================================================================
 */

@Scope("prototype")
public class BaseAction extends BaseAdminAction {

	protected static JsonMapper binder = JsonMapper.nonEmptyMapper();

	protected static final String APP_KEYWORDS = "";

    @Resource
    private DictService dictService;
    @Resource
    protected ResultService resultService;

	//用于App权限访问
	protected String u;
	protected String digest;
	public String getU() {
		return u;
	}

	public void setU(String u) {
		this.u = u;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}


	protected Users users;
	protected Admin admin;
	protected Company company;
	public Admin getAdmin() {
		return admin;
	}
	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
	public Users getUsers() {
		return users;
	}
	public void setUsers(Users users) {
		this.users = users;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}

    /**
     * 得到Shiro中的人员
     * */
    public Admin getRequestAdmin(){
        return (Admin)getRequest().getAttribute(Constants.CURRENT_USER);
    }

    public Users getRequestUsers() {
        Admin admin = (Admin)getRequest().getAttribute(Constants.CURRENT_USER);
        if(admin != null){
            Users users =  usersService.getLoginInfo();
            if(users == null){
                sendError(getRequest(),getResponse(), HttpServletResponse.SC_UNAUTHORIZED,new CurrentUserNotFoundException("登录账户异常，找不到用户"));
            }else{
                return users;
            }
        }else{
            sendError(getRequest(),getResponse(), HttpServletResponse.SC_UNAUTHORIZED,new CurrentUserNotFoundException("登录账户异常，找不到用户"));
        }
        return null;
    }

	public void sendError(HttpServletRequest request, HttpServletResponse response,int code, Exception e){
		response.setContentType("text/plain;charset=UTF-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setStatus(code);
		try {
			response.getWriter().write(e.getMessage());
			response.getWriter().flush();
		} catch (IOException e1) {
			LogUtil.error(e1.getMessage(), e1);
		}
	}
	//<s:property value="dictName(client.type.id,client.type.name)" />
    //系统字典名称显示函数
    public String dictMpName(String dictId){
        company = getRequestUsers().getCompany();
        return dictService.getDefinedName(dictId, company.getId());
    }
	public String dictMpName(String dictId,String name){
		company = getRequestUsers().getCompany();
		return dictService.getDefinedName(dictId, company.getId());
	}
    public String dictComName(String dictId,String dictName){
        company = getRequestUsers().getCompany();
        return dictService.getDefinedName(dictId, company.getId());
    }
    public String dictAppName(String dictId,String dictName,String companyId){
        company = getRequestUsers().getCompany();
        return dictService.getDefinedName(dictId, company.getId());
    }

	public String formatBr(String text){
		if(StringUtils.isEmpty(text)){
			return "";
		}
		return text.replaceAll("\r\n","<br />");
	}

	public String formatAppBr(String text){
		return text;
	}

    /**
     * 打印Request信息
     */
    protected void printRequestInfo(){
        HttpServletRequest request = getRequest();
        HttpSession httpSession = request.getSession();

        System.out.println("--------------------------------------------------------");
        System.out.println("SessionId:		" + httpSession.getId());
        System.out.println("Host:				" + request.getHeader("Host"));
        System.out.println("Connection:	" + request.getHeader("Connection"));
        System.out.println("Cookie:			" + request.getHeader("Cookie"));
        System.out.println("User:				" + request.getHeader("User-Agent"));
        System.out.println("uid:				" + request.getParameter("uid"));
        System.out.println("--------------------------------------------------------");
    }

	public String ajaxAppResult(int errorCode, String errorMessage, Map object ){
		Map<String, Object> mpMap = new HashMap<String, Object>();
		Map<String, Object> rsMap = new HashMap<String, Object>();
		rsMap.put("errorCode", errorCode);
		rsMap.put("errorMessage", errorMessage);

		mpMap.put("result", rsMap);
		mpMap.put("data", object==null?"":object);
		return ajaxJson(binder.toJson(mpMap));
	}

	/**
	 * 转换来自于Service的保存和更新的简单result
	 * */
	public String mpResult(Result result){
		if(result.getState() == 1){
			if(result.getData() == null && StringUtils.isNotEmpty(result.getMessage())){
				return ajaxResult(resultService.successWithId(result.getMessage()));
			}else if(result.getData() != null){
				Map data = result.getData();
				data.put("id",result.getMessage());
				return ajaxResult(resultService.build(1,0,"操作成功",data));
			}
		}
		return ajaxResult(result);
	}

    /**
     * 返回App Json数据/结果
     * @param errorCode 成功 = 1
     * @param errorMessage 错误消息
     * @param jsonArray  JsonArray数据
     * @return
     */
    public String ajaxHtmlAppJSONArrayResult(int errorCode, String errorMessage, JSONArray jsonArray ){
        //{"result":{"errorCode":"","errorMessage":""},"data":""}
        Map<String, Object> mpMap = new HashMap<String, Object>();

        Map<String, Object> rsMap = new HashMap<String, Object>();
        rsMap.put("errorCode", errorCode);
        rsMap.put("errorMessage", errorMessage);

        mpMap.put("result", JSONObject.fromObject(rsMap));
        mpMap.put("data", jsonArray==null?"":jsonArray);

        return ajaxJson(JSONObject.fromObject(mpMap).toString());
    }




    /**
     * mongodb
     * @throws IOException
     * */
    public FileManage gridFSSave(InputStream inputStream, String contentType, String filename,Company company,Users creater) throws IOException {
        return gridFSSave(null, null, inputStream, contentType, filename, company, creater);
    }

    /**
     * mongodb
     * @throws IOException
     * */
    public FileManage gridFSSave(String targetId, Class<?> targetClass, InputStream inputStream, String contentType, String filename,Company company,Users creater) throws IOException {
        DBObject metaData = new BasicDBObject();
        metaData.put("filename", filename);
        metaData.put("contentType", contentType);
        metaData.put("company", company.getId());

        //GridFSFile file = gridOperations.store(inputStream, filename, metaData);
        GridFSFile file = gridOperations.store(inputStream, filename, contentType,
                metaData);
        String gridId = file.getId().toString();

        FileManage fileManage = new FileManage();
        if(targetClass!=null&& StringUtils.isNotEmpty(targetId)){
            fileManage.setTargetId(targetId);
            fileManage.setTargetClass(targetClass.getName());
        }
        fileManage.setUrl("file.action?keyId="+gridId);
        fileManage.setGridId(gridId);
        fileManage.setName(filename);
        fileManage.setSize(file.getChunkSize());
        fileManage.setCompany(company);
        fileManage.setCreater(creater);
        fileManage.setState(BaseEnum.StateEnum.Enable);
        fileManageService.save(fileManage);

        metaData.put("fileId", fileManage.getId());
        file.save();

        return fileManage;
    }

    protected static OutputStream getTheOutputStream(){
        try {
            return getTheResponse().getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    protected static HttpServletResponse getTheResponse(){
        return ServletActionContext.getResponse();
    }

    public void download(FileManage fileManage){
        //获取所有的参数，去除公共参数
        HttpServletResponse response = getTheResponse();
        FileInputStream fs = null;
        OutputStream out = null;
        BufferedOutputStream bos = null;
        //String filePath = fileManage.getUrl();
        //String fileName = fileManage.getName();
        try {
            fs = new FileInputStream(new File(fileManage.getUrl()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("未找到文件！");
        }
        // 设置响应头和保存文件名
        response.setContentType(getContentType(fileManage.getName()));
        try {
            response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(fileManage.getName().getBytes(), "ISO8859-1") + "\"");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 写出流信息
        int b = 0;
        byte buffer[] = new byte[1024];
        try {
            out = getTheOutputStream();
//			bos = new BufferedOutputStream(out);
            while ((b = fs.read(buffer)) != -1) {
                out.write(buffer,0,b);
//				bos.write(buffer,0,b);
            }
            //System.out.println("文件下载完毕。");
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("下载文件失败!");
        }finally{
            try {
                if(fs!=null){
                    fs.close();
                }
                if(out!=null){
                    out.flush();
                    out.close();
                }
                if(bos!=null){
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public long getTotal() {
        return total;
    }
    public void setTotal(long total) {
        this.total = total;
    }
    public String is_search() {
        return _search;
    }
}
