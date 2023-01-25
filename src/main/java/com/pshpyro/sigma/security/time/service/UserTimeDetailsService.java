package com.pshpyro.sigma.security.time.service;

import com.pshpyro.sigma.security.time.entity.UserTimeDetails;
import com.pshpyro.sigma.user.entity.User;

import java.util.UUID;


public interface UserTimeDetailsService {
    UserTimeDetails getByUserId(UUID userId);
    UserTimeDetails getByUser(User user);
    UserTimeDetails getByUserUsername(String username);
    void saveOrUpdate(UserTimeDetails userTimeDetails);
}
