
# NasaLogParser
_based on Apache Spark  & Hadoop._

## Objective of the project
This is a training project that processes NASA logs using Apache Spark in Standalone and over Yarn mode.
HDFS is used to store input and output data.

**Tasks:**
- Prepare a list of requests that ended in a 5xx error, with the number of failed requests.
- Prepare a time series with the number of requests by dates for all combinations of http methods 
and return codes used. Exclude combinations from the resulting file where the number of events 
in the total was less than 10.
- Calculate, by a sliding window in one week, the number of requests ending with codes 4xx and 5xx

## Prerequisites
- java 8
- Maven 
- Docker

Also be aware, [volumes][1] used (docker-compose.yml).

## Start from scratch (from project root folder)
> Tested on Windows 10. If there is a problem, try the [Troubleshooting section](#troubleshooting)

* Add logs file named 'access_log_Jul95' to /logs folder. Direct link to archive: 
ftp://ita.ee.lbl.gov/traces/NASA_access_log_Jul95.gz

* Build jar with program and start docker containers. 
```shell
mvn clean install 
docker-compose up -d 
```

* Attach console to master with command like (node names will be printed after the cluster starts):
```shell
docker exec -it nasa_log_parser_master_1 /bin/bash
```

* Start Apache Spark in Standalone mode:
```shell
/scripts/standalone_cluster_start.sh
/scripts/standalone_nasalogparser_start.sh
```

* **_//OR//_** Start Apache Spark in YARN mode:
```shell
/scripts/yarn_cluster_start.sh
/scripts/yarn_nasalogparser_start.sh
```

This four 'start' scripts applicable for restart.

**Results:** three *.csv files (per task) in mounted 'logs' directory (near 'access_log_Jul95')


## Other useful information

### Process names:
useful when check start up using 'jps'

    - NameNode = master hdfs
    - DataNode = worker hdfs
    
    - ResourceManager = master yarn
    - NodeManager = worker yarn
    
    - Worker = worker spark
    - Master = master spark

### Useful links
    HDFS
    http://localhost:9870/ - NameNode
    http://localhost:9864/ - DataNode +[9865,9866]
    
    http://localhost:9990/ - History Server (port in conf)
    
    YARN MODE
    http://localhost:8088/ - master yarn (also proxy to spark)
    http://localhost:8042/ - worker yarn +[8044,8045]
    
    STANDALONE MODE
    http://localhost:8080/ - master spark  
    http://localhost:8081/ - worker spark +[8082,8083]


## Troubleshooting
- Files within directories 'conf' and 'scripts' **must be with 'LF'** line separators. Be sure!<br>
Can help for IDEA IDE: https://www.jetbrains.com/help/idea/configuring-line-endings-and-line-separators.html#5979109a
- On Windows, you can sometimes see error like: <br>
_"error while creating mount source path '/host_mnt/c/nasa_log_parser/jars': mkdir /host_mnt/c: file exists"_, <br>
It is [volumes][1] problem. Try:
https://stackoverflow.com/a/53823591/9809131
- You got to have about 3GB memory for current configuration

[1]: https://docs.docker.com/compose/compose-file/#volumes