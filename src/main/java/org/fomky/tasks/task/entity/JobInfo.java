package org.fomky.tasks.task.entity;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.Trigger;

import java.util.Date;
import java.util.List;

/**
 * @author Created by Fomky on 2017/4/1116:52.
 */
public class JobInfo {
    private JobDataMap jobDataMap;
    private Class<? extends Job> jobClass;
    private JobKey key;
    private String description;
    private List<? extends Trigger> triggers;
    private Date endTime;
    private Date startTime;
    private Date finalFireTime;
    private Date nextFireTime;
    private Date previousFireTime;
    private int misfireInstruction;
    private int priority;
    private int runtimes;
    private TaskModel task;

    public void setJobDataMap(JobDataMap jobDataMap) {
        this.jobDataMap = jobDataMap;
    }

    public List<? extends Trigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<? extends Trigger> triggers) {
        this.triggers = triggers;
    }

    public JobDataMap getJobDataMap() {
        return jobDataMap;
    }

    public void setJobClass(Class<? extends Job> jobClass) {
        this.jobClass = jobClass;
    }

    public void setKey(JobKey key) {
        this.key = key;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Class<? extends Job> getJobClass() {
        return jobClass;
    }

    public JobKey getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setFinalFireTime(Date finalFireTime) {
        this.finalFireTime = finalFireTime;
    }

    public void setNextFireTime(Date nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public void setPreviousFireTime(Date previousFireTime) {
        this.previousFireTime = previousFireTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getFinalFireTime() {
        return finalFireTime;
    }

    public Date getNextFireTime() {
        return nextFireTime;
    }

    public Date getPreviousFireTime() {
        return previousFireTime;
    }

    public void setMisfireInstruction(int misfireInstruction) {
        this.misfireInstruction = misfireInstruction;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getMisfireInstruction() {
        return misfireInstruction;
    }

    public int getPriority() {
        return priority;
    }

    public void setTask(TaskModel task) {
        this.task = task;
    }

    public TaskModel getTask() {
        return task;
    }

    public int getRuntimes() {
        return runtimes;
    }

    public void setRuntimes(int runtimes) {
        this.runtimes = runtimes;
    }

    public static class JobKey{
        public String name;
        public String group;

        public JobKey() {
        }

        public JobKey(String name, String group) {
            this.name = name;
            this.group = group;
        }
    }
    public static JobKey create(String name,String group){
        return new JobKey(name,group);
    }
}
