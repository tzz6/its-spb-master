package com.its.common.redis.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.its.common.redis.service.RedisService;

/**
  * Description: RedisServiceImpl
  * Company: tzz
  * @Author: tzz
  * Date: 2019/07/10
  */
@Service
public class RedisServiceImpl implements RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean expire(String key, long time) {
        boolean flag = false;
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            flag = true;
        } catch (Exception e) {
            logger.error("redis expire:" + e.getMessage(), e);
        }
        return flag;
    }

    @Override
    public boolean expire(String key, long time, TimeUnit unit) {
        boolean flag = false;
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, unit);
            }
            flag = true;
        } catch (Exception e) {
            logger.error("redis expire:" + e.getMessage(), e);
        }
        return flag;
    }

    @Override
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    @Override
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            logger.error("redis hasKey:" + e.getMessage(), e);
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /** ================String================ */
    @Override
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    @Override
    public boolean set(String key, Object value) {
        boolean flag = false;
        try {
            redisTemplate.opsForValue().set(key, value);
            flag = true;
        } catch (Exception e) {
            logger.error("redis set:" + e.getMessage(), e);
        }
        return flag;
    }

    @Override
    public boolean set(String key, Object value, long time) {
        boolean flag = false;
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            flag = true;
        } catch (Exception e) {
            logger.error("redis set:" + e.getMessage(), e);
        }
        return flag;
    }

    @Override
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    /** ================Hash Map================ */
    @Override
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    @Override
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public boolean hmset(String key, Map<String, Object> map) {
        boolean flag = false;
        try {
            redisTemplate.opsForHash().putAll(key, map);
            flag = true;
        } catch (Exception e) {
            logger.error("redis hmset:" + e.getMessage(), e);
        }
        return flag;
    }

    @Override
    public boolean hmset(String key, Map<String, Object> map, long time) {
        boolean flag = false;
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                boolean expire = expire(key, time);
                logger.info("expire:" + expire);
            }
            flag = true;
        } catch (Exception e) {
            logger.error("redis hmset:" + e.getMessage(), e);
        }
        return flag;
    }

    @Override
    public boolean hset(String key, String item, Object value) {
        boolean flag = false;
        try {
            redisTemplate.opsForHash().put(key, item, value);
            flag = true;
        } catch (Exception e) {
            logger.error("redis hmset put:" + e.getMessage(), e);
        }
        return flag;
    }

    @Override
    public boolean hset(String key, String item, Object value, long time) {
        boolean flag = false;
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            flag = true;
        } catch (Exception e) {
            logger.error("redis hmset put:" + e.getMessage(), e);
        }
        return flag;
    }

    @Override
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    @Override
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    @Override
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    @Override
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /** ================set================ */
    @Override
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            logger.error("redis sGet:" + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            logger.error("redis sHasKey:" + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            logger.error("redis sSet:" + e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            logger.error("redis sSetAndTime:" + e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            logger.error("redis sGetSetSize:" + e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            logger.error("redis setRemove:" + e.getMessage(), e);
            return 0;
        }
    }

    /** ================sort set================ */
    @Override
    public Set<Object> szGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            logger.error("redis sGet:" + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean szHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            logger.error("redis sHasKey:" + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean szSet(String key, Object value, double score) {
        try {
            return redisTemplate.opsForZSet().add(key, value, score);
        } catch (Exception e) {
            logger.error("redis sSet:" + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean szSetAndTime(String key, long time, Object value, double score) {
        boolean flag = true;
        try {
            flag = redisTemplate.opsForZSet().add(key, value, score);
            if (time > 0) {
                expire(key, time);
            }
        } catch (Exception e) {
            logger.error("redis sSetAndTime:" + e.getMessage(), e);
            flag = false;
        }
        return flag;
    }

    @Override
    public long szGetSetSize(String key) {
        try {
            return redisTemplate.opsForZSet().size(key);
        } catch (Exception e) {
            logger.error("redis sGetSetSize:" + e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public long zsetRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForZSet().remove(key, values);
            return count;
        } catch (Exception e) {
            logger.error("redis setRemove:" + e.getMessage(), e);
            return 0;
        }
    }

    /** ================list================ */
    @Override
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            logger.error("redis lGet:" + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            logger.error("redis lGetListSize:" + e.getMessage(), e);
            return 0;
        }
    }

    @Override
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            logger.error("redis lGetIndex:" + e.getMessage(), e);
            return null;
        }
    }

    @Override
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            logger.error("redis lSet:" + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error("redis lSet:" + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            logger.error("redis lSet:" + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            logger.error("redis lSet:" + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            logger.error("redis lUpdateIndex:" + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public long lRemove(String key, long count, Object value) {
        try {
            return redisTemplate.opsForList().remove(key, count, value);
        } catch (Exception e) {
            logger.error("redis lRemove:" + e.getMessage(), e);
            return 0;
        }
    }
}
