package com.luciano.pruebatecnicasupermercado.service;

import com.luciano.pruebatecnicasupermercado.dto.ProductoDTO;

import java.util.List;

public interface IProductoService {
    List<ProductoDTO> getProductos();
    ProductoDTO getProductoById(Long id);
    ProductoDTO postProducto(ProductoDTO producto);
    void deleteProducto(Long id);
    ProductoDTO updateProducto(Long id, ProductoDTO producto);
}
