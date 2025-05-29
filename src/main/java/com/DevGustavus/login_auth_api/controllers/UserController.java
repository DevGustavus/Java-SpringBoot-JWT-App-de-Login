package com.DevGustavus.login_auth_api.controllers;

import com.DevGustavus.login_auth_api.dto.UpdateDTO;
import com.DevGustavus.login_auth_api.models.User;
import com.DevGustavus.login_auth_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteUserById(@PathVariable String id) {
        return userRepository.findById(id).map(user -> {
            userRepository.deleteById(id);
            return ResponseEntity.ok().body(Map.of("message", "Usuário deletado com sucesso", "id", id));
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Usuário não encontrado", "id", id)));
    }

}
