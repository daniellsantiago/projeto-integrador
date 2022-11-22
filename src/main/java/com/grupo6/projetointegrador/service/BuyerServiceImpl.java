package com.grupo6.projetointegrador.service;

import com.grupo6.projetointegrador.dto.CreateBuyerDto;
import com.grupo6.projetointegrador.model.entity.Buyer;
import com.grupo6.projetointegrador.repository.BuyerRepo;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

  @Override
  @Transactional
  public Buyer save(CreateBuyerDto buyer) {
    Buyer buyerEntity = new Buyer();
    buyerEntity.setName(buyer.getName());
    buyerEntity.setCpf(buyer.getCpf());
    buyerEntity.setAddress(buyer.getAddress());
    buyerEntity.setNeighborhood(buyer.getNeighborhood());
    buyerEntity.setCity(buyer.getCity());
    buyerEntity.setState(buyer.getState());
    buyerEntity.setZipCode(buyer.getZipCode());
    return buyerRepo.save(buyerEntity);
  }

  @Override
  public Buyer update(Long id, CreateBuyerDto buyer) {
    return null;
  }

  public void deleteById(Long id) {
        buyerRepo.deleteById(id);
    }

}
