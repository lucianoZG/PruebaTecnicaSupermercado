package com.luciano.pruebatecnicasupermercado.dto;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class ProductoMasVendidoDTO {
    private ProductoDTO producto;
    private Long cantidadVendida;
}
