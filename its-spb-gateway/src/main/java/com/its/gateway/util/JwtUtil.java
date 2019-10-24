package com.its.gateway.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;

/**
 * description: JWT(JSON WEB TOKEN)
 * company: 顺丰科技有限公司国际业务科技部
 * @author: 01115486
 * date: 2019/08/19 15:17
 */
public class JwtUtil { 
    
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    /** 秘钥 */
    public static final String SECRET_KEY = "JWT!2#4%6";
    /** 签发人 */
    private static final String ISSUER = "issuer";

    /**
     * 
     * description: 验证token
     * 
     * @author: tzz 
     * date: 2019/08/19 15:22
     * @param token
     * @return boolean
     */
    public static boolean verify(String token) {
        try {
            // 算法
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            log.error("JwtUtil verify" + "_" + e.getMessage(), e);
        }
        return false;
    }

    /**
     * 
     * description: 从token获取username
     * 
     * @author: tzz 
     * date: 2019/08/19 15:23
     * @param token
     * @return String
     */
    public static String getUsername(String token) {
        try {
            return JWT.decode(token).getClaim("username").asString();
        } catch (Exception e) {
            log.error("JwtUtil getUsername" + "_" + e.getMessage(), e);
        }
        return "";
    }
}