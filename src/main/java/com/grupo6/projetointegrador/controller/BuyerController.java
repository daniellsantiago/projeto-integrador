package com.grupo6.projetointegrador.controller;

import com.grupo6.projetointegrador.dto.CreateBuyerDto;
import com.grupo6.projetointegrador.model.entity.Buyer;
import com.grupo6.projetointegrador.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/buyer")
@Validated
public class BuyerController {

    @Autowired
    BuyerService buyerService;

    @PostMapping
    public ResponseEntity<Buyer> saveBuyer(@RequestBody @Valid CreateBuyerDto createBuyerDto) {
        return new ResponseEntity<>(buyerService.save(createBuyerDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Buyer> findBuyer(@PathVariable Long id) {
        return new ResponseEntity<>(buyerService.findById(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Buyer> updateBuyer(@PathVariable Long id,
                                             @RequestBody @Valid CreateBuyerDto createBuyerDto) {
        return new ResponseEntity<>(buyerService.update(id, createBuyerDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBuyer(@PathVariable Long id) {
        buyerService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
