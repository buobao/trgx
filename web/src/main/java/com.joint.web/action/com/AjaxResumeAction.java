package com.joint.web.action.com;

import com.joint.base.entity.module.Resume;
import com.joint.base.service.CompanyService;
import com.joint.base.service.UsersService;
import com.joint.core.service.common.ResumeBeanService;
import com.joint.web.action.BaseAdminAction;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;

import javax.annotation.Resource;
import java.util.List;

/**
 * 后台Action类 - 网站注册 - 系统字典请求
 * ============================================================================
 * 版权所有 2013 min_xu。
 * ----------------------------------------------------------------------------
 * 
 * @author min_xu
 * 
 * @version 0.1 2013-7-15
 */

@ParentPackage("com")
public class AjaxResumeAction extends BaseAdminAction {
	private static final long serialVersionUID = -5383463207248344967L;

	@Resource
	private UsersService usersService;
	@Resource
	private ResumeBeanService resumeBeanService;
	@Resource
	private CompanyService companyService;

	private List<Resume> listResume;



	public String targetList(){

		if(StringUtils.isNotEmpty(keyId)){
			listResume = resumeBeanService.getTargetResumeList(keyId);

		}
		return "targetList";
	}




	public List<Resume> getListResume() {
		return listResume;
	}

	public void setListResume(List<Resume> listResume) {
		this.listResume = listResume;
	}
}
