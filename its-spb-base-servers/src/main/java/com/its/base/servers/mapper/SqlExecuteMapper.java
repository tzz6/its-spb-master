package com.its.base.servers.mapper;

import java.util.List;
import java.util.Map;

/**
 * @author tzz
 */
public interface SqlExecuteMapper {

    /** 
     * execute
     * @param sql
     * @return
     */
    public List<Map<String, Object>> execute(String sql);

    /**
     * get
     * @param sql
     * @return
     */
    public Long getCount(String sql);

    /**
     * executeIUD
     * @param sql
     * @return
     */
    Object executeIUD(String sql);
}
