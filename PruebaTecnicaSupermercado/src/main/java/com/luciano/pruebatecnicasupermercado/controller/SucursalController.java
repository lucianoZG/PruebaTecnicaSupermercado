package com.luciano.pruebatecnicasupermercado.controller;

import com.luciano.pruebatecnicasupermercado.dto.SucursalDTO;
import com.luciano.pruebatecnicasupermercado.service.ISucursalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/sucursales")
public class SucursalController {

    @Autowired
    private ISucursalService sucursalService;

    @GetMapping
    public ResponseEntity<List<SucursalDTO>> getSucursales() {
        return ResponseEntity.ok(sucursalService.getSucursales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SucursalDTO> getSucursalById(@PathVariable Long id) {
        return ResponseEntity.ok(sucursalService.getSucursalById(id));
    }

    @PostMapping
    public ResponseEntity<SucursalDTO> postSucursal(@RequestBody SucursalDTO sucursalDTO) {
        SucursalDTO sucursalCreada = sucursalService.postSucursal(sucursalDTO);
        return ResponseEntity.created(URI.create("/api/sucursales/" + sucursalCreada.getId()))
                .body(sucursalCreada);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SucursalDTO> putSucursal(@PathVariable Long id, @RequestBody SucursalDTO sucursalDTO) {
        return ResponseEntity.ok(sucursalService.updateSucursal(id, sucursalDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSucursal(@PathVariable Long id) {
        sucursalService.deleteSucursal(id);
        return ResponseEntity.noContent().build();
    }
}
