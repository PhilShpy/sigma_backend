package com.pshpyro.sigma.security.filter;

import com.pshpyro.sigma.security.filter.util.RequestDataUtil;
import com.pshpyro.sigma.security.jwt.service.RefreshTokenService;
import com.pshpyro.sigma.security.jwt.util.JwtTokenUtil;
import com.pshpyro.sigma.security.jwt.validation.impl.AccessTokenValidator;
import com.pshpyro.sigma.security.jwt.validation.impl.RefreshTokenValidator;
import com.pshpyro.sigma.security.time.service.UserTimeDetailsService;
import com.pshpyro.sigma.user.entity.User;
import com.pshpyro.sigma.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class JWTRequestFilter extends OncePerRequestFilter {
    private final RequestDataUtil requestDataUtil;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AccessTokenValidator accessTokenValidator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> accessToken = requestDataUtil.extractAccessToken(request);
        String subject = null;
        if (accessToken.isPresent() && accessTokenValidator.isValid(accessToken.get())) {
            subject = jwtTokenUtil.getSubjectFromToken(accessToken.get());
        }

        if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = User.getUserDetailsOf(userService.get(UUID.fromString(subject))); // TODO: UserNotFound handling
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        filterChain.doFilter(request, response);
    }


}
