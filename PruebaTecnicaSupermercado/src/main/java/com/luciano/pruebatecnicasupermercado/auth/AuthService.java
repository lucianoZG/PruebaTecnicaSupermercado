package com.luciano.pruebatecnicasupermercado.auth;

import com.luciano.pruebatecnicasupermercado.exception.NotFoundException;
import com.luciano.pruebatecnicasupermercado.exception.UserAlreadyExistsException;
import com.luciano.pruebatecnicasupermercado.model.RolUsuario;
import com.luciano.pruebatecnicasupermercado.model.Usuario;
import com.luciano.pruebatecnicasupermercado.repository.UsuarioRepository;
import com.luciano.pruebatecnicasupermercado.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService{

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username, request.password));
        Usuario usuario = usuarioRepository.findByUsername(request.username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        String token = jwtService.generateToken(usuario);

        return AuthResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public AuthResponse registro(RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.username)) {
            throw new UserAlreadyExistsException("El nombre de usuario ya está en uso, debe elegir otro");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.nombre)
                .username(request.username)
                .password(passwordEncoder.encode(request.password))
                .rol(RolUsuario.USUARIO)
                .build();

        usuarioRepository.save(usuario);

        return AuthResponse.builder()
                .token(jwtService.generateToken(usuario))
                .build();
    }
}
