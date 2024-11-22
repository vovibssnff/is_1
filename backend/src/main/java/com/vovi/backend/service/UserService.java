package com.vovi.backend.service;

import com.vovi.backend.entity.User;
import com.vovi.backend.entity.UserRole;
import com.vovi.backend.repository.UserRepository;
import com.vovi.backend.security.PasswordEncoder;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class UserService {

    @Inject
    private UserRepository userRepository;

    @Transactional
    public User register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(PasswordEncoder.encode(password));
        user.setRole(UserRole.USER); // Default role
        return userRepository.save(user);
    }

    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(PasswordEncoder.encode(password)));
    }
}
