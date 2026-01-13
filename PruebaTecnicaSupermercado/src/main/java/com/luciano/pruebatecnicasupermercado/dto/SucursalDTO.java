package com.luciano.pruebatecnicasupermercado.dto;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class SucursalDTO {
    private Long id;
    private String nombre;
    private String direccion;
}
