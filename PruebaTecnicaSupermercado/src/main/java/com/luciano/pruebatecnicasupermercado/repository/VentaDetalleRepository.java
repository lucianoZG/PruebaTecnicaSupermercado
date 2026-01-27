package com.luciano.pruebatecnicasupermercado.repository;

import com.luciano.pruebatecnicasupermercado.model.VentaDetalle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VentaDetalleRepository extends JpaRepository<VentaDetalle, Long> {
    @Query("SELECT vd.producto, SUM(vd.cantidadProd) " +
            "FROM VentaDetalle vd " +
            "GROUP BY vd.producto " +
            "ORDER BY SUM(vd.cantidadProd) DESC")
    List<Object[]> findProductoMasVendido(Pageable pageable);
}
