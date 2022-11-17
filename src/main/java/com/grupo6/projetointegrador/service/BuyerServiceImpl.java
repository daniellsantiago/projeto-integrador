package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.model.entity.Buyer;
import com.grupo6.projetointegrador.repository.BuyerRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BuyerServiceImpl implements BuyerService {
  private BuyerRepo buyerRepo;

  public BuyerServiceImpl(BuyerRepo buyerRepo) {
    this.buyerRepo = buyerRepo;
  }

  /**
   * Find a buyer by id, and return the buyer.
   *
   * @param id The id of the buyer you want to find.
   * @return The buyer object is being returned.
   */
  public Buyer findById(Long id) {
    Optional<Buyer> buyer = buyerRepo.findById(id);
    return buyer.get();
  }
}