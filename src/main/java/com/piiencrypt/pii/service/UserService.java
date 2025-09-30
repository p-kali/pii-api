package com.piiencrypt.pii.service;

import com.piiencrypt.pii.dto.UserUnmaskedDTO;
import com.piiencrypt.pii.entity.User;
import com.piiencrypt.pii.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        log.info("Creating user: {}", user); // safe (ssn/dob excluded from toString)
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Admin-only
    public List<UserUnmaskedDTO> getAllUnmasked() {
        return userRepository.findAll().stream()
                .map(u -> new UserUnmaskedDTO(u.getId(), u.getName(), u.getSsn(), u.getDob()))
                .toList();
    }

    // Admin-only
    public UserUnmaskedDTO getUnmaskedById(Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserUnmaskedDTO(u.getId(), u.getName(), u.getSsn(), u.getDob());
    }
}
