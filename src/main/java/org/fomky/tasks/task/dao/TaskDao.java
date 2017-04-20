package org.fomky.tasks.task.dao;

import org.fomky.tasks.core.utils.StringUtil;
import org.fomky.tasks.task.entity.BaseTask;
import org.fomky.tasks.task.entity.TaskLog;
import org.fomky.tasks.task.entity.TaskModel;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Created by Fomky on 2017/4/1214:42.
 */
@Repository
public class TaskDao {
    @Resource(name = "sqliteJdbcTemplate")
    private JdbcTemplate sqlite;

    public void saveTaskLog(TaskLog log) {
        sqlite.update("INSERT INTO QRTZ_RUNLOG(task_id, startTime, endTime, status, error) VALUES (?,?,?,?,?)"
                , log.getTask_id(), log.getStartTime(), log.getEndTime(), log.getStatus(), log.getError());
    }

    public int saveTask(TaskModel task) {
        String sql = "INSERT INTO QRTZ_TASK(name, \"group\", class_name, cronExpression";
        String sql2 = ") VALUES (?,?,?,?";
        List<Object> list = new ArrayList<>();
        list.add(task.getName());
        list.add(task.getGroup());
        list.add(task.getClass_name());
        list.add(task.getCronExpression());
        //path, log_path, status, descr,
        if (StringUtil.isNotEmpty(task.getPath())) {
            sql += " ,path ";
            sql2 += ",?";
            list.add(task.getPath());
        }
        if (StringUtil.isNotEmpty(task.getLog_path())) {
            sql += " ,log_path ";
            sql2 += ",?";
            list.add(task.getLog_path());
        }
        if (task.getStatus() != null) {
            sql += " ,status ";
            sql2 += ",?";
            list.add(task.getStatus());
        }
        if (task.getCreateTime() != null) {
            sql += " ,createTime ";
            sql2 += ",?";
            list.add(task.getCreateTime());
        }
        if (StringUtil.isNotEmpty(task.getDescr())) {
            sql += " ,descr ";
            sql2 += ",?";
            list.add(task.getDescr());
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String save_sql = sql + sql2 + ")";
        sqlite.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(save_sql);
            for (int i = 0; i < list.size(); i++) {
                ps.setObject(i + 1, list.get(i));
            }
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public List<TaskModel> findAllTaskModels() {
        return sqlite.query("SELECT * FROM QRTZ_TASK", new BeanPropertyRowMapper<>(TaskModel.class));
    }


    public List<BaseTask> findAllBaseTask() {
        return sqlite.query("SELECT * FROM QRTZ_BASE", new BeanPropertyRowMapper<>(BaseTask.class));
    }

    public List<TaskLog> findTaskLogs(Integer task_id, int start) {
        String ppp = "";
        List<Object> objects = new ArrayList<>();
        if (task_id > 0) {
            ppp += " AND task.id=? ";
            objects.add(task_id);
        }
        objects.add(start);
        return sqlite.query("SELECT log.*,task.name FROM QRTZ_RUNLOG log JOIN QRTZ_TASK task ON log.task_id=task.id WHERE 1=1 " + ppp + " ORDER BY log.startTime desc LIMIT ?,20"
                , objects.toArray(), new BeanPropertyRowMapper<>(TaskLog.class));
    }

    public void deleteTask(TaskModel task) {
        sqlite.update("DELETE FROM QRTZ_TASK WHERE id=?", task.getId());
        //删除日志
        sqlite.update("DELETE FROM QRTZ_RUNLOG WHERE task_id=?", task.getId());
    }

    public void update(TaskModel task) {
        sqlite.update("UPDATE QRTZ_TASK SET cronExpression=? , descr=? WHERE id=?",
                task.getCronExpression(), task.getDescr(), task.getId());
    }

    public boolean findTaskByNameGroup(String name, String group) {
        List<TaskModel> taskModels = findTasksByNameGroup(name, group);
        return taskModels != null && !taskModels.isEmpty();
    }

    public List<TaskModel> findTasksByNameGroup(String name, String group) {
        return sqlite.query("SELECT * FROM QRTZ_TASK WHERE name=? AND \"group\"=?", new Object[]{name, group}, new BeanPropertyRowMapper<>(TaskModel.class));
    }


}
