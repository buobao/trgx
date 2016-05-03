package com.joint.base.service.impl;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Pager;
import com.fz.us.base.bean.Result;
import com.fz.us.base.util.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.joint.base.bean.EnumManage;
import com.joint.base.bean.SystemConfig;
import com.joint.base.dao.BaseEntityDao;
import com.joint.base.dao.UsersDao;
import com.joint.base.entity.*;
import com.joint.base.entity.system.Admin;
import com.joint.base.mp.WxMpInCacheConfigStorage;
import com.joint.base.service.*;
import com.joint.base.util.security.Digests;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service实现类 - 用户信息
 * ============================================================================
 * 版权所有 2013 qshihua。
 * ----------------------------------------------------------------------------
 * 
 * @author qshihua
 * 
 * @version 0.1 2013-05-06
 */

@Service
public class UsersServiceImpl extends BaseEntityServiceImpl<Users, String> implements UsersService {

	private static Logger log = LoggerFactory.getLogger(UsersServiceImpl.class);

    @Resource
	private UsersDao usersDao;
    @Resource
    private AdminService adminService;
    @Resource
    private CompanyService companyService;
    @Resource
    private WxMpInCacheConfigStorage wxMpConfigStorage;
    @Resource
    private SystemConfig systemConfig;
    @Resource
    private AuthCodeService authCodeService;
    @Resource
    private SmsService smsService;
    @Resource
    private DutyService dutyService;
    @Resource
    private FileManageService fileManageService;
    @Resource
    private  DepartmentService departmentService;


    /**
     * 照片头像
     */
    private FileManage headImage;
    private String fileId;

	public static final int HASH_INTERATIONS = 1024;

	@Override
	public BaseEntityDao<Users, String> getBaseEntityDao() {
		return usersDao;
	}

    @Override
    public String salt(Users users){
        return adminService.get(users.getAdminId()).getSalt();
    }

    @Override
    public Map<String, Object> getEasyMap(Users users) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", StringUtils.isNotEmpty(users.getId())?users.getId():"");
        map.put("uid", StringUtils.isNotEmpty(users.getId())?users.getId():"");
        map.put("name", StringUtils.isNotEmpty(users.getName())?users.getName():"");

