package com.github.mohamad4444.springtemplate.service;

import com.github.mohamad4444.springtemplate.model.User;
import com.github.mohamad4444.springtemplate.repository.UserRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@org.springframework.context.annotation.Profile("messaging")
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        // Publish event to RabbitMQ
        rabbitTemplate.convertAndSend("user.exchange", "user.created", savedUser.getUsername());

        return savedUser;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
