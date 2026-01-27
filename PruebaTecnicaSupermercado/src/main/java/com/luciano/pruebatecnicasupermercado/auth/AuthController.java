package com.luciano.pruebatecnicasupermercado.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Devuelve un JWT si las credenciales son correctas")
    public ResponseEntity<AuthResponse> login (@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/registro")
    @Operation(summary = "Registrar nuevo usuario", description = "Crea un usuario y devuelve el JWT automáticamente")
    @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente")
    public ResponseEntity<AuthResponse> registro (@RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registro(request));
    }
}
