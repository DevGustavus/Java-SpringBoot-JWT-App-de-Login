package com.DevGustavus.login_auth_api.mocks;

import com.DevGustavus.login_auth_api.models.User;
import com.DevGustavus.login_auth_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class MockUserInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner loadMockUsers() {
        return args -> {
            if (userRepository.count() == 0) {

                // Admin
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@email.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(2);
                userRepository.save(admin);

                // 14 usuários comuns
                for (int i = 1; i <= 14; i++) {
                    User user = new User();
                    user.setName("Usuário " + i);
                    user.setEmail("user" + i + "@email.com");
                    user.setPassword(passwordEncoder.encode("user" + i + "123"));
                    user.setRole(1);
                    userRepository.save(user);
                }

                System.out.println("15 usuários mock criados com sucesso.");
            }
        };
    }
}
