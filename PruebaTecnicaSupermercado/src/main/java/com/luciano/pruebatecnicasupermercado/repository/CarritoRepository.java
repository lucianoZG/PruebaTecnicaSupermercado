package com.luciano.pruebatecnicasupermercado.repository;

import com.luciano.pruebatecnicasupermercado.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsId (Long usId);
}
