package com.joint.web.action.com;

import com.fz.us.base.bean.BaseEnum;
import com.fz.us.base.bean.Result;
import com.fz.us.base.service.common.ResultService;
import com.fz.us.base.util.CommonUtil;
import com.fz.us.base.util.PinyinUtil;
import com.joint.base.bean.EnumManage;
import com.joint.base.entity.Department;
import com.joint.base.entity.Users;
import com.joint.base.service.*;
import com.joint.web.action.BaseAdminAction;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;

import javax.annotation.Resource;

/**
 * 后台Action类 - 网站注册 - 登录注册
 * ============================================================================
 * 版权所有 2013 min_xu。
 * ----------------------------------------------------------------------------
 * 
 * @author min_xu
 * 
 * @version 0.1 2013-7-15
 */

@ParentPackage("com")
public class RegisterAction extends BaseAdminAction {
	private static final long serialVersionUID = -5383463207248344967L;

	
	@Resource
	private UsersService usersService;
    @Resource
    private AuthCodeService authCodeService;
    @Resource
    private CompanyService companyService;
    @Resource
    private ResultService resultService;
    @Resource
    private DepartmentService departmentService;
    @Resource
    private AdminService adminService;

    private String username;
    private String passwordConfirm;
    private String password;
    private String authcode;

    private String terms;

	public String execute(){
		return "webregister";
	}

    public String testExperienceRegister(){
        if( adminService.getByMobile("tycompany") != null){
            return  ajaxResult(resultService.build(1,0,"体验账户已注册",null));
        }
        Result result=usersService.registerCompanyAndUser("tycompany", "pass");
        String userId=result.getId();
        Department department=new Department();
        department.setName("体验组");
        department.setState(BaseEnum.StateEnum.Enable);
        department.setCompany(usersService.getCompanyByUser(usersService.get(userId)));
        departmentService.save(department);
        return  ajaxResult(resultService.build(1,0,"注册成功",null));
    }
	
	public String loginshow(){
//		if(getSession("webUsername")==null){
//			loginname = "";
//		}else{
//			loginname = (String) getSession("webUsername");
//		}
		return "loginshow";
	}
    public String ajaxRegister() throws InterruptedException {
        if (!passwordConfirm.equals(password)) {
            return ajaxHtmlAppResult(0, "两次输入密码不一致！",null);
        }
        if (!StringUtils.isNotEmpty(terms)) {
            return ajaxHtmlAppResult(0, "请同意注册条款！",null);
        }
        if (StringUtils.isEmpty(password)) {
            return ajaxHtmlAppResult(0, "密码不能为空！",null);
        }
        if(!authCodeService.isValid(username, authcode)){
            return ajaxHtmlAppResult(0, "验证码输入错误！",null);
        }

        return ajaxResult(usersService.registerCompanyAndUser(username, password));
    }
    //体验注册
    public String experienceRegister(){
        if(!authCodeService.isValid("ty"+username, authcode)){
            return ajaxHtmlAppResult(0, "验证码输入错误！",null);
        }
        return ajaxResult(resultService.build(1,0,"",null));
    }

    //体验短信模版
    public String sendexperienceAuthCode(){
        if(!CommonUtil.isMobile(username)){
            return ajaxResult(resultService.build(0,1,"用户名必须为手机号！",null));
        }
        if(usersService.getUsersByMobile("ty"+username)!=null){
            return ajaxResult(resultService.build(0,1,"已经存在该体验用户，请重新输入！",null));
        }
        Users users=new Users();
        users.setAdminType("12");
        users.setName("ty" + username);
        users.setMobile("ty" + username);
        users.setPassword("pass");
        users.setState(BaseEnum.StateEnum.Enable);
        users.setPinYin(PinyinUtil.getPingYin("ty" + username));
        users.setPinYinHead(PinyinUtil.getPinYinHeadChar("ty" + username));

        return ajaxResult(usersService.registerExperienceUser(username,users));
    }

    public String sendAuthCode(){
        return ajaxResult(usersService.sendAuthCode(username, EnumManage.AuthCodeEnum.register,1,null));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getAuthcode() {
        return authcode;
    }

    public void setAuthcode(String authcode) {
        this.authcode = authcode;
    }
}
