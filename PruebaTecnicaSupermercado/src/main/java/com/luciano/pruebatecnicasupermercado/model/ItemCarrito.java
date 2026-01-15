package com.luciano.pruebatecnicasupermercado.model;

import com.luciano.pruebatecnicasupermercado.dto.ProductoDTO;
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
    @Column(name = "itemCarrito")
    private Carrito carrito;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "proId")
    @Column(name = "itemProducto")
    private Producto producto;
}
