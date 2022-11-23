package com.grupo6.projetointegrador.controller;

import com.grupo6.projetointegrador.dto.HeatmapWarehouseDto;
import com.grupo6.projetointegrador.dto.HeatmapWarehouseSectionDto;
import com.grupo6.projetointegrador.exception.NotFoundException;
import com.grupo6.projetointegrador.service.WarehouseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/heatmap")
public class HeatmapController {

    private final WarehouseService service;

    public HeatmapController(WarehouseService service) {
        this.service = service;
    }

    @GetMapping("/section/{id}")
    public ResponseEntity<HeatmapWarehouseSectionDto> heatmapBySection(@PathVariable Long id) {
        HeatmapWarehouseSectionDto response = service.getVolumeBySection(id);
        if (response == null) {
            throw new NotFoundException("Nenhum setor encontrado para calculo de heatmap.");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sections-used/warehouse/{id}")
    public ResponseEntity<HeatmapWarehouseDto> heatmapWarehouseBySectionUsed(@PathVariable Long id) {
        HeatmapWarehouseDto response = service.getWarehouseVolumeBySectionUsed(id);
        if (response == null) {
            throw new NotFoundException("Nenhum produto encontrado neste armazem para calculo de heatmap.");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/sections/warehouse/{id}")
    public ResponseEntity<HeatmapWarehouseDto> heatmapWarehouseByAllSection(@PathVariable Long id) {
        HeatmapWarehouseDto response = service.getWarehouseVolumeByAllSection(id);
        if (response == null) {
            throw new NotFoundException("Nenhum armazém encontrado.");
        }
        return ResponseEntity.ok(response);
    }
}