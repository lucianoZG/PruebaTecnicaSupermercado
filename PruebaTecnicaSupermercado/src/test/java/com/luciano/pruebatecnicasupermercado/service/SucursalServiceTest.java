package com.luciano.pruebatecnicasupermercado.service;

import com.luciano.pruebatecnicasupermercado.dto.SucursalDTO;
import com.luciano.pruebatecnicasupermercado.exception.NotFoundException;
import com.luciano.pruebatecnicasupermercado.mapper.Mapper;
import com.luciano.pruebatecnicasupermercado.model.Sucursal;
import com.luciano.pruebatecnicasupermercado.repository.SucursalRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SucursalServiceTest {
    @Mock
    private SucursalRepository sucursalRepository;

    @InjectMocks
    private SucursalService sucursalService;

    @Captor
    private ArgumentCaptor<Sucursal> sucursalCaptor;

    @Test
    public void postSucursalTest() {
        SucursalDTO sucursalDTO = SucursalDTO.builder()
                .nombre("Sucursal centro")
                .direccion("Av. Belgrano 123")
                .build();

        Sucursal sucursalGuardada = Sucursal.builder()
                .id(1L)
                .nombre("Sucursal centro")
                .direccion("Av. Belgrano 123")
                .build();
        when(sucursalRepository.save(any(Sucursal.class))).thenReturn(sucursalGuardada);

        sucursalService.postSucursal(sucursalDTO);
        verify(sucursalRepository).save(sucursalCaptor.capture());

        Sucursal sucursalCapturada = sucursalCaptor.getValue();
        Assertions.assertEquals("Sucursal centro", sucursalCapturada.getNombre());
        Assertions.assertEquals("Av. Belgrano 123", sucursalCapturada.getDireccion());
        Assertions.assertNull(sucursalCapturada.getId());

        verifyNoMoreInteractions(sucursalRepository);
    }

    @Test
    void postSucursalExceptionTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> sucursalService.postSucursal(null));
    }

    @Test
    void getSucursalesTest() {
        Sucursal sucursal1 = Sucursal.builder()
                .id(1L)
                .nombre("Sucursal Centro")
                .direccion("Av. Belgrano 123")
                .build();

        Sucursal sucursal2 = Sucursal.builder()
                .id(2L)
                .nombre("Sucursal Sur")
                .direccion("Siglo XXI")
                .build();

        when(sucursalRepository.findAll()).thenReturn(List.of(sucursal1, sucursal2));
        List<SucursalDTO> sucursales = sucursalService.getSucursales();

        Assertions.assertNotNull(sucursales);
        Assertions.assertEquals(2, sucursales.size());
        Assertions.assertEquals(1L, sucursales.get(0).getId());
        Assertions.assertEquals(2L, sucursales.get(1).getId());
        Assertions.assertEquals("Sucursal Centro", sucursales.get(0).getNombre());
        Assertions.assertEquals("Sucursal Sur", sucursales.get(1).getNombre());

        verify(sucursalRepository).findAll();
        verifyNoMoreInteractions(sucursalRepository);
    }

    @Test
    void updateParcialSucursalTest() {
        Sucursal sucursalVieja = Sucursal.builder()
                .id(1L)
                .nombre("Nombre viejo")
                .direccion("Dirección vieja")
                .build();

        SucursalDTO sucursalActualizada = SucursalDTO.builder()
                .nombre("Nombre nuevo")
                .build();

        when(sucursalRepository.findById(1L)).thenReturn(Optional.of(sucursalVieja));
        when(sucursalRepository.save(any(Sucursal.class))).thenAnswer(invocation -> invocation.getArgument(0));

        SucursalDTO resultado = sucursalService.updateSucursal(1L, sucursalActualizada);

        Assertions.assertEquals("Nombre nuevo", resultado.getNombre());
        Assertions.assertEquals("Dirección vieja", resultado.getDireccion());
    }

    @Test
    void updateSucursalExceptionTest() {
        when(sucursalRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> sucursalService.updateSucursal(99L, new SucursalDTO()));

        verify(sucursalRepository, never()).save(any());
    }

    @Test
    void deleteSucursalTest() {
        when(sucursalRepository.existsById(1L)).thenReturn(true);

        sucursalService.deleteSucursal(1L);

        verify(sucursalRepository).deleteById(1L);
    }

    @Test
    void deleteSucursalExceptionTest() {
        when(sucursalRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> sucursalService.deleteSucursal(1L));

        verify(sucursalRepository, never()).deleteById(anyLong());
    }

    @Test
    void getSucursalByIdExceptionTest() {
        when(sucursalRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> sucursalService.getSucursalById(1L));
    }
}
