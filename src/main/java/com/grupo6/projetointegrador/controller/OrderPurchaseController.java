package com.grupo6.projetointegrador.controller;

import com.grupo6.projetointegrador.dto.CreateOrderPurchaseDto;
import com.grupo6.projetointegrador.dto.OrderPurchaseDto;
import com.grupo6.projetointegrador.dto.TotalPriceDto;
import com.grupo6.projetointegrador.service.OrderPurchaseService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-purchase")
public class OrderPurchaseController {

  private final OrderPurchaseService service;

  public OrderPurchaseController(OrderPurchaseService service) { this.service = service; }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public TotalPriceDto createOrderPurchase(@RequestBody CreateOrderPurchaseDto orderPurchaseDto) {
      return service.createOrderPurchase(orderPurchaseDto);
  }

  @GetMapping("/{id}")
  public OrderPurchaseDto findOrderPurchase(@PathVariable Long id) {
    return service.findById(id);
  }

  @PutMapping("/{id}")
  public String alterOrderPurchase(@PathVariable Long id) {
    return service.endOrder(id);
  }
}
