package com.example.spb.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final String SECRET = "zxcvhdakuwsfhgiowreqgoireqwhoitfguerqwiufhrewuighroew";
    private static final long EXPIRE = 5*60*1000;

    public static final String HEADER = "Authorization";

    public String generateToken(String jobID) {
        // 生成签名密钥
        byte[] signingKeyBytes = SECRET.getBytes(StandardCharsets.UTF_8);
        // 使用HMAC SHA-256算法生成签名密钥
        byte[] signingKey = Keys.hmacShaKeyFor(signingKeyBytes).getEncoded();

        // 设置token过期时间
        LocalDateTime tokenExpirationTime = LocalDateTime.now().plusMinutes(EXPIRE);
        Date expirationDate = java.sql.Timestamp.valueOf(tokenExpirationTime);

        // 生成JWT token
        String token = Jwts.builder()
                .setSubject(jobID) // 设置token的主题为jobID
                .setExpiration(expirationDate) // 设置token的过期时间
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS256) // 设置签名密钥和算法
                .compact(); // 生成token字符串
        return token;
    }
    public Claims getClaimsByToken(String token) {
        try {
            // 解析 token
            byte[] signingKeyBytes = SECRET.getBytes(StandardCharsets.UTF_8);
            byte[] signingKey = Keys.hmacShaKeyFor(signingKeyBytes).getEncoded();

            Jws<Claims> parsedToken = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(signingKey)) // 使用与生成 token 相同的密钥和算法
                    .build()
                    .parseClaimsJws(token);

            // 提取用户信息，假设信息存储在 "jobID" claim 中
            return parsedToken.getBody();
        } catch (Exception e) {
            // 如果解析 token 失败，打印错误信息并返回 null
            System.err.println("Error extracting user info from token: " + e.getMessage());
            return null;
        }
    }
    public boolean isVerify(String token) {
        try {
            // 解析 token
            byte[] signingKeyBytes = SECRET.getBytes(StandardCharsets.UTF_8);
            byte[] signingKey = Keys.hmacShaKeyFor(signingKeyBytes).getEncoded();

            Jws<Claims> parsedToken = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(signingKey)) // 使用与生成 token 相同的密钥和算法
                    .build()
                    .parseClaimsJws(token);

            // 检查 token 是否在有效期内
            Date expiration = parsedToken.getBody().getExpiration();
            return expiration.after(new Date()); // 如果过期时间在当前时间之后，返回 true
        } catch (Exception e) {
            // 如果解析 token 失败或 token 过期，则捕获异常并返回 false
            System.err.println("Token validation error: " + e.getMessage());
            return false;
        }
    }
    }

