package com.luciano.pruebatecnicasupermercado.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sucId")
    private Long id;

    @NonNull
    @Column(name = "sucNombre")
    private String nombre;

    @NonNull
    @Column(name = "sucDireccion")
    private String direccion;
}
