package com.luciano.pruebatecnicasupermercado.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class CarritoDTO {
    private Long id;
    private Long idUsuario;
    private Double totalEstimado;
    private List<ItemCarritoDTO> items;
}
