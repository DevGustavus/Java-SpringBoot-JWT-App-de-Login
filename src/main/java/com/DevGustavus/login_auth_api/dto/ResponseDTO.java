package com.DevGustavus.login_auth_api.dto;

public record ResponseDTO (String id, String name, String email, int role, String token) {
}
