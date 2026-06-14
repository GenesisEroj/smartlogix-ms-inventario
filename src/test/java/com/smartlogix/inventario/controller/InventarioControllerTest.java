package com.smartlogix.inventario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogix.inventario.dto.ProductoDTO;
import com.smartlogix.inventario.model.Producto;
import com.smartlogix.inventario.service.InventarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventarioController.class)
@DisplayName("InventarioController - Pruebas de Integración Web")
class InventarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventarioService inventarioService;

    @Autowired
    private ObjectMapper objectMapper;

    private Producto productoMock;

    @BeforeEach
    void setUp() {
        productoMock = Producto.builder()
                .id(1L)
                .productoId("PROD-001")
                .nombre("Laptop HP")
                .stock(50)
                .precio(899.99)
                .categoria("Electrónica")
                .build();
    }

    @Test
    @DisplayName("POST /api/inventario - debe crear producto y retornar 201")
    void agregarProducto_debeRetornar201() throws Exception {
        when(inventarioService.agregarProducto(any(Producto.class))).thenReturn(productoMock);

        mockMvc.perform(post("/api/inventario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productoMock)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productoId").value("PROD-001"))
                .andExpect(jsonPath("$.nombre").value("Laptop HP"));

        verify(inventarioService, times(1)).agregarProducto(any(Producto.class));
    }

    @Test
    @DisplayName("GET /api/inventario - debe retornar lista de productos con 200")
    void obtenerTodos_debeRetornar200ConLista() throws Exception {
        Producto producto2 = Producto.builder()
                .id(2L).productoId("PROD-002").nombre("Mouse").stock(100)
                .precio(19.99).categoria("Periféricos").build();

        when(inventarioService.obtenerTodos()).thenReturn(Arrays.asList(productoMock, producto2));

        mockMvc.perform(get("/api/inventario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].productoId").value("PROD-001"))
                .andExpect(jsonPath("$[1].productoId").value("PROD-002"));
    }

    @Test
    @DisplayName("GET /api/inventario/{productoId} - debe retornar producto por productoId")
    void obtenerPorProductoId_debeRetornarProducto() throws Exception {
        when(inventarioService.obtenerPorProductoId("PROD-001")).thenReturn(productoMock);

        mockMvc.perform(get("/api/inventario/PROD-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productoId").value("PROD-001"))
                .andExpect(jsonPath("$.nombre").value("Laptop HP"));
    }

    @Test
    @DisplayName("GET /api/inventario/categoria/{categoria} - debe retornar productos por categoría")
    void obtenerPorCategoria_debeRetornarListaFiltrada() throws Exception {
        when(inventarioService.obtenerPorCategoria("Electrónica"))
                .thenReturn(List.of(productoMock));

        mockMvc.perform(get("/api/inventario/categoria/Electrónica"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].categoria").value("Electrónica"));
    }

    @Test
    @DisplayName("GET /api/inventario/stock-bajo/{limite} - debe retornar productos con stock bajo")
    void obtenerStockBajo_debeRetornarProductosConStockBajo() throws Exception {
        Producto stockBajo = Producto.builder()
                .id(3L).productoId("PROD-003").nombre("Cable").stock(5)
                .precio(9.99).categoria("Accesorios").build();

        when(inventarioService.obtenerStockBajo(10)).thenReturn(List.of(stockBajo));

        mockMvc.perform(get("/api/inventario/stock-bajo/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("PUT /api/inventario/{id} - debe actualizar producto y retornar 200")
    void actualizarProducto_debeActualizarYRetornar200() throws Exception {
        ProductoDTO dto = ProductoDTO.builder()
                .nombre("Laptop Dell")
                .precio(1199.99)
                .build();

        Producto actualizado = Producto.builder()
                .id(1L).productoId("PROD-001").nombre("Laptop Dell")
                .stock(50).precio(1199.99).categoria("Electrónica").build();

        when(inventarioService.actualizarProducto(eq(1L), any(ProductoDTO.class)))
                .thenReturn(actualizado);

        mockMvc.perform(put("/api/inventario/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Laptop Dell"))
                .andExpect(jsonPath("$.precio").value(1199.99));
    }

    @Test
    @DisplayName("DELETE /api/inventario/{id} - debe eliminar y retornar 204")
    void eliminarProducto_debeRetornar204() throws Exception {
        doNothing().when(inventarioService).eliminarProducto(1L);

        mockMvc.perform(delete("/api/inventario/1"))
                .andExpect(status().isNoContent());

        verify(inventarioService, times(1)).eliminarProducto(1L);
    }
}
