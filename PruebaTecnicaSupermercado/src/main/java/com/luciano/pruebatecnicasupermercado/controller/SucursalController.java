package com.luciano.pruebatecnicasupermercado.controller;

import com.luciano.pruebatecnicasupermercado.dto.SucursalDTO;
import com.luciano.pruebatecnicasupermercado.service.ISucursalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
@RequiredArgsConstructor
public class SucursalController {

    private final ISucursalService sucursalService;

    @Operation(summary = "Obtener lista de todas las sucursales realizadas", description = "Devuelve al Admin una lista de todas las sucursales presentes en la base de datos")
    @ApiResponse(responseCode = "200", description = "Sucursales encontradas")
    @GetMapping
    public ResponseEntity<List<SucursalDTO>> getSucursales() {
        return ResponseEntity.ok(sucursalService.getSucursales());
    }

    @Operation(summary = "Obtener sucursal por ID", description = "Devuelve al Admin una sucursal cuyo ID indicado corresponda con el que tiene en la base de datos")
    @ApiResponse(responseCode = "200", description = "Sucursal encontrada")
    @ApiResponse(responseCode = "404", description = "Entidad no encontrada: El ID de Sucursal no existe", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<SucursalDTO> getSucursalById(@PathVariable Long id) {
        return ResponseEntity.ok(sucursalService.getSucursalById(id));
    }

    @Operation(summary = "Dar de alta una sucursal", description = "El admin ingresa los datos correspondientes a una sucursal y se la da de alta en la base de datos, luego se devuelve la sucursal creada")
    @ApiResponse(responseCode = "201", description = "Sucursal creada")
    @ApiResponse(responseCode = "400", description = "Error de validación: Faltan datos obligatorios (Sucursal)", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acceso denegado: Requiere rol de Administrador", content = @Content)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SucursalDTO> postSucursal(@Valid @RequestBody SucursalDTO sucursalDTO) {
        SucursalDTO sucursalCreada = sucursalService.postSucursal(sucursalDTO);
        return ResponseEntity.created(URI.create("/api/sucursales/" + sucursalCreada.getId()))
                .body(sucursalCreada);
    }

    @Operation(summary = "Modificar una sucursal", description = "El admin selecciona la sucursal e ingresa los datos correspondientes que quiera modificar, luego se devuelve la sucursal actualizada")
    @ApiResponse(responseCode = "200", description = "Sucursal actualizada")
    @ApiResponse(responseCode = "403", description = "Acceso denegado: Requiere rol de Administrador", content = @Content)
    @ApiResponse(responseCode = "404", description = "Entidad no encontrada: El ID de Sucursal no existe", content = @Content)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SucursalDTO> putSucursal(@PathVariable Long id, @Valid @RequestBody SucursalDTO sucursalDTO) {
        return ResponseEntity.ok(sucursalService.updateSucursal(id, sucursalDTO));
    }

    @Operation(summary = "Eliminar una sucursal", description = "El admin selecciona la sucursal y se la elimina de la base de datos")
    @ApiResponse(responseCode = "204", description = "Sucursal eliminada exitosamente", content = @Content)
    @ApiResponse(responseCode = "403", description = "Acceso denegado: Requiere rol de Administrador", content = @Content)
    @ApiResponse(responseCode = "404", description = "Entidad no encontrada: El ID de Sucursal no existe", content = @Content)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSucursal(@PathVariable Long id) {
        sucursalService.deleteSucursal(id);
        return ResponseEntity.noContent().build();
    }
}
