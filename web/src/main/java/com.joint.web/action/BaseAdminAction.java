package com.joint.web.action;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Pager;
import com.fz.us.base.bean.Result;
import com.joint.base.entity.Company;
import com.joint.base.entity.FileManage;
import com.joint.base.entity.Users;
import com.joint.base.service.FileManageService;
import com.joint.base.service.LocationsService;
import com.joint.base.service.UsersService;
import com.joint.base.service.activiti.WorkflowService;
import com.joint.base.util.DataUtil;
import com.joint.core.service.EcPushLogService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 后台Action类 - 管理中心基类
 * ============================================================================
 * 版权所有 2013	qshihua
 * ----------------------------------------------------------------------------
 * 
 * @author hpj
 * 
 * @version 0.1 2013-3-1
 */
@Scope("prototype")
public class  BaseAdminAction extends ActionSupport {
	private static final long serialVersionUID = 6718838822334455667L;
	protected static final int mpManager = 0; //0->cookie 1->session
	
	protected final static int LISTVIEW_WEB_SIZE = 30;
	protected final static int LISTVIEW_MP_SIZE = 10;
	
	protected final static int LISTVIEW_DATATYPE_CLINET = 0x01;
	protected final static int LISTVIEW_DATATYPE_LINKMAN = 0x02;
	protected final static int LISTVIEW_DATATYPE_NEGOTIATION = 0x03;
	protected final static int LISTVIEW_DATATYPE_BUSINESS = 0x04;
	protected final static int LISTVIEW_DATATYPE_CONTENT = 0x05;
	protected final static int LISTVIEW_DATATYPE_USER= 0x06;
	protected final static int LISTVIEW_DATATYPE_PRODUCT= 0x07;
	protected final static int LISTVIEW_DATATYPE_RESERVATION= 0x08;
	protected final static int LISTVIEW_DATATYPE_PERFORMANCE= 0x09;
	protected final static int LISTVIEW_DATATYPE_CATEGORY_PRODUCT = 0x10;
	
	protected final static int LISTVIEW_DATATYPE_CLINET_LINKMAN = 0x11;
	protected final static int LISTVIEW_DATATYPE_CLINET_NEGOTIATION = 0x12;
	protected final static int LISTVIEW_DATATYPE_NEGOTIATION_BUSINESS = 0x13;
	protected final static int LISTVIEW_DATATYPE_BUSINESS_PRODUCT = 0x14;
	protected final static int LISTVIEW_DATATYPE_BUSINESS_CONTRACT = 0x15;
	protected final static int LISTVIEW_DATATYPE_NEGOTIATION_LINKMAN = 0x16;
	protected final static int LISTVIEW_DATATYPE_NEGOTIATION_USER = 0x17;
	protected final static int LISTVIEW_DATATYPE_CLINET_RESERVATION= 0x18;
	protected final static int LISTVIEW_DATATYPE_CONTRACT_PERFORMANCE= 0x19;
	protected final static int LISTVIEW_DATATYPE_BUSINESS_NEGOTIATION = 0x20;
	protected final static int LISTVIEW_DATATYPE_CLINET_BUSINESS = 0x21;
	protected final static int LISTVIEW_DATATYPE_USER_BOSS = 0x22;
	
	protected final static int LISTVIEW_DATATYPE_RESERVATION_BYTIME= 0x31;
	protected final static int LISTVIEW_DATATYPE_NEGOTIATION_BYTIME = 0x32;
	
	protected final static int LISTVIEW_DATATYPE_PUSHLOG_MESSAGE = 0x61;

	protected final static int LISTVIEW_DATATYPE_COMMENT_TARGET = 0x71;
	protected final static int LISTVIEW_DATATYPE_COMMENT_ABOUTME = 0x72;
	protected final static int LISTVIEW_DATATYPE_COMMENT_USER = 0x73;
	
	public static final String VIEW = "view";
	public static final String LIST = "list";
	public static final String STATUS = "status";
	public static final String WARN = "warn";
	public static final String SUCCESS = "success";
	public static final String ERROR = "error";
	public static final String MESSAGE = "message";
	public static final String CONTENT = "content";
	public static final String IMPORT = "import";
	public static final String INPUT = "input";
	
