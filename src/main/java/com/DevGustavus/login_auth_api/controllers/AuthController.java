package com.DevGustavus.login_auth_api.controllers;

import com.DevGustavus.login_auth_api.dto.LoginRequestDTO;
import com.DevGustavus.login_auth_api.dto.RegisterRequestDTO;
import com.DevGustavus.login_auth_api.dto.ResponseDTO;
import com.DevGustavus.login_auth_api.infra.security.TokenService;
import com.DevGustavus.login_auth_api.models.User;
import com.DevGustavus.login_auth_api.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body){
        User user = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if(passwordEncoder.matches(body.password(), user.getPassword())) {
            String token = this.tokenService.generateToken(user);
            System.out.println("Login feito com sucesso!");
            return ResponseEntity.ok(new ResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getRole(), token));
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRequestDTO body){
        Optional<User> user = this.repository.findByEmail(body.email());

        if(user.isEmpty()) {
            User newUser = new User();
            newUser.setPassword(passwordEncoder.encode(body.password()));
            newUser.setEmail(body.email());
            newUser.setName(body.name());
            newUser.setRole(1);
            this.repository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            System.out.println("Usuario registrado com sucesso!");
            return ResponseEntity.ok(new ResponseDTO(newUser.getId(), newUser.getName(), newUser.getEmail(), newUser.getRole(), token));
        }
        return ResponseEntity.badRequest().build();
    }
}
