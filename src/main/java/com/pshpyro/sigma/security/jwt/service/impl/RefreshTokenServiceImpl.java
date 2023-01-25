package com.pshpyro.sigma.security.jwt.service.impl;

import com.pshpyro.sigma.security.jwt.entity.RefreshToken;
import com.pshpyro.sigma.security.jwt.repository.RefreshTokenRepository;
import com.pshpyro.sigma.security.jwt.service.RefreshTokenService;
import com.pshpyro.sigma.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public List<RefreshToken> getByUserId(UUID userId) {
        return refreshTokenRepository.findByUserId(userId);
    }

    @Override
    public List<RefreshToken> getByUser(User user) {
        return refreshTokenRepository.findByUser(user);
    }

    @Override
    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public boolean existsByValueAndUserIdAndValid(String value, UUID userId) {
        return refreshTokenRepository.existsByValueAndUserIdAndValidIsTrue(value, userId);
    }

    @Override
    public boolean existsByValueAndUserAndValid(String value, User user) {
        return refreshTokenRepository.existsByValueAndUserAndValidIsTrue(value, user);
    }

    @Override
    public RefreshToken getByValue(String value) {
        return refreshTokenRepository.findById(value).orElseThrow(RuntimeException::new);
    }

    @Override
    public void deleteByValue(String value) {
        refreshTokenRepository.deleteById(value);
    }
}
