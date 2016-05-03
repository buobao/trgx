package com.joint.web.action.com;


import com.fz.us.base.service.memcached.SpyMemcachedClient;
import com.joint.base.entity.Permission;
import com.joint.base.entity.Users;
import com.joint.base.exception.users.UserExpiredException;
import com.joint.base.service.FileManageService;
import com.joint.base.service.PermissionService;
import com.joint.base.service.UsersService;
import com.joint.base.util.StringUtils;
import com.joint.core.service.ProInfoService;
import com.joint.web.action.BaseAdminAction;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.struts2.convention.annotation.ParentPackage;

import javax.annotation.Resource;
import java.util.List;


@ParentPackage("com")
public class LoginAction extends BaseAdminAction {

	private static final long serialVersionUID = 4710860121487609820L;
	
	@Resource
	private RepositoryService repositoryService;
    @Resource
    private UsersService usersService;
    @Resource
    private TaskService taskService;
    @Resource
    private FileManageService fileManageService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private ProInfoService proInfoService;
    @Resource
    private SpyMemcachedClient spyMemcachedClient;

    private Users user;
    private String password;
    private String username;
    private String _t;
    private String filePath;
    private String error;

    private List<Permission> permissionList;

    /**
     * 登陆
     * @return
     */
    public String execute(){
        String method = getRequest().getMethod();
        if(!"POST".equalsIgnoreCase(method)){
            return "weblogin";
        }
        //ExcessiveAttemptsException
        String exceptionClassName = (String)getRequest().getAttribute("shiroLoginFailure");
        error = null;
        if(UnknownAccountException.class.getName().equals(exceptionClassName)) {
            error = "找不到当前账户";
        } else if(IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
            error = "密码错误";
        } else if(ExcessiveAttemptsException.class.getName().equals(exceptionClassName)) {
            error = "密码输入错误超过5次，请10分钟后再试";
        } else if(AuthenticationException.class.getName().equals(exceptionClassName)){
            error = "您的账号没有访问权限";
        } else if("jCaptcha.error".equals(exceptionClassName)) {
            error = "验证码错误";
        } else if(UserExpiredException.class.getName().equals(exceptionClassName)){
            error = "帐号未充值/帐号已过期";
        }else if(exceptionClassName != null) {
            error = "其他错误：" + exceptionClassName;
        }
        System.out.println("error : "+error);
        if(StringUtils.isNotEmpty(error)){
            return "weblogin";
        }

        //得到user可以访问的proInfo的id的集合，并写入缓存
        user = usersService.getLoginInfo();
        //pager= proInfoService.findByPagerAndLimit(new Pager(),user,com,rmap);
//        List<ProInfo> proInfoList= (List<ProInfo>) pager.getList();
//        for(ProInfo proInfo:proInfoList){
//            proInfoIds.add(proInfo.getId());
//        }
//        if(proInfoIds!=null && proInfoIds.size()>0){
//            String key = "proInfoIds_"+user.getId();
//            spyMemcachedClient.set(key, 0, proInfoIds);
//        }

        return "toindex";
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String get_t() {
        return _t;
    }

    public void set_t(String _t) {
        this._t = _t;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<Permission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
