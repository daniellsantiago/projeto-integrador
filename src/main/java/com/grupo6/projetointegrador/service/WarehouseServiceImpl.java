package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.HeatmapSectionDto;
import com.grupo6.projetointegrador.dto.HeatmapWarehouseDto;
import com.grupo6.projetointegrador.dto.HeatmapWarehouseSectionDto;
import com.grupo6.projetointegrador.repository.WarehouseRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepo warehouseRepo;

    public WarehouseServiceImpl(WarehouseRepo warehouseRepo) {
        this.warehouseRepo = warehouseRepo;
    }
    
    /**
     * This function is used to get the volume of a section of a warehouse.<p>
     * Also, check the {@link WarehouseRepo#getVolumeBySection(Long)} method for more movement details.
     *
     * @param id The id of the warehouse section
     * @return A HeatmapWarehouseSectionDto object or {@code null} if warehouse list is empty.
     */
    @Override
    public HeatmapWarehouseSectionDto getVolumeBySection(Long id) {
        HeatmapSectionDto heatmapSectionDtos = warehouseRepo.getVolumeBySection(id);
        if (heatmapSectionDtos == null) {
            return null;
        }
        return new HeatmapWarehouseSectionDto(heatmapSectionDtos);
    }

    /**
     * This function returns a heatmap of the warehouse with the given id, where each section is colored according to the
     * percentage of its volume that is currently used.<p>
     * Also, check the {@link WarehouseRepo#getWarehouseVolumeBySectionUsed(Long)} method for more movement details.
     *
     * @param id the id of the warehouse
     * @return A list of HeatmapSectionDto objects or {@code null} if warehouse list is empty.
     */
    @Override
    public HeatmapWarehouseDto getWarehouseVolumeBySectionUsed(Long id) {
        List<HeatmapSectionDto> heatmapSectionDtos = warehouseRepo.getWarehouseVolumeBySectionUsed(id);
        if (heatmapSectionDtos.isEmpty()) {
            return null;
        }
        return new HeatmapWarehouseDto(id, heatmapSectionDtos);
    }

    /**
     * This function returns a heatmap of the warehouse with the given id, where each section is colored according to the
     * percentage of its volume that is currently used.<p>
     * Also, check the {@link WarehouseRepo#getWarehouseVolumeByAllSection(Long)} method for more movement details.
     *
     * @param id the id of the warehouse
     * @return A list of HeatmapSectionDto objects or {@code null} if warehouse list is empty.
     */
    @Override
    public HeatmapWarehouseDto getWarehouseVolumeByAllSection(Long id) {
        List<HeatmapSectionDto> heatmapSectionDtos = warehouseRepo.getWarehouseVolumeByAllSection(id);
        if (heatmapSectionDtos.isEmpty()) {
            return null;
        }
        return new HeatmapWarehouseDto(id, heatmapSectionDtos);
    }
}