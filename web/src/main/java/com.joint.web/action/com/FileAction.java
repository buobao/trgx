package com.joint.web.action.com;

import com.fz.us.base.dao.jdbc.JdbcDao;
import com.fz.us.base.util.LogUtil;
import com.joint.base.entity.FileManage;
import com.joint.base.service.FileManageService;
import com.joint.base.service.UsersService;
import com.joint.web.action.BaseAdminAction;
import com.mongodb.gridfs.GridFSDBFile;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class FileAction extends BaseAdminAction {
	private static final long serialVersionUID = -5383463207248344967L;
	
	@Resource
	private UsersService usersService;
	@Resource
	private FileManageService fileManageService;
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	private GridFsTemplate gridOperations;

    private String gridId;
    private String targetClass;
	
	public String execute() throws IOException{
		GridFSDBFile file = null;
        String fileId=null;
        if(StringUtils.isNotEmpty(keyId)) {
            List<FileManage> manageList=fileManageService.getFileByKeyId(keyId);
            if(manageList.size()>0){
                fileId=manageList.get(0).getGridId();
            }
            file=gridFSGet(fileId);
        }
       if (StringUtils.isNotEmpty(gridId)){
            file = gridFSGet(fileManageService.get("gridId", gridId).getGridId());
        }

        OutputStream out = null;
        HttpServletResponse response = getTheResponse();
        System.out.println("contentType"+file.getContentType());
        response.setContentType(file.getContentType());
        String suffix = file.getFilename().substring(file.getFilename().lastIndexOf("."),file.getFilename().length());
        response.setHeader("Content-Disposition", "attachment; filename=\"" +new String(file.getFilename().getBytes() + suffix) + "\"");
        out = response.getOutputStream();
        file.writeTo(out);

		return null;
	}
	public void readDoc() throws IOException {
		GridFSDBFile file = null;
		String fileId=null;
		if(StringUtils.isNotEmpty(keyId)) {
			FileManage fm = fileManageService.get(keyId);
			file=gridFSGet(fm.getGridId());
		}
        if (StringUtils.isNotEmpty(gridId)){
            file = gridFSGet(fileManageService.get("gridId", gridId).getGridId());
        }
		OutputStream out = null;
		HttpServletResponse response = getTheResponse();
		response.setContentType(file.getContentType());
		LogUtil.info("{file name}" + file.getFilename());
		String suffix = file.getFilename().substring(file.getFilename().lastIndexOf("."),file.getFilename().length());
		LogUtil.info("{suffix }" + suffix);
		response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(file.getFilename().getBytes() + suffix) + "\"");


		out = response.getOutputStream();
		file.writeTo(out);
	}
	
	//
	public String remove() throws IOException{
		if (StringUtils.isNotEmpty(keyId)) {
			GridFSDBFile file = gridFSGet(keyId);
			String fileId = file.getMetaData().get("fileId").toString();
			
			if (StringUtils.isNotEmpty(fileId)) {
				String sql = "delete from ss_product_ss_filemanage where picsUrl_id = :fileId ";
				
				Map<String,String> filter = new HashMap<String,String>();
		        filter.put("fileId", fileId);
		        try {
		            @SuppressWarnings("unchecked")
		            int i = jdbcDao.executeForMap(sql,filter);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		        
		        String sql2 = "update ss_filemanage set state = 'Delete' where id = :fileId ";
				Map<String,String> filter2 = new HashMap<String,String>();
		        filter2.put("fileId", fileId);
		        try {
		            @SuppressWarnings("unchecked")
		            int i =  jdbcDao.executeForMap(sql2,filter2);
		        
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
			}
			
			gridOperations.delete(new Query(Criteria.where("_id").is(
					new ObjectId(keyId))));
		}
		
		return ajaxHtmlCallback("200", "选项已删除成功！", "操作成功");
	}


    /**
     * 获得文件名称
     * @return
     */
    public String getFileName(){
        GridFSDBFile file = null;
        String fileId=null;
        if(StringUtils.isNotEmpty(keyId)) {
            List<FileManage> manageList=fileManageService.getFileByKeyId(keyId);
            if(manageList.size()>0){
                fileId=manageList.get(0).getGridId();
            }
            file=gridFSGet(fileId);
        }
        if (StringUtils.isNotEmpty(gridId)){
            file = gridFSGet(fileManageService.get("gridId", gridId).getGridId());
        }
        return ajaxText(file.getFilename());
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }
}