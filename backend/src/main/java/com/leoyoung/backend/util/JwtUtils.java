package com.leoyoung.backend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类 —— 负责 Token 的签发、解析、校验
 * 根据文档要求：JWT 仅由后端签发、客户端保存，不存数据库，不实现 Refresh Token
 */
@Component // 交给 Spring 管理，便于后续 Service 注入
public class JwtUtils {

    /** 签名密钥（练手项目硬编码，生产环境应放配置文件） */
    private static final String SECRET = "TeacraftStudio2024SecretKeyForJWT!";
    /** Token 过期时间：7 天 */
    private static final long EXPIRE_MS = 7 * 24 * 60 * 60 * 1000L;

    /** 根据密钥字符串生成 HMAC-SHA256 签名 Key */
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    /**
     * 签发 JWT
     * @param userId   用户或管理员 ID
     * @param userType 用户类型：user 或 admin，用于区分 C 端和商家端
     * @return JWT 字符串
     */
    public String generateToken(Long userId, String userType) {
        Date now = new Date();
        return Jwts.builder()
                .claim("userId", userId)
                .claim("userType", userType)
                .setIssuedAt(now)                              // 签发时间
                .setExpiration(new Date(now.getTime() + EXPIRE_MS)) // 过期时间
                .signWith(key, SignatureAlgorithm.HS256)        // HS256 签名
                .compact();
    }

    /**
     * 解析并校验 Token，返回 Claims 数据体
     * @param token JWT 字符串
     * @return Claims 对象，校验失败返回 null
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // Token 过期、格式错误、签名不匹配统一返回 null
            return null;
        }
    }

    /** 从 Token 中提取用户 ID */
    public Long getUserId(String token) {
        Claims claims = parseToken(token);
        return claims != null ? claims.get("userId", Long.class) : null;
    }

    /** 从 Token 中提取用户类型（user / admin） */
    public String getUserType(String token) {
        Claims claims = parseToken(token);
        return claims != null ? claims.get("userType", String.class) : null;
    }

    /** 判断 Token 是否有效 */
    public boolean isValid(String token) {
        return parseToken(token) != null;
    }
}
