package com.luciano.pruebatecnicasupermercado.repository;

import com.luciano.pruebatecnicasupermercado.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
