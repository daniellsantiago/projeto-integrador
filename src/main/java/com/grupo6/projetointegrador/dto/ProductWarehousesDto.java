package com.grupo6.projetointegrador.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWarehousesDto {
    private Long productId;
    List<WarehouseDto> warehouses;
}
