package com.luciano.pruebatecnicasupermercado.mapper;

import com.luciano.pruebatecnicasupermercado.dto.ProductoDTO;
import com.luciano.pruebatecnicasupermercado.dto.SucursalDTO;
import com.luciano.pruebatecnicasupermercado.dto.VentaDTO;
import com.luciano.pruebatecnicasupermercado.dto.VentaDetalleDTO;
import com.luciano.pruebatecnicasupermercado.model.Producto;
import com.luciano.pruebatecnicasupermercado.model.Sucursal;
import com.luciano.pruebatecnicasupermercado.model.Venta;

import java.util.stream.Collectors;

//Clase creada para mapear un tipo de dato a otro
public class Mapper {
    //Producto a ProductoDTO
    public static ProductoDTO toDTO(Producto p) {
        if (p == null) return null;

        return ProductoDTO.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .cantidad(p.getCantidad())
                .precioActual(p.getPrecioActual())
                .categoria(p.getCategoria())
                .build();
    }

    //Venta a VentaDTO
    public static VentaDTO toDTO(Venta v) {
        if (v == null) return null;

        var detallesLista = v.getListaDetalles().stream().map(det ->
            VentaDetalleDTO.builder()
                    .id(det.getId())
                    .idProd(det.getProducto().getId())
                    .nombreProd(det.getProducto().getNombre())
                    .cantidadProd(det.getCantidadProd())
                    .precioUnitario(det.getPrecioUnitario())
                    //Se calcula en el service
//                    .subtotal(det.getPrecioUnitario() * det.getCantidadProd())
//                            .multiply(Double.valueOf(det.getCantidadProd())))
                    .subtotal(det.getSubtotal())
                    .build()
        ).collect(Collectors.toList());

        //Se calcula en el service
//        var total = detallesLista.stream()
//                .map(VentaDetalleDTO::getSubtotal)
//                //Resumir los datos con reduce
//                .reduce(0.0, Double::sum);

        return VentaDTO.builder()
                .id(v.getId())
                .fecha(v.getFecha())
                .estado(v.getEstado())
                .idSucursal(v.getSucursal().getId())
                .precioTotal(v.getPrecioTotal())
                .ventaDetalleDTOList(detallesLista)
                .build();
    }

    //Sucursal a SucursalDTO
    public static SucursalDTO toDTO(Sucursal s) {
        if (s == null) return null;

        return SucursalDTO.builder()
                .id(s.getId())
                .nombre(s.getNombre())
                .direccion(s.getDireccion())
                .build();
    }
}
