package com.luciano.pruebatecnicasupermercado.service;

import com.luciano.pruebatecnicasupermercado.dto.SucursalDTO;

import java.util.List;

public interface ISucursalService {
    List<SucursalDTO> getSucursales();
    SucursalDTO getSucursalById(Long id);
    //También puede devolver vacío
    SucursalDTO postSucursal(SucursalDTO sucursal);
    void deleteSucursal(Long id);
    SucursalDTO updateSucursal(Long id, SucursalDTO sucursal);
}
