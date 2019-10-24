package com.its.gateway.filter;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import com.its.gateway.util.JwtUtil;

import reactor.core.publisher.Mono;

/**
 * 
 * description: JWT鉴权
 * company: tzz
 * @author: tzz
 * date: 2019/08/17 14:53
 */
@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {
    private final Logger log = LoggerFactory.getLogger(IpBlackListFilter.class);
    private static final String ITS_TOKEN = "its-token";
    private static final String REFRESHTOKEN = "its-refreshToken";
    /** refreshToken过期时间 */
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 30 * 60 * 1000;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String url = exchange.getRequest().getURI().getPath();

        // 不需要鉴权URL
        String[] noAuthlist = new String[] {"/auth-service/", "/api-base/login", "/api-base/sysRole/updateSysRole"};
        if (checkFilter(url, noAuthlist)) {
            // 从请求头中取得token
            String refreshToken = exchange.getRequest().getHeaders().getFirst(REFRESHTOKEN);
            String token = exchange.getRequest().getHeaders().getFirst(ITS_TOKEN);
            // 从cookie中取得token(登录成功后将token设置到cookie，请求的header中会带上cookie)
            MultiValueMap<String, HttpCookie> cookies = exchange.getRequest().getCookies();
            List<HttpCookie> refreshTokenHttpCookieList = cookies.get(REFRESHTOKEN);
            if (refreshTokenHttpCookieList != null && refreshTokenHttpCookieList.size() > 0) {
                refreshToken = refreshTokenHttpCookieList.get(0).getValue();
            }
            List<HttpCookie> tokenHttpCookieList = cookies.get(ITS_TOKEN);
            if (tokenHttpCookieList != null && tokenHttpCookieList.size() > 0) {
                token = tokenHttpCookieList.get(0).getValue();
            }
            log.info("its refreshToken: {} its token : {}", refreshToken, token);
            boolean tokenFlag = true;
            String message = null;
            // token为空则鉴权失败
            if ((token == null || "".equals(token))) {
                tokenFlag = false;
                message = "{\"code\": \"401\",\"msg\": \"401 Unauthorized.\"}";

            }
            // token验证非法
            boolean verifyResult = JwtUtil.verify(token);
            if (!verifyResult) {
                tokenFlag = false;
                message = "{\"code\": \"1004\",\"msg\": \"1004 Invalid Token.\"}";
            }
            // 请求中的token是否在redis中存在
            String username = null;
            String lang = null;
            String redisToken = null;
            Map<Object, Object> map = redisTemplate.opsForHash().entries(refreshToken);
            username = map.get("username").toString();
            lang = map.get("lang").toString();
            redisToken = map.get("token").toString();
            //注意：从Redis获取到的字符数据需要去掉前后双引号
            // redis中保存的会话为空或与当前请求用户的token不一致，请求非法
            if (redisToken == null || !token.equals(redisToken.replace("\"", ""))) {
                tokenFlag = false;
                message = "{\"code\": \"1004\",\"msg\": \"1004 Invalid Token.\"}";
            }
            if (!tokenFlag) {
                // 鉴权失败
                ServerHttpResponse response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
                log.info("401 Unauthorized");
                byte[] responseByte = message.getBytes(StandardCharsets.UTF_8);
                DataBuffer buffer = response.bufferFactory().wrap(responseByte);
                return response.writeWith(Mono.just(buffer));
            } else {
                // 鉴权成功
                ServerHttpRequest request = exchange.getRequest();
                if (username != null && lang != null) {
                    // 将用户信息添加至header,用于微服务后台获取当前登录用户名
                    request.mutate().header("its-username", username.replace("\"", ""))
                        .header("its-language", lang.replace("\"", "")).build();
                }
                // 刷新redis中登录会话的过期时间
                redisTemplate.expire(refreshToken, REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }

    /** 检查是否是要过滤 */
    protected boolean checkFilter(String value, String[] notFilter) {
        for (String str : notFilter) {
            if (str.equals(value)) {
                return false;
            }
        }
        return true;
    }
    
}
