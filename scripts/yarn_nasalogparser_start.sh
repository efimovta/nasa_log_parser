#!/bin/bash

#   useful for 'docker exec' while not use ENV in Dockerfile
#   /root/.bashrc does not work! :(
set -a && . "/root/.bashrc" && set +a


hadoop fs -rm -r /task1_results /task2_results /task3_results


spark-submit --class ru.eta.spark.nasalogparser.NasaLogParser\
 --master yarn\
 --deploy-mode cluster\
 /jars/nasalogparser-1.0.jar logs/access_log_Jul95


hdfs dfs -getmerge /task1_results /logs/task1_results.csv
hdfs dfs -getmerge /task2_results /logs/task2_results.csv
hdfs dfs -getmerge /task3_results /logs/task3_results.csv
rm -v /logs/.*.crc > /dev/null