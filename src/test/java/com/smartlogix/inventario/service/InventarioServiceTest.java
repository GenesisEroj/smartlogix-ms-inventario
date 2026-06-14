package com.smartlogix.inventario.service;

import com.smartlogix.inventario.dto.ProductoDTO;
import com.smartlogix.inventario.model.Producto;
import com.smartlogix.inventario.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("InventarioService - Pruebas Unitarias")
class InventarioServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private InventarioService inventarioService;

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

    // ─────────────────────────────────────────────────────────
    //  COMMAND: agregarProducto
    // ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("agregarProducto - debe guardar y retornar el producto creado")
    void agregarProducto_debeGuardarYRetornarProducto() {
        when(productoRepository.save(any(Producto.class))).thenReturn(productoMock);

        Producto resultado = inventarioService.agregarProducto(productoMock);

        assertNotNull(resultado);
        assertEquals("PROD-001", resultado.getProductoId());
        assertEquals("Laptop HP", resultado.getNombre());
        assertEquals(50, resultado.getStock());
        verify(productoRepository, times(1)).save(productoMock);
    }

    // ─────────────────────────────────────────────────────────
    //  QUERY: obtenerTodos
    // ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("obtenerTodos - debe retornar lista con todos los productos")
    void obtenerTodos_debeRetornarListaDeProductos() {
        Producto producto2 = Producto.builder()
                .id(2L)
                .productoId("PROD-002")
                .nombre("Mouse Logitech")
                .stock(200)
                .precio(29.99)
                .categoria("Periféricos")
                .build();

        when(productoRepository.findAll()).thenReturn(Arrays.asList(productoMock, producto2));

        List<Producto> resultado = inventarioService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("PROD-001", resultado.get(0).getProductoId());
        assertEquals("PROD-002", resultado.get(1).getProductoId());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("obtenerTodos - debe retornar lista vacía si no hay productos")
    void obtenerTodos_debeRetornarListaVacia() {
        when(productoRepository.findAll()).thenReturn(List.of());

        List<Producto> resultado = inventarioService.obtenerTodos();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // ─────────────────────────────────────────────────────────
    //  QUERY: obtenerPorId
    // ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("obtenerPorId - debe retornar producto cuando el ID existe")
    void obtenerPorId_debeRetornarProductoCuandoExiste() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoMock));

        Producto resultado = inventarioService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Laptop HP", resultado.getNombre());
        verify(productoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("obtenerPorId - debe lanzar excepción cuando el ID no existe")
    void obtenerPorId_debeLanzarExcepcionCuandoNoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> inventarioService.obtenerPorId(99L));

        assertTrue(ex.getMessage().contains("Producto no encontrado"));
        verify(productoRepository, times(1)).findById(99L);
    }

    // ─────────────────────────────────────────────────────────
    //  QUERY: obtenerPorProductoId
    // ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("obtenerPorProductoId - debe retornar producto cuando el productoId existe")
    void obtenerPorProductoId_debeRetornarProducto() {
        when(productoRepository.findByProductoId("PROD-001")).thenReturn(Optional.of(productoMock));

        Producto resultado = inventarioService.obtenerPorProductoId("PROD-001");

        assertNotNull(resultado);
        assertEquals("PROD-001", resultado.getProductoId());
    }

    @Test
    @DisplayName("obtenerPorProductoId - debe lanzar excepción si no existe")
    void obtenerPorProductoId_debeLanzarExcepcion() {
        when(productoRepository.findByProductoId("PROD-999")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> inventarioService.obtenerPorProductoId("PROD-999"));
    }

    // ─────────────────────────────────────────────────────────
    //  QUERY: obtenerPorCategoria
    // ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("obtenerPorCategoria - debe retornar productos de la categoría indicada")
    void obtenerPorCategoria_debeRetornarProductosDeCategoria() {
        when(productoRepository.findByCategoria("Electrónica"))
                .thenReturn(List.of(productoMock));

        List<Producto> resultado = inventarioService.obtenerPorCategoria("Electrónica");

        assertEquals(1, resultado.size());
        assertEquals("Electrónica", resultado.get(0).getCategoria());
    }

    // ─────────────────────────────────────────────────────────
    //  QUERY: obtenerStockBajo
    // ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("obtenerStockBajo - debe retornar productos con stock menor al límite")
    void obtenerStockBajo_debeRetornarProductosConStockBajo() {
        Producto stockBajo = Producto.builder()
                .id(3L).productoId("PROD-003").nombre("Teclado").stock(5)
                .precio(49.99).categoria("Periféricos").build();

        when(productoRepository.findByStockLessThan(10)).thenReturn(List.of(stockBajo));

        List<Producto> resultado = inventarioService.obtenerStockBajo(10);

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).getStock() < 10);
    }

    // ─────────────────────────────────────────────────────────
    //  COMMAND: actualizarStock
    // ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("actualizarStock - debe incrementar el stock correctamente")
    void actualizarStock_debeIncrementarStock() {
        when(productoRepository.findByProductoId("PROD-001")).thenReturn(Optional.of(productoMock));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoMock);

        Producto resultado = inventarioService.actualizarStock("PROD-001", 20);

        // stock original 50 + 20 = 70
        assertEquals(70, productoMock.getStock());
        verify(productoRepository, times(1)).save(productoMock);
    }

    @Test
    @DisplayName("actualizarStock - debe decrementar el stock con cantidad negativa")
    void actualizarStock_debeDecrementarConCantidadNegativa() {
        when(productoRepository.findByProductoId("PROD-001")).thenReturn(Optional.of(productoMock));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoMock);

        inventarioService.actualizarStock("PROD-001", -10);

        // stock original 50 - 10 = 40
        assertEquals(40, productoMock.getStock());
    }

    // ─────────────────────────────────────────────────────────
    //  COMMAND: actualizarProducto
    // ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("actualizarProducto - debe actualizar solo los campos no nulos del DTO")
    void actualizarProducto_debeActualizarCamposNulos() {
        ProductoDTO dto = ProductoDTO.builder()
                .nombre("Laptop Dell")
                .precio(1199.99)
                .build();

        when(productoRepository.findById(1L)).thenReturn(Optional.of(productoMock));
        when(productoRepository.save(any(Producto.class))).thenReturn(productoMock);

        Producto resultado = inventarioService.actualizarProducto(1L, dto);

        assertEquals("Laptop Dell", productoMock.getNombre());
        assertEquals(1199.99, productoMock.getPrecio());
        // Categoria y stock no cambian porque el DTO los tiene null
        assertEquals("Electrónica", productoMock.getCategoria());
        verify(productoRepository, times(1)).save(productoMock);
    }

    // ─────────────────────────────────────────────────────────
    //  COMMAND: eliminarProducto
    // ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("eliminarProducto - debe invocar deleteById con el ID correcto")
    void eliminarProducto_debeInvocarDeleteById() {
        doNothing().when(productoRepository).deleteById(1L);

        assertDoesNotThrow(() -> inventarioService.eliminarProducto(1L));

        verify(productoRepository, times(1)).deleteById(1L);
    }
}
