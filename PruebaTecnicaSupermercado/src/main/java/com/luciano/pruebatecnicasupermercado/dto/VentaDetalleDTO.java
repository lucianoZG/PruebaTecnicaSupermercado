package com.luciano.pruebatecnicasupermercado.dto;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class VentaDetalleDTO {
    private Long id;
    private Long idProd;
    private Double precioUnitario;
    private String nombreProd;
    private Integer cantidadProd;
    private Double subtotal;
}
