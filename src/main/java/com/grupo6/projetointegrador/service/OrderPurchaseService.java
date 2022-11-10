package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.model.OrderPurchase;

import com.grupo6.projetointegrador.repository.OrderPurchaseRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface OrderPurchaseService {
  OrderPurchase findById(Long id);
}
