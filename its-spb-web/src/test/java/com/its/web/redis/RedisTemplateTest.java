package com.its.web.redis;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.its.web.BaseTest;

/***
 * 
 * @author tzz
 * @工号: 
 * @date 2019/07/11
 * @Introduce: RedisTemplateTest
 */
public class RedisTemplateTest extends BaseTest{
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Test
    public void getRedisTemplate() {
        System.out.println(redisTemplate);
    }

}
