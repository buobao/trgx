package com.joint.base.service.impl;


import com.fz.us.base.bean.BaseEnum;
import com.joint.base.dao.BaseEntityDao;
import com.joint.base.dao.FileManageDao;
import com.joint.base.entity.Company;
import com.joint.base.entity.FileManage;
import com.joint.base.entity.Users;
import com.joint.base.service.FileManageService;
import com.joint.base.service.UsersService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSFile;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service实现类 - 附件文件
 * ============================================================================
 * 版权所有 2014 。
 * 
 * @author min_xu
 * 
 * @version 0.1 2014-01-20
 */

@Service
public class FileManageServiceImpl extends BaseEntityServiceImpl<FileManage, String> implements FileManageService {

	@Resource
	private FileManageDao fileManageDao;
	@Resource
	private GridFsTemplate gridOperations;
    @Resource
    private UsersService usersService;

	@Override
	public BaseEntityDao<FileManage, String> getBaseEntityDao() {
		return fileManageDao;
	}

	@Override
	public String saveAndEnable(FileManage fileManage) {
		return fileManageDao.save(fileManage);
	}


	public FileManage gridFSSave(InputStream inputStream, String contentType,
			String filename) throws IOException {
		DBObject metaData = new BasicDBObject();
		metaData.put("filename", filename);
		metaData.put("contentType", contentType);
		
		//GridFSFile file = gridOperations.store(inputStream, filename, metaData);
		GridFSFile file = gridOperations.store(inputStream, filename, contentType,
				metaData);
		String gridId = file.getId().toString();
		
		FileManage fileManage = new FileManage();
		fileManage.setUrl("file.action?keyId="+gridId);
		fileManage.setGridId(gridId);
		fileManage.setName(filename);
		fileManage.setSize(file.getLength());

		fileManage.setState(BaseEnum.StateEnum.Enable);
		save(fileManage);
		
		metaData.put("fileId", fileManage.getId());
		file.save();
		
		return fileManage;
	}

    @Override
    public FileManage gridFSSave(File file, FileManage fileManage, String contentType,String filename) throws IOException {
        DBObject metaData = new BasicDBObject();
        Company company = usersService.getCompanyByUser();
        Users loginer = usersService.getLoginInfo();
        metaData.put("filename", filename);
        metaData.put("contentType", contentType);

        //GridFSFile file = gridOperations.store(inputStream, filename, metaData);
        GridFSFile gFile = gridOperations.store(new FileInputStream(file), filename, contentType,
                metaData);
        String gridId = gFile.getId().toString();

        fileManage.setUrl("file.action?keyId="+gridId);
        fileManage.setGridId(gridId);
        fileManage.setName(filename);
        fileManage.setSize(gFile.getLength());
        fileManage.setCompany(company);
        fileManage.setCreater(loginer);

        fileManage.setState(BaseEnum.StateEnum.Enable);
        updateAndEnable(fileManage);

        metaData.put("fileId", fileManage.getId());
        gFile.save();
        file.delete();

        return fileManage;
    }

    public void gridFSDelete(FileManage fileManage){
		fileManage.setState(BaseEnum.StateEnum.Delete);
		update(fileManage);
		
		String gridId = fileManage.getGridId();
		if (StringUtils.isNotEmpty(gridId)) {
			gridOperations.delete(new Query(Criteria.where("_id").is(
					new ObjectId(gridId))));
		}
	}
	@Override
	public void delete(FileManage entity){
		gridOperations.delete(new Query(Criteria.where("_id").is(new ObjectId(entity.getGridId()))));
		fileManageDao.delete(entity);
	}
    @Override
    public List<FileManage> getFileByKeyId(String keyId) {

        return fileManageDao.getFileById(keyId);
    }
    @Override
    public List<FileManage> getFileByKeyIdAndTarget(String keyId,String targerClass) {

        return fileManageDao.getFileByIdAndTarget(keyId,targerClass);
    }

	@Override
	public List<FileManage> getFileByTask(String keyId, String taskId, String processInstanceId) {
		return fileManageDao.getFileByTask(keyId,taskId,processInstanceId);
	}

	@Override
	public List<FileManage> getFileByTask(String keyId, String taskId, String proId, String type) {
		return fileManageDao.getFileByTask(keyId,taskId,proId,type);
	}

    /**
     * App用
     * @param targetId
     * @param targetClass
     * @param states
     * @return
     */
    @Override
    public List<FileManage> getList(String targetId, Class<?> targetClass, BaseEnum.StateEnum... states) {
        Map<String, Object> params = new HashMap<String, Object>();
        if(StringUtils.isNotEmpty(targetId))
            params.put("targetId", targetId);
        if(targetClass!=null)
            params.put("targetClass", targetClass.getName());
        if(states!=null&&states.length>0)
            params.put("state", states);
        return getList(params, "createDate", BaseEnum.OrderType.asc);
    }
}