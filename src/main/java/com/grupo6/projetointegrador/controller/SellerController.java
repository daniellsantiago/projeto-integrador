package com.grupo6.projetointegrador.controller;

import com.grupo6.projetointegrador.dto.CreateSellerDto;
import com.grupo6.projetointegrador.model.entity.Seller;
import com.grupo6.projetointegrador.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/seller")
@Validated
public class SellerController {
    @Autowired
    private SellerService sellerService;

    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody @Valid CreateSellerDto createSellerDto) {
        return ResponseEntity.ok(sellerService.createSeller(createSellerDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> findSeller(@PathVariable Long id) {
        return ResponseEntity.ok(sellerService.findSeller(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Seller> updateSeller(@PathVariable Long id,
                                               @RequestBody @Valid CreateSellerDto createSellerDto) {
        return ResponseEntity.ok(sellerService.updateSeller(id, createSellerDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity.HeadersBuilder<?> deleteSeller(@PathVariable Long id) {
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent();
    }
}
