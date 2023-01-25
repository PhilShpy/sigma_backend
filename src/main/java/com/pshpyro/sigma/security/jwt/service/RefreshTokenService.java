package com.pshpyro.sigma.security.jwt.service;

import com.pshpyro.sigma.security.jwt.entity.RefreshToken;
import com.pshpyro.sigma.user.entity.User;

import java.util.List;
import java.util.UUID;

public interface RefreshTokenService {
    List<RefreshToken> getByUserId(UUID userId);
    List<RefreshToken> getByUser(User user);
    void save(RefreshToken refreshToken);
    boolean existsByValueAndUserIdAndValid(String value, UUID userId);
    boolean existsByValueAndUserAndValid(String value, User user);
    RefreshToken getByValue(String value);
    void deleteByValue(String value);
}
