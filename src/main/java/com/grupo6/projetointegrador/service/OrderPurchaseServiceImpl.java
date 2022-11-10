package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.model.OrderPurchase;
import com.grupo6.projetointegrador.repository.OrderPurchaseRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class OrderPurchaseServiceImpl implements OrderPurchaseService {
  private OrderPurchaseRepo orderPurchaseRepo;

  public OrderPurchaseServiceImpl(OrderPurchaseRepo orderPurchaseRepo) {
    this.orderPurchaseRepo = orderPurchaseRepo;
  }

  public OrderPurchase findById(Long id) {
    Optional<OrderPurchase> orderPurchase = orderPurchaseRepo.findById(id);
    return orderPurchase.get();
  }
}
