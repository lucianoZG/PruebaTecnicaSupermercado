package com.luciano.pruebatecnicasupermercado.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usId")
    private Long id;

    @Column(name = "usNombre", nullable = false)
    private String nombre;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(name = "usPassword", nullable = false)
    private String password;

    @Column(name = "usRol", nullable = false)
    @Enumerated(EnumType.STRING)
    private RolUsuario rol;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Carrito carrito;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Venta> compras = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
