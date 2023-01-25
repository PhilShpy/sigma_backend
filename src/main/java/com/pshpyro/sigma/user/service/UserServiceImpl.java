package com.pshpyro.sigma.user.service;

import com.pshpyro.sigma.user.entity.Role;
import com.pshpyro.sigma.user.entity.User;
import com.pshpyro.sigma.user.entity.dto.DataPage;
import com.pshpyro.sigma.user.entity.dto.UserDTO;
import com.pshpyro.sigma.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public boolean userExistsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean userExists(UUID id) {
        return repository.existsById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createUser(User user) {
        repository.save(user);
    }

    @Override
    public User get(UUID id) {
        return repository.findById(id).orElseThrow(RuntimeException::new); // TODO Use Custom UserNotFound RuntimeException
    }

    @Override
    public User get(String username) {
        return repository.findByUsername(username).orElseThrow(RuntimeException::new); // TODO Use Custom UserNotFound RuntimeException
    }

    @Override
    public DataPage<UserDTO> getDataPageOf(int page, int size) {
        Page<User> pageOfUsers = repository.findAll(PageRequest.of(page, size));
        return new DataPage<>(
                pageOfUsers.map(UserDTO::of).getContent(),
                page,
                pageOfUsers.getTotalPages()
        );
    }
}
