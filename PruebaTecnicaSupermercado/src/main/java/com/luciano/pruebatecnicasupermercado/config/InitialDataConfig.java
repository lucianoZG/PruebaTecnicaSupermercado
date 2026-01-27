package com.luciano.pruebatecnicasupermercado.config;

import com.luciano.pruebatecnicasupermercado.model.RolUsuario;
import com.luciano.pruebatecnicasupermercado.model.Usuario;
import com.luciano.pruebatecnicasupermercado.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.logging.Logger;

@Configuration
public class InitialDataConfig {

    @Value("${app.admin.password}")
    private String adminPassword;

    Logger logger = Logger.getLogger(getClass().getName());

    private static final String ADMIN = "admin";

    @Bean
    public CommandLineRunner loader(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Verificamos si ya existe el admin para no duplicarlo
            if (!usuarioRepository.existsByUsername(ADMIN)) {
                Usuario admin = Usuario.builder()
                        .nombre("Super Admin")
                        .username(ADMIN)
                        .password(passwordEncoder.encode(adminPassword))
                        .rol(RolUsuario.ADMIN)
                        .build();

                usuarioRepository.save(admin);
                logger.info("Usuario ADMIN creado por defecto: admin / admin");
            }
        };
    }
}