	protected String result;
	protected String id;
	protected String keyId;
	protected String tempId;
	protected String timestamp;
	protected String keyIds;
	protected String keyNames;
	protected String[] ids;
	protected Pager pager;
	protected String logInfo;// 日志记录信息
	protected String redirectionUrl;// 操作提示后的跳转URL,为null则返回前一页
	
	/*常用对象*/
	protected String view;//视图名
	protected String cat;//关键字
	protected int page;//页数
	protected int rows;//Size
	protected long total = 0;
	protected String stringData;//Size
	protected String filters;
	protected String order;//desc asc
	protected String sort;//desc asc

    /*jqGrid*/
    protected String sidx;
    protected String _search;
    protected String nd;
    protected String sord;
	
	protected int pageIndex;
	protected int pageSize;
	protected String pageProperty;
	protected String pageKeyword;

	protected int catalog;

	//当前项目ID
	protected String current_ProjectId;
	
	//提交还是保存的状态 空或者null 则为save submit 判断不等于submit
	protected String saveoption;
	
	/* Edit 对象*/
	protected String oper;
	protected String parent;//父ID	// 最后登录异常Session名称
	/* 通用树节点名 对象*/
	protected String nodeName;
	
	protected static final String EDIT = "edit";
	protected static final String ADD = "add";
	protected static final String DEL = "del";
	
	//上传功能
	private String root;
	private String lastName;
	protected static final String fileDirName = "SmartSales";
	protected SimpleDateFormat sdf;
	protected Calendar calendar;

	//校验
	protected String newValue;
	protected String oldValue;
	protected String entityName;
	protected String validatePro;

    //微信通用参数
    protected String code;
    protected String state;

	
	protected String _;//实时刷新
    @Resource
    protected WorkflowService workflowService;
    @Resource
    protected UsersService usersService;
    @Resource
	protected GridFsTemplate gridOperations;
    @Resource
	protected FileManageService fileManageService;
    @Resource
    protected LocationsService locationsService;
    @Resource
    protected EcPushLogService ecPushLogService;
	
	public String formatTime(Date time){
		return DataUtil.friendly_time(time);
	}
	//格式化数字显示
	public String formatMoney(BigDecimal m){
		if(m==null){
			return "0.000,000";
		}else if(m.doubleValue() == 0){
			return "0.000,000";
		}else{
			return String.format("%,.6f",m);
		}
	}
	//获取省略掉的内容，最长12个字符
	public String getShort(String content){
		return content.length() >12?(content.substring(0,9)+"..."):content;
	}
	public boolean isSubmit(String saveoption){
		return (!StringUtils.isEmpty(saveoption))&&(StringUtils.equals("submit", saveoption));
	}

	public String validateUser(Set<Users> entitySet){
		String str = "";
		for(Users n : entitySet){
			if(!n.getState().equals(BaseEnum.StateEnum.Enable)) continue;
			str = str + n.getName()+",";
		}
		str = str.substring(0,str.length()-1);
		return str;
	}

	/**
	 * 获取文件Entity
	 * @param fileId
	 * @return
	 */
	public String getFileManagerName(String fileId){
		FileManage fileManage = fileManageService.get(fileId);
		return fileManage.getName();
	}
	/**
	 * 获取文件路径
	 * @param keyId
	 * @return
	 */
    public String getFile(String keyId){
        List<FileManage> fileManageList = fileManageService.getFileByKeyId(keyId);
        if(fileManageList.size() >0){
            FileManage fileManage = fileManageList.get(0);
            String  filePath = fileManage.getUrl();
            return filePath;
        }else {
            return "";
        }
    }

