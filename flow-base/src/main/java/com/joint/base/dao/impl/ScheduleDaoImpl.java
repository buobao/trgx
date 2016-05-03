package com.joint.base.dao.impl;

import com.google.common.collect.Lists;
import com.joint.base.Factory.QuartzJobFactory;
import com.joint.base.Factory.QuartzJobFactoryDisallowConcurrentExecution;
import com.joint.base.dao.ScheduleJobDao;
import com.joint.base.entity.ScheduleJob;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Dao实现类 - 岗位
 * ============================================================================
 * 版权所有 2013 。
 * 
 * @author 
 * @version 0.1 2013-1-16
 * ============================================================================
 */

@Repository
public class ScheduleDaoImpl extends BaseEntityDaoImpl<ScheduleJob, String> implements ScheduleJobDao {

    @Resource
    private SchedulerFactoryBean schedulerFactoryBean;

    @Override
    public List<ScheduleJob> findAllJob() {
        Scheduler scheduler = schedulerFactoryBean.getObject();
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        try {
            Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
            List<ScheduleJob> jobList = new ArrayList<ScheduleJob>();
            for (JobKey jobKey : jobKeys) {
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers) {
                    ScheduleJob job = new ScheduleJob();
                    job.setJobName(jobKey.getName());
                    job.setJobGroup(jobKey.getGroup());
                    job.setDescription("触发器:" + trigger.getKey());
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    job.setJobStatus(triggerState.name());
                    if (trigger instanceof CronTrigger) {
                        CronTrigger cronTrigger = (CronTrigger) trigger;
                        String cronExpression = cronTrigger.getCronExpression();
                        job.setCronExpression(cronExpression);
                    }
                    jobList.add(job);
                }
            }
            return jobList;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Lists.newArrayList();
        }


    }

    @Override
    public List<ScheduleJob> findRunningJob() {
        Scheduler scheduler = schedulerFactoryBean.getObject();
        List<JobExecutionContext> executingJobs = null;
        try {
            executingJobs = scheduler.getCurrentlyExecutingJobs();
            List<ScheduleJob> jobList = new ArrayList<ScheduleJob>(executingJobs.size());
            for (JobExecutionContext executingJob : executingJobs) {
                ScheduleJob job = new ScheduleJob();
                JobDetail jobDetail = executingJob.getJobDetail();
                JobKey jobKey = jobDetail.getKey();
                Trigger trigger = executingJob.getTrigger();
                job.setJobName(jobKey.getName());
                job.setJobGroup(jobKey.getGroup());
                job.setDescription("触发器:" + trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.setJobStatus(triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.setCronExpression(cronExpression);
                }
                jobList.add(job);
            }
            return jobList;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return Lists.newArrayList();
        }


    }

    @Override
    public void pauseJob(ScheduleJob scheduleJob) {
        Scheduler scheduler = schedulerFactoryBean.getObject();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        try {
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resumeJob(ScheduleJob scheduleJob) {
        Scheduler scheduler = schedulerFactoryBean.getObject();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        try {
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteJob(ScheduleJob scheduleJob) {
        Scheduler scheduler = schedulerFactoryBean.getObject();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        try {
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void runJobNow(ScheduleJob scheduleJob) {
        Scheduler scheduler = schedulerFactoryBean.getObject();
        JobKey jobKey = JobKey.jobKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());
        try {
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateJobCron(ScheduleJob scheduleJob) {
        Scheduler scheduler = schedulerFactoryBean.getObject();

        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobName(), scheduleJob.getJobGroup());

        CronTrigger trigger = null;
        try {
            trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression());

            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            scheduler.rescheduleJob(triggerKey, trigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void addJob(ScheduleJob job) {
        if (job == null || !ScheduleJob.STATUS_RUNNING.equals(job.getJobStatus())) {
            return;
        }

        Scheduler scheduler = schedulerFactoryBean.getObject();

        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());

        CronTrigger trigger = null;
        try {
            trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (null == trigger) {
                Class clazz = ScheduleJob.CONCURRENT_IS.equals(job.getIsConcurrent()) ? QuartzJobFactory.class : QuartzJobFactoryDisallowConcurrentExecution.class;

                JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobName(), job.getJobGroup()).build();

                jobDetail.getJobDataMap().put("scheduleJob", job);

                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

                trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();

                scheduler.scheduleJob(jobDetail, trigger);
            } else {
                // Trigger已存在，那么更新相应的定时设置
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

                // 按新的cronExpression表达式重新构建trigger
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

                // 按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        // 不存在，创建一个

    }
}