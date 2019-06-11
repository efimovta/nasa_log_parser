#!/bin/bash

yes | cp -rf /conf/hadoop/* $HADOOP_CONF_DIR/
service ssh start

tail -f /dev/null