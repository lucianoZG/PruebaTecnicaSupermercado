package com.luciano.pruebatecnicasupermercado.service;

import com.luciano.pruebatecnicasupermercado.dto.ProductoDTO;
import com.luciano.pruebatecnicasupermercado.exception.NotFoundException;
import com.luciano.pruebatecnicasupermercado.mapper.Mapper;
import com.luciano.pruebatecnicasupermercado.model.Producto;
import com.luciano.pruebatecnicasupermercado.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductoService implements IProductoService{

    //Sin el final se puede inyectar con @Autowired
    private final ProductoRepository repository;

    @Override
    public List<ProductoDTO> getProductos() {
        return repository.findAll().stream()
                .map(Mapper::toDTO)
                .toList();
    }

    @Override
    public ProductoDTO getProductoById(Long id) {
        Producto prod = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));

        return Mapper.toDTO(prod);
    }

    @Override
    public ProductoDTO postProducto(ProductoDTO productoDto) {
        if (productoDto == null) throw new RuntimeException("ProductoDTO es null");

        var prod = Producto.builder()
                .nombre(productoDto.getNombre())
                .precioActual(productoDto.getPrecioActual())
                .cantidad(productoDto.getCantidad())
                .categoria(productoDto.getCategoria())
                .build();

        return Mapper.toDTO(repository.save(prod));
    }

    @Override
    public void deleteProducto(Long id) {
        //Solo nos fijamos que exista el producto, no hace falta traerlo.
        if (!repository.existsById(id)) {
            throw new NotFoundException("Producto no encontrado para eliminar");
        }
        repository.deleteById(id);
    }

    @Override
    public ProductoDTO updateProducto(Long id, ProductoDTO productoDTO) {
        Producto prod = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));

        if (productoDTO.getNombre() != null) prod.setNombre(productoDTO.getNombre());

        if (productoDTO.getCategoria() != null) prod.setCategoria(productoDTO.getCategoria());
        if (productoDTO.getCantidad() != null) prod.setCantidad(productoDTO.getCantidad());
        if (productoDTO.getPrecioActual() != null) prod.setPrecioActual(productoDTO.getPrecioActual());

        return Mapper.toDTO(repository.save(prod));
    }
}
