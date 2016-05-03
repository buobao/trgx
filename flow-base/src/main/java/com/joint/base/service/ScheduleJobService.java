package com.joint.base.service;


import com.joint.base.entity.ScheduleJob;

import java.util.List;

/**
 * Service接口 - 角色
 * ============================================================================
 * 版权所有 2013
 * ----------------------------------------------------------------------------
 * 
 * @author 
 * 
 * @version 0.1 2013-05-09
 */

public interface ScheduleJobService extends BaseEntityService<ScheduleJob, String> {

    /**
     * 查找所有的job
     * @return
     */
    public List<ScheduleJob> findAllJob();

    /**
     * 增加定时任务
     */
    public void addJob(ScheduleJob job);

}