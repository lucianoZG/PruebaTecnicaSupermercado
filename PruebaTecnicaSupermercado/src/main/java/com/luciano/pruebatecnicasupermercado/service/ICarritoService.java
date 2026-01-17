package com.luciano.pruebatecnicasupermercado.service;

import com.luciano.pruebatecnicasupermercado.dto.CarritoDTO;
import com.luciano.pruebatecnicasupermercado.dto.VentaDTO;

public interface ICarritoService {
    CarritoDTO obtenerCarritoPorUsuario(Long usId);
    //Innecesario, se crea el carrito automáticamente en otros métodos.
//    CarritoDTO postCarrito(CarritoDTO carrito);
    CarritoDTO agregarProducto(Long carritoId, Long productoId, Integer cantidad);
    CarritoDTO quitarProducto(Long carritoId, Long productoId, Integer cantidad);
    CarritoDTO eliminarProducto(Long carritoId, Long productoId);
    VentaDTO finalizarCompra(Long carritoId, Long sucursalId);
}
