package com.pshpyro.sigma.security.service;

import com.pshpyro.sigma.security.jwt.dto.JwtTokenResponse;
import com.pshpyro.sigma.security.jwt.dto.RefreshTokenDTO;
import com.pshpyro.sigma.security.jwt.dto.RegistrationRequest;
import com.pshpyro.sigma.security.jwt.dto.UsernamePasswordRequest;
import com.pshpyro.sigma.security.jwt.entity.RefreshToken;
import com.pshpyro.sigma.security.jwt.service.RefreshTokenService;
import com.pshpyro.sigma.security.jwt.util.JwtTokenUtil;
import com.pshpyro.sigma.security.jwt.validation.impl.RefreshTokenValidator;
import com.pshpyro.sigma.user.entity.User;
import com.pshpyro.sigma.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenValidator refreshTokenValidator;
    private final PasswordEncoder passwordEncoder;

    public JwtTokenResponse authenticateUserAndGetTokensAndSaveRefreshTokenToDB(UsernamePasswordRequest usernamePasswordRequest) {
        String username = usernamePasswordRequest.username();
        String password = usernamePasswordRequest.password();
        authenticate(username, password);
        User user = userService.get(usernamePasswordRequest.username());
        JwtTokenResponse response = generateTokensFor(user);
        saveUserRefreshTokenToDB(user, response.refreshToken());
        return response;
    }

    public JwtTokenResponse refreshAuthenticationToken(RefreshTokenDTO refreshTokenDTO) {
        String refreshTokenString = refreshTokenDTO.refreshToken();
        String userId = jwtTokenUtil.getSubjectFromToken(refreshTokenString);
        User user = userService.get(UUID.fromString(userId));
        if (refreshTokenValidator.isValid(refreshTokenString)) {
            deleteRefreshTokenFromDB(refreshTokenString);
            JwtTokenResponse response = generateTokensFor(user);
            saveUserRefreshTokenToDB(user, response.refreshToken());
            return response;
        } else {
            throw new RuntimeException("Refresh token is not valid or expired.");
        }
    }

    public JwtTokenResponse registerUserAndGetTokens(RegistrationRequest request) {
        String username = request.username();
        String email = request.email();
        if (userWithSuchUsernameOrEmailExists(username, email)) {
            throw new RuntimeException("Such username or email is taken");
        }

        String passwordHash = passwordEncoder.encode(request.password());
        User user = new User(username, email, passwordHash);
        userService.createUser(user);
        authenticate(username, request.password());
        JwtTokenResponse response = generateTokensFor(userService.get(username));
        saveUserRefreshTokenToDB(user, response.refreshToken());
        return response;
    }

    private JwtTokenResponse generateTokensFor(User user) {
        String accessToken = jwtTokenUtil.generateAccessToken(user);
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getId().toString());
        return new JwtTokenResponse(accessToken, refreshToken);
    }

    private void authenticate(final String username, final String password) throws RuntimeException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new RuntimeException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("INVALID_CREDENTIALS", e);
        }
    }

    private void saveUserRefreshTokenToDB(User user, String refreshTokenValue) {
        RefreshToken refreshToken = new RefreshToken(
                refreshTokenValue,
                jwtTokenUtil.getIssuedAtDateFromToken(refreshTokenValue),
                jwtTokenUtil.getExpirationDateFromToken(refreshTokenValue),
                true,
                user
        );

        refreshTokenService.save(refreshToken);
    }

    private void deleteRefreshTokenFromDB(String refreshTokenValue) {
        refreshTokenService.deleteByValue(refreshTokenValue);
    }

    private boolean userWithSuchUsernameOrEmailExists(String username, String email) {
        return userWithSuchUsernameExists(username) || userWithSuchEmailExists(email);
    }

    private boolean userWithSuchUsernameExists(String username) {
        return userService.userExistsByUsername(username);
    }

    private boolean userWithSuchEmailExists(String email) {
        return userService.userExistsByEmail(email);
    }
}
