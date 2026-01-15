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
    @Column(name = "carUsId")
    private Long usId;
    @Column(name = "carItems")
    private List<ItemCarrito> items = new ArrayList<>();

    public Double getTotalEstimado () {
        return items.stream()
                .mapToDouble(item -> item.getCantidad() * item.getProducto().getPrecioActual())
                .sum();
    }
}
