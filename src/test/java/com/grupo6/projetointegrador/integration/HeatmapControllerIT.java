package com.grupo6.projetointegrador.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HeatmapControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void heatmapBySection_getHeatmapWarehouseSectionDto_whenSectionFound() throws Exception {
        // Given
        long sectionRegistered = 1L;
        String categoryType = "FRESCO";

        // When / Then
        ResultActions result = mockMvc.perform(get("/api/heatmap/section/" + sectionRegistered)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.heatmapSectionDtos.categoryType").value(categoryType));

        assertThat(result).isNotNull();
    }

    @Test
    void heatmapBySection_getNotFound_whenSectionNotFound() throws Exception {
        // Given
        long sectionNotRegistered = 999999;

        // When / Then
        mockMvc.perform(get("/api/heatmap/section/" + sectionNotRegistered)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void heatmapWarehouseBySectionUsed_getHeatmapWarehouseDto_whenWarehouseAndFound() throws Exception {
        // Given
        long warehouseRegistered = 1L;

        // When / Then
        ResultActions result = mockMvc.perform(get("/api/heatmap/sections-used/warehouse/" + warehouseRegistered)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1));

        assertThat(result).isNotNull();
    }

    @Test
    void heatmapWarehouseBySectionUsed_getNotFound_whenWarehouseNotFound() throws Exception {
        // Given
        long warehouseNotRegistered = 999999;

        // When / Then
        mockMvc.perform(get("/api/heatmap/sections-used/warehouse/" + warehouseNotRegistered)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void heatmapWarehouseByAllSection_getHeatmapWarehouseDto_whenWarehouseAndSectionFound() throws Exception {
        // Given
        long warehouseRegistered = 1L;

        // When / Then
        mockMvc.perform(get("/api/heatmap/sections/warehouse/" + warehouseRegistered)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void heatmapWarehouseByAllSection_getNotFound_whenWarehouseAndSectionNotFound() throws Exception {
        // Given
        long warehouseNotRegistered = 999999;

        // When / Then
        mockMvc.perform(get("/api/heatmap/sections/warehouse/" + warehouseNotRegistered)
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
    }
}