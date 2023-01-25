package com.pshpyro.sigma.user.service;

import com.pshpyro.sigma.user.entity.User;
import com.pshpyro.sigma.user.entity.dto.DataPage;
import com.pshpyro.sigma.user.entity.dto.UserDTO;

import java.util.UUID;

public interface UserService {
    boolean userExistsByUsername(String username);
    boolean userExistsByEmail(String email);
    boolean userExists(UUID id);
    void createUser(User user);
    User get(UUID id);
    User get(String username);
    default UserDTO getUserData(String username) {
        return UserDTO.of(get(username));
    }
    DataPage<UserDTO> getDataPageOf(int page, int size);
}
