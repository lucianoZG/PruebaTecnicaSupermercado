package com.luciano.pruebatecnicasupermercado.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itemId")
    private Long id;

    @NonNull
    @Column(name = "itemCant")
    private Integer cantidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carId")
    @ToString.Exclude // Evita bucles al imprimir logs
    @EqualsAndHashCode.Exclude // Evita bucles al comparar objetos
    private Carrito carrito;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proId")
    private Producto producto;
}
