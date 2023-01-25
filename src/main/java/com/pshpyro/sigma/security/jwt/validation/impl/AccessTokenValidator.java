package com.pshpyro.sigma.security.jwt.validation.impl;

import com.pshpyro.sigma.security.jwt.service.RefreshTokenService;
import com.pshpyro.sigma.security.jwt.util.JwtTokenUtil;
import com.pshpyro.sigma.security.jwt.validation.TokenValidator;
import com.pshpyro.sigma.security.time.entity.UserTimeDetails;
import com.pshpyro.sigma.security.time.service.UserTimeDetailsService;
import com.pshpyro.sigma.user.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class AccessTokenValidator implements TokenValidator {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserServiceImpl userService;
    private final UserTimeDetailsService userTimeDetailsService;
    @Override
    public boolean isValid(String accessToken) {
        final String subject = jwtTokenUtil.getSubjectFromToken(accessToken);
        UUID userId = UUID.fromString(subject);
        if (userService.userExists(userId)) {
            UserTimeDetails details = userTimeDetailsService.getByUserId(userId);
            Date issuedAt = jwtTokenUtil.getIssuedAtDateFromToken(accessToken);
            return !jwtTokenUtil.isTokenExpired(accessToken)
                    && issuedAt.after(details.getPasswordChangedTime())
                    && issuedAt.after(details.getRightsChangedTime());
        }

        return false;
    }
}
