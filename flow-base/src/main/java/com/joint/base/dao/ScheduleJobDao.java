package com.joint.base.dao;


import com.joint.base.entity.ScheduleJob;

import java.util.List;

/**
 * Dao接口 - 角色
 * ============================================================================
 * 版权所有 2013 。
 * 
 * @author 
 * @version 0.1 2013-1-16
 * ============================================================================
 */

public interface ScheduleJobDao extends BaseEntityDao<ScheduleJob, String> {

    /**
     * 查找所有的job
     * @return
     */
    public List<ScheduleJob> findAllJob();


    /**
     * 查找所有正在运行中的job
     * @return
     */
    public List<ScheduleJob> findRunningJob();

    /**
     * 暂停一个job
     * @param scheduleJob
     */
    public void pauseJob(ScheduleJob scheduleJob);

    /**
     * 恢复一个job
     * @param scheduleJob
     */
    public void resumeJob(ScheduleJob scheduleJob);

    /**
     * 删除一个job
     * @param scheduleJob
     */
    public void deleteJob(ScheduleJob scheduleJob);

    /**
     * 立即执行job
     * @param scheduleJob
     */
    public void runJobNow(ScheduleJob scheduleJob);

    /**
     * 更新时间表达式
     * @param scheduleJob
     */
    public void updateJobCron(ScheduleJob scheduleJob);
    /**
     * 更新时间表达式
     * @param job
     */
    public void addJob(ScheduleJob job);
}