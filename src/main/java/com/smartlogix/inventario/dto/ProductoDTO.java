package com.smartlogix.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    private String productoId;
    private String nombre;
    private Integer stock;
    private Double precio;
    private String categoria;
}