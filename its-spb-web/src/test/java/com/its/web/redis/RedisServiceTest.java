package com.its.web.redis;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.its.common.redis.service.RedisService;
import com.its.web.BaseTest;

/**
  * Description:
  * Company: tzz
  * @Author: tzz
  * Date: 2019/07/11
  */
public class RedisServiceTest extends BaseTest {

    @Autowired
    private RedisService redisService;

    @Test
    public void testString() {
        String key = "test";
        redisService.set(key, "abc");
        System.out.println(redisService.get(key));
        System.out.println(redisService.hasKey(key));
        redisService.del(key);
        System.out.println(redisService.get(key));
    }

    @Test
    public void testHash() {
        String key = "testHash";
        HashMap<String, Object> map = new HashMap<>(16);
        map.put("a", "a");
        map.put("b", 12);
        map.put("c", true);
        map.put("d", new Date());
        User user= new User();
        user.setId(1L);
        user.setName("abc");
        map.put("user", user);
        redisService.hmset(key, map, 100);
        Map<Object, Object> value = redisService.hmget(key);
        System.out.println(value);
        User userValue = (User)value.get("user");
        System.out.println(userValue);
        // 判断hash表中是否有该项的值
        System.out.println(redisService.hHasKey(key, "a"));
        System.out.println(redisService.hHasKey(key, "f"));
        // 删除hash表中的值
        redisService.hdel(key, "a");
        //向一张Hash表中插入数据
        redisService.hset(key, "f", "ff", 100);
        redisService.hset(key, "e", "ee", 100);
        redisService.hset(key, "a", "aa", 100);
        System.out.println(redisService.hasKey(key));
        System.out.println(redisService.hmget(key));
//        redisService.del(key);
        System.out.println(redisService.hmget(key));
    }
    
    @Test
    public void testSet() {
        String key = "testSet";
        Set<String> sets = new HashSet<>();
        sets.add("a");
        sets.add("b");
        sets.add("c");
        redisService.sSet(key, sets);
        Set<Object> getSetS = redisService.sGet(key);
        System.out.println(getSetS);
        redisService.sSet(key, "d");
        redisService.sSet(key, "e");
        System.out.println(redisService.sGet(key));
       
        
    }

    @Test
    public void testSortSet() {
        String key = "testSortSet";
        int max = 10;
        for (int i = 0; i < max; i++) {
            redisService.szSet(key, "zone" + i, new Random().nextInt(10));
        }
        Set<Object> getSetS = redisService.szGet(key, 0, 8);
        System.out.println(getSetS);

    }

}
