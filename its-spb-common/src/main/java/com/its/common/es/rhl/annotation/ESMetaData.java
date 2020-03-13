package com.its.common.es.rhl.annotation;

import java.lang.annotation.*;

/**
  * Description: es索引元数据的注解，在es entity class上添加
  * Company: tzz
  * @Author: tzz
  * Date: 2019/12/18 16:35
  */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface ESMetaData {
    /**
     * 索引名称，必须配置
     */
    String indexName();
    /**
     * 索引类型，必须配置，墙裂建议每个index下只有一个type
     */
    String indexType();
    /**
     * 主分片数量
     */
    int numberOfShards() default 5;
    /**
     * 备份分片数量
     */
    int numberOfReplicas() default 1;

    /**
     * 是否打印日志
     */
    boolean printLog() default false;
}
