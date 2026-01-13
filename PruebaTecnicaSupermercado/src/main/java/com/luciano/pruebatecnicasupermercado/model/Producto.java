package com.luciano.pruebatecnicasupermercado.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proId")
    private Long id;

    @NonNull
    @Column(name = "proNombre")
    private String nombre;

    @NonNull
    @Column(name = "proPrecioActual")
    private Double precioActual;

    @NonNull
    @Column(name = "proCantidad")
    private Integer cantidad;

    @NonNull
    @Column(name = "proCategoria")
    private String categoria;
}
