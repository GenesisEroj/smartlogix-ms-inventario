package com.smartlogix.inventario.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String productoId;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private String categoria;

    @Column
    private LocalDateTime fechaActualizacion;

    @PreUpdate
    @PrePersist
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}