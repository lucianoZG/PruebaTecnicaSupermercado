package com.luciano.pruebatecnicasupermercado.service;

import com.luciano.pruebatecnicasupermercado.dto.SucursalDTO;
import com.luciano.pruebatecnicasupermercado.exception.NotFoundException;
import com.luciano.pruebatecnicasupermercado.mapper.Mapper;
import com.luciano.pruebatecnicasupermercado.model.Sucursal;
import com.luciano.pruebatecnicasupermercado.repository.SucursalRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SucursalService implements ISucursalService{

    private final SucursalRepository repository;

    @Override
    public List<SucursalDTO> getSucursales() {
        return repository.findAll().stream()
                .map(Mapper::toDTO)
                .toList();
    }

    @Override
    public SucursalDTO getSucursalById(Long id) {
        Sucursal sucursal = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sucursal no encontrada"));

        return Mapper.toDTO(sucursal);
    }

    @Override
    public SucursalDTO postSucursal(SucursalDTO sucursalDto) {
        if (sucursalDto == null) throw new RuntimeException("SucursalDTO es null");

        Sucursal sucursal = Sucursal.builder()
                .direccion(sucursalDto.getDireccion())
                .nombre(sucursalDto.getNombre())
                .build();

        return Mapper.toDTO(repository.save(sucursal));
    }

    @Override
    public void deleteSucursal(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Sucursal no encontrada para eliminar");
        }

        repository.deleteById(id);
    }

    @Override
    public SucursalDTO updateSucursal(Long id, SucursalDTO sucursalDto) {
        Sucursal sucursal = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Sucursal no encontrada"));

        if (sucursalDto.getDireccion() != null) sucursal.setDireccion(sucursalDto.getDireccion());
        if (sucursalDto.getNombre() != null) sucursal.setNombre(sucursalDto.getNombre());

        return Mapper.toDTO(repository.save(sucursal));
    }
}
