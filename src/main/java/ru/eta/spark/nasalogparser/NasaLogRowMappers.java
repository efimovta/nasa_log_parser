package ru.eta.spark.nasalogparser;

import org.apache.spark.api.java.function.MapFunction;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NasaLogRowMappers {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.US);
    private static final Pattern REQUEST_ROW_PATTERN = Pattern.compile("(.*)( - - )\\[(.*)\\].*\"([a-zA-Z]*) (.*)(\"[^0-9]*)([0-9]*)(.*$)");

    public static final MapFunction<String, RequestRow> toRequestRow = s -> {
        Matcher m = REQUEST_ROW_PATTERN.matcher(s);
        if (m.find()) {
            String method = m.group(4);
            String path = m.group(5);
            int returnCode = Integer.valueOf(m.group(7));
            Date date = Date.valueOf(LocalDate.parse(m.group(3), DATE_TIME_FORMATTER));
            return new RequestRow(method, path, returnCode, date);
        }
        System.out.println("Error matching request row: " + s + ". ");
        return null;
    };
}
