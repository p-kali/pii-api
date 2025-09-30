package com.piiencrypt.pii.controller;

import com.piiencrypt.pii.dto.UserUnmaskedDTO;
import com.piiencrypt.pii.entity.User;
import com.piiencrypt.pii.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private static final String ADMIN_TOKEN = "admin-secret-token";

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // --- Admin endpoints ---
    @GetMapping("/admin/users")
    public ResponseEntity<?> getAllUnmasked(@RequestHeader("X-Auth-Token") String token) {
        if (!ADMIN_TOKEN.equals(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        }
        return ResponseEntity.ok(userService.getAllUnmasked());
    }

    @GetMapping("/admin/users/{id}")
    public ResponseEntity<?> getUnmaskedById(
            @PathVariable Long id,
            @RequestHeader("X-Auth-Token") String token) {
        if (!ADMIN_TOKEN.equals(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
        }
        return ResponseEntity.ok(userService.getUnmaskedById(id));
    }
}
