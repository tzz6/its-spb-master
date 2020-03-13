package com.its.base.servers.es.rhl;

/**
  * Description: ElasticsearchBase
  * Company: tzz
  * @Author: tzz
  * Date: 2019/12/18 17:28
  */
public interface ElasticSearchBase<T> {
    /**
     * 创建索引
     * @param clazz clazz
     * @throws Exception ex
     */
    public void createIndex(Class<T> clazz) throws Exception;
    /**
     * 删除索引
     * @param clazz clazz
     * @throws Exception ex
     */
    public void delete(Class<T> clazz) throws Exception;
    /**
     * 索引是否存在
     * @param clazz clazz
     * @return boolean
     * @throws Exception ex
     */
    public boolean exists(Class<T> clazz) throws Exception;

}
