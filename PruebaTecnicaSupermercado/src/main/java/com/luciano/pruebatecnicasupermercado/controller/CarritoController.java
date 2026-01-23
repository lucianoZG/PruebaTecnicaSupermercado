package com.luciano.pruebatecnicasupermercado.controller;

import com.luciano.pruebatecnicasupermercado.dto.CarritoDTO;
import com.luciano.pruebatecnicasupermercado.dto.VentaDTO;
import com.luciano.pruebatecnicasupermercado.model.Usuario;
import com.luciano.pruebatecnicasupermercado.service.ICarritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private ICarritoService carritoService;

    @Operation(summary = "Obtener el carrito mediante el ID del usuario", description = "Devuelve el carrito correspondiente al ID del usuario ingresado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Carrito encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
        @ApiResponse(responseCode = "404", description = "Carrito no encontrado")
    })
    @GetMapping("/mi-carrito")
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<CarritoDTO> obtenerMiCarrito(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(carritoService.obtenerCarritoPorUsuario(usuario.getId()));
    }

    @Operation(summary = "Agregar un producto al carrito", description = "Agrega un item o suma cantidad. Devuelve el carrito actualizado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carrito actualizado"),
            @ApiResponse(responseCode = "400", description = "Stock insuficiente o cantidad ingresada inválida"),
            @ApiResponse(responseCode = "404", description = "El carrito o el producto no fueron encontrados")
    })
    @PostMapping("/{carritoId}/producto/{productoId}")
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<CarritoDTO> agregarProducto(@PathVariable Long carritoId,
                                                      @PathVariable Long productoId,
                                                      @RequestParam(required = false, defaultValue = "1") Integer cantidad){
        return ResponseEntity.ok(carritoService.agregarProducto(carritoId, productoId, cantidad));
    }

    @Operation(summary = "Disminuir la cantidad o borrar el producto del carrito", description = "Devuelve el carrito actualizado quitando el producto o disminuyendo su cantidad dependiendo de si el usuario ingresó una cantidad a quitar")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto quitado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Cantidad ingresada inválida"),
            @ApiResponse(responseCode = "404", description = "Carrito o producto no encontrado")
    })
    @DeleteMapping("/{carritoId}/producto/{productoId}")
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<CarritoDTO> quitarOEliminarProducto(@PathVariable Long carritoId,
                                                      @PathVariable Long productoId,
                                                      @RequestParam(required = false) Integer cantidad){
        if (cantidad == null) {
            return ResponseEntity.ok(carritoService.eliminarProducto(carritoId, productoId));
        } else {
            return ResponseEntity.ok(carritoService.quitarProducto(carritoId, productoId, cantidad));
        }
    }


    @Operation(summary = "Crear la venta y limpiar el carrito", description = "Se crea la venta y se la guarda en la base de datos, a la vez que se limpia el carrito(se quitan todos sus productos agregados)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Venta creada"),
            @ApiResponse(responseCode = "400", description = "Carrito vacío o stock insuficiente de producto"),
            @ApiResponse(responseCode = "404", description = "Carrito o sucursal no encontrado")
    })
    @PostMapping("/{carritoId}/checkout/{sucursalId}")
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<VentaDTO> finalizarCompra(@PathVariable Long carritoId,
                                                    @PathVariable Long sucursalId,
                                                    @AuthenticationPrincipal Usuario usuario){
        VentaDTO ventaCreada = carritoService.finalizarCompra(carritoId, sucursalId, usuario);
        return ResponseEntity.created(URI.create("/api/ventas/" + ventaCreada.getId()))
                .body(ventaCreada);
    }
}
