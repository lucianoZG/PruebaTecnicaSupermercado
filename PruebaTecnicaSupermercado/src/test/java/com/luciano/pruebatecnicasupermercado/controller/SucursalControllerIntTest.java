package com.luciano.pruebatecnicasupermercado.controller;

import com.luciano.pruebatecnicasupermercado.dto.SucursalDTO;
import com.luciano.pruebatecnicasupermercado.model.Sucursal;
import com.luciano.pruebatecnicasupermercado.repository.SucursalRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase
public class SucursalControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SucursalRepository sucursalRepository;

    //Para transformar objetos Java en JSON
    @Autowired
    private ObjectMapper objectMapper;

    private static final String URL_CREATE = "/api/sucursales";

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) //Al tener el method con preAuthorize(hasRole.ADMIN) hay que agregar esta anotación para simular un usuario admin.
    void postSucursalTest() throws Exception {

        //Arrange, preparo una sucursalDTO tal como lo solicita el endpoint postSucursal
        SucursalDTO sucursal = SucursalDTO.builder()
                .nombre("Sucursal norte")
                .direccion("Av Belgrano 123")
                .build();

        //Act
        MvcResult result = mockMvc.perform(post(URL_CREATE)
                        //Con contentType le decimos que el contenido que enviamos en el cuerpo es un JSON
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(sucursal))
                )
                //Assert de capa web, se comprueba estado y tipo de respuesta, en este caso al devolver SucursalDTO, tengo que verificar que lo que se devuelva sea un JSON, no texto plano.
                //Compruebo status code, debe ser 201 (created)
                .andExpect(status().isCreated())
                //Compruebo tipo de dato
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombre").value("Sucursal norte"))
                .andExpect(jsonPath("$.direccion").value("Av Belgrano 123"))
                //Si uso andReturn, tengo que guardar en una variable el resultado, luego sacar ese valor de la variable a un String de la siguiente manera:
                //String jsonResponse = result.getResponse().getContentAsString();
                //Como aquí estoy haciendo las validaciones con andExpect, esto no hace falta, pero si es que lo hiciera luego debería transformar el String
                //a un objeto de tipo Sucursal y validar con Asserts
                .andReturn();

        //Assert persistencia/BD
        Sucursal sucursalEnBd = sucursalRepository.findAll().stream()
                .filter(suc -> "Sucursal norte".equals(suc.getNombre()))
                .filter(suc -> "Av Belgrano 123".equals(suc.getDireccion()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No se encontró la sucursal persistida en la BD"));

        //Verificaciones finales
        assertThat(sucursalEnBd.getId()).isNotNull();
        assertThat(sucursalEnBd.getNombre()).isEqualTo("Sucursal norte");
        assertThat(sucursalEnBd.getDireccion()).isEqualTo("Av Belgrano 123");
    }

    @Test
    void getSucursales() throws Exception {
        Sucursal sucursal = Sucursal.builder()
                .nombre("Sucursal sur")
                .direccion("Siglo XXI")
                .build();

        sucursalRepository.save(sucursal);

        mockMvc.perform(get(URL_CREATE)
                //Con accept le decimos que queremos recibir un JSON como respuesta
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                //hasItem equivalente a value, hasItem se usa para colecciones
                .andExpect(jsonPath("$[*].nombre", hasItem("Sucursal sur")))
                .andExpect(jsonPath("$[*].direccion", hasItem("Siglo XXI")));
    }

    @Test
    void getSucursalByIdTest() throws Exception {
        Sucursal sucursal = Sucursal.builder()
                .nombre("Sucursal norte")
                .direccion("Siglo XIX")
                .build();

        sucursalRepository.save(sucursal);

        mockMvc.perform(get("/api/sucursales/" + sucursal.getId())
                        //Con accept le decimos que queremos recibir un JSON como respuesta
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                //hasItem equivalente a value, hasItem se usa para colecciones
                .andExpect(jsonPath("$.nombre").value("Sucursal norte"))
                .andExpect(jsonPath("$.direccion").value("Siglo XIX"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void postSucursalConDatosInvalidosTest() throws Exception {
        //Creo sucursal invalida, ambos campos deberían fallar por la anotación @NotBlank
        SucursalDTO sucursal = SucursalDTO.builder()
                .nombre("  ")
                .direccion(null)
                .build();

        mockMvc.perform(post(URL_CREATE)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(objectMapper.writeValueAsString(sucursal))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre").exists())
                .andExpect(jsonPath("$.direccion").exists())
                .andExpect(jsonPath("$.nombre").value("El nombre de la sucursal no puede estar vacío"));
    }
}
