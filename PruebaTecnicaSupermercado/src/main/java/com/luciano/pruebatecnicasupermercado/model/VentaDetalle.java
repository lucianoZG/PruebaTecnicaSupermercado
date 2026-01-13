package com.luciano.pruebatecnicasupermercado.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class VentaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venDetId")
    private Long id;

    @NonNull
    @Column(name = "venDetCantidadProd")
    private Integer cantidadProd;

    @NonNull
    @Column(name = "venDetPrecUnitario")
    private Double precioUnitario;

    @NonNull
    @Column(name = "venDetSubtotal")
    private Double subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venId")
    private Venta venta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proId")
    private Producto producto;
}
