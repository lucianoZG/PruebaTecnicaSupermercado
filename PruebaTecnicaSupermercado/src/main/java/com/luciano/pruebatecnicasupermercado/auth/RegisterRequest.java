package com.luciano.pruebatecnicasupermercado.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class RegisterRequest {
    String nombre;
    String username;
    String password;
}
