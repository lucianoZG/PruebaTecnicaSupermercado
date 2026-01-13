package com.luciano.pruebatecnicasupermercado.dto;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class ProductoDTO {
    private Long id;
    private String nombre;
    private Double precioActual;
    private Integer cantidad;
    private String categoria;
}
