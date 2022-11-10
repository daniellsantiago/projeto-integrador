package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.OrderPurchaseDto;

public interface OrderPurchaseService {
  OrderPurchaseDto findById(Long id);
  String endOrder(Long id);
}
