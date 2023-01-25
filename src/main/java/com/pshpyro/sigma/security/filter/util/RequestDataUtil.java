package com.pshpyro.sigma.security.filter.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pshpyro.sigma.security.jwt.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RequestDataUtil {
    private final JwtTokenUtil jwtTokenUtil;
    private final ObjectMapper objectMapper;

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String tokenStartsWith = "Bearer ";
        if (requestTokenHeader != null && requestTokenHeader.startsWith(tokenStartsWith)) {
            return Optional.of(requestTokenHeader.substring(tokenStartsWith.length()));
        }

        return Optional.empty();
    }

    public Optional<String> extractSubjectFromToken(String jwtToken) {
        String subject = null;
        try {
            subject = jwtTokenUtil.getSubjectFromToken(jwtToken);
        } catch (IllegalArgumentException e) {
            System.err.println("Unable to get JWT"); // TODO: -> to logger
        } catch (ExpiredJwtException e) {
            System.err.println("JWT has expired"); // TODO: use good solution for expired token
        }

        return Optional.ofNullable(subject);
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        final String refreshTokenKeyName = "refresh_token";
        return Optional.ofNullable(request.getParameter(refreshTokenKeyName));
    }
}
