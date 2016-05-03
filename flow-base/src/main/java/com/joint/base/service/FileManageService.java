package com.joint.base.service;


import com.fz.us.base.bean.BaseEnum;
import com.joint.base.entity.FileManage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * Service接口 - 附件
 * ============================================================================
 * 版权所有 2013 qshihua。
 * ----------------------------------------------------------------------------
 * 
 * @author qshihua
 * 
 * @version 0.1 2013-01-06
 */

public interface FileManageService extends BaseEntityService<FileManage, String> {
	/**
	 * 保存并启用
	 * @param
	 * @return
	 */
	public String saveAndEnable(FileManage fileManage);
	
	public FileManage gridFSSave(InputStream inputStream, String contentType,
                                 String filename) throws IOException;

    /**
     * 生成mongodb文件本删除本地文件
     * @param file
     * @param fileManage
     * @param contentType
     * @param filename
     * @return
     * @throws IOException
     */
    public FileManage gridFSSave(File file, FileManage fileManage, String contentType, String filename) throws IOException;
	
	
	public void gridFSDelete(FileManage fileManage);

    /**
     * 通过keyId来查找 fileManager
     * @param keyId
     * @return
     */
    public List<FileManage> getFileByKeyId(String keyId);

    /**
     * 通过keyId和targetClass来查找 fileManager
     * @param keyId
     * @param targetClass
     * @return
     */
    public List<FileManage> getFileByKeyIdAndTarget(String keyId, String targetClass);

	/**
	 * 通过keyId,taskId,processInstanceId来查找 fileManager
	 * @param keyId -- entity的Id
	 * @param taskId -- 节点的Id
	 * @param processInstanceId --- 流程实例的Id
	 * @return
	 */
	public List<FileManage> getFileByTask(String keyId, String taskId, String processInstanceId);
	/**
	 * 通过keyId,taskId,processInstanceId来查找 fileManager
	 * @param keyId -- entity的Id
	 * @param taskId -- 节点的Id
	 * @param proId --- 流程实例的Id
	 * @return
	 */
	public List<FileManage> getFileByTask(String keyId, String taskId, String proId, String swf);

    /**
     * App用获取目标关联文件
     *
     * @param targetId
     * @param targetClass
     * @param states
     * @return
     */
    public List<FileManage> getList(String targetId, Class<?> targetClass, BaseEnum.StateEnum... states);

}