package com.its.common.redis.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author tzz
 * @工号: 
 * @date 2019/07/10
 * @Introduce: RedisService
 */
public interface RedisService {

    /**
     * 
     * description: 设置Redis失效时间
     * @author: tzz
     * date: 2019/08/19 16:20
     * @param key 键
     * @param time 时间(秒)
     * @return boolean
     */
    public boolean expire(String key, long time);
    
    /**
     * 
     * description: 设置Redis失效时间
     * @author: tzz
     * date: 2019/08/19 16:48
     * @param key 键
     * @param time 时间
     * @param unit 单位
     * @return boolean
     */
    public boolean expire(String key, long time, TimeUnit unit);

    /**
     * 
          * 根据key获取过期时间
     * 
     * @param key 键不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key);

    /**
     * 
          * 判断key是否存在
     * 
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key);

    /**
     * 
          * 删除缓存
     * 
     * @param key 可以传一个值 或多个
     */
    public void del(String... key);

    /**
     * 
     * String获取
     * 
     * @param key 键
     * @return 值
     * 
     */
    public Object get(String key);

    /**
     * 
     * String保存
     * 
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     * 
     */
    public boolean set(String key, Object value);

    /**
     * 
     * String保存并设置时间
     * 
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     * 
     */
    public boolean set(String key, Object value, long time);

    /**
     * 
          * 递增
     * 
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     * 
     */
    public long incr(String key, long delta);

    /**
     * 
          * 递减
     * 
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     * 
     */
    public long decr(String key, long delta);

    // ================================Hash Map=================================
    /**
     * 
     * Hash Get
     * 
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     * 
     */
    public Object hget(String key, String item);

    /**
     * 
          * 获取hashKey对应的所有键值
     * 
     * @param key 键
     * @return 对应的多个键值
     * 
     */
    public Map<Object, Object> hmget(String key);

    /**
     * 
     * Hash数据类型 set
     * 
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     * 
     */
    public boolean hmset(String key, Map<String, Object> map);

    /**
     * 
     * Hash数据类型 set并设置时间
     * 
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     * 
     */
    public boolean hmset(String key, Map<String, Object> map, long time);

    /**
     * 
          * 向一张Hash表中放入数据,如果不存在将创建
     * 
     * @param key   redis key
     * @param item  hash key
     * @param value hash value
     * @return true 成功 false失败
     * 
     */
    public boolean hset(String key, String item, Object value);

    /**
     * 
          * 向一张Hash表中加入数据,如果不存在将创建
     * 
     * @param key   redis key
     * @param item  hash key
     * @param value hash value
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     * 
     */
    public boolean hset(String key, String item, Object value, long time);

    /**
     * 
          * 删除Hash表中的值
     * 
     * @param key  redis key 不能为null
     * @param item hash key 可以使多个 不能为null
     * 
     */
    public void hdel(String key, Object... item);

    /**
     * 
          * 判断Hash表中是否有该项的值
     * 
     * @param key  redis key 不能为null
     * @param item hash key 不能为null
     * @return true 存在 false不存在
     * 
     */
    public boolean hHasKey(String key, String item);

    /**
     * 
     * Hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * 
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     * 
     */
    public double hincr(String key, String item, double by);

    /**
     * 
     * Hash递减
     * 
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     * 
     */
    public double hdecr(String key, String item, double by);

    // ============================set=============================
    /**
     * 
          * 根据key获取Set中的所有值
     * 
     * @param key 键
     * @return
     * 
     */
    public Set<Object> sGet(String key);

    /**
     * 
          * 根据value从一个set中查询,是否存在
     * 
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     * 
     */
    public boolean sHasKey(String key, Object value);

    /**
     * 
          * 将数据放入set缓存
     * 
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     * 
     */
    public long sSet(String key, Object... values);

    /**
     * 
          * 将set数据放入缓存
     * 
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     * 
     */
    public long sSetAndTime(String key, long time, Object... values);

    /**
     * 
          * 获取set缓存的长度
     * 
     * @param key 键
     * @return
     * 
     */
    public long sGetSetSize(String key);

    /**
     * 
          * 移除值为value的
     * 
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     * 
     */
    public long setRemove(String key, Object... values);
    
    // ============================sort set=============================
    /**
     * 
     * description: 据key获取ZSet中的值
     * @author: tzz
     * date: 2019/11/12 18:44
     * @param key
     * @param start
     * @param end
     * @return Set<Object>
     */
    public Set<Object> szGet(String key, long start, long end);
    
    /**
     * 
     * 根据value从一个set中查询,是否存在
     * 
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     * 
     */
    public boolean szHasKey(String key, Object value);
    
    /**
     * 
          * 将数据放入set缓存
     * 
     * @param key    键
     * @param value 值
     * @param score 权重
     * @return 是否成功
     * 
     */
    public boolean szSet(String key, Object value, double score);
    
    /**
     * 
          * 将set数据放入缓存
     * 
     * @param key    键
     * @param time   时间(秒)
     * @param value 值
     * @param score 权重
     * @return 是否成功
     * 
     */
    public boolean szSetAndTime(String key, long time, Object value, double score);
    
    /**
     * 
     * 获取set缓存的长度
     * 
     * @param key 键
     * @return
     * 
     */
    public long szGetSetSize(String key);
    
    /**
     * 
     * 移除值为value的
     * 
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     * 
     */
    public long zsetRemove(String key, Object... values);

    // ===============================list=================================
    /**
     * 
          * 获取list缓存的内容
     * 
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return
     * 
     */
    public List<Object> lGet(String key, long start, long end);

    /**
     * 
          * 获取list缓存的长度
     * 
     * @param key 键
     * @return
     * 
     */
    public long lGetListSize(String key);

    /**
     * 
          * 通过索引 获取list中的值
     * 
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     * 
     */
    public Object lGetIndex(String key, long index);

    /**
     * 
          * 将list放入缓存
     * 
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     * 
     */
    public boolean lSet(String key, Object value);

    /**
     * 
          * 将list放入缓存
     * 
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     * 
     */
    public boolean lSet(String key, Object value, long time);

    /**
     * 
          * 将list放入缓存
     * 
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     * 
     */
    public boolean lSet(String key, List<Object> value);

    /**
     * 
          * 将list放入缓存
     * 
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     * 
     */
    public boolean lSet(String key, List<Object> value, long time);

    /**
     * 
          * 根据索引修改list中的某条数据
     * 
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     * 
     */
    public boolean lUpdateIndex(String key, long index, Object value);

    /**
     * 
          * 移除N个值为value
     * 
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     * 
     */
    public long lRemove(String key, long count, Object value);
}