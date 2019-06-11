package ru.eta.spark.nasalogparser;


import java.sql.Date;

public class RequestRow {
    private String path;
    private String method;
    private int returnCode;
    private Date date;

    public RequestRow(String method, String path, int returnCode, Date date) {
        this.path = path;
        this.method = method;
        this.returnCode = returnCode;
        this.date = date;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
