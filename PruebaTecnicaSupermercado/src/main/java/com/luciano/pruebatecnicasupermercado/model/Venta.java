package com.luciano.pruebatecnicasupermercado.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venId")
    private Long id;

    @NonNull
    @Column(name = "venFecha")
    private LocalDate fecha;

    // Dejamos nullable = false en @Column para que la Base de Datos se queje
    // si intentamos guardar sin precio, pero Java nos deje crear el objeto temporal.
    @Column(name = "venPrecioTotal", nullable = false)
    private Double precioTotal;
    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "venEstado")
    private EstadoVenta estado;

    @ManyToOne
//    @JoinColumn(name = "sucId")
    private Sucursal sucursal;

    @OneToMany(mappedBy = "venta",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<VentaDetalle> listaDetalles = new ArrayList<>();
}
