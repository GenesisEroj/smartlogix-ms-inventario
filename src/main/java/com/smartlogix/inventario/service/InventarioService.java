package com.smartlogix.inventario.service;

import com.smartlogix.inventario.dto.ProductoDTO;
import com.smartlogix.inventario.model.Producto;
import com.smartlogix.inventario.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final ProductoRepository productoRepository;

    // CQRS - Command
    public Producto agregarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    // CQRS - Query
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    // CQRS - Query
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
    }

    // CQRS - Query
    public Producto obtenerPorProductoId(String productoId) {
        return productoRepository.findByProductoId(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + productoId));
    }

    // CQRS - Query
    public List<Producto> obtenerPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    // CQRS - Query
    public List<Producto> obtenerStockBajo(Integer limite) {
        return productoRepository.findByStockLessThan(limite);
    }

    // CQRS - Command
    public Producto actualizarStock(String productoId, Integer cantidad) {
        Producto producto = obtenerPorProductoId(productoId);
        producto.setStock(producto.getStock() + cantidad);
        return productoRepository.save(producto);
    }

    // CQRS - Command
    public Producto actualizarProducto(Long id, ProductoDTO dto) {
        Producto producto = obtenerPorId(id);
        if (dto.getNombre() != null)
            producto.setNombre(dto.getNombre());
        if (dto.getPrecio() != null)
            producto.setPrecio(dto.getPrecio());
        if (dto.getStock() != null)
            producto.setStock(dto.getStock());
        if (dto.getCategoria() != null)
            producto.setCategoria(dto.getCategoria());
        return productoRepository.save(producto);
    }

    // CQRS - Command
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}