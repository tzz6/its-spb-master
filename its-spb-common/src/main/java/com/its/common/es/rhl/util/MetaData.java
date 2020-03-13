package com.its.common.es.rhl.util;

/**
  * Description: MetaData元数据载体类
  * Company: tzz
  * @Author: tzz
  * Date: 2019/12/18 16:46
  */
public class MetaData {
    public MetaData(String indexName, String indextype) {
        this.indexName = indexName;
        this.indextype = indextype;
    }

    private String indexName = "";
    private String indextype = "";
    private int numberOfShards;
    private int numberOfReplicas;

    boolean printLog = false;

    public boolean isPrintLog() {
        return printLog;
    }

    public void setPrintLog(boolean printLog) {
        this.printLog = printLog;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getIndextype() {
        return indextype;
    }

    public void setIndextype(String indextype) {
        this.indextype = indextype;
    }

    public int getNumberOfShards() {
        return numberOfShards;
    }

    public void setNumberOfShards(int numberOfShards) {
        this.numberOfShards = numberOfShards;
    }

    public int getNumberOfReplicas() {
        return numberOfReplicas;
    }

    public void setNumberOfReplicas(int numberOfReplicas) {
        this.numberOfReplicas = numberOfReplicas;
    }

    public MetaData(String indexName, String indextype, int numberOfShards, int numberOfReplicas) {
        this.indexName = indexName;
        this.indextype = indextype;
        this.numberOfShards = numberOfShards;
        this.numberOfReplicas = numberOfReplicas;
    }

    public MetaData(int numberOfShards, int numberOfReplicas) {
        this.numberOfShards = numberOfShards;
        this.numberOfReplicas = numberOfReplicas;
    }
}