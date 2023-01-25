package com.pshpyro.sigma.security.jwt.util;

import com.pshpyro.sigma.user.entity.User;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
    private static final long ACCESS_TOKEN_LIFETIME_DURATION_IN_MILLIS = 15 * 60 * 1000; // TODO change or USE @Value
    private static final long REFRESH_TOKEN_LIFETIME_DURATION_IN_MILLIS = 30L * 24 * 60 * 60 * 1000; // TODO change or USE @Value
    private String secret;
    private JwtParser jwtParser;
    private JwtBuilder jwtBuilder;

    public JwtTokenUtil(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
        jwtParser = Jwts.parserBuilder().setSigningKey(secret).build();
        jwtBuilder = Jwts.builder();
    }

    public String getSubjectFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return jwtParser.parseClaimsJws(token).getBody(); // TODO: use secret.getBytes() instead of secret
        // TODO add Key as in video https://youtu.be/KxqlJblhzfI?t=3689
    }

    public boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateRefreshToken(String subject) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateRefreshToken(claims, subject);
    }

    public String generateAccessToken(User user) {
        Map<String, Object> claims = Map.of("roles", user.getRoles());
        String subject = user.getId().toString();
        return doGenerateAccessToken(claims, subject);
    }

    public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
        return doGenerateToken(claims, subject, REFRESH_TOKEN_LIFETIME_DURATION_IN_MILLIS);
    }

    public String doGenerateAccessToken(Map<String, Object> claims, String subject) {
        return doGenerateToken(claims, subject, ACCESS_TOKEN_LIFETIME_DURATION_IN_MILLIS);
    }

    public String doGenerateToken(Map<String, Object> claims, String subject, long lifetimeDurationInMilliseconds) {
        return jwtBuilder
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + lifetimeDurationInMilliseconds))
                .signWith(SignatureAlgorithm.HS256, secret) // TODO: use secret.getBytes() instead of secret
                .compact();
    }
}
