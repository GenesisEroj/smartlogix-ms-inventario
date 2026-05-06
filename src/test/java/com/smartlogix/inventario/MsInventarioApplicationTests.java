package com.smartlogix.inventario;

import com.smartlogix.inventario.model.Producto;
import com.smartlogix.inventario.repository.ProductoRepository;
import com.smartlogix.inventario.service.InventarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MsInventarioApplicationTests {

	@Mock
	private ProductoRepository productoRepository;

	@InjectMocks
	private InventarioService inventarioService;

	private Producto producto;

	@BeforeEach
	void setUp() {
		producto = Producto.builder()
				.id(1L)
				.productoId("prod-001")
				.nombre("Laptop")
				.stock(10)
				.precio(999.99)
				.categoria("Electronica")
				.build();
	}

	@Test
	void agregarProducto_debeRetornarProductoGuardado() {
		when(productoRepository.save(producto)).thenReturn(producto);
		Producto resultado = inventarioService.agregarProducto(producto);
		assertNotNull(resultado);
		assertEquals("prod-001", resultado.getProductoId());
		verify(productoRepository, times(1)).save(producto);
	}

	@Test
	void obtenerTodos_debeRetornarListaDeProductos() {
		when(productoRepository.findAll()).thenReturn(Arrays.asList(producto));
		List<Producto> resultado = inventarioService.obtenerTodos();
		assertFalse(resultado.isEmpty());
		assertEquals(1, resultado.size());
		verify(productoRepository, times(1)).findAll();
	}

	@Test
	void obtenerPorProductoId_debeRetornarProducto() {
		when(productoRepository.findByProductoId("prod-001")).thenReturn(Optional.of(producto));
		Producto resultado = inventarioService.obtenerPorProductoId("prod-001");
		assertNotNull(resultado);
		assertEquals("Laptop", resultado.getNombre());
	}

	@Test
	void obtenerPorProductoId_debeArrojarExcepcionSiNoExiste() {
		when(productoRepository.findByProductoId("prod-999")).thenReturn(Optional.empty());
		assertThrows(RuntimeException.class, () -> inventarioService.obtenerPorProductoId("prod-999"));
	}

	@Test
	void actualizarStock_debeActualizarStockDelProducto() {
		when(productoRepository.findByProductoId("prod-001")).thenReturn(Optional.of(producto));
		when(productoRepository.save(producto)).thenReturn(producto);
		Producto resultado = inventarioService.actualizarStock("prod-001", 5);
		assertEquals(15, resultado.getStock());
		verify(productoRepository, times(1)).save(producto);
	}
}