        return map;
    }

    @Override
    public Map<String, Object> getListItemMap(Users users) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", StringUtils.isNotEmpty(users.getId())?users.getId():"");
        map.put("uid", StringUtils.isNotEmpty(users.getId())?users.getId():"");
        map.put("name", StringUtils.isNotEmpty(users.getName())?users.getName():"");
        map.put("pinYinHead", StringUtils.isNotEmpty(users.getPinYinHead())?users.getPinYinHead():"");
        map.put("pinYin", StringUtils.isNotEmpty(users.getPinYin())?users.getPinYin():"");

        if(users.getCompany()!=null){
            Company company = users.getCompany();
            map.put("companyId", company.getId());
            map.put("companyName", company.getName());
            map.put("companySubName", company.getSubname());
        }else{
            map.put("companyId", "");
            map.put("companyName", "");
            map.put("companySubName", "");
        }
        String departmentId = "";
        String departmentName = "";
        List<Department> listDepartment = dutyService.getDepartments(users,users.getCompany());
        if(listDepartment!=null&&listDepartment.size()>0){
            for(Department d:listDepartment){
                if(StringUtils.isEmpty(departmentId)){
                    departmentId += d.getId();
                    departmentName += d.getName();
                }else{
                    departmentId += "," + d.getId();
                    departmentName += "," + d.getName();
                }
            }
        }
        map.put("departmentId", departmentName);
        map.put("departmentName", departmentName);

        map.put("createDate", DataUtil.DateTimeToString(users.getCreateDate()));
        map.put("modifyDate", DataUtil.DateTimeToString(users.getModifyDate()));
        if(users.getCreater()!=null){
            Users creater = users.getCreater();
            map.put("createrId", creater.getId());
            map.put("createrName", creater.getName());
        }else{
            map.put("createrId", "");
            map.put("createrName", "");
        }

        return map;
    }

    @Override
    public Map<String, Object> getLoginItemMap(Users users) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("aid", users.getAdminId());
        map.put("salt", salt(users));
        map.put("id", StringUtils.isNotEmpty(users.getId())?users.getId():"");
        map.put("name", StringUtils.isNotEmpty(users.getName())?users.getName():"");
        map.put("pinYinHead", StringUtils.isNotEmpty(users.getPinYinHead())?users.getPinYinHead():"");
        map.put("pinYin", StringUtils.isNotEmpty(users.getPinYin())?users.getPinYin():"");

        if(users.getCompany()!=null){
            Company company = users.getCompany();
            map.put("companyId", company.getId());
            map.put("companyName", company.getName());
            map.put("companySubName", company.getSubname());
        }else{
            map.put("companyId", "");
            map.put("companyName", "");
            map.put("companySubName", "");
        }
        String departmentId = "";
        String departmentName = "";
        List<Department> listDepartment = dutyService.getDepartments(users,users.getCompany());
        if(listDepartment!=null&&listDepartment.size()>0){
            for(Department d:listDepartment){
                if(StringUtils.isEmpty(departmentId)){
                    departmentId += d.getId();
                    departmentName += d.getName();
                }else{
                    departmentId += "," + d.getId();
                    departmentName += "," + d.getName();
                }
            }
        }
        map.put("departmentId", departmentName);
        map.put("departmentName", departmentName);
        fileId = users.getFileId();
        if(StringUtils.isNotEmpty(fileId)){
            headImage = fileManageService.get(fileId);
            map.put("portrait", (headImage != null && StringUtils.isNotEmpty(headImage.getGridId())) ? headImage.getGridId() : "");
        }

        map.put("createDate", DataUtil.DateTimeToString(users.getCreateDate()));
        map.put("modifyDate", DataUtil.DateTimeToString(users.getModifyDate()));

        return map;
    }

    @Override
	public Users getUsersByMobile(String mobile) {
		return usersDao.getUsersByMobile(mobile);
	}

    @Override
    public Users getUsersByMobileDel(String mobile) {
        return usersDao.getUsersByMobileDel(mobile);
    }
	
	@Override
	public Users getUsersByEmail(String email) {
		return usersDao.getUsersByEmail(email);
	}

	@Override
	public List<Department> getUsersPrincipal(Users users) {
		return usersDao.getUsersPrincipal(users);
	}

    @Override
    public Users getLoginInfo() {
        Subject subject= SecurityUtils.getSubject();
		String userId = (String) subject.getPrincipal();
        if(userId == null){
            return null;
        }
        Admin admin = adminService.getByMobile(userId);
        Users user = usersDao.getByAdmin(admin);
        return user;
    }
	@Override
	public String encodedPassword(String name,String password,String salt){
		int hashIterations = 2;
		SimpleHash hash = new SimpleHash("md5", password, name + salt, hashIterations);
		return hash.toHex();
	}

	@Override
	public boolean validatePassword(String plainPassword, String password) {
		byte[] salt = Encodes.decodeHex(password.substring(0, 16));
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(Encodes.encodeHex(salt) + Encodes.encodeHex(hashPassword));
	}


    @Override
    public Result registerUserAndAdmin(String name,String password,Company company){
        //创建Admin
        Result result = adminService.registe(name, password, EnumManage.AccountType.User);
        if(result.getState() == 0){
            return result;
        }
        return registerUser(name, company, result.getId());
    }

    @Override
    public Result registerUserAndAdmin(Users users, Company company) {
        //创建Admin
        Result result = adminService.registe(users.getMobile(),users.getPassword(), EnumManage.AccountType.User);
        if(result.getState() == 0){
            return result;
        }
        users.setAdminId(result.getId());
        users.setCompany(company);
        updateNameAndEnable(users);
        return resultService.successWithId(users.getId());
    }


    @Override
    public Result registerCompanyAndUser(String name,String password){
        //创建Admin
        Result result = adminService.registe(name,password, EnumManage.AccountType.Manager);
        if(result.getState() == 0){
            return result;
        }
        //创建Company
        String adminId = result.getId();
        Company company = new Company(name,adminId);
        companyService.updateNameAndEnable(company);

        //创建Users
        return registerUser(name, company, adminId);
    }

    @Override
    public Result registerExperienceUser(String username, Users users) {
        //Company company=companyService.get("name", "ty13916370375");
        Map<String,Object> rmap= Maps.newHashMap();
        rmap.put("state", BaseEnum.StateEnum.Enable);
        rmap.put("name","ty");

        List<Company> companyList= (List<Company>) companyService.findByPagerAndCompany(new Pager(0), null, null, rmap).getList();
        if(companyList==null || companyList.size()==0){
            return resultService.build(0,1,"请先注册体验企业!",null);
        }
        Company company = companyList.get(0);

        Map<String,Object> dmap= Maps.newHashMap();
        dmap.put("state", BaseEnum.StateEnum.Enable);
        dmap.put("company",company);
        List<Department> departmentList= (List<Department>) departmentService.findByPagerAndCompany(new Pager(0),null,null,dmap).getList();

        if(departmentList==null || departmentList.size()==0){
            return resultService.build(0,1,"请先新建体验组!",null);
        }
        Department department = departmentList.get(0);
        Result result = registerUserAndAdmin(users, company);
        Duty duty = new Duty();
        duty.setUsers(users);
        duty.setDepartment(department);
        duty.setDutyDefault(1);
        duty.setDutyState(EnumManage.DutyState.Default);
        duty.setCompany(company);
        dutyService.save(duty);
        return sendAuthCode(username, EnumManage.AuthCodeEnum.register, 0, get(result.getData().get("id").toString()));
    }

    @Override
    public Company getCompanyByUser() {
        Users users = getLoginInfo();
        return users.getCompany();
    }

    @Override
    public Company getCompanyByUser(Users creater) {
        return creater.getCompany();
    }


    /**
     content = "您的登录账号为，"+1+"\n" +
     "绑定/解除绑定账号请点击下面链接：" +
     "<a href=\"http://"+mp_host+"/smartsales/mp/users!account.action?openId="+reqBean.getFromUserName()+"\">账号设置</a>"
     + "";
     * */
    public String getMpAccount(Users users,String openId){
        LogUtil.info(openId);
        if(users==null){
            users = getUsersByOpenId(openId);
        }
        if(users==null){
            return getLoginContent(openId);
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append("[微笑] "+users.getName()).append("，您好：\n");
        buffer.append("您的账号已绑定成功，点击下面链接： ").append("\n\n");
        buffer.append("<a href=\""+wxMpConfigStorage.getHttpHost()+"/"+systemConfig.getWebroot()+"/mp/account!resetMp.action?_="+ CommonUtil.getRandomString(8)+"\">设置我的账号</a>").append("\n");

        return buffer.toString();
    }
    @Override
    public boolean isWxUsers(String openId){
        Admin admin = adminService.getAdminByOpenId(openId);
        if(admin != null){
            return BaseEnum.AccountEnum.WEIXIN.equals(admin.getAccountEnum());
        }
        return false;
    }

    @Override
    public void ResetPass(Users users,String password){
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        users.setPassword(password);
        Admin admin=adminService.getByMobile(users.getMobile());
        admin.setPassword(encodedPassword(users.getMobile(),password,salt));
        admin.setSalt(salt);
        adminService.update(admin);
        update(users);
//        Sms sms = new Sms("通知:您的密码已被管理员重置。", users.getMobile(), null);
//        SMSUtil.httpSend(sms);
    }

    @Override
    public Pager findNotInDep(Pager pager,Department department,Map rmap) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Users.class);
        List<Users> usersList = dutyService.getPersons(department);
        List<String> idList = Lists.newArrayList();
        for(Users users : usersList){
            idList.add(users.getId());
        }
        if(idList.size()>0)detachedCriteria.add(Restrictions.not(Restrictions.in("id", idList)));

        rmap.put("state", BaseEnum.StateEnum.Enable);
        return findByPager(pager, detachedCriteria, null, null, null, null, rmap);
    }

    @Override
    public Pager findInDep(Pager pager, Department department, Map rmap) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Users.class);
        List<Users> usersList = dutyService.getPersons(department);
        List<String> idList = Lists.newArrayList();
        for(Users users : usersList){
            idList.add(users.getId());
        }
        if(idList.size()==0){
            return pager;
        }else {
            detachedCriteria.add(Restrictions.in("id", idList));
            rmap.put("state", BaseEnum.StateEnum.Enable);
            return findByPager(pager, detachedCriteria, null, null, null, null, rmap);
        }

    }


    @Override
    public String openId(String name) {
        Admin admin = adminService.getByMobile(name);
        if(admin == null){
            return null;
        }
        return admin.getOpenId();
    }

    @Override
    public String openId(Users users) {
        Admin admin = adminService.get(users.getAdminId());
        if(admin == null){
            return null;
        }
        return admin.getOpenId();
    }

    @Override
    public Users getByAdmin(Admin admin) {
        return usersDao.getByAdmin(admin);
    }

    @Override
    public Result linkUser(HttpServletResponse response, Admin admin, String tmpOpenId){
        Result result=new Result();
        if(admin == null){
            result.setState(0);
            return  result;
        }
        Admin tmpAdmin =  adminService.getAdminByOpenId(StringUtils.trim(tmpOpenId));
        String openId = admin.getOpenId();
        if( StringUtils.isNotEmpty(openId)){
            if(StringUtils.trim(openId).equals(StringUtils.trim(tmpOpenId))  ){
                result.setState(1);
            }else{
                result.setState(0);
            }
        }else if(tmpAdmin!=null){
            result.setState(0);
        }else{
            result = adminService.updateOpenId(admin, tmpOpenId);
        }

        return result;
    }

    public Users getUsersByOpenId(String openId){
        Admin admin = adminService.getAdminByOpenId(openId);
        if(admin != null){
            return usersDao.getByAdmin(admin);
        }
        return null;
    }

    /**
     * 注册用户
     * */
    private Result registerUser(String name,Company company,String adminId){
        Users users = new Users(name,company,adminId);
        users.setMobile(adminService.get(adminId).getUsermobile());
        users.setAdminType("2");
        users.setIsCompany(true);
        updateNameAndEnable(users);
        return resultService.successWithId(users.getId());
    }
    /**
     * 得到登录提示语
     * */
    private String getLoginContent(String openId){
        StringBuffer buffer = new StringBuffer();
        buffer.append("[难过]您的微信未绑定工程泛联客账号，请绑定或者注册一个新账号。\n");
        buffer.append("<a href=\""+wxMpConfigStorage.getHttpHost()+"/"+systemConfig.getWebroot()+"/mp/login.action?_="+ Identities.randomBase62(8)+"&openId="+openId+"\">绑定工程泛联客账号/体验注册</a>").append("\n\n");
        return buffer.toString();
    }

    public Result checkNameString(String name){
        if (StringUtils.isEmpty(name)) {
            return resultService.build(0, 1, "用户名不能为空！", null);
        }
        if(!CommonUtil.isMobile(name)){
            return resultService.build(0,1,"用户名必须为手机号！",null);
        }
        return resultService.success();
    }

    public Result sendAuthCode(String name,EnumManage.AuthCodeEnum type,int experience,Users users) {
        Result result = checkNameString(name);
        LogUtil.info("{ result }"+result.getData());
        if (result.getState() == 0) {
            return result;
        }
        if (CommonUtil.isMobile(name)) {
            // 检查用户名
            if(1==experience){
                if (StringUtils.equals(EnumManage.AuthCodeEnum.passwordForget.name(), type.name())) {
                    if (adminService.getByMobile(name) == null) return resultService.build(0, 1, "该用户没有注册，请重新输入！", null);
                } else {
                    if (adminService.getByMobile(name) != null) return resultService.build(0, 1, "已经存在该用户，请重新输入！", null);
                }
            }else{

            }
            //生成验证码
            if(1==experience){
                String random = authCodeService.createAuthCode(name);
                return smsService.send("感谢您对工程泛联客的支持，您的验证码为："+random + "。", name);
            }else{
                String random = authCodeService.createAuthCode("ty"+name);
                return smsService.send("您的验证码为："+random + "。"+"登录帐号:"+users.getMobile()+",密码:"+users.getPassword(), name);
            }

        }
        return result;
    }


    public Result resetPassword(String name,String password){
        Result result = checkNameString(name);
        if(result.getState() == 0){
            return result;
        }
        //检查用户名应该存在
        if(!checkNameExist(name)){
            return resultService.build(0,1,"该用户名不存在",null);
        }
        Admin admin = adminService.getAdminByAccount(name);
        return adminService.updatePassword(admin,password);
    }

    public boolean checkNameExist(String name){
        // 检查用户名
        if(CommonUtil.isMobile(name)){
            // 检查用户名
            return adminService.checkIsExistAccount(name, BaseEnum.AccountEnum.MOBILE);
        }else{
            return false;
        }
    }

}