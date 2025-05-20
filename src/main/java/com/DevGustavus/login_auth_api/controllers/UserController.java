package com.DevGustavus.login_auth_api.controllers;

import com.DevGustavus.login_auth_api.dto.UpdateDTO;
import com.DevGustavus.login_auth_api.models.User;
import com.DevGustavus.login_auth_api.repositories.UserRepository;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable String id, @RequestBody UpdateDTO dto) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setName(dto.name());
            existingUser.setEmail(dto.email());
            existingUser.setRole(dto.role());

            userRepository.save(existingUser);
            return ResponseEntity.ok(existingUser);
        }).orElse(ResponseEntity.notFound().build());
    }
}
