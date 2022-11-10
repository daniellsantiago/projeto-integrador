package com.grupo6.projetointegrador.controller;

import com.grupo6.projetointegrador.dto.CreateInboundOrderDto;
import com.grupo6.projetointegrador.dto.ItemBatchDto;
import com.grupo6.projetointegrador.model.OrderPurchase;
import com.grupo6.projetointegrador.service.InboundOrderService;
import com.grupo6.projetointegrador.service.OrderPurchaseService;
import com.grupo6.projetointegrador.service.OrderPurchaseServiceImpl;
import org.hibernate.criterion.Order;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-purchase")
public class OrderPurchaseController {

  private OrderPurchaseService service;

  public OrderPurchaseController(OrderPurchaseService service) { this.service = service; }

  @GetMapping("/{id}")
  public OrderPurchase createInboundOrder(@PathVariable Long id) {
    return service.findById(id);
  }
}
