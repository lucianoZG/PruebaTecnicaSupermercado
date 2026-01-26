package com.luciano.pruebatecnicasupermercado.config;

import com.luciano.pruebatecnicasupermercado.model.RolUsuario;
import com.luciano.pruebatecnicasupermercado.model.Usuario;
import com.luciano.pruebatecnicasupermercado.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class InitialDataConfig {

    @Bean
    public CommandLineRunner loader(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Verificamos si ya existe el admin para no duplicarlo
            if (!usuarioRepository.existsByUsername("admin")) {
                Usuario admin = Usuario.builder()
                        .nombre("Super Admin")
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .rol(RolUsuario.ADMIN)
                        .build();

                usuarioRepository.save(admin);
                System.out.println("Usuario ADMIN creado por defecto: admin / admin");
            }
        };
    }
}