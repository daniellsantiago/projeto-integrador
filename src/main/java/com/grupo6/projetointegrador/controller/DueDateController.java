package com.grupo6.projetointegrador.controller;

import com.grupo6.projetointegrador.dto.DueDateItemBatchDto;
import com.grupo6.projetointegrador.service.DueDateService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/due-date")
@Validated
public class DueDateController {
    private DueDateService dueDateService;

    public DueDateController(DueDateService dueDateService) {
        this.dueDateService = dueDateService;
    }

    @GetMapping
    public ResponseEntity<List<DueDateItemBatchDto>> findItemBatchBySection(
            @RequestParam(name = "sectionId") Long sectionId,
            @RequestParam(name = "days") int days){
        return ResponseEntity.ok(dueDateService.findItemBatchBySection(sectionId, days));
    }

    @GetMapping("/list")
    public ResponseEntity<List<DueDateItemBatchDto>> findItemBatchByCategory(
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "days") int days,
            @RequestParam(name = "order", defaultValue = "ASC") String order){
        return ResponseEntity.ok(dueDateService.findItemBatchByCategory(category, days, order));
    }
}
