package com.its.web.util;

import java.util.Date;

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
    
    // JWT(JSON WEB TOKEN)是基于RFC
    // 7519标准定义的一种可以安全传输的小巧和自包含的JSON对象。由于数据是使用数字签名的，所以是可信任的和安全的。JWT可以使用HMAC算法对secret进行加密或者使用RSA的公钥私钥对来进行签名。
    // JWT通常由头部(Header)，负载(Payload)，签名(Signature)三个部分组成，中间以.号分隔，其格式为Header.Payload.Signature
    // Header：声明令牌的类型和使用的算法
    // alg：签名的算法
    //  typ：token的类型，比如JWT
    //  Payload：也称为JWT Claims，包含用户的一些信息
    // 系统保留的声明（Reserved claims）：
    //  iss (issuer)：签发人
    //  exp (expiration time)：过期时间
    //  sub (subject)：主题
    //  aud (audience)：受众用户
    //  nbf (Not Before)：在此之前不可用
    //  iat (Issued At)：签发时间
    //  jti (JWT ID)：JWT唯一标识，能用于防止JWT重复使用

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
   
    /** 秘钥 */
    public static final String SECRET_KEY = "JWT!2#4%6";
    /** token过期时间30分钟 */
    public static final long TOKEN_EXPIRE_TIME = 30 * 60 * 1000;
    /** refreshToken过期时间 */
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 30 * 60 * 1000;
    /** 签发人 */
    private static final String ISSUER = "issuer";

    /**
     * 
     * description: 生成签名token
     * 
     * @author: tzz date: 2019/08/19 15:21
     * @param username 身份标识
     * @param language 语言
     * @return String
     */
    public static String generateToken(String username, String language) {
        // 算法
        Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
        // 签发时间
        Date now = new Date();
        //token过期时间
        Date expiresAt = new Date(now.getTime() + TOKEN_EXPIRE_TIME);
        String token = JWT.create().withIssuer(ISSUER).withIssuedAt(now)
            .withExpiresAt(expiresAt)
            .withClaim("username", username)
            .withClaim("language", language)
            .sign(algorithm);
        return token;
    }

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