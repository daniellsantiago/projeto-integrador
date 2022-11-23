package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.HeatmapWarehouseDto;
import com.grupo6.projetointegrador.dto.HeatmapWarehouseSectionDto;

public interface WarehouseService {

    HeatmapWarehouseSectionDto getVolumeBySection(Long id);

    HeatmapWarehouseDto getWarehouseVolumeBySectionUsed(Long id);

    HeatmapWarehouseDto getWarehouseVolumeByAllSection(Long id);
}