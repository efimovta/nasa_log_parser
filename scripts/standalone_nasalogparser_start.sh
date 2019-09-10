#!/bin/bash

## todo: find better way? 
#   useful when executing script directly from 'docker exec'
. "/root/.bashrc"


hadoop fs -rm -r /task1_results /task2_results /task3_results


spark-submit --class ru.eta.spark.nasalogparser.NasaLogParser\
 --master spark://master:7077 \
 --deploy-mode client\
 /jars/nasalogparser-1.0.jar logs/access_log_Jul95


hdfs dfs -getmerge /task1_results /logs/task1_results.csv
hdfs dfs -getmerge /task2_results /logs/task2_results.csv
hdfs dfs -getmerge /task3_results /logs/task3_results.csv
rm -v /logs/.*.crc > /dev/null
