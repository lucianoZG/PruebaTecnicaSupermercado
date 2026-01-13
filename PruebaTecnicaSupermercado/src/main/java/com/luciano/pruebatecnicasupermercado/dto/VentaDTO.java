package com.luciano.pruebatecnicasupermercado.dto;

import com.luciano.pruebatecnicasupermercado.model.EstadoVenta;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class VentaDTO {
    private Long id;
    private LocalDate fecha;
    private Double precioTotal;
    private EstadoVenta estado;
    private Long idSucursal;
    private List<VentaDetalleDTO> ventaDetalleDTOList;
}
