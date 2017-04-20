package org.fomky.tasks.task.service;

import org.fomky.tasks.task.dao.TaskDao;
import org.fomky.tasks.task.entity.TaskLog;
import org.fomky.tasks.task.entity.TaskModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Created by Fomky on 2017/4/1310:34.
 */
@Service
public class TaskService {
    @Resource
    private TaskDao taskDao;


    public List<TaskLog> findAllLog(Integer task_id, int start){
        return taskDao.findTaskLogs(task_id,start);
    }

    public void deleteTask(TaskModel task) {
        taskDao.deleteTask(task);
    }

    public void updateTask(TaskModel task) {
        taskDao.update(task);
    }

    public boolean findTaskExits(String name,String group){
        return taskDao.findTaskByNameGroup(name,group);
    }
}
