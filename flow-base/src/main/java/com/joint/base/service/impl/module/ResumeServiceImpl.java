package com.joint.base.service.impl.module;

import com.fz.us.base.bean.Pager;
import com.fz.us.base.bean.Result;
import com.fz.us.base.dao.MongoBaseDao;
import com.fz.us.base.service.impl.MongoBaseServiceImpl;
import com.joint.base.bean.module.EventBean;
import com.joint.base.dao.module.ResumeDao;
import com.joint.base.entity.module.Resume;
import com.joint.base.service.UsersService;
import com.joint.base.service.module.ResumeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created with us-parent -> com.fz.us.modules.service.impl.
 * User: min_xu
 * Date: 2014/9/28
 * Time: 16:44
 * 说明：
 */
@Service
public class ResumeServiceImpl extends MongoBaseServiceImpl<Resume, String> implements ResumeService {

    @Resource
    private ResumeDao resumeDao;

    @Resource
    private UsersService usersService;

    @Override
    public MongoBaseDao<Resume, String> getMongoBaseDao() {
        return resumeDao;
    }

    public Result createMongoEntity(Resume resume,StackTraceElement stack){
        if(null != stack){
            resume.setEventClass(stack.getClassName());
            resume.setEventMethod(stack.getMethodName());
        }
        save(resume);

        return resultService.successWithId(resume.getId());
    }

    public Result createMongoEntity(Resume resume){

        save(resume);
        return resultService.successWithId(resume.getId());
    }

    public Result updateMongoEntity(Resume resume){
        save(resume);
        return resultService.successWithId(resume.getId());
    }

    public Pager getPager(Pager pager,String usersId, String companyId, String targetId,Map<String,Object> params){
        Query query = new Query();
        Criteria criteria = where("companyId").is(companyId);
        if(null != targetId){
            criteria.and("targetId").is(targetId);
        }
        if(null != usersId){
            criteria.and("usersId").is(usersId);
        }
        query.addCriteria(criteria);
        return findPage(pager,query);
    }

    public Pager getClientPager(Pager pager, String companyId ,String clientId,Map<String,Object> params){
        Query query = new Query();
        Criteria criteria = where("companyId").is(companyId);
        if(null != clientId){
            criteria.and("clientId").is(clientId);
        }
        query.addCriteria(criteria);
        return findPage(pager,query);
    }


    public Pager getTargetResume(Pager pager,String targetId){
        Query query = new Query();
        Criteria criteria = where("targetId").is(targetId);
        query.addCriteria(criteria);
        return findPage(pager,query);
    }

    public Resume getLastTargetResume(String targetId) {
        Query query = new Query();
        Criteria criteria = where("targetId").is(targetId);
        query.addCriteria(criteria);
        query.with(new Sort(Sort.Direction.DESC, "createDate"));
        return findOne(query);
    }

    public Pager getCompanyResume(Pager pager,EventBean.EventEnum event,EventBean.TargetTypeEnum targetType,String companyId){
        Query query = new Query();
        Criteria criteria = where("companyId").is(companyId);
        if(null != event){
            criteria.and("event").is(event);
        }
        if(null != targetType){
            criteria.and("targetType").is(targetType);
        }
        query.addCriteria(criteria);
        return findPage(pager,query);
    }

    /**
     * app用
     * @param resume
     * @return
     */
    @Override
    public Map<String, Object> getListItemMap(Resume resume) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", StringUtils.isNotEmpty(resume.getId())?resume.getId():"");

        map.put("companyId", StringUtils.isNotEmpty(resume.getCompanyId())?resume.getCompanyId():"");
        map.put("targetId", StringUtils.isNotEmpty(resume.getTargetId())?resume.getTargetId():"");
        map.put("targetClass", StringUtils.isNotEmpty(resume.getTargetClass())?resume.getTargetClass().substring(StringUtils.lastIndexOf(resume.getTargetClass(), ".") + 1):"");
        map.put("eventDate", StringUtils.isNotEmpty(resume.getEventDate())?resume.getEventDate():"");
        map.put("text", StringUtils.isNotEmpty(resume.getText())?resume.getText():"");
        map.put("textObject", StringUtils.isNotEmpty(resume.getTextObject())?resume.getTextObject():"");
        if(StringUtils.isNotEmpty(resume.getUserId())){
            map.put("bizerId", resume.getUserId());
            map.put("bizerName", usersService.get(resume.getUserId()).getName());
        }else{
            map.put("bizerId", "");
            map.put("bizerName", "");
        }

        map.put("event", resume.getEvent()!=null?resume.getEvent().name():"");
        map.put("eventName", resume.getEvent()!=null?resume.getEvent().value():"");
        map.put("level", resume.getLevel()!=null?resume.getLevel().name():"");
        map.put("levelName", resume.getLevel()!=null?resume.getLevel().value():"");
        return map;
    }

    @Override
    public Map<String, Object> getDetailMap(Resume resume) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", StringUtils.isNotEmpty(resume.getId())?resume.getId():"");

        map.put("event",resume.getEvent()==null?"":resume.getEvent().value());
        map.put("text",resume.getText());
        map.put("eventDate",resume.getEventDate());
        map.put("targetType",resume.getTargetType()==null?"":resume.getTargetType().value());
        map.put("companyId",resume.getCompanyId());
        map.put("bizerId",resume.getUserId());
        return map;
    }

    @Override
    public Map<String,Object> jqGridDetailMap(Pager pager,String usersId, String companyId, String targetId,Map<String,Object> params){
        //pager = findByPager(pager,users,company,client, params,null);
        pager = getCompanyResume(pager,null,null,companyId);
        return pagerToJqGridMap(pager);
    }

    @Override
    public Map<String,Object> pagerToJqGridMap(Pager pager){
        List<Map> dataRows = new ArrayList<Map>();
        Map<String, Object> data = new HashMap<String, Object>();
        List<Resume> list = (List<Resume>) pager.getList();
        for(Resume mongoEntity:list){
            Map map = getDetailMap(mongoEntity);
            dataRows.add(map);
        }
        data.put("dataRows", dataRows);
        data.put("records", pager.getTotalCount());
        data.put("page",pager.getPageNumber());
        data.put("rows",pager.getPageSize());
        data.put("total",pager.getPageCount());
        return data;
    }

}
