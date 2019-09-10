package ru.eta.spark.nasalogparser;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.util.Objects;

import static org.apache.spark.sql.functions.*;
import static ru.eta.spark.nasalogparser.NasaLogRowMappers.toRequestRow;


public class NasaLogParser {

    private static final String HDFS_URL = "hdfs://master:9000/";
    private static final String APP_NAME = "NasaLogParser";

    private static final String TASK1_RESULTS_FOLDER = "task1_results";
    private static final String TASK2_RESULTS_FOLDER = "task2_results";
    private static final String TASK3_RESULTS_FOLDER = "task3_results";

    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("Folder with logs not specified, closing program...");
        }

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("    NasaLogParser started!  ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        SparkSession spark = SparkSession.builder().appName(APP_NAME).getOrCreate();
        Dataset<RequestRow> df = spark.read().textFile(HDFS_URL + args[0])
                .map(toRequestRow, Encoders.bean(RequestRow.class)).filter(Objects::nonNull);

        task1(df);
        task2(df);
        task3(df);

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("    NasaLogParser finished!  ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }



    /**
     * Prepare a list of requests that ended in a 5xx error, with the number of failed requests.
     */
    private static void task1(Dataset<RequestRow> df) {
        df.filter(col("returnCode").between(500, 599))
                .toJavaRDD()
                .mapToPair(e -> new Tuple2<>(e.getPath(), 1))
                .reduceByKey(Integer::sum)
                .saveAsTextFile(HDFS_URL + TASK1_RESULTS_FOLDER);

        /* NOTE: groupBy() does redundant sorting -> alg works slower */

//        df.filter(col("returnCode").between(500, 599))
//                .groupBy("path")
//                .count()
//                .select("count", "path")
//                .sort(desc("count"))
//                .write().mode(SaveMode.Overwrite).option("sep", ";")
//                .csv(HDFS_URL + TASK1_RESULTS_FOLDER);
    }

    /**
     * Prepare a time series with the number of requests by dates for all combinations of
     * http methods and return codes used. Exclude combinations from the resulting file where
     * the number of events in the total was less than 10.
     */
    private static void task2(Dataset<RequestRow> df) {
        df.groupBy("date", "method", "returnCode")
                .count()
                .filter(col("count").$greater$eq(10))
                .select("date", "method", "returnCode", "count")
                .sort("date")
                .write().mode(SaveMode.Overwrite).option("sep", ";")
                .csv(HDFS_URL + TASK2_RESULTS_FOLDER);
    }

    /**
     *  Calculate, by a sliding window in one week, the number of requests ending with codes
     *  4xx and 5xx
     */
    private static void task3(Dataset<RequestRow> df) {
        df.filter(col("returnCode").between(400, 599))
                .groupBy(window(col("date"), "1 week", "1 day"))
                .count()
                .select(date_format(col("window.start"), "yyyy-MM-dd"),
                        date_format(col("window.end"), "yyyy-MM-dd"),
                        col("count"))
                .sort("window.start")
                .write().mode(SaveMode.Overwrite).option("sep", ";")
                .csv(HDFS_URL + TASK3_RESULTS_FOLDER);
    }

}
