package com.pshpyro.sigma.security.time.service;

import com.pshpyro.sigma.security.time.entity.UserTimeDetails;
import com.pshpyro.sigma.security.time.repository.UserTimeDetailsRepository;
import com.pshpyro.sigma.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserTimeDetailsServiceImpl implements UserTimeDetailsService {
    private final UserTimeDetailsRepository repository;

    @Override
    public UserTimeDetails getByUserId(UUID userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public UserTimeDetails getByUser(User user) {
        return repository.findByUser(user);
    }

    @Override
    public UserTimeDetails getByUserUsername(String username) {
        return repository.findByUserUsername(username);
    }

    @Override
    public void saveOrUpdate(UserTimeDetails userTimeDetails) {
        User user = userTimeDetails.getUser();
        if (user == null) {
            throw new RuntimeException("The 'user' field of UserTimeDetails object is null.");
        }

        if (repository.existsByUser(user)) {
            UserTimeDetails userTimeDetailsFromDB = repository.findByUser(user);
            userTimeDetailsFromDB.setRightsChangedTime(userTimeDetails.getRightsChangedTime());
            userTimeDetailsFromDB.setPasswordChangedTime(userTimeDetails.getPasswordChangedTime());
            userTimeDetails = userTimeDetailsFromDB;
        }

        repository.save(userTimeDetails);
    }
}
