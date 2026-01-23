package com.luciano.pruebatecnicasupermercado.auth;

public interface IAuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse registro(RegisterRequest request);
}
