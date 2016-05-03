package com.joint.web.action.com;


import com.joint.base.entity.Permission;
import com.joint.base.entity.Users;
import com.joint.base.service.FileManageService;
import com.joint.base.service.PermissionService;
import com.joint.base.service.UsersService;
import com.joint.web.action.BaseAdminAction;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.ParentPackage;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@ParentPackage("com")
public class IndexAction extends BaseAdminAction {

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

    private Users user;
    private String password;
    private String username;
    private String _t;
    private String filePath;
    private String ajax;

    private List<Permission> permissionList;

	public String execute(){
        user=usersService.getLoginInfo();
        return "center";

	}
	public String index(){
		return "login";
	}


    /**
     * 登陆
     * @return
     */
    public String login(){
        getResourceList();
        return "center";
    }

    /**
     * 登出
     * @return
     */
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "login";
    }

    /**
     *代办
     * @return
     */
    public Object numToDo(){
        List<String> idList = workflowService.getAllInProcessByUsers();
        return idList.size();

    }

    /**
     * 草稿
     * @return
     */
    public Object numDraft(){
        List<String> docIdList = workflowService.findDocIdByNum(0, usersService.getLoginInfo());
        return docIdList.size();
    }

    /**
     * 获取资源列表
     */
    private void getResourceList(){
        user = usersService.getLoginInfo();
        permissionList = new ArrayList<Permission>();
        if(user.getEmail().equals("admin")){
            for(Permission p : permissionService.getAll()){
                if(p.getType()!=null && p.getType().equals("menu") && p.getParent()==null){
                    permissionList.add(p);
                }
            }
        }else {
            List<Permission> pList = permissionService.getAllByUser(user.getId());

            for(Permission p : pList){
                if(p.getType()!=null && p.getType().equals("menu") && p.getParent()==null){
                    permissionList.add(p);
                }
            }
        }

    }
    public boolean isMenu(Set<Permission> perList){
        System.out.println("size:"+perList.size());
        for(Permission p :perList){
            System.out.println(p.getName());
            if(p.getType().equals("menu")){
                return true;
            }
        }
        return false;
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

    public String getAjax() {
        return ajax;
    }

    public void setAjax(String ajax) {
        this.ajax = ajax;
    }
}
