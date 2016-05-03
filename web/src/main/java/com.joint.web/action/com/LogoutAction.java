package com.joint.web.action.com;

import com.joint.web.action.BaseAdminAction;
import org.apache.struts2.convention.annotation.ParentPackage;

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
public class LogoutAction extends BaseAdminAction {
	private static final long serialVersionUID = -5383463207248344967L;
	public String execute(){
		return "weblogin";
	}
}