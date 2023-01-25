package com.pshpyro.sigma.user.entity.dto;

import com.pshpyro.sigma.user.entity.Role;
import com.pshpyro.sigma.user.entity.User;

import java.util.Set;

public record UserDTO (
        String username,
        Set<Role> roles
) {
    public static UserDTO of(User user) {
        return new UserDTO(user.getUsername(), user.getRoles());
    }
}