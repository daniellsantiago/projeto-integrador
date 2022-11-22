package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateBuyerDto;
import com.grupo6.projetointegrador.model.entity.Buyer;

public interface BuyerService {
  Buyer findById(Long id);
  Buyer save(CreateBuyerDto buyer);
  Buyer update(Long id, CreateBuyerDto buyer);
  void deleteById(Long id);
}
