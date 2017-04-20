package org.fomky.tasks.task.core;

import org.fomky.tasks.task.entity.TaskLog;
import org.fomky.tasks.task.entity.TaskModel;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;

/**
 * @author Created by Fomky on 2017/3/3113:24.
 */
public abstract class TaskInterface implements Job {
    protected ApplicationContext context;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException{
        TaskModel model = (TaskModel) context.getMergedJobDataMap().get(TaskExecute.KEY_TASK);
        this.context = (ApplicationContext) context.getMergedJobDataMap().get(TaskExecute.APPLICATIONCONTEXT);
        TaskLog log = createLog(model);
        TaskExecute execute = (TaskExecute) context.getMergedJobDataMap().get(TaskExecute.KEY_TASKEXECUTE);
        try{
            run(execute,model);
        }catch (Exception e){
            log.setError(e.getMessage());
        }finally {
            //设置结束时间
            log.setEndTime(System.currentTimeMillis());
            //保存执行日志
            execute.taskDao.saveTaskLog(log);
        }
    }

    /**
     * 创建日志对象
     */
    private TaskLog createLog(TaskModel model){
        TaskLog log = new TaskLog();
        log.setTask_id(model.getId());
        log.setStartTime(System.currentTimeMillis());
        log.setEndTime(0L);
        log.setError("");
        log.setStatus(1);
        return log;
    }

    protected abstract void run(TaskExecute execute, TaskModel model);
}
