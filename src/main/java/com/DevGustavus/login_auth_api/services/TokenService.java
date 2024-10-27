package com.DevGustavus.login_auth_api.services;

import com.DevGustavus.login_auth_api.models.User;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    public String generatedToken(User user){
        try {

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error while authenticating");
        }
    }
}
