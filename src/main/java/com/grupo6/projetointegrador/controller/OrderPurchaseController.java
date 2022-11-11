package com.grupo6.projetointegrador.controller;

import com.grupo6.projetointegrador.dto.CreateOrderPurchaseDto;
import com.grupo6.projetointegrador.dto.OrderPurchaseDto;
import com.grupo6.projetointegrador.dto.TotalPriceDto;
import com.grupo6.projetointegrador.service.OrderPurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-purchase")
public class OrderPurchaseController {

  private OrderPurchaseService service;

  public OrderPurchaseController(OrderPurchaseService service) { this.service = service; }

  @GetMapping("/{id}")
  public OrderPurchaseDto findOrderPurchase(@PathVariable Long id) {
    return service.findById(id);
  }

  @PutMapping("/{id}")
  public ResponseEntity<String> alterOrderPurchase(@PathVariable Long id) {
    return ResponseEntity.ok(service.endOrder(id));
  }

  @PostMapping
  public ResponseEntity<TotalPriceDto> createOrderPurchase(@RequestBody CreateOrderPurchaseDto orderPurchaseDto) {
    return ResponseEntity.ok(service.createOrderPurchase(orderPurchaseDto));
  }
}
