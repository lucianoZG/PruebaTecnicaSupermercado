package com.luciano.pruebatecnicasupermercado.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carId")
    private Long id;

    @OneToOne
    @JoinColumn(name = "carUsId", referencedColumnName = "usId")
    private Usuario usuario;

    @OneToMany(orphanRemoval = true,
            mappedBy = "carrito",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @Builder.Default // Para que el Builder respete el "new ArrayList<>()"
    private List<ItemCarrito> items = new ArrayList<>();

    public Double getTotalEstimado () {
        if (items == null) return 0.0;

        return items.stream()
                .mapToDouble(item -> item.getCantidad() * item.getProducto().getPrecioActual())
                .sum();
    }
}
