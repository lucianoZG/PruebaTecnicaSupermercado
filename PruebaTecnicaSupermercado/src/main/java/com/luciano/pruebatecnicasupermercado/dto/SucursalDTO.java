package com.luciano.pruebatecnicasupermercado.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class SucursalDTO {
    private Long id;
    @NotBlank (message = "El nombre de la sucursal no puede estar vacío")
    private String nombre;
    @NotBlank (message = "La dirección de la sucursal no puede estar vacía")
    private String direccion;
}
