package com.its.web.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.its.web.BaseTest;

/**
  * Description: RedisTemplateTest
  * Company: tzz
  * @Author: tzz
  * Date: 2019/07/11
  */
public class RedisTemplateTest extends BaseTest{
    
    @Autowired(required = true)
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String HASHMAP_KEY = "REDISTEMPLATE_HASHMAP_KEY";
    
    @Test
    public void getRedisTemplate() {
        System.out.println(redisTemplate);
    }
    
    public void hmget(String key) {
        Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
        System.out.println(map);
        map = stringRedisTemplate.opsForHash().entries(key);
        System.out.println(map);
    }

    @Test
    public void hmset() {
        redisTemplate.opsForValue().set("token", "16235dd");
        System.out.println(redisTemplate.opsForValue().get("token"));
        Map<String, String> map = new HashMap<>(16);
        map.put("token", "16235dd");
        map.put("username", "abc");
        map.put("lang", "zh");
        redisTemplate.opsForHash().putAll(HASHMAP_KEY, map);
        // 设置过期时间
        redisTemplate.expire(HASHMAP_KEY, 30, TimeUnit.SECONDS);
        System.err.println(redisTemplate.getExpire(HASHMAP_KEY));
        hmget(HASHMAP_KEY);
    }

}
