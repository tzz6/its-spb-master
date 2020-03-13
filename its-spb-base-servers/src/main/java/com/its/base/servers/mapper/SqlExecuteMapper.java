package com.its.base.servers.mapper;

import java.util.List;
import java.util.Map;

/**
 * @author tzz
 */
public interface SqlExecuteMapper {

    /** 
     * execute
     * @param sql sql
     * @return
     */
    List<Map<String, Object>> execute(String sql);

    /**
     * get
     * @param sql sql
     * @return
     */
    Long getCount(String sql);

    /**
     * executeIUD
     * @param sql sql
     * @return
     */
    Object executeIUD(String sql);
}
