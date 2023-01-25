package com.pshpyro.sigma.security.time.repository;

import com.pshpyro.sigma.security.time.entity.UserTimeDetails;
import com.pshpyro.sigma.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserTimeDetailsRepository extends JpaRepository<UserTimeDetails, Long> {
    UserTimeDetails findByUserId(UUID userId);
    UserTimeDetails findByUser(User user); // I hope it uses the id field
    UserTimeDetails findByUserUsername(String username);
    boolean existsByUser(User user); // I hope it uses the id field
}
