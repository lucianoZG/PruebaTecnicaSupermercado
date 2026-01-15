package com.luciano.pruebatecnicasupermercado.dto;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class ItemCarritoDTO {
    private Long id;
    private Long idProducto;
    private String nombreProducto;
    private Double precioUnitActual;
    private Integer cantidad;
    private Double subTotal;
}
