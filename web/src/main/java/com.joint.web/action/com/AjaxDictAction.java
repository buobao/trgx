package com.joint.web.action.com;


import com.fz.us.base.bean.Pager;
import com.fz.us.dict.bean.DictBean;
import com.fz.us.dict.entity.Dict;
import com.fz.us.dict.entity.DictSetting;
import com.fz.us.dict.service.DictService;
import com.fz.us.dict.service.DictSettingService;
import com.joint.base.entity.Company;
import com.joint.base.entity.Users;
import com.joint.base.service.CompanyService;
import com.joint.base.service.UsersService;
import com.joint.web.action.BaseAdminAction;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class AjaxDictAction extends BaseAdminAction {
	private static final long serialVersionUID = -5383463207248344967L;
	
	@Resource
	private DictService dictService;
	@Resource
	private UsersService usersService;
	@Resource
	private CompanyService companyService;
	@Resource
	private DictSettingService dictSettingService;
	
	private String dictName;
	private String type;
	private String name;
	private List<Dict> list;
	
	//自定义配置的id
	private String did;
	private String sortList;

	private List<Dict> dictList;
	private String superNode;
	
	/**
	 * 列表(包含不带公司名称的字典)
	 * */
	public String list(){
        Users users = usersService.getLoginInfo();
		list = dictService.listDefinedAll(DictBean.DictEnum.valueOf(dictName), users.getCompany().getId());
		return "list";
	}

	public String tree(){
		getRequest().setAttribute("dictName", dictName);
		return "tree";
	}

	public String listtree(){
		Users users = usersService.getLoginInfo();
		Company com = usersService.getCompanyByUser();
		dictList = dictService.listDefinedAll(DictBean.DictEnum.valueOf(dictName), users.getCompany().getId());

		int startNumber = 0;
		List<JSONObject> dataRows=new ArrayList<JSONObject>();
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String,Object> rMap;
		Map<String,Object> param = new HashMap<>();
		for (Dict d:dictList){
			if (d.getLevel() == 0 && (d.getSuperNode() == null || d.getSuperNode().equals("0"))) {
				rMap = new HashMap<String, Object>();
				rMap.put("id", d.getId());
				rMap.put("name", d.getName());
				param.put("dictId",d.getId());
				List<DictSetting> settingList = (List<DictSetting>) dictSettingService.findByPager(new Pager(0), param).getList();
				if (settingList!=null && settingList.size()>0) {
					rMap.put("state", settingList.get(0).getState().value());
					rMap.put("did", settingList.get(0).getId());
				}else{
					rMap.put("state","启用");
					rMap.put("did", "");
				}

				//rMap.put("date", DataUtil.DateToString(d.getCreateDate(), "yyyy-MM-dd"));
				rMap.put("lft", ++startNumber);
				rMap.put("level", d.getLevel());
				rMap.put("uiicon", "");
				dataRows.add(JSONObject.fromObject(rMap));
				int index = dataRows.size()-1;
				startNumber = getchild(d.getId(),startNumber,dataRows);
				rMap.put("rgt", ++startNumber);
				dataRows.set(index,JSONObject.fromObject(rMap));
			}
		}
		data.put("rows", dataRows);
		//System.out.println(JSONObject.fromObject(data).toString());
		return ajaxJson(JSONObject.fromObject(data).toString());
	}

	private int getchild(String id, int startNumber, List<JSONObject> dataRows){
		Map<String,Object> param = new HashMap<>();
		for (Dict d:dictList){
			if (d.getSuperNode()!=null && d.getSuperNode().equals(id)){
				Map<String,Object> rMap= new HashMap<String, Object>();
				rMap.put("id", d.getId());
				rMap.put("name", d.getName());
				param.put("dictId",d.getId());
				List<DictSetting> settingList = (List<DictSetting>) dictSettingService.findByPager(new Pager(0), param).getList();
				if (settingList!=null && settingList.size()>0) {
					rMap.put("state", settingList.get(0).getState().value());
					rMap.put("did", settingList.get(0).getId());
				}else{
					rMap.put("state","启用");
					rMap.put("did", "");
				}
				//rMap.put("date", DataUtil.DateToString(d.getCreateDate(), "yyyy-MM-dd"));
				rMap.put("lft", ++startNumber);
				rMap.put("level", d.getLevel());
				rMap.put("uiicon", "");
				dataRows.add(JSONObject.fromObject(rMap));
				int index = dataRows.size()-1;
				startNumber = getchild(d.getId(), startNumber, dataRows);
				rMap.put("rgt", ++startNumber);
				dataRows.set(index, JSONObject.fromObject(rMap));
			}
		}
		return startNumber;
	}
	
	public String method() throws ClassNotFoundException {
		if (StringUtils.isEmpty(keyId) && StringUtils.isEmpty(name)) {
			return ajaxHtmlCallback("400", "操作错误！", "");
		}
        Users users = usersService.getLoginInfo();
		Company company = users.getCompany();
		if (superNode != null && StringUtils.isEmpty(superNode)){
			superNode = null;
		}

		if (StringUtils.equals(type, "save")) {
			dictService.createAndEdit(type, keyId, did, name, company.getId(), dictName,superNode);
			return ajaxHtmlCallback("200", "选项已添加成功！", "操作成功");
		} else if (StringUtils.equals(type, "edit")) {
			dictService.createAndEdit(type, keyId, did, name, company.getId(), dictName, superNode);
			return ajaxHtmlCallback("200", "选项已更新成功！", "操作成功");
		} else if (StringUtils.equals(type, "disabled")) {
			Dict dict = dictService.get(keyId);
			dictService.methodEnableAndDisabled(type, keyId, did, null, company.getId(), dictName);
			return ajaxHtmlCallback("200", "选项已禁用成功！", "操作成功");
		} else if (StringUtils.equals(type, "enable")) {
			dictService.methodEnableAndDisabled(type, keyId, did, null, company.getId(), dictName);
			return ajaxHtmlCallback("200", "选项已启用成功！", "操作成功");
		} else {
		}
		return ajaxHtmlCallback("200", "选项已添加成功！", "操作成功");
	}
	public String sort() throws ClassNotFoundException{
		//sortList
        Users users = usersService.getLoginInfo();
		Company company = users.getCompany();
		if(company!=null){
			dictService.sort(sortList, dictName);
		}
		return ajaxHtmlCallback("200", "排序已生效！","操作成功");
	}
	
	public String getDictName() {
		return dictName;
	}
	public void setDictName(String dictName) {
		this.dictName = dictName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public List<Dict> getList() {
		return list;
	}
	public void setList(List<Dict> list) {
		this.list = list;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getSortList() {
		return sortList;
	}

	public void setSortList(String sortList) {
		this.sortList = sortList;
	}

	public String getSuperNode() {
		return superNode;
	}

	public void setSuperNode(String superNode) {
		this.superNode = superNode;
	}
}
