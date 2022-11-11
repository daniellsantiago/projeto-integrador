package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateOrderPurchaseDto;
import com.grupo6.projetointegrador.dto.OrderPurchaseDto;
import com.grupo6.projetointegrador.dto.TotalPriceDto;

public interface OrderPurchaseService {
  OrderPurchaseDto findById(Long id);

  String endOrder(Long id);

  TotalPriceDto createOrderPurchase(CreateOrderPurchaseDto createOrderPurchaseDto);
}
