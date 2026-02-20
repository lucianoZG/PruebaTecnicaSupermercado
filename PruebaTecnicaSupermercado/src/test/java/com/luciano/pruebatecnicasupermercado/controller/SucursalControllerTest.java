package com.luciano.pruebatecnicasupermercado.controller;

import com.luciano.pruebatecnicasupermercado.dto.SucursalDTO;
import com.luciano.pruebatecnicasupermercado.security.JwtService;
import com.luciano.pruebatecnicasupermercado.service.ISucursalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import java.util.List;
import static org.mockito.Mockito.when;

@WebMvcTest(SucursalController.class)
// Esto desactiva la seguridad (403 Forbidden) para que la petición pase directo
@AutoConfigureMockMvc(addFilters = false)
public class SucursalControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ISucursalService sucursalService;

    // Agrego el mock de JwtService para que el filtro no rompa al iniciarse
    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;


    @Test
    public void getSucursalesTest() throws Exception {
        SucursalDTO  sucursalDTO = SucursalDTO.builder()
                .nombre("Sucursal Norte")
                .direccion("Av. Solis 300")
                .build();

        when(sucursalService.getSucursales()).thenReturn(List.of(sucursalDTO));

        mvc.perform(get("/api/sucursales").accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre").value("Sucursal Norte"))
                .andExpect(jsonPath("$[0].direccion").value("Av. Solis 300"));
    }

}
