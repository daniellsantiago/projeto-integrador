package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.model.Buyer;
import com.grupo6.projetointegrador.model.OrderPurchase;
import com.grupo6.projetointegrador.repository.BuyerRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuyerServiceImpl implements BuyerService {
  private BuyerRepo buyerRepo;

  public BuyerServiceImpl(BuyerRepo buyerRepo) {
    this.buyerRepo = buyerRepo;
  }

  public Buyer findById(Long id) {
    Optional<Buyer> buyer = buyerRepo.findById(id);
    return buyer.get();
  }
}
