package com.pshpyro.sigma.security.jwt.dto;

import com.pshpyro.sigma.security.jwt.entity.RefreshToken;

public record RefreshTokenDTO(String refreshToken) {
    public static RefreshTokenDTO of(RefreshToken refreshToken) {
        return new RefreshTokenDTO(refreshToken.getValue());
    }
}
