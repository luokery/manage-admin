package com.example.manageadmin.shiro.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * JWT 工具类
 * 用于生成、解析和验证 JWT Token
 */
@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret:mySecretKeyForJwtTokenGenerationAndValidationMustBeAtLeast256BitsLong}")
    private String secret;

    @Value("${jwt.expiration:86400000}") // 默认24小时
    private Long expiration;

    @Value("${jwt.header:Authorization}")
    private String header;

    @Value("${jwt.prefix:Bearer }")
    private String prefix;

    /**
     * 生成密钥
     */
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 生成 JWT Token
     *
     * @param userId   用户ID
     * @param username 用户名
     * @param role     角色
     * @return JWT Token
     */
    public String generateToken(Long userId, String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);
        claims.put("role", role);
        return createToken(claims, username);
    }

    /**
     * 创建 Token
     */
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + expiration);

        //这里其实就是new一个JwtBuilder，设置jwt的body
        JwtBuilder builder = Jwts.builder()
            //如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
            .claims(claims)
            //设置JWT ID：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
            .id(UUID.randomUUID().toString())
            //jwt的签发时间
            .issuedAt(now)
            //代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
            .subject(subject)
            //设置过期时间
            .expiration(expirationDate)
            //设置签名使用的签名算法和签名使用的秘钥
            .signWith(getSecretKey());
        
        return builder.compact();
    }

    /**
     * 从 Token 中获取用户名
     */
    public String getUsernameFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            log.error("从Token获取用户名失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从 Token 中获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.get("userId", Long.class);
        } catch (Exception e) {
            log.error("从Token获取用户ID失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从 Token 中获取角色
     */
    public String getRoleFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.get("role", String.class);
        } catch (Exception e) {
            log.error("从Token获取角色失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 解析 Token 获取 Claims
     */
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
            		// 设置签名的秘钥
                    .verifyWith(getSecretKey())
                    .build()
                    // 设置需要解析的jwt
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("解析Token失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 验证 Token 是否有效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                return false;
            }
            return !isTokenExpired(claims);
        } catch (Exception e) {
            log.error("验证Token失败: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 判断 Token 是否过期
     */
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    /**
     * 获取 Token 过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getExpiration();
        } catch (Exception e) {
            log.error("获取Token过期时间失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 判断 Token 是否可以刷新
     */
    public boolean canRefresh(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims != null && !isTokenExpired(claims);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 刷新 Token
     */
    public String refreshToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims == null) {
                return null;
            }
            Long userId = claims.get("userId", Long.class);
            String username = claims.getSubject();
            String role = claims.get("role", String.class);
            return generateToken(userId, username, role);
        } catch (Exception e) {
            log.error("刷新Token失败: {}", e.getMessage());
            return null;
        }
    }

    public String getHeader() {
        return header;
    }

    public String getPrefix() {
        return prefix;
    }
}
