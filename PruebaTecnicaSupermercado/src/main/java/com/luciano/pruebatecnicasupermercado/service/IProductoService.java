package com.luciano.pruebatecnicasupermercado.service;

import com.luciano.pruebatecnicasupermercado.dto.ProductoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductoService {
    Page<ProductoDTO> getProductos(Pageable pageable);
    ProductoDTO getProductoById(Long id);
    ProductoDTO postProducto(ProductoDTO producto);
    void deleteProducto(Long id);
    ProductoDTO updateProducto(Long id, ProductoDTO producto);
}
