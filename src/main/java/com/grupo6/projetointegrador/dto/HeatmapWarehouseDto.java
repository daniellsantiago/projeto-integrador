package com.grupo6.projetointegrador.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeatmapWarehouseDto {
    private Long id;
    List<HeatmapSectionDto> heatmapSectionDtos;
}