package com.its.web.common.listener;

import com.its.common.redis.service.RedisService;
import com.its.common.utils.Constants;
import com.its.common.utils.HttpUtil;
import com.its.web.util.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.IOException;
import java.util.List;

/**
 * Description: SessionListener
 * Company: tzz
 * @Author: tzz
 * Date: 2020/4/20 17:21
 */
@WebListener
public class SessionListener implements HttpSessionListener {
    private static final Logger logger = LoggerFactory.getLogger(SessionListener.class);
    private RedisService redisService;

    public SessionListener(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.error("Session Invalidate");
    }
}
