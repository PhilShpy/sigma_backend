package com.pshpyro.sigma.security.jwt.validation.impl;

import com.pshpyro.sigma.security.jwt.service.RefreshTokenService;
import com.pshpyro.sigma.security.jwt.util.JwtTokenUtil;
import com.pshpyro.sigma.security.jwt.validation.TokenValidator;
import com.pshpyro.sigma.security.time.entity.UserTimeDetails;
import com.pshpyro.sigma.security.time.service.UserTimeDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RefreshTokenValidator implements TokenValidator {
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenService refreshTokenService;
    private final UserTimeDetailsService userTimeDetailsService;

    @Override
    public boolean isValid(String refreshToken) {
        final String subject = jwtTokenUtil.getSubjectFromToken(refreshToken);
        final UUID userId = UUID.fromString(subject);
        if (refreshTokenService.existsByValueAndUserIdAndValid(refreshToken, userId)) {
            UserTimeDetails details = userTimeDetailsService.getByUserId(userId);
            Date issuedAt = jwtTokenUtil.getIssuedAtDateFromToken(refreshToken);
            return !jwtTokenUtil.isTokenExpired(refreshToken)
                    && issuedAt.after(details.getPasswordChangedTime());
        }

        return false;
    }
}
