package com.luciano.pruebatecnicasupermercado.controller;

import com.luciano.pruebatecnicasupermercado.dto.ProductoMasVendidoDTO;
import com.luciano.pruebatecnicasupermercado.dto.SucursalDTO;
import com.luciano.pruebatecnicasupermercado.dto.VentaDTO;
import com.luciano.pruebatecnicasupermercado.service.IVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private IVentaService ventaService;

    @GetMapping
    public ResponseEntity<List<VentaDTO>> getVentas() {
        return ResponseEntity.ok(ventaService.getVentas());
    }

    @GetMapping("({id}")
    public ResponseEntity<VentaDTO> getVentaById(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.getVentaById(id));
    }

    @GetMapping("/producto_mas_vendido")
    public ResponseEntity<ProductoMasVendidoDTO> getProductoMasVendido() {
        return ResponseEntity.ok(ventaService.getProductoMasVendido());
    }

    @PostMapping
    public ResponseEntity<VentaDTO> postVenta(@RequestBody VentaDTO ventaDTO) {
        VentaDTO ventaCreada = ventaService.postVenta(ventaDTO);
        return ResponseEntity.created(URI.create("/api/ventas/" + ventaCreada.getId()))
                .body(ventaCreada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VentaDTO> putVenta(@PathVariable Long id, @RequestBody VentaDTO ventaDTO) {
        return ResponseEntity.ok(ventaService.updateVenta(id, ventaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVenta(@PathVariable Long id) {
        ventaService.deleteVenta(id);
        return ResponseEntity.noContent().build();
    }
}
