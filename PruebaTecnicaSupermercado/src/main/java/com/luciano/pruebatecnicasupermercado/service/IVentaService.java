package com.luciano.pruebatecnicasupermercado.service;

import com.luciano.pruebatecnicasupermercado.dto.ProductoMasVendidoDTO;
import com.luciano.pruebatecnicasupermercado.dto.VentaDTO;

import java.util.List;

public interface IVentaService {
    List<VentaDTO> getVentas();
    VentaDTO getVentaById(Long id);
    VentaDTO postVenta(VentaDTO venta);
    void deleteVenta(Long id);
    VentaDTO updateVenta(Long id, VentaDTO venta);
    ProductoMasVendidoDTO getProductoMasVendido();
}
