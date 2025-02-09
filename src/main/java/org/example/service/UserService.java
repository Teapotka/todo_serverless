package org.example.service;

import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();  // To hash passwords
    }

    public String register(String username, String password) {
        if (userRepository.findByUsername(username) != null) {
            throw new RuntimeException("Username already exists.");
        }

        User newUser = new User();
        newUser.setId(UUID.randomUUID().toString());
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));

        String token = UUID.randomUUID().toString();
        newUser.setToken(token);

        userRepository.registerUser(newUser);
        return token;
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password.");
        }

        String token = UUID.randomUUID().toString();
        userRepository.updateUserToken(user.getId(), token);

        return token;
    }

    public User authenticate(String token) {
        return userRepository.findByToken(token);
    }
}
