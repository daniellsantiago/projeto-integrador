package com.grupo6.projetointegrador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductWarehousesDto {
    private Long productId;
    List<WarehouseDto> warehouses;
}
