package org.fomky.tasks.task.core;


import org.fomky.tasks.task.entity.TaskModel;

/**
 * @author Created by Fomky on 2017/3/3113:49.
 */
public class TestTask extends TaskInterface {
    @Override
    public void run(TaskExecute execute, TaskModel model){
        System.out.println("运行了一次了 ---- " + model.getName());
    }
}
