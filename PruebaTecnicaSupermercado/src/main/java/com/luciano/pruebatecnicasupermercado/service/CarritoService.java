package com.luciano.pruebatecnicasupermercado.service;

import com.luciano.pruebatecnicasupermercado.dto.CarritoDTO;
import com.luciano.pruebatecnicasupermercado.dto.ItemCarritoDTO;
import com.luciano.pruebatecnicasupermercado.dto.VentaDTO;
import com.luciano.pruebatecnicasupermercado.exception.NotFoundException;
import com.luciano.pruebatecnicasupermercado.exception.StockInsuficienteException;
import com.luciano.pruebatecnicasupermercado.mapper.Mapper;
import com.luciano.pruebatecnicasupermercado.model.*;
import com.luciano.pruebatecnicasupermercado.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CarritoService implements ICarritoService{

    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;
    private final SucursalRepository sucursalRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public CarritoDTO obtenerCarritoPorUsuario(Long usId) {
        Usuario usuario = usuarioRepository.findById(usId)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

//        Optional<Carrito> carritoOp = carritoRepository.findByUsuario(usuario);
//
//        Carrito carrito;
//
//        if (carritoOp.isPresent()) {
//            carrito = carritoOp.get();
//        } else {
//            carrito = new Carrito();
//            carrito.setUsuario(usuario);
//            carritoRepository.save(carrito);
//        }

        // Busca el carrito, o si no existe, crea uno nuevo, lo guarda y lo devuelve.
        Carrito carrito = carritoRepository.findByUsuario(usuario)
                .orElseGet(() -> {
                    Carrito nuevoCarrito = Carrito.builder()
                            .usuario(usuario)
                            .items(new ArrayList<>())
                            .build();
                    return carritoRepository.save(nuevoCarrito);
                });

        return Mapper.toDTO(carrito);
    }

    @Override
    @Transactional
    public CarritoDTO agregarProducto(Long carritoId, Long productoId, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) throw new IllegalArgumentException("Indique la cantidad");

        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new NotFoundException("Carrito no encontrado"));

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));

        Optional<ItemCarrito> itemExistente = carrito.getItems().stream()
                .filter(item -> item.getProducto().getId().equals(productoId))
                .findFirst();

        if (itemExistente.isPresent()) {
            ItemCarrito item = itemExistente.get();

            Integer cantidadRequerida = item.getCantidad() + cantidad;

            if (!verificarStock(item.getCantidad(), cantidadRequerida)){
                throw new StockInsuficienteException("Stock insuficiente. Solo quedan "
                        + producto.getCantidad() + " unidades y ya tienes "
                        + item.getCantidad() + " en el carrito.");
            }

            item.setCantidad(cantidadRequerida);
        } else {
            if (!verificarStock(producto.getCantidad(), cantidad)) {
                throw new StockInsuficienteException("Stock insuficiente. Solo quedan "
                + producto.getCantidad() + " unidades");
            }

            ItemCarrito nuevoItem = ItemCarrito.builder()
                    .cantidad(cantidad)
                    .carrito(carrito)
                    .producto(producto)
                    .build();

            carrito.getItems().add(nuevoItem);
        }

        Carrito carritoGuardado = carritoRepository.save(carrito);

        return Mapper.toDTO(carritoGuardado);
    }

    @Override
    public CarritoDTO quitarProducto(Long carritoId, Long productoId, Integer cantidad) {
        if (cantidad == null || cantidad <= 0) throw new IllegalArgumentException("Indique una cantidad válida");
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new NotFoundException("Carrito no encontrado"));

        //No hcae falta buscar el producto en si
        Optional<ItemCarrito> itemExistente = carrito.getItems().stream()
                .filter(item -> item.getProducto().getId().equals(productoId))
                .findFirst();

        if (itemExistente.isPresent()) {
            ItemCarrito item = itemExistente.get();
//            Producto productoEnCarrito = itemExistente.get().getProducto();
//            Integer cantidadFinal = productoEnCarrito.getCantidad() - cantidad;

            int cantidadFinal = item.getCantidad() - cantidad;

            if (cantidadFinal <= 0) {
                //Solo se lo remueve de la lista para no llamar a otro method, como eliminarProducto, que usaría repositorio.save de nuevo y complicaría las cosas
                carrito.getItems().remove(item);
            } else {
                item.setCantidad(cantidadFinal);
            }
        } else {
            throw new NotFoundException("Producto no encontrado en el carrito");
        }

        Carrito carritoGuardado = carritoRepository.save(carrito);

        return Mapper.toDTO(carritoGuardado);
    }

    @Override
    public CarritoDTO eliminarProducto(Long carritoId, Long productoId) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new NotFoundException("Carrito no encontrado"));

        Optional<ItemCarrito> itemExistente = carrito.getItems().stream()
                .filter(item -> item.getProducto().getId().equals(productoId))
                .findFirst();

        if (itemExistente.isPresent()) {
            ItemCarrito itemParaBorrar = itemExistente.get();

            carrito.getItems().remove(itemParaBorrar);
        } else {
            throw new NotFoundException("Producto no encontrado");
        }

        Carrito carritoGuardado = carritoRepository.save(carrito);

        return Mapper.toDTO(carritoGuardado);
    }

    @Override
    @Transactional
    public VentaDTO finalizarCompra(Long carritoId, Long sucursalId, Usuario usuario) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new NotFoundException("Carrito no encontrado"));

        if (!carrito.getUsuario().getId().equals(usuario.getId())) {
            throw new AccessDeniedException("No tiene permiso para operar sobre este carrito");
        }

        if (carrito.getItems().isEmpty()) {
            throw new IllegalStateException("El carrito está vacío, no se puede realizar la venta");
        }

        Sucursal sucursal = sucursalRepository.findById(sucursalId)
                .orElseThrow(() -> new NotFoundException("Sucursal no encontrada"));

        Venta venta = Venta.builder()
                .fecha(LocalDate.now())
                .estado(EstadoVenta.COMPLETADA)
                .sucursal(sucursal)
                .build();

        List<VentaDetalle> detalles = carrito.getItems().stream()
                .map(item -> {
                    Producto producto = item.getProducto();

                    if (producto.getCantidad() < item.getCantidad()) throw new StockInsuficienteException("Stock insuficiente de producto");

                    //Descuento el stock del producto que se vendió
                    producto.setCantidad(producto.getCantidad() - item.getCantidad());
                    return VentaDetalle.builder()
                            .cantidadProd(item.getCantidad())
                            .precioUnitario(producto.getPrecioActual())
                            .subtotal(item.getCantidad()*producto.getPrecioActual())
                            .venta(venta)
                            .producto(producto)
                            .build();
                })
                .toList();

        Double totalVenta = detalles.stream()
                .mapToDouble(VentaDetalle::getSubtotal)
                .sum();

        venta.setListaDetalles(detalles);
        venta.setPrecioTotal(totalVenta);

        Venta ventaGuardada = ventaRepository.save(venta);

        //Limpiar el carrito
        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return Mapper.toDTO(ventaGuardada);
    }

    public boolean verificarStock(Integer stockActual, Integer cantidad) {
        return stockActual >= cantidad;
    }
}
