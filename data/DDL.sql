DROP TABLE IF EXISTS QRTZ_TASK;
CREATE TABLE IF NOT EXISTS QRTZ_TASK (
  id             INTEGER PRIMARY KEY AUTOINCREMENT,
  name           VARCHAR,
  "group"        VARCHAR,
  class_name     VARCHAR,
  path           VARCHAR,
  log_path       VARCHAR,
  status         INT DEFAULT 1,
  descr          VARCHAR,
  cronExpression VARCHAR,
  createTime     BIGINT
);

CREATE TABLE IF NOT EXISTS QRTZ_RUNLOG (
  task_id   INTEGER,
  startTime BIGINT,
  endTime   BIGINT,
  status    INT DEFAULT 1, -- 状态
  error     TEXT
);
CREATE TABLE IF NOT EXISTS QRTZ_BASE (
  name           VARCHAR,
  "group"        VARCHAR,
  class_name     VARCHAR UNIQUE ,
  path           VARCHAR,
  log_path       VARCHAR,
  descr          VARCHAR,
  cronExpression VARCHAR
);
