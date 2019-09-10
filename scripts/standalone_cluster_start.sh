#!/bin/bash

#   useful when executing script directly from 'docker exec'
. "/root/.bashrc"


echo Timur: tring stop all started at first...
stop-yarn.sh; stop-slaves.sh; stop-master.sh; stop-dfs.sh

echo Timur: starting standalone cluster...
source /scripts/update_conf.sh
start-dfs.sh & start-master.sh & start-slaves.sh
hdfs dfsadmin -safemode leave

hadoop fs -mkdir /logs
hadoop fs -put /logs/access_log_Jul95 /logs/access_log_Jul95

hdfs dfs -mkdir /spark-logs
$SPARK_HOME/sbin/start-history-server.sh

