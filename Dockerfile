FROM gettyimages/spark:2.4.1-hadoop-3.0

RUN apt-get -y update && apt-get -y install ssh openjdk-8-jdk nano

RUN ssh-keygen -t rsa -f ~/.ssh/id_rsa -P '' && \
	cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
COPY /conf/ssh_conf /root/.ssh/config

#	There is already some configs in $HADOOP_CONF_DIR/, can not just mount
COPY /conf/hadoop/* $HADOOP_CONF_DIR/

RUN mkdir -p /root/hdfs/datanode && \
	mkdir -p /root/hdfs/namenode


#	From '.bashrc' env vars visible even when exec: ' ssh localhost "env" '
# 	( Useful for 'start-slaves.sh', that uses ssh
#	  At list in case of unstandard SPARK_CONF_DIR )
COPY /conf/env_vars.sh /root/
RUN cat /root/env_vars.sh >> /root/.bashrc
RUN rm -f /root/env_vars.sh

RUN . /root/.bashrc && hdfs namenode -format

ENTRYPOINT ["/bin/bash", "-c", "sh /scripts/entrypoint.sh"]

