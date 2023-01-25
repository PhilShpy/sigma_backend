package com.pshpyro.sigma.security.jwt.repository;

import com.pshpyro.sigma.security.jwt.entity.RefreshToken;
import com.pshpyro.sigma.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    List<RefreshToken> findByUserId(UUID userId);
    List<RefreshToken> findByUser(User user);
    boolean existsByValueAndUserId(String value, UUID userId);
    boolean existsByValueAndUser(String value, User user);
    boolean existsByValueAndUserIdAndValidIsTrue(String value, UUID userId);
    boolean existsByValueAndUserAndValidIsTrue(String value, User user);
}
