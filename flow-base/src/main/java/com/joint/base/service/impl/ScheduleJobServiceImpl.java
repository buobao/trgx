package com.joint.base.service.impl;


import com.joint.base.dao.BaseEntityDao;
import com.joint.base.dao.ScheduleJobDao;
import com.joint.base.entity.ScheduleJob;
import com.joint.base.service.ScheduleJobService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * Service实现类 - 
 * ============================================================================
 * 版权所有 2013
 * ----------------------------------------------------------------------------
 * 
 * @author 
 * 
 * @version 0.1 2011-6-13
 */

@Service
public class ScheduleJobServiceImpl extends BaseEntityServiceImpl<ScheduleJob, String> implements ScheduleJobService {
	@Resource
    private ScheduleJobDao scheduleJobDao;

	@Override
	public BaseEntityDao<ScheduleJob, String> getBaseEntityDao() {
		return scheduleJobDao;
	}

    @Override
    public List<ScheduleJob> findAllJob() {
        return scheduleJobDao.findAllJob();
    }

    @Override
    public void addJob(ScheduleJob job) {
        scheduleJobDao.addJob(job);
    }

    @Resource
    public void addTask(ScheduleJob job) {
        job.setCreateTime(new Date());
        scheduleJobDao.save(job);
    }


    @PostConstruct
    public void init() {
        System.out.println("init schedule");

        // 这里获取任务信息数据
        List<ScheduleJob> jobList = scheduleJobDao.getAll();

        for (ScheduleJob job : jobList) {
            addJob(job);
        }
    }

}