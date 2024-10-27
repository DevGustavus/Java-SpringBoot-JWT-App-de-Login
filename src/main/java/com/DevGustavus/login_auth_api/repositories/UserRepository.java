package com.DevGustavus.login_auth_api.repositories;

import com.DevGustavus.login_auth_api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
