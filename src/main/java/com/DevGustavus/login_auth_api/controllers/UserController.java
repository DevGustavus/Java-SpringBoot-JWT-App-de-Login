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
import java.util.Optional;

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
    public ResponseEntity<Map<String, String>> deleteUserById(
            @PathVariable String id,
            @RequestParam(name = "executorId") String executorId) {

        Optional<User> userToDeleteOpt = userRepository.findById(id);
        Optional<User> executorOpt = userRepository.findById(executorId);

        if (userToDeleteOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuário a ser deletado não encontrado", "id", id));
        }

        if (executorOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Usuário executor não encontrado", "executorId", executorId));
        }

        User userToDelete = userToDeleteOpt.get();
        User executor = executorOpt.get();

        // Só administradores (role 2) podem excluir usuários
        if (executor.getRole() != 2) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Apenas administradores podem excluir usuários"));
        }

        // Administrador não pode excluir outro administrador
        if (userToDelete.getRole() == 2) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Você não pode excluir outro administrador"));
        }

        userRepository.deleteById(id);
        return ResponseEntity.ok()
                .body(Map.of("message", "Usuário deletado com sucesso", "id", id));
    }
}
