package org.fomky.tasks.task.core;

import org.apache.log4j.Logger;
import org.fomky.tasks.task.dao.TaskDao;
import org.fomky.tasks.task.entity.BaseTask;
import org.fomky.tasks.task.entity.JobInfo;
import org.fomky.tasks.task.entity.TaskModel;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.quartz.CronScheduleBuilder.cronSchedule;

/**
 * @author Created by Fomky on 2017/3/3016:49.
 */
@Service
public class TaskExecute {
    public static String APPLICATIONCONTEXT = "APPLICATIONCONTEXT";
    private Logger logger = Logger.getLogger(TaskExecute.class);
    public static String KEY_TASKEXECUTE = "TASKEXECUTE";
    public static String KEY_TASK = "TASK";
    @Resource
    private ApplicationContext context;
    @Resource
    private Scheduler scheduler;
    @Resource
    public
    TaskDao taskDao;

    public void taskRepeatForever(TaskModel task) {
        try {
            Class job_class = Class.forName(task.getClass_name());
            // -- 组装任务数据 ---
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(KEY_TASK, task);
            dataMap.put(KEY_TASKEXECUTE, this);
            dataMap.put(APPLICATIONCONTEXT, context);
            @SuppressWarnings("unchecked")
            JobDetail job = JobBuilder.newJob(job_class)
                    .setJobData(dataMap)
                    .withIdentity(task.getName(), task.getGroup())
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(task.getName(), task.getGroup())
                    .withSchedule(cronSchedule(task.getCronExpression()))
                    .build();
            scheduler.scheduleJob(job, trigger);
            scheduler.start();
        } catch (ClassNotFoundException | SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public List<JobInfo> list() throws SchedulerException {
        List<JobInfo> details = new ArrayList<>();
        scheduler.getJobGroupNames().forEach(s -> {
            try {
                Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.groupContains(s));
                jobKeys.forEach(jobKey -> {
                    try {
                        JobInfo info = new JobInfo();
                        JobDetail detail = scheduler.getJobDetail(jobKey);
                        Object object = detail.getJobDataMap().get(TaskExecute.KEY_TASK);
                        if (object != null && object instanceof TaskModel) {
                            info.setTask((TaskModel) object);
                        }

                        info.setJobClass(detail.getJobClass());
                        info.setKey(JobInfo.create(detail.getKey().getName(), detail.getKey().getGroup()));
                        info.setDescription(detail.getDescription());
                        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                        if (triggers.size() > 0) {
                            CronTrigger trigger = (CronTrigger) triggers.get(0);
                            info.setEndTime(trigger.getEndTime());
                            info.setStartTime(trigger.getStartTime());
                            info.setFinalFireTime(trigger.getFinalFireTime());
                            info.setNextFireTime(trigger.getNextFireTime());
                            info.setPreviousFireTime(trigger.getPreviousFireTime());
                            info.setMisfireInstruction(trigger.getMisfireInstruction());
                            info.setPriority(trigger.getPriority());
                            info.getTask().setCronExpression(trigger.getCronExpression());
                        }
                        details.add(info);
                    } catch (SchedulerException e) {
                        e.printStackTrace();
                    }
                });

            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        });
        return details;
    }

    public Object scheduler() {
        return scheduler;
    }

    public void saveTask(TaskModel req) {
        req.setCreateTime(System.currentTimeMillis());
        int id = taskDao.saveTask(req);
        req.setId(id);
    }

    public List<BaseTask> findAllBaseTask() {
        return taskDao.findAllBaseTask();
    }

    List<TaskModel> findAllTaskModels() {
        return taskDao.findAllTaskModels();
    }

    public void deleteTask(JobInfo info) {
        try {
            scheduler.deleteJob(new JobKey(info.getKey().name, info.getKey().group));
        } catch (SchedulerException e) {
            logger.error("[deleteTask]" + e.getMessage(), e);
        }
    }

    public void update(TaskModel task) {
        try {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(new JobKey(task.getName(),task.getGroup()));
            if(triggers==null || triggers.isEmpty()) return;
            Trigger oldCronTrigger = triggers.get(0);
            String oldTime = ((CronTrigger)oldCronTrigger).getCronExpression();
            if (!oldTime.equalsIgnoreCase(task.getCronExpression())) {
                Trigger trigger = TriggerBuilder.newTrigger()
                        .withIdentity(task.getName(), task.getGroup())
                        .withSchedule(cronSchedule(task.getCronExpression()))
                        .build();
                // 重启触发器
                scheduler.rescheduleJob(TriggerKey.triggerKey(task.getName(),task.getGroup()), trigger);
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
