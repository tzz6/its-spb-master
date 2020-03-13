package com.its.common.es.rhl.util;

import com.its.common.es.rhl.annotation.ESMapping;
import com.its.common.es.rhl.annotation.ESMetaData;

import java.lang.reflect.Field;

/**
 * Description: 索引信息操作工具类
 * Company: tzz
 *
 * @Author: tzz
 * Date: 2019/12/18 16:52
 */
public class IndexTools {
    /**
     * 获取索引元数据：indexname、indextype
     *
     * @param clazz clazz
     * @return MetaData
     */
    public static MetaData getIndexType(Class<?> clazz) {
        if (clazz.getAnnotation(ESMetaData.class) != null) {
            String indexName = clazz.getAnnotation(ESMetaData.class).indexName();
            String indextype = clazz.getAnnotation(ESMetaData.class).indexType();
            MetaData metaData = new MetaData(indexName, indextype);
            metaData.setPrintLog(clazz.getAnnotation(ESMetaData.class).printLog());
            return metaData;
        }
        return null;
    }

    /**
     * 获取索引元数据：主分片、备份分片数的配置
     *
     * @param clazz clazz
     * @return MetaData
     */
    public static MetaData getShardsConfig(Class<?> clazz) {
        if (clazz.getAnnotation(ESMetaData.class) != null) {
            int numberOfShards = clazz.getAnnotation(ESMetaData.class).numberOfShards();
            int numberOfReplicas = clazz.getAnnotation(ESMetaData.class).numberOfReplicas();
            MetaData metaData = new MetaData(numberOfShards, numberOfReplicas);
            metaData.setPrintLog(clazz.getAnnotation(ESMetaData.class).printLog());
            return metaData;
        }
        return null;
    }

    /**
     * 获取索引元数据：indexname、indextype、主分片、备份分片数的配置
     *
     * @param clazz
     * @return
     */
    public static MetaData getMetaData(Class<?> clazz) {
        if (clazz.getAnnotation(ESMetaData.class) != null) {
            String indexName = clazz.getAnnotation(ESMetaData.class).indexName();
            String indextype = clazz.getAnnotation(ESMetaData.class).indexType();
            int numberOfShards = clazz.getAnnotation(ESMetaData.class).numberOfShards();
            int numberOfReplicas = clazz.getAnnotation(ESMetaData.class).numberOfReplicas();
            MetaData metaData = new MetaData(indexName, indextype, numberOfShards, numberOfReplicas);
            metaData.setPrintLog(clazz.getAnnotation(ESMetaData.class).printLog());
            return metaData;
        }
        return null;
    }

    /**
     * 获取配置于Field上的mapping信息，如果未配置注解，则给出默认信息
     *
     * @param field filed
     * @return MappingData
     */
    public static MappingData getMappingData(Field field) {
        if (field == null) {
            return null;
        }
        field.setAccessible(true);
        MappingData mappingData = new MappingData();
        mappingData.setFieldName(field.getName());
        ESMapping esMapping = field.getAnnotation(ESMapping.class);
        if (esMapping != null) {
            //将枚举类中的数据类型去掉后缀_type
            mappingData.setDatatype(esMapping.datatype().toString().replaceAll("_type", ""));
            mappingData.setAnalyzer(esMapping.analyzer().toString());
            mappingData.setAutocomplete(esMapping.autocomplete());
            mappingData.setIgnoreAbove(esMapping.ignore_above());
            mappingData.setSearchAnalyzer(esMapping.search_analyzer().toString());
            mappingData.setKeyword(esMapping.keyword());
            mappingData.setSuggest(esMapping.suggest());
            mappingData.setAllowSearch(esMapping.allow_search());
            mappingData.setCopyTo(esMapping.copy_to());
        } else {
            mappingData.setDatatype("text");
            mappingData.setAnalyzer("standard");
            mappingData.setAutocomplete(false);
            mappingData.setIgnoreAbove(256);
            mappingData.setSearchAnalyzer("standard");
            mappingData.setKeyword(true);
            mappingData.setSuggest(false);
            mappingData.setAllowSearch(true);
            mappingData.setCopyTo("");
        }
        return mappingData;
    }

    /**
     * 批量获取配置于Field上的mapping信息，如果未配置注解，则给出默认信息
     *
     * @param clazz clazz
     * @return MappingData
     */
    public static MappingData[] getMappingData(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        MappingData[] mappingDataList = new MappingData[fields.length];
        for (int i = 0; i < fields.length; i++) {
            if ("serialVersionUID".equals(fields[i].getName())) {
                continue;
            }
            mappingDataList[i] = getMappingData(fields[i]);
        }
        return mappingDataList;
    }

}
