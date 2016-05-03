package com.joint.base.service.module;

import com.fz.us.base.bean.Pager;
import com.fz.us.base.bean.Result;
import com.fz.us.base.service.MongoBaseService;
import com.joint.base.bean.module.EventBean;
import com.joint.base.entity.module.Resume;

import java.util.Map;

/**
 * Created with us-parent -> com.fz.us.modules.service.
 * User: min_xu
 * Date: 2014/9/28
 * Time: 16:33
 * 说明：
 */
public interface ResumeService extends MongoBaseService<Resume,String> {

    public Result createMongoEntity(Resume resume, StackTraceElement stack);
    public Result createMongoEntity(Resume resume);

    public Result updateMongoEntity(Resume resume);

    public Pager getTargetResume(Pager pager, String targetId);
    public Resume getLastTargetResume(String targetId);

    public Pager getCompanyResume(Pager pager, EventBean.EventEnum event, EventBean.TargetTypeEnum targetType, String companyId);

    public Map<String, Object> getListItemMap(Resume resume);
    public Map<String, Object> getDetailMap(Resume resume);
    public Map<String,Object> jqGridDetailMap(Pager pager, String usersId, String companyId, String targetId, Map<String, Object> params);
    public Map<String,Object> pagerToJqGridMap(Pager pager);

    public Pager getPager(Pager pager, String usersId, String companyId, String targetId, Map<String, Object> params);

}
