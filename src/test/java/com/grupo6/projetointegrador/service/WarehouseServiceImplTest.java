package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.HeatmapSectionDto;
import com.grupo6.projetointegrador.dto.HeatmapWarehouseDto;
import com.grupo6.projetointegrador.dto.HeatmapWarehouseSectionDto;
import com.grupo6.projetointegrador.repository.WarehouseRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class WarehouseServiceImplTest {

    @Mock
    private WarehouseRepo warehouseRepo;

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    @Test
    void getVolumeBySection_getHeatmapWarehouseSectionDto_whenSectionFound() {
        Long sectionId = 1L;

        HeatmapSectionDto heatmapSectionDtos = buildHeatmapSectionDto();

        Mockito.when(warehouseRepo.getVolumeBySection(sectionId))
                .thenReturn(heatmapSectionDtos);
        HeatmapWarehouseSectionDto result = warehouseService.getVolumeBySection(sectionId);

        assertThat(result).isNotNull();
    }

    @Test
    void getVolumeBySection_getNull_whenSectionNotFound() {
        Long sectionId = 1L;

        Mockito.when(warehouseRepo.getVolumeBySection(sectionId)).thenReturn(null);
        HeatmapWarehouseSectionDto result = warehouseService.getVolumeBySection(sectionId);

        assertThat(result).isNull();
    }

    @Test
    void getWarehouseVolumeBySectionUsed_getHeatmapSectionDto_whenWarehouseFound() {
        Long warehouseId = 1L;

        HeatmapSectionDto heatmapSectionDtos = buildHeatmapSectionDto();

        Mockito.when(warehouseRepo.getWarehouseVolumeBySectionUsed(warehouseId))
                .thenReturn(List.of(heatmapSectionDtos));
        HeatmapWarehouseDto result = warehouseService.getWarehouseVolumeBySectionUsed(warehouseId);

        assertThat(result).isNotNull();
    }

    @Test
    void getWarehouseVolumeBySectionUsed_getNull_whenWarehouseNotFound() {
        Long warehouseId = 1L;

        Mockito.when(warehouseRepo.getWarehouseVolumeBySectionUsed(warehouseId)).thenReturn(List.of());
        HeatmapWarehouseDto result = warehouseService.getWarehouseVolumeBySectionUsed(warehouseId);

        assertThat(result).isNull();
    }

    @Test
    void getWarehouseVolumeByAllSection_getHeatmapSectionDto_whenWarehouseFound() {
        Long warehouseId = 1L;

        HeatmapSectionDto heatmapSectionDtos = buildHeatmapSectionDto();

        Mockito.when(warehouseRepo.getWarehouseVolumeByAllSection(warehouseId))
                .thenReturn(List.of(heatmapSectionDtos));
        HeatmapWarehouseDto result = warehouseService.getWarehouseVolumeByAllSection(warehouseId);

        assertThat(result).isNotNull();
    }

    @Test
    void getWarehouseVolumeByAllSection_getNull_whenWarehouseNotFound() {
        Long warehouseId = 1L;

        Mockito.when(warehouseRepo.getWarehouseVolumeByAllSection(warehouseId)).thenReturn(List.of());
        HeatmapWarehouseDto result = warehouseService.getWarehouseVolumeByAllSection(warehouseId);

        assertThat(result).isNull();
    }

    private HeatmapSectionDto buildHeatmapSectionDto() {
        return new HeatmapSectionDto() {
            @Override
            public Integer getTotalProduct() {
                return 1;
            }

            @Override
            public Integer getTotalSku() {
                return 20;
            }

            @Override
            public Long getSectionId() {
                return 1L;
            }

            @Override
            public Double getSectionVolume() {
                return null;
            }

            @Override
            public String getCategoryType() {
                return null;
            }

            @Override
            public Double getCubeProduct() {
                return null;
            }

            @Override
            public String getVolumeCmCubicUsed() {
                return HeatmapSectionDto.super.getVolumeCmCubicUsed();
            }

            @Override
            public String getVolumeCmCubicDisponible() {
                return HeatmapSectionDto.super.getVolumeCmCubicDisponible();
            }

            @Override
            public String getVolumeLiterUsed() {
                return HeatmapSectionDto.super.getVolumeLiterUsed();
            }
        };
    }
}