	/**
     * Cookieの追加
     * @return
     * @throws Exception
     */
	public void addCookie(String name,String value){
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(60*60*24*365);
        ServletActionContext.getResponse().addCookie(cookie);
    }
    /**
     * Cookieの、删除
     * @return
     * @throws Exception
     */
	public void deleteCookie(String name){
        HttpServletRequest request = ServletActionContext.getRequest();
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
        	for(Cookie cookie : cookies)
            {
        		//System.out.println("cookie.getName():"+cookie.getName() + " name:" + name);
                if(cookie.getName().equals(name))
                {
                	cookie.setMaxAge(0);
                	cookie.setValue("");
                	cookie.setPath("/");
                	ServletActionContext.getResponse().addCookie(cookie);
                    //return cookie.getValue();
                }
            }
        }
    }
	/**
     * Cookieの取得
     * @return
     * @throws Exception
     */
	public String getCookie(String name){
        HttpServletRequest request = ServletActionContext.getRequest();
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
        	for(Cookie cookie : cookies)
            {
                if(cookie.getName().equals(name))
                {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
	
	/*
	 * state = 200 成功，300必须提示，有警告信息存在，400失败
	 * */
	public String ajaxCallback(String state,String message){
		String json = "{\"state\":\""+state+"\",\"message\":\""+message+"\"}";
		return ajaxJson(json);
	}
	public String ajaxCallback(String state,String message,String title){
		String json = "{\"state\":\""+state+"\",\"message\":\""+message+"\",\"title\":\""+title+"\"}";
		return ajaxJson(json);
	}
	public String ajaxHtmlCallback(String state,String message){
		String json = "{\"state\":\""+state+"\",\"message\":\""+message+"\"}";
		return ajaxHtml(json);
	}
	public String ajaxHtmlCallback(String state,String message,String title){
		String json = "{\"state\":\""+state+"\",\"message\":\""+message+"\",\"title\":\""+title+"\"}";
		return ajaxHtml(json);
	}
	

    /**
     * 返回App Json数据/结果
     * @param errorCode 成功 = 1
     * @param errorMessage 错误消息
     * @param object  JsonObject数据
     * @return
     */
    public String ajaxHtmlAppResult(int errorCode, String errorMessage, JSONObject object ){
        //{"result":{"errorCode":"","errorMessage":""},"data":""}
        Map<String, Object> mpMap = new HashMap<String, Object>();

        Map<String, Object> rsMap = new HashMap<String, Object>();
        rsMap.put("errorCode", errorCode);
        rsMap.put("errorMessage", errorMessage);

        mpMap.put("result", JSONObject.fromObject(rsMap));
        mpMap.put("data", object==null?"":object);

        return ajaxJson(JSONObject.fromObject(mpMap).toString());
    }

	public String ajaxHtmlAppResult(Result result){
		return ajaxHtmlAppResult(result.getState(), StringUtils.isNotEmpty(result.getMessage()) ? result.getMessage() : "", result.getData() != null ? JSONObject.fromObject(result.getData()) : null);
	}
    // 获取Attribute
	public Object getAttribute(String name) {
		return ServletActionContext.getRequest().getAttribute(name);
	}

	// 设置Attribute
	public void setAttribute(String name, Object value) {
		ServletActionContext.getRequest().setAttribute(name, value);
	}

	// 获取Parameter
	public String getParameter(String name) {
		return getRequest().getParameter(name);
	}

	// 获取Parameter数组
	public String[] getParameterValues(String name) {
		return getRequest().getParameterValues(name);
	}

	// 获取Session
	public Object getSession(String name) {
		ActionContext actionContext = ActionContext.getContext();
		Map<String, Object> session = actionContext.getSession();
		return session.get(name);
	}

	// 获取Session
	public Map<String, Object> getSession() {
		ActionContext actionContext = ActionContext.getContext();
		Map<String, Object> session = actionContext.getSession();
		return session;
	}

	// 设置Session
	public void setSession(String name, Object value) {
		ActionContext actionContext = ActionContext.getContext();
		Map<String, Object> session = actionContext.getSession();
		session.put(name, value);
	}

	// 获取Request
	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	// 获取Response
	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	// 获取Application
	public ServletContext getApplication() {
		return ServletActionContext.getServletContext();
	}

	// AJAX输出，返回null
	public String ajax(String content, String type) {
		try {
			HttpServletResponse response = ServletActionContext.getResponse();
			response.setContentType(type + ";charset=UTF-8");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.getWriter().write(content);
			response.getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
    public String ajaxResult(Result result){
        return ajaxHtmlAppResult(result.getState(), StringUtils.isNotEmpty(result.getMessage())?result.getMessage():"", result.getData()!=null? JSONObject.fromObject(result.getData()):null);
    }

	// AJAX输出文本，返回null
	public String ajaxText(String text) {
		return ajax(text, "text/plain");
	}

	// AJAX输出HTML，返回null
	public String ajaxHtml(String html) {
		return ajax(html, "text/html");
	}

	// AJAX输出XML，返回null
	public String ajaxXml(String xml) {
		return ajax(xml, "text/xml");
	}

	// 根据字符串输出JSON，返回null
	public String ajaxJson(String jsonString) {
		return ajax(jsonString, "application/json");
	}
	
	// 根据Map输出JSON，返回null
	public String ajaxJson(Map<String, String> jsonMap) {
		JSONObject jsonObject = JSONObject.fromObject(jsonMap);
		return ajax(jsonObject.toString(), "application/json");
	}
	
	// 输出JSON警告消息，返回null
	public String ajaxJsonWarnMessage(String message) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put(STATUS, WARN);
		jsonMap.put(MESSAGE, message);
		JSONObject jsonObject = JSONObject.fromObject(jsonMap);
		return ajax(jsonObject.toString(), "text/json");
	}
	
	// 输出JSON成功消息，返回null
	public String ajaxJsonSuccessMessage(String message) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put(STATUS, SUCCESS);
		jsonMap.put(MESSAGE, message);
		JSONObject jsonObject = JSONObject.fromObject(jsonMap);
		return ajax(jsonObject.toString(), "text/html");
	}
	
	// 输出JSON错误消息，返回null
	public String ajaxJsonErrorMessage(String message) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		jsonMap.put(STATUS, ERROR);
		jsonMap.put(MESSAGE, message);
		JSONObject jsonObject = JSONObject.fromObject(jsonMap);
		return ajax(jsonObject.toString(), "text/html");
	}

	/**
	 * 保存附件到mongodb
	 * @param inputStream
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public FileManage gridFSSave(InputStream inputStream,String filename) throws IOException {
		DBObject metaData = new BasicDBObject();
		metaData.put("filename", filename);

		//GridFSFile file = gridOperations.store(inputStream, filename, metaData);
		GridFSFile file = gridOperations.store(inputStream, filename,metaData);
		String gridId = file.getId().toString();

		FileManage fileManage = new FileManage();
		fileManage.setUrl("file.action?gridId=" + gridId);
		fileManage.setGridId(gridId);
		fileManage.setName(filename);
		fileManage.setState(BaseEnum.StateEnum.Enable);
        fileManage.setCompany(usersService.getCompanyByUser());
		fileManageService.save(fileManage);

		metaData.put("fileId", fileManage.getId());
		file.save();

		return fileManage;
	}

	/**
	 * mongodb
	 *
	 * @throws IOException
	 */
	public FileManage gridFSSave(InputStream inputStream, String filename, String keyId) throws IOException {
		DBObject metaData = new BasicDBObject();
		metaData.put("filename", filename);

		//GridFSFile file = gridOperations.store(inputStream, filename, metaData);
		GridFSFile file = gridOperations.store(inputStream, filename, metaData);
		String gridId = file.getId().toString();

		FileManage fileManage = new FileManage();
		fileManage.setUrl("file.action?keyId=" + keyId);
		fileManage.setKeyId(keyId);
		fileManage.setGridId(gridId);
		fileManage.setName(filename);
		fileManage.setState(BaseEnum.StateEnum.Enable);
		fileManageService.save(fileManage);

		metaData.put("fileId", fileManage.getId());
		file.save();

		return fileManage;
	}

    /**
     * mongodb
     * @throws IOException
     * */
    public FileManage gridFSSave(InputStream inputStream,String filename,String keyId,String contentType) throws IOException {
        DBObject metaData = new BasicDBObject();
        metaData.put("filename", filename);
        metaData.put("contentType",contentType);

        //GridFSFile file = gridOperations.store(inputStream, filename, metaData);
        GridFSFile file = gridOperations.store(inputStream, filename,metaData);
        String gridId = file.getId().toString();

        Company company=usersService.getCompanyByUser();
        FileManage fileManage = new FileManage();
        if(StringUtils.isNotEmpty(keyId)){
            fileManage.setUrl("file.action?keyId="+keyId);
        }else {
            fileManage.setUrl("file.action?gridId="+gridId);
        }

        fileManage.setKeyId(usersService.getLoginInfo().getId());
        fileManage.setGridId(gridId);
        fileManage.setName(filename);
        fileManage.setState(BaseEnum.StateEnum.Enable);
        fileManage.setCompany(company);
        fileManageService.save(fileManage);

        metaData.put("fileId", fileManage.getId());
        file.save();

        return fileManage;
    }

    public FileManage gridFSSave(InputStream inputStream,String filename,String keyId,String contentType,Class targetclass) throws IOException {
        DBObject metaData = new BasicDBObject();
        metaData.put("filename", filename);
        metaData.put("contentType",contentType);

        //GridFSFile file = gridOperations.store(inputStream, filename, metaData);
        GridFSFile file = gridOperations.store(inputStream, filename,metaData);
        String gridId = file.getId().toString();

        Company company=usersService.getCompanyByUser();
        FileManage fileManage = new FileManage();
        if(StringUtils.isNotEmpty(keyId)){
            fileManage.setUrl("file.action?keyId="+keyId+"&targetClass="+targetclass);
            fileManage.setKeyId(keyId);
        }else {
            fileManage.setUrl("file.action?gridId="+gridId+"&targetClass="+targetclass);
        }
        fileManage.setGridId(gridId);
        fileManage.setName(filename);
        fileManage.setState(BaseEnum.StateEnum.Enable);
        fileManage.setCompany(company);
        fileManage.setTargetClass(targetclass.toString());
        fileManageService.save(fileManage);

        metaData.put("fileId", fileManage.getId());
        file.save();

        return fileManage;
    }


	/**
	 * 保存附件到mongodb
	 * @param inputStream
	 * @param filename
	 * @param keyId -- entityID
	 * @param taskId -- 节点Id
	 * @param proInstanceId -- 实例Id
	 * @return
	 * @throws IOException
	 */
	public FileManage gridFSSave(InputStream inputStream,String filename,String keyId,String taskId,String proInstanceId) throws IOException {
		DBObject metaData = new BasicDBObject();
		metaData.put("filename", filename);

		GridFSFile file = gridOperations.store(inputStream, filename,metaData);
		String gridId = file.getId().toString();

		FileManage fileManage = new FileManage();
		fileManage.setUrl("file.action?keyId="+gridId);
		fileManage.setTaskId(taskId);
		fileManage.setProIntanceId(proInstanceId);
		fileManage.setKeyId(keyId);
		fileManage.setGridId(gridId);
		fileManage.setName(filename);
		fileManage.setState(BaseEnum.StateEnum.Enable);
		fileManageService.save(fileManage);

		metaData.put("fileId", fileManage.getId());
		file.save();

		return fileManage;
	}

	public FileManage gridFSSave(InputStream inputStream,String filename,String keyId,String taskId,String proInstanceId,String type) throws IOException {
		DBObject metaData = new BasicDBObject();
		metaData.put("filename", filename);

		GridFSFile file = gridOperations.store(inputStream, filename,metaData);
		String gridId = file.getId().toString();

		FileManage fileManage = new FileManage(filename,gridId,keyId,proInstanceId,taskId,type);
		fileManageService.save(fileManage);

		metaData.put("fileId", fileManage.getId());
		file.save();

		return fileManage;
	}
    public GridFSDBFile gridFSGet(String id) {
        return gridOperations.findOne(new Query(Criteria.where("_id").is(new ObjectId(id))));
    }


	//上传文件
	/**
	 * 上传文件
	 * 文件路径 = root = upload(myFile, myFileFileName,fileDirName);
	 * (fileDirName 在Action中定义为常量，对象下文件存放文件夹 名称)
	 * 详细 参考 LaborPlanAction 及 人工计划 页面
	 *
	 * @param file
	 *            上传文件对象
	 * @param fileName
	 *            上传文件对象名（用于前天显示文件名，存放时文件重命名为随机数）
	 *
	 * @return String
	 * 			      保存文件绝对路径
	 */
	public String upload(File file,String fileName,String dirName){
		InputStream is = null;
		OutputStream os = null;
		try{
			//取文件的后缀名
			File dirFile;
		    lastName=null;
		    int t=fileName.lastIndexOf(".");
		    lastName=fileName.substring(t+1);
		    //取得当前日期
		    sdf=new SimpleDateFormat("yyyy-MM-dd");
		    calendar=Calendar.getInstance();
		    String date=sdf.format(calendar.getTime());
		    //10亿随机数
		    Random r=new Random();
		    String random=String.valueOf(r.nextInt(1000000000));
		    fileName=date+random+"."+lastName;
			//先把文件上传到服务器file文件夹上
			//root=ServletActionContext.getServletContext().getRealPath("/upload/file");
			root = ServletActionContext.getServletContext().getRealPath("");
			//ckc文件存放文件夹路径
			root = root.substring(0, root.indexOf('\\')+1) + fileDirName;
			dirFile = new File(root);
			if(!dirFile.exists()){
				dirFile.mkdir();
			}
			//指定表单文件存放文件夹路径
			root = root + "\\" + dirName;
			dirFile = new File(root);
			if(!dirFile.exists()){
				dirFile.mkdir();
			}
			//得到传过来的文件对象
			is=new FileInputStream(file);
			//输出到指定的路径中
			File destFile=new File(root,fileName);
			os=new FileOutputStream(destFile);
			byte[] buffer=new byte[1024];
			int len=0;
			while((len=is.read(buffer))>0){
				os.write(buffer,0,len);
			}
			return root+"\\"+fileName;
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(is!=null){
					is.close();
				}
				if(os!=null){
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
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

    /**
     * 根据查询项加载JqGrid里的数据
     * @param _search
     * @param params
     * @param filters
     * @return
     */
    public Map getSearchFilterParams(String _search,Map<String,Object> params,String filters){
        //createAlias Key集合
        List<String> alias = new ArrayList<String>();
        if(StringUtils.isNotEmpty(_search)){
            boolean __search = Boolean.parseBoolean(_search);
            if(__search&& StringUtils.isNotEmpty(filters)){
                JSONObject filtersJson = JSONObject.fromObject(filters);
                if(StringUtils.equals(filtersJson.getString("groupOp"), "AND")){
                    //查询条件
                    JSONArray rulesJsons = filtersJson.getJSONArray("rules");
                    //遍历条件
                    for(int i=0;i<rulesJsons.size();i++){
                        JSONObject rule = rulesJsons.getJSONObject(i);
                        if(rule.has("field")&& StringUtils.isNotEmpty(rule.getString("field"))&&rule.has("data")&& StringUtils.isNotEmpty(rule.getString("data"))){
                            String field = rule.getString("field");
                            if(field.contains("_")){
                                String[] fields = field.split("_");
                                if(StringUtils.isNotEmpty(rule.getString("data"))){
                                    params.put(field.replaceAll("_", "."), rule.getString("data"));
                                }
                            }else{
                                if(StringUtils.isNotEmpty(rule.getString("data"))){
                                    params.put(field, rule.getString("data"));
                                }
                            }
                        }
                    }

                }
            }
        }

        return params;
    }

    protected String getContentType(String fileName) {
		 String fileNameTmp = fileName.toLowerCase();
		 String ret = "";
		 if (fileNameTmp.endsWith("txt")) {
			 ret = "text/plain";
		 }else if (fileNameTmp.endsWith("gif")) {
			 ret = "image/gif"; 
		 }else if (fileNameTmp.endsWith("jpg")) {
			 ret = "image/jpeg";   
		 }else if (fileNameTmp.endsWith("jpeg")) {
			 ret = "image/jpeg";
		 }else if (fileNameTmp.endsWith("jpe")) {
			 ret = "image/jpeg";
		 }else if (fileNameTmp.endsWith("zip")) {
			 ret = "application/zip";
		 }else if (fileNameTmp.endsWith("rar")) {
			 ret = "application/rar";
		 }else if (fileNameTmp.endsWith("doc")) {
			 ret = "application/msword";
		 }else if (fileNameTmp.endsWith("ppt")) {
			 ret = "application/vnd.ms-powerpoint";
		 }else if (fileNameTmp.endsWith("xls")) {
			 ret = "application/vnd.ms-excel";
		 }else if (fileNameTmp.endsWith("html")) {
			 ret = "text/html";
		 }else if (fileNameTmp.endsWith("htm")) {
			 ret = "text/html";
		 }else if (fileNameTmp.endsWith("tif")) {
			 ret = "image/tiff";
		 }else if (fileNameTmp.endsWith("tiff")) {
			 ret = "image/tiff";
		 }else if (fileNameTmp.endsWith("pdf")) {
			 ret = "application/pdf";
		 }else if (fileNameTmp.endsWith("rtf")) {
			 ret = "application/rtf";
		 }else if (fileNameTmp.endsWith("asf")) {
			 ret = "video/x-ms-asf";
		 }else if (fileNameTmp.endsWith("avi")) {
			 ret = "video/avi";
		 }else if (fileNameTmp.endsWith("wav")) {
			 ret = "audio/wav";
		 }else if (fileNameTmp.endsWith("mp3")) {
			 ret = "audio/mpeg3";
		 }else{
			 ret = "APPLICATION/OCTET-STREAM";
		 }
		 return ret;
	 }
	// 设置页面不缓存
	public void setResponseNoCache() {
		getResponse().setHeader("progma", "no-cache");
		getResponse().setHeader("Cache-Control", "no-cache");
		getResponse().setHeader("Cache-Control", "no-store");
		getResponse().setDateHeader("Expires", 0);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}

	public String getRedirectionUrl() {
		return redirectionUrl;
	}

	public void setRedirectionUrl(String redirectionUrl) {
		this.redirectionUrl = redirectionUrl;
	}
	public String getView() {
		return view;
	}
	public void setView(String view) {
		this.view = view;
	}
	public String getCat() {
		return cat;
	}
	public void setCat(String cat) {
		this.cat = cat;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public String getStringData() {
		return stringData;
	}
	public void setStringData(String stringData) {
		this.stringData = stringData;
	}
	public String getOper() {
		return oper;
	}
	public void setOper(String oper) {
		this.oper = oper;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	public String getCurrent_ProjectId() {
		return current_ProjectId;
	}
	public void setCurrent_ProjectId(String current_ProjectId) {
		this.current_ProjectId = current_ProjectId;
	}
	public String getSaveoption() {
		return saveoption;
	}
	public void setSaveoption(String saveoption) {
		this.saveoption = saveoption;
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getKeyIds() {
		return keyIds;
	}

	public void setKeyIds(String keyIds) {
		this.keyIds = keyIds;
	}

	public String getKeyNames() {
		return keyNames;
	}

	public void setKeyNames(String keyNames) {
		this.keyNames = keyNames;
	}

	public String get_() {
		return _;
	}

	public void set_(String _) {
		this._ = _;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCatalog() {
		return catalog;
	}

	public void setCatalog(int catalog) {
		this.catalog = catalog;
	}

	public String getPageProperty() {
		return pageProperty;
	}

	public void setPageProperty(String pageProperty) {
		this.pageProperty = pageProperty;
	}

	public String getPageKeyword() {
		return pageKeyword;
	}

	public void setPageKeyword(String pageKeyword) {
		this.pageKeyword = pageKeyword;
	}

	public String getTempId() {
		return tempId;
	}
	
	public void setTempId(String tempId) {
		this.tempId = tempId;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public String get_search() {
        return _search;
    }

    public void set_search(String _search) {
        this._search = _search;
    }

    public String getNd() {
        return nd;
    }

    public void setNd(String nd) {
        this.nd = nd;
    }

    public String getSord() {
        return sord;
    }

    public void setSord(String sord) {
        this.sord = sord;
    }

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getValidatePro() {
		return validatePro;
	}

	public void setValidatePro(String validatePro) {
		this.validatePro = validatePro;
	}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
