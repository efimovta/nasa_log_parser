
export HADOOP_CONF_DIR="${HADOOP_CONF_DIR:-"${HADOOP_HOME}/etc/hadoop"}"

# With this can use any hadoop version. And fix start-slaves.sh bag ofcourse:
# 	Script uses ssh (and 'env'?) and $SPARK_DIST_CLASSPATH becomes emty
#	(before SPARK_DIST_CLASSPATH setted in Dockerfile in 'gettyimages')
export SPARK_DIST_CLASSPATH=$( ${HADOOP_HOME}/bin/hadoop --config ${HADOOP_CONF_DIR} classpath )


export MASTER="spark://master:7077"
export SPARK_WORKER_CORES=1
export SPARK_WORKER_MEMORY=512m
export SPARK_WORKER_INSTANCES=1
export SPARK_WORKER_PORT=8881
export SPARK_WORKER_WEBUI_PORT=8081


