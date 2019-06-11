# NasaLogParser
based on Apache Spark  & Hadoop

## Do not forger:
Add 'access_log_Jul95' to /logs folder.

From: http://ita.ee.lbl.gov/html/contrib/NASA-HTTP.html

Direct link: ftp://ita.ee.lbl.gov/traces/NASA_access_log_Jul95.gz


## Useful commands:


` docker-compose up -d ` OR ` docker-compose up -d --build ` in some cases

` docker exec -it nasalogparser_master_1 /bin/bash `


```shell
mvn clean install && \
docker exec -it nasalogparser_master_1 standalone_nasalogparser_start.sh
```


### Process names:
useful when use 'jps'

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
