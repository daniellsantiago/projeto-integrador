package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateOrderPurchaseDto;
import com.grupo6.projetointegrador.dto.OrderPurchaseDto;
import com.grupo6.projetointegrador.model.ItemBatch;
import com.grupo6.projetointegrador.model.OrderPurchase;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderPurchaseService {
  OrderPurchaseDto findById(Long id);
  String endOrder(Long id);

  String createOrderPurchase(CreateOrderPurchaseDto createOrderPurchaseDto);
}
