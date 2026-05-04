package com.smartlogix.inventario.repository;

import com.smartlogix.inventario.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByProductoId(String productoId);
    List<Producto> findByCategoria(String categoria);
    List<Producto> findByStockLessThan(Integer stock);
}