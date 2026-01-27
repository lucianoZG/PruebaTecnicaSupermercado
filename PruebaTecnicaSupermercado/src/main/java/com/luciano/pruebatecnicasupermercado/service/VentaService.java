package com.luciano.pruebatecnicasupermercado.service;

import com.luciano.pruebatecnicasupermercado.dto.ProductoMasVendidoDTO;
import com.luciano.pruebatecnicasupermercado.dto.VentaDTO;
import com.luciano.pruebatecnicasupermercado.dto.VentaDetalleDTO;
import com.luciano.pruebatecnicasupermercado.exception.NotFoundException;
import com.luciano.pruebatecnicasupermercado.mapper.Mapper;
import com.luciano.pruebatecnicasupermercado.model.Producto;
import com.luciano.pruebatecnicasupermercado.model.Sucursal;
import com.luciano.pruebatecnicasupermercado.model.Venta;
import com.luciano.pruebatecnicasupermercado.model.VentaDetalle;
import com.luciano.pruebatecnicasupermercado.repository.ProductoRepository;
import com.luciano.pruebatecnicasupermercado.repository.SucursalRepository;
import com.luciano.pruebatecnicasupermercado.repository.VentaDetalleRepository;
import com.luciano.pruebatecnicasupermercado.repository.VentaRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VentaService implements IVentaService{

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    private final VentaDetalleRepository ventaDetalleRepository;

    @Override
    public List<VentaDTO> getVentas() {
        return ventaRepository.findAll()
                .stream()
                .map(Mapper::toDTO)
                .toList();
    }

    @Override
    public VentaDTO getVentaById(Long id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Venta no encontrada"));

        return Mapper.toDTO(venta);
    }

    @Override
    public VentaDTO postVenta(VentaDTO ventaDTO) {
        //Primero nos fijamos si se indicó algún valor en todos los campos correspondientes.
        if (ventaDTO == null) throw new IllegalArgumentException("ventaDTO es null");
        if (ventaDTO.getIdSucursal() == null) throw new IllegalArgumentException("Se debe indicar la sucursal");
        if (ventaDTO.getVentaDetalleDTOList() == null || ventaDTO.getVentaDetalleDTOList().isEmpty())
            throw new IllegalArgumentException("Se debe indicar el detalle");

        //Buscar sucursal
        Sucursal sucursal = sucursalRepository.findById(ventaDTO.getIdSucursal())
                .orElseThrow(() -> new NotFoundException("No se encontró la sucursal indicada"));

        Venta venta = Venta.builder()
                .fecha(ventaDTO.getFecha())
                .estado(ventaDTO.getEstado())
                .sucursal(sucursal)
                .build();

        // Mapear la lista de Detalles usando streams
        List<VentaDetalle> detalles = ventaDTO.getVentaDetalleDTOList().stream()
                .map(detalleDTO -> {
                    // Buscar el producto (Base de datos)
                    Producto producto = productoRepository.findById(detalleDTO.getIdProd())
                            .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + detalleDTO.getIdProd()));

                    // Calcular el subtotal aquí para que se guarde en BD
                    double subtotalCalculado = producto.getPrecioActual() * detalleDTO.getCantidadProd();

                    // Crear la entidad DetalleVenta
                    return VentaDetalle.builder()
                            .producto(producto)
                            .precioUnitario(producto.getPrecioActual())
                            .cantidadProd(detalleDTO.getCantidadProd())
                            .subtotal(subtotalCalculado)
                            .venta(venta)
                            .build();
                })
                .collect(Collectors.toList()); // O .toList() si usas Java 16+

        // Asignar los detalles a la venta
        venta.setListaDetalles(detalles);

        // Calcular el Total de la Venta sumando los subtotales
        double totalVenta = detalles.stream()
                .mapToDouble(VentaDetalle::getSubtotal)
                .sum();

        venta.setPrecioTotal(totalVenta);

        return Mapper.toDTO(ventaRepository.save(venta));
    }

    @Override
    public void deleteVenta(Long id) {
        if (!ventaRepository.existsById(id)){
            throw new NotFoundException("Venta no encontrada para eliminar");
        }

        ventaRepository.deleteById(id);
    }

    @Override
    public VentaDTO updateVenta(Long id, VentaDTO ventaDTO) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Venta no encontrada"));

        if (ventaDTO.getFecha() != null) venta.setFecha(ventaDTO.getFecha());

        if (ventaDTO.getEstado() != null) venta.setEstado(ventaDTO.getEstado());

        if (ventaDTO.getPrecioTotal() != null) venta.setPrecioTotal(ventaDTO.getPrecioTotal());

        if (ventaDTO.getIdSucursal() != null){
            Sucursal sucursal = sucursalRepository.findById(ventaDTO.getIdSucursal())
                    .orElseThrow(() -> new NotFoundException("Sucursal no encontrada"));

            venta.setSucursal(sucursal);
        }

        return Mapper.toDTO(ventaRepository.save(venta));
    }

    @Override
    public ProductoMasVendidoDTO getProductoMasVendido() {
        List<Object[]> resultado = ventaDetalleRepository.findProductoMasVendido(PageRequest.of(0,1));

        if (resultado.isEmpty()) throw new NotFoundException("No se encontraron ventas registradas");

        Object[] top = resultado.get(0);

        Producto productoEntidad = (Producto) top[0];
        Long cantidadVendida = (Long) top[1];

        return ProductoMasVendidoDTO.builder()
                .producto(Mapper.toDTO(productoEntidad))
                .cantidadVendida(cantidadVendida).build();
    }
}
