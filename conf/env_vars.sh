
export	JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
export	HADOOP_HOME=/usr/hadoop-3.0.0
export	SPARK_HOME=/usr/spark-2.4.1
	
export	HADOOP_CONF_DIR=${HADOOP_HOME}/etc/hadoop
export	SPARK_CONF_DIR="/conf/spark"

export  PATH=$PATH:/usr/hadoop-3.0.0/bin:/usr/hadoop-3.0.0/sbin:/usr/spark-2.4.1/bin:/usr/spark-2.4.1/sbin

export  LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$HADOOP_HOME/lib/native