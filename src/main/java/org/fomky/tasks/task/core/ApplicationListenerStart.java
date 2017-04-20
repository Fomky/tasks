package org.fomky.tasks.task.core;

import org.apache.log4j.Logger;
import org.fomky.tasks.task.entity.TaskModel;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ApplicationListenerStart implements ApplicationListener<ContextRefreshedEvent> {
    private Logger logger = Logger.getLogger(ApplicationListenerStart.class);
    @Resource
    private TaskExecute taskExecute;
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        recoveryTask();
    }

    /**
     * 初始化 -- 恢复任务 ---
     */
    private void recoveryTask(){
        logger.info("加载并恢复进行中的任务....");
        List<TaskModel> list = taskExecute.findAllTaskModels();
        if(list!=null && !list.isEmpty()){
            list.forEach(task -> {
                if(task.getStatus() == 1 ) {
                    logger.info("任务 : " + task.getName() +
                            "\t Class: " + task.getClass_name() +
                            "\t CronExpression: " + task.getCronExpression());
                    taskExecute.taskRepeatForever(task);
                }
            });
        }
        logger.info("恢复完成....");
    }
}