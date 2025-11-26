package com.library.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret:}")
    private String secret;

    // 当未通过配置或环境变量提供 secret 时回退的开发默认值（仅用于本地开发）
    private static final String DEFAULT_SECRET = "library-management-system-secret-key-2024-very-long-secret-key-for-production-use-at-least-256-bits";

    @Value("${jwt.expiration:86400000}") // 24小时，单位：毫秒
    private Long expiration;

    /**
     * 生成Token
     */
    public String generateToken(Long userId, String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("role", role);
        return createToken(claims, username);
    }

    /**
     * 创建Token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        SecretKey key = getSigningKey();

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key)
                .compact();
    }

    /**
     * 从Token中获取Claims
     */
    public Claims getClaimsFromToken(String token) {
        try {
            SecretKey key = getSigningKey();
            // 使用与项目编译环境兼容的 JJWT API：parser() 返回一个 builder-like 对象，调用 build() 获取解析器
            return Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            logger.warn("解析 JWT 失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从Token中获取用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    /**
     * 从Token中获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims != null && claims.get("userId") != null) {
            return Long.valueOf(claims.get("userId").toString());
        }
        return null;
    }

    /**
     * 从Token中获取角色
     */
    public String getRoleFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? (String) claims.get("role") : null;
    }

    /**
     * 验证Token是否有效
     */
    public Boolean validateToken(String token, String username) {
        String tokenUsername = getUsernameFromToken(token);
        return (tokenUsername != null && tokenUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * 判断Token是否过期
     */
    private Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                return true;
            }
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 获取Token过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? claims.getExpiration() : null;
    }

    private SecretKey getSigningKey() {
        String effective = secret;
        if (effective == null || effective.isEmpty()) {
            // 尝试从环境变量读取
            String env = System.getenv("JWT_SECRET");
            if (env != null && !env.isEmpty()) {
                effective = env;
            } else {
                // 回退到开发默认值，记录警告
                effective = DEFAULT_SECRET;
                logger.warn("未配置 JWT secret，已使用开发默认 secret（仅用于本地开发），建议在运行时通过环境变量 JWT_SECRET 注入真实密钥");
            }
        }

        byte[] keyBytes = effective.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalStateException("JWT secret 太短，必须至少 32 字节（256 位）。请设置更长的 JWT_SECRET 或配置 jwt.secret。");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }
}


