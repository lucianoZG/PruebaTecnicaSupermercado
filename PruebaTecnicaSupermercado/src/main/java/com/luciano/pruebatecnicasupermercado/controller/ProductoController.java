package com.luciano.pruebatecnicasupermercado.controller;

import com.luciano.pruebatecnicasupermercado.dto.ProductoDTO;
import com.luciano.pruebatecnicasupermercado.service.IProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    @Operation(summary = "Listar productos paginados", description = "Devuelve una lista de productos presentes en la base de datos separados por páginas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Productos encontrados")
    })
    @GetMapping
    public ResponseEntity<Page<ProductoDTO>> getProductos(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "10") int size,
                                                          @RequestParam(defaultValue = "id") String sortField,
                                                          @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(productoService.getProductos(pageable));
    }

    @Operation(summary = "Obtener producto por ID", description = "Devuelve un producto cuyo ID indicado corresponda con el que tiene en la base de datos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getProductoById(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.getProductoById(id));
    }

    @Operation(summary = "Dar de alta un producto", description = "Se ingresan los datos correspondientes a un producto y se lo da de alta en la base de datos, luego se devuelve el producto creado")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Producto creado")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoDTO> postProducto(@RequestBody ProductoDTO productoDTO) {
        ProductoDTO prodCreado = productoService.postProducto(productoDTO);
        return ResponseEntity.created(URI.create("/api/productos/" + prodCreado.getId()))
                .body(prodCreado);
    }

    @Operation(summary = "Editar un producto", description = "Se ingresa el ID de un producto y se modifican los datos que el usuario ingrese, luego se devuelve el producto modificado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Producto modificado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductoDTO> putProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        return ResponseEntity.ok(productoService.updateProducto(id, productoDTO));
    }

    @Operation(summary = "Eliminar un producto", description = "Se ingresa el ID de un producto y se lo elimina de la base de datos")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Producto eliminado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        productoService.deleteProducto(id);
        return ResponseEntity.noContent().build();
    }

}
