package com.luciano.pruebatecnicasupermercado.controller;

import com.luciano.pruebatecnicasupermercado.dto.ProductoMasVendidoDTO;
import com.luciano.pruebatecnicasupermercado.dto.SucursalDTO;
import com.luciano.pruebatecnicasupermercado.dto.VentaDTO;
import com.luciano.pruebatecnicasupermercado.service.IVentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final IVentaService ventaService;

    @Operation(summary = "Obtener lista de todas las ventas realizadas", description = "Devuelve al Admin una lista de todas las ventas presentes en la base de datos")
    @ApiResponse(responseCode = "200", description = "Ventas encontradas")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<VentaDTO>> getVentas() {
        return ResponseEntity.ok(ventaService.getVentas());
    }

    @Operation(summary = "Obtener venta por ID", description = "Devuelve al Admin una venta cuyo ID indicado corresponda con el que tiene en la base de datos")
    @ApiResponse(responseCode = "200", description = "Venta encontrada")
    @ApiResponse(responseCode = "404", description = "Venta no encontrada", content = @Content)
    @GetMapping("({id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VentaDTO> getVentaById(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.getVentaById(id));
    }

    @Operation(summary = "Obtener el producto más vendido", description = "Devuelve al Admin el producto del que se hayan vendido más unidades")
    @ApiResponse(responseCode = "200", description = "Producto más vendido encontrado")
    @ApiResponse(responseCode = "404", description = "No se encontraron ventas registradas", content = @Content)
    @GetMapping("/producto_mas_vendido")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoMasVendidoDTO> getProductoMasVendido() {
        return ResponseEntity.ok(ventaService.getProductoMasVendido());
    }

    @Operation(summary = "Dar de alta una venta", description = "El admin ingresa los datos correspondientes a una venta y se la da de alta en la base de datos, luego se devuelve la venta creada")
    @ApiResponse(responseCode = "201", description = "Venta creada")
    @ApiResponse(responseCode = "400", description = "Error de validación: Faltan datos obligatorios (Sucursal, Detalles, etc.)", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acceso denegado: Requiere rol de Administrador", content = @Content)
    @ApiResponse(responseCode = "404", description = "Entidad no encontrada: El ID de Sucursal o Producto indicado no existe", content = @Content)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VentaDTO> postVenta(@RequestBody VentaDTO ventaDTO) {
        VentaDTO ventaCreada = ventaService.postVenta(ventaDTO);
        return ResponseEntity.created(URI.create("/api/ventas/" + ventaCreada.getId()))
                .body(ventaCreada);
    }

    //No sería común modificar o borrar una venta

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VentaDTO> putVenta(@PathVariable Long id, @RequestBody VentaDTO ventaDTO) {
        return ResponseEntity.ok(ventaService.updateVenta(id, ventaDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVenta(@PathVariable Long id) {
        ventaService.deleteVenta(id);
        return ResponseEntity.noContent().build();
    }
}
