package org.fomky.tasks.task.core.impl;

import org.apache.log4j.Logger;
import org.fomky.tasks.task.core.TaskExecute;
import org.fomky.tasks.task.core.TaskInterface;
import org.fomky.tasks.task.entity.TaskModel;

/**
 * @author Created by Fomky on 2017/4/1317:35.
 */
public class UserTransfer extends TaskInterface {
	private Logger logger = Logger.getLogger(UserTransfer.class);
	@Override
	protected void run(TaskExecute execute, TaskModel model) {
		logger.info("全量同步-OA用户账号....");
		logger.info("获取OA数据源连接....");
		logger.info("获取VR数据库连接....");
	}